package dev.obscuria.caravans.content.caravan;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.config.CaravanConfig;
import dev.obscuria.caravans.content.registry.CaravanRegistries;
import dev.obscuria.caravans.content.IWeighted;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.CustomSpawner;
import org.jetbrains.annotations.Nullable;

public final class CaravanSpawner implements CustomSpawner {

    public static final CaravanSpawner INSTANCE = new CaravanSpawner();

    public int cooldownTick = 12000;

    @Override
    public int tick(ServerLevel level, boolean spawnHostile, boolean spawnFriendly) {

        if (!shouldSpawn(level, spawnHostile)) return 0;
        final @Nullable var player = selectPlayer(level);
        if (player == null) return 0;
        final @Nullable var pos = selectPosition(level, player);
        if (pos == null) return 0;
        final @Nullable var caravan = selectCaravan(level, pos);
        if (caravan == null) return 0;

        final var totalSpawned = caravan.value().spawn(caravan, level, pos);
        if (totalSpawned <= 0) return 0;
        cooldownTick = caravan.value().actualCooldown();
        return totalSpawned;
    }

    private boolean shouldSpawn(ServerLevel level, boolean spawnHostile) {
        if (!spawnHostile || !level.getGameRules().getBoolean(PillagerCaravans.RULE_DO_CARAVAN_SPAWNING)) return false;
        if (--cooldownTick > 0) return false;
        cooldownTick = CaravanConfig.common.spawning.failedCooldown;
        return level.getDayTime() / CaravanConfig.common.spawning.worldAgeRequired >= 1;
    }

    private @Nullable ServerPlayer selectPlayer(ServerLevel level) {
        final var players = level.players().size();
        if (players < 1) return null;
        final var player = level.players().get(level.random.nextInt(players));
        if (player.isSpectator()) return null;
        if (level.isCloseToVillage(player.blockPosition(), 2)) return null;
        return player;
    }

    @SuppressWarnings("deprecation")
    private @Nullable BlockPos.MutableBlockPos selectPosition(ServerLevel level, ServerPlayer player) {
        final var random = level.random;
        final var distance = CaravanConfig.common.spawning.distance;
        final var variance = CaravanConfig.common.spawning.distanceVariance;
        final var xOffset = (distance + random.nextInt(variance)) * (random.nextBoolean() ? -1 : 1);
        final var yOffset = (distance + random.nextInt(variance)) * (random.nextBoolean() ? -1 : 1);
        final var pos = player.blockPosition().mutable().move(xOffset, 0, yOffset);
        if (!level.hasChunksAt(pos.getX() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getZ() + 10)) return null;
        return pos;
    }

    private @Nullable Holder<CaravanVariation> selectCaravan(ServerLevel level, BlockPos pos) {
        final var registry = level.registryAccess().registryOrThrow(CaravanRegistries.Keys.PLACEMENT);
        final var biome = level.getBiome(pos);
        final var candidates = registry.stream().filter(it -> it.isFor(biome)).toList();
        final @Nullable var placement = IWeighted.pickRandom(candidates, level.random);
        return placement == null ? null : placement.getRandomVariation(level.random);
    }
}
