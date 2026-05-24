package dev.obscuria.caravans.common;

import dev.obscuria.caravans.common.registry.CaravanGameRules;
import dev.obscuria.caravans.common.registry.CaravanRegistries;
import dev.obscuria.caravans.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;

public final class CaravanSpawner implements CustomSpawner {

    public static final CaravanSpawner INSTANCE = new CaravanSpawner();

    public int cooldownTick = 12000;

    @Override
    public void tick(ServerLevel level, boolean spawnEnemies) {

        if (!shouldSpawn(level, spawnEnemies)) return;
        final @Nullable var player = selectPlayer(level);
        if (player == null) return;
        final @Nullable var pos = selectPosition(level, player);
        if (pos == null) return;
        final @Nullable var caravan = selectVariation(level, pos);
        if (caravan == null) return;

        final var totalSpawned = caravan.value().spawn(caravan, level, pos);
        if (totalSpawned <= 0) return;
        cooldownTick = caravan.value().spawnCooldown();
    }

    private boolean shouldSpawn(ServerLevel level, boolean spawnHostile) {
        if (!CommonConfig.ENABLE_PILLAGER_CARAVANS.get()) return false;
        if (!spawnHostile || !level.getGameRules().get(CaravanGameRules.DO_CARAVAN_SPAWNING)) return false;
        if (--cooldownTick > 0) return false;
        cooldownTick = CommonConfig.SPAWN_ATTEMPT_COOLDOWN.get();
        int worldAgeRequired = CommonConfig.WORLD_AGE_REQUIRED.get();
        return worldAgeRequired <= 0 || level.getOverworldClockTime() / worldAgeRequired >= 1;
    }

    private @Nullable ServerPlayer selectPlayer(ServerLevel level) {
        final var players = level.players().size();
        if (players < 1) return null;
        final var player = level.players().get(level.getRandom().nextInt(players));
        if (player.isSpectator()) return null;
        if (level.isCloseToVillage(player.blockPosition(), CommonConfig.VILLAGE_SAFE_ZONE.get())) return null;
        return player;
    }

    @SuppressWarnings("deprecation")
    private @Nullable BlockPos.MutableBlockPos selectPosition(ServerLevel level, ServerPlayer player) {
        final var random = level.getRandom();
        final var distance = (int) CommonConfig.SPAWN_DISTANCE.get();
        final var variance = (int) CommonConfig.SPAWN_DISTANCE_VARIANCE.get();
        final var xOffset = (distance + random.nextInt(variance)) * (random.nextBoolean() ? -1 : 1);
        final var yOffset = (distance + random.nextInt(variance)) * (random.nextBoolean() ? -1 : 1);
        final var pos = player.blockPosition().mutable().move(xOffset, 0, yOffset);
        if (level.getBrightness(LightLayer.BLOCK, pos) > CommonConfig.MAX_BLOCK_LIGHT.get()) return null;
        if (!level.hasChunksAt(pos.getX() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getZ() + 10)) return null;
        return pos;
    }

    private @Nullable Holder<CaravanVariation> selectVariation(ServerLevel level, BlockPos pos) {
        final var registry = level.registryAccess().lookupOrThrow(CaravanRegistries.Keys.PLACEMENT);
        final var biome = level.getBiome(pos);
        final var candidates = registry.stream().filter(it -> it.isFor(biome)).toList();
        final @Nullable var placement = IWeighted.pickRandom(candidates, level.getRandom());
        return placement == null ? null : placement.getRandomVariation(level.getRandom());
    }
}
