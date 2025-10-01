package dev.obscuria.caravans.world.caravans;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.caravans.world.IHorseExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
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
        Optional<ResourceLocation> lootTable,
        Optional<ResourceLocation> cargoTable,
        int count
) {

    public static final Codec<CaravanMember> CODEC;
    public static boolean generatingCaravanCargo;

    public @Nullable Entity spawn(ServerLevel level, BlockPos pos, boolean isLeader) {

        final var state = level.getBlockState(pos);
        final var fluidState = state.getFluidState();
        if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, state, fluidState, EntityType.PILLAGER)) return null;
        final @Nullable var entity = type.create(level);
        if (entity == null) return null;

        if (isLeader && entity instanceof PatrollingMonster patrollingMonster) {
            patrollingMonster.setPatrolLeader(true);
            patrollingMonster.findPatrolTarget();
        }

        entity.setPos(pos.getX(), pos.getY(), pos.getZ());

        if (entity instanceof Mob mob) {
            final var difficulty = level.getCurrentDifficultyAt(pos);
            mob.finalizeSpawn(level, difficulty, MobSpawnType.PATROL, null, null);
        }

        finalize(level, entity);
        level.addFreshEntityWithPassengers(entity);
        return entity;
    }

    private void finalize(ServerLevel level, Entity entity) {

        if (entity instanceof AbstractHorse horse) {
            horse.setTamed(true);
            armorItem.ifPresent(armor -> IHorseExtension.equipArmor(horse, armor.getDefaultInstance()));
        }

        if (entity instanceof AbstractChestedHorse chested) {
            chested.setChest(true);
            IHorseExtension.createInventory(chested);
            cargoTable.ifPresent(cargo -> generateCargo(level, chested, cargo));
        }
    }

    private void generateCargo(ServerLevel level, AbstractChestedHorse entity, ResourceLocation cargo) {

        final var inventory = IHorseExtension.getInventory(entity);
        final var table = level.getServer().getLootData().getLootTable(cargo);
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

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(CaravanMember::type),
                BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("armor").forGetter(CaravanMember::armorItem),
                ResourceLocation.CODEC.optionalFieldOf("loot_table").forGetter(CaravanMember::lootTable),
                ResourceLocation.CODEC.optionalFieldOf("cargo").forGetter(CaravanMember::cargoTable),
                Codec.INT.optionalFieldOf("count", 1).forGetter(CaravanMember::count)
        ).apply(codec, CaravanMember::new));
    }
}
