package dev.obscuria.caravans.config;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.fragmentum.v2.api.config.ConfigBuilder;
import dev.obscuria.fragmentum.v2.api.config.ConfigValue;
import lombok.experimental.UtilityClass;

import java.util.HashSet;

@UtilityClass
public final class CommonConfig {

    public static final ConfigValue<Boolean> ENABLE_PILLAGER_CARAVANS;
    public static final ConfigValue<Boolean> ENABLE_ENCOUNTER_TOAST;
    public static final ConfigValue<Boolean> TOAST_LIGHT_MODE;

    public static final ConfigValue<Integer> WORLD_AGE_REQUIRED;
    public static final ConfigValue<Integer> SPAWN_ATTEMPT_COOLDOWN;
    public static final ConfigValue<Double> SPAWN_COOLDOWN_MULTIPLIER;
    public static final ConfigValue<Integer> SMALL_CARAVAN_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> SMALL_CARAVAN_SPAWN_COOLDOWN;
    public static final ConfigValue<Integer> SMALL_CARAVAN_DESPAWN_DELAY;
    public static final ConfigValue<Integer> MEDIUM_CARAVAN_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> MEDIUM_CARAVAN_SPAWN_COOLDOWN;
    public static final ConfigValue<Integer> MEDIUM_CARAVAN_DESPAWN_DELAY;
    public static final ConfigValue<Integer> LARGE_CARAVAN_SPAWN_WEIGHT;
    public static final ConfigValue<Integer> LARGE_CARAVAN_SPAWN_COOLDOWN;
    public static final ConfigValue<Integer> LARGE_CARAVAN_DESPAWN_DELAY;

    public static final ConfigValue<Integer> SPAWN_DISTANCE;
    public static final ConfigValue<Integer> SPAWN_DISTANCE_VARIANCE;
    public static final ConfigValue<Integer> MAX_BLOCK_LIGHT;
    public static final ConfigValue<Integer> VILLAGE_SAFE_ZONE;

    public static final ConfigValue<Boolean> ALLOW_FOLLOWER_TELEPORT;
    public static final ConfigValue<Boolean> ALLOW_LEASHED_TELEPORT;

    public static final HashSet<ConfigValue<?>> ALL_VALUES;

    public static void init() {}

    static {
        final var builder = ConfigBuilder.create("obscuria/pillager_caravans-common.toml");

        builder.comment(
                "=========[ Pillager Caravans Common Config ]========",
                " Please prefer editing the config through the in-game ",
                " configuration screen (available via the mods list),",
                " as it includes many hints and quality-of-life improvements.",
                "====================================================");

        ENABLE_PILLAGER_CARAVANS = builder.defineBoolean("enablePillagerCaravans", true);
        ENABLE_ENCOUNTER_TOAST = builder.defineBoolean("enableEncounterToast", true);
        TOAST_LIGHT_MODE = builder.defineBoolean("toastLightMode", false);

        WORLD_AGE_REQUIRED = builder.defineInt("worldAgeRequired", 24000, 0, Integer.MAX_VALUE);
        SPAWN_ATTEMPT_COOLDOWN = builder.defineInt("spawnAttemptCooldown", 1200, 0, Integer.MAX_VALUE);
        SPAWN_COOLDOWN_MULTIPLIER = builder.defineDouble("spawnCooldownMultiplier", 1.0, 0.1, 10.0);
        SMALL_CARAVAN_SPAWN_WEIGHT = builder.defineInt("smallCaravanSpawnWeight", 9, 0, 100);
        SMALL_CARAVAN_SPAWN_COOLDOWN = builder.defineInt("smallCaravanSpawnCooldown", 48000, 12000, Integer.MAX_VALUE);
        SMALL_CARAVAN_DESPAWN_DELAY = builder.defineInt("smallCaravanDespawnDelay", 12000, 12000, Integer.MAX_VALUE);
        MEDIUM_CARAVAN_SPAWN_WEIGHT = builder.defineInt("mediumCaravanSpawnWeight", 3, 0, 100);
        MEDIUM_CARAVAN_SPAWN_COOLDOWN = builder.defineInt("mediumCaravanSpawnCooldown", 96000, 12000, Integer.MAX_VALUE);
        MEDIUM_CARAVAN_DESPAWN_DELAY = builder.defineInt("mediumCaravanDespawnDelay", 24000, 12000, Integer.MAX_VALUE);
        LARGE_CARAVAN_SPAWN_WEIGHT = builder.defineInt("largeCaravanSpawnWeight", 1, 0, 100);
        LARGE_CARAVAN_SPAWN_COOLDOWN = builder.defineInt("largeCaravanSpawnCooldown", 144000, 12000, Integer.MAX_VALUE);
        LARGE_CARAVAN_DESPAWN_DELAY = builder.defineInt("largeCaravanDespawnDelay", 36000, 12000, Integer.MAX_VALUE);

        SPAWN_DISTANCE = builder.defineInt("spawnDistance", 32, 0, Integer.MAX_VALUE);
        SPAWN_DISTANCE_VARIANCE = builder.defineInt("spawnDistanceVariance", 32, 0, Integer.MAX_VALUE);
        MAX_BLOCK_LIGHT = builder.defineInt("maxBlockLight", 4, 0, 15);
        VILLAGE_SAFE_ZONE = builder.defineInt("villageSafeZone", 3, 0, 64);

        ALLOW_FOLLOWER_TELEPORT = builder.defineBoolean("allowFollowerTeleport", true);
        ALLOW_LEASHED_TELEPORT = builder.defineBoolean("allowLeashedTeleport", true);

        ALL_VALUES = builder.buildCommon(PillagerCaravans.MOD_ID);
    }
}
