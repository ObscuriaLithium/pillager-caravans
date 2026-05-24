package dev.obscuria.caravans.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.common.data.CaravanLeaderData;
import dev.obscuria.caravans.common.data.CaravanMemberData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CaravanMember(
        EntityType<?> type,
        Optional<Item> armorItem,
        Optional<Identifier> lootTable,
        Optional<Identifier> cargoTable,
        int count
) {

    public static final Codec<CaravanMember> CODEC;
    public static boolean generatingCaravanCargo;

    public @Nullable Entity spawn(Holder<CaravanVariation> variation, ServerLevel level, BlockPos pos, boolean isLeader) {

        var state = level.getBlockState(pos);
        var fluidState = state.getFluidState();
        if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, state, fluidState, type)) return null;
        @Nullable var entity = type.create(level, EntitySpawnReason.EVENT);
        if (entity == null) return null;

        entity.setPos(pos.getX(), pos.getY(), pos.getZ());

        if (entity instanceof LivingEntity living) {
            @Nullable var variationId = variation.unwrapKey().map(ResourceKey::identifier).orElse(null);
            var despawnDelay = variation.value().despawnDelay();
            ILivingExtension.setCaravanData(living, isLeader
                    ? CaravanLeaderData.create(variationId, despawnDelay)
                    : CaravanMemberData.create(despawnDelay));
        }

        if (isLeader && entity instanceof PatrollingMonster monster) {
            monster.setPatrolLeader(true);
            monster.findPatrolTarget();
        }

        if (entity instanceof Mob mob) {
            final var difficulty = level.getCurrentDifficultyAt(pos);
            mob.finalizeSpawn(level, difficulty, EntitySpawnReason.EVENT, null);
        }

        if (entity instanceof Animal animal) {
            final @Nullable var attribute = animal.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) attribute.addPermanentModifier(makeAnimalSpeedModifier());
        }

        if (entity instanceof AbstractHorse horse) {
            horse.setTamed(true);
            armorItem.ifPresent(armor -> {
                var stack = armor.getDefaultInstance();
                if (!canEquipArmor(stack, horse)) return;
                horse.setBodyArmorItem(stack);
            });
        }

        if (entity instanceof AbstractChestedHorse horse) {
            horse.setChest(true);
            horse.createInventory();
            cargoTable.ifPresent(cargo -> generateCargo(level, horse, cargo));
        }

        level.addFreshEntityWithPassengers(entity);
        return entity;
    }

    private void generateCargo(ServerLevel level, AbstractChestedHorse entity, Identifier cargo) {

        var inventory = entity.inventory;
        var cargoKey = ResourceKey.create(Registries.LOOT_TABLE, cargo);
        var table = level.getServer().reloadableRegistries().getLootTable(cargoKey);
        var params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                .create(LootContextParamSets.CHEST);

        generatingCaravanCargo = true;
        inventory.setItem(0, Items.STRUCTURE_VOID.getDefaultInstance());
        table.fill(inventory, params, entity.getLootTableSeed());
        inventory.setItem(0, ItemStack.EMPTY);
        generatingCaravanCargo = false;
    }

    private AttributeModifier makeAnimalSpeedModifier() {
        return new AttributeModifier(
                PillagerCaravans.id("caravan_modifier"),
                -0.33, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    private static boolean canEquipArmor(ItemStack stack, LivingEntity entity) {
        @Nullable var equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.slot() != EquipmentSlot.BODY) return false;
        return equippable.canBeEquippedBy(entity.typeHolder());
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(CaravanMember::type),
                BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("armor").forGetter(CaravanMember::armorItem),
                Identifier.CODEC.optionalFieldOf("loot_table").forGetter(CaravanMember::lootTable),
                Identifier.CODEC.optionalFieldOf("cargo").forGetter(CaravanMember::cargoTable),
                Codec.INT.optionalFieldOf("count", 1).forGetter(CaravanMember::count)
        ).apply(codec, CaravanMember::new));
    }
}
