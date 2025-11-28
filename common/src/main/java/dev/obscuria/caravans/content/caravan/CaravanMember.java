package dev.obscuria.caravans.content.caravan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.content.IHorseExtension;
import dev.obscuria.caravans.content.ILivingExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CaravanMember(
        EntityType<?> type,
        Optional<Item> armorItem,
        Optional<ResourceLocation> lootTable,
        Optional<ResourceKey<LootTable>> cargoTable,
        int count
) {

    public static final Codec<CaravanMember> CODEC;
    public static boolean generatingCaravanCargo;
    private static final ResourceLocation CARAVAN_MODIFIER;

    public @Nullable Entity spawn(Holder<CaravanVariation> variation, ServerLevel level, BlockPos pos, boolean isLeader) {

        final var state = level.getBlockState(pos);
        final var fluidState = state.getFluidState();
        if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, state, fluidState, type)) return null;
        final @Nullable var entity = type.create(level);
        if (entity == null) return null;

        entity.setPos(pos.getX(), pos.getY(), pos.getZ());

        if (entity instanceof LivingEntity living) {
            ILivingExtension.setCaravanMember(living, true);
        }

        if (isLeader && entity instanceof PatrollingMonster monster) {
            monster.setPatrolLeader(true);
            monster.findPatrolTarget();
            final @Nullable var type = variation.unwrapKey().map(ResourceKey::location).orElse(null);
            ILivingExtension.setCaravanType(monster, type);
        }

        if (entity instanceof Mob mob) {
            final var difficulty = level.getCurrentDifficultyAt(pos);
            mob.finalizeSpawn(level, difficulty, MobSpawnType.PATROL, null);
        }

        if (entity instanceof Animal animal) {
            final @Nullable var attribute = animal.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) attribute.addPermanentModifier(makeAnimalSpeedModifier());
        }

        if (entity instanceof AbstractHorse horse) {
            horse.setTamed(true);
            armorItem.ifPresent(armor -> horse.setBodyArmorItem(armor.getDefaultInstance()));
        }

        if (entity instanceof AbstractChestedHorse horse) {
            horse.setChest(true);
            IHorseExtension.createInventory(horse);
            cargoTable.ifPresent(cargo -> generateCargo(level, horse, cargo));
        }

        level.addFreshEntityWithPassengers(entity);
        return entity;
    }

    private void generateCargo(ServerLevel level, AbstractChestedHorse entity, ResourceKey<LootTable> cargo) {

        final var inventory = IHorseExtension.getInventory(entity);
        final var table = level.getServer().reloadableRegistries().getLootTable(cargo);
        final var params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                .create(LootContextParamSets.CHEST);

        generatingCaravanCargo = true;
        inventory.setItem(0, Items.STRUCTURE_VOID.getDefaultInstance());
        table.fill(IHorseExtension.getInventory(entity), params, entity.getLootTableSeed());
        inventory.setItem(0, ItemStack.EMPTY);
        generatingCaravanCargo = false;
    }

    private AttributeModifier makeAnimalSpeedModifier() {
        return new AttributeModifier(CARAVAN_MODIFIER, -0.33, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    static {
        CARAVAN_MODIFIER = PillagerCaravans.key("caravan_modifier");
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(CaravanMember::type),
                BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("armor").forGetter(CaravanMember::armorItem),
                ResourceLocation.CODEC.optionalFieldOf("loot_table").forGetter(CaravanMember::lootTable),
                ResourceKey.codec(Registries.LOOT_TABLE).optionalFieldOf("cargo").forGetter(CaravanMember::cargoTable),
                Codec.INT.optionalFieldOf("count", 1).forGetter(CaravanMember::count)
        ).apply(codec, CaravanMember::new));
    }
}
