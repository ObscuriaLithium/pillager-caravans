package dev.obscuria.caravans.config;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.fragmentum.config.ConfigBuilder;
import dev.obscuria.fragmentum.config.ConfigValue;

import java.util.HashSet;

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

    public static final HashSet<ConfigValue<?>> ALL_VALUES = new HashSet<>();

    public static void init() {}

    private static <T> ConfigValue<T> register(ConfigValue<T> value) {
        ALL_VALUES.add(value);
        return value;
    }

    static {
        final var builder = new ConfigBuilder("obscuria/pillager_caravans-common.toml");

        builder.comment(
                "=========[ Pillager Caravans Common Config ]========",
                " Please prefer editing the config through the in-game ",
                " configuration screen (available via the mods list),",
                " as it includes many hints and quality-of-life improvements.",
                "====================================================");

        ENABLE_PILLAGER_CARAVANS = register(builder.defineBoolean("enablePillagerCaravans", true));
        ENABLE_ENCOUNTER_TOAST = register(builder.defineBoolean("enableEncounterToast", true));
        TOAST_LIGHT_MODE = register(builder.defineBoolean("toastLightMode", false));

        WORLD_AGE_REQUIRED = register(builder.defineInt("worldAgeRequired", 24000, 0, Integer.MAX_VALUE));
        SPAWN_ATTEMPT_COOLDOWN = register(builder.defineInt("spawnAttemptCooldown", 1200, 0, Integer.MAX_VALUE));
        SPAWN_COOLDOWN_MULTIPLIER = register(builder.defineDouble("spawnCooldownMultiplier", 1.0, 0.1, 10.0));
        SMALL_CARAVAN_SPAWN_WEIGHT = register(builder.defineInt("smallCaravanSpawnWeight", 9, 0, 100));
        SMALL_CARAVAN_SPAWN_COOLDOWN = register(builder.defineInt("smallCaravanSpawnCooldown", 48000, 12000, Integer.MAX_VALUE));
        SMALL_CARAVAN_DESPAWN_DELAY = register(builder.defineInt("smallCaravanDespawnDelay", 12000, 12000, Integer.MAX_VALUE));
        MEDIUM_CARAVAN_SPAWN_WEIGHT = register(builder.defineInt("mediumCaravanSpawnWeight", 3, 0, 100));
        MEDIUM_CARAVAN_SPAWN_COOLDOWN = register(builder.defineInt("mediumCaravanSpawnCooldown", 96000, 12000, Integer.MAX_VALUE));
        MEDIUM_CARAVAN_DESPAWN_DELAY = register(builder.defineInt("mediumCaravanDespawnDelay", 24000, 12000, Integer.MAX_VALUE));
        LARGE_CARAVAN_SPAWN_WEIGHT = register(builder.defineInt("largeCaravanSpawnWeight", 1, 0, 100));
        LARGE_CARAVAN_SPAWN_COOLDOWN = register(builder.defineInt("largeCaravanSpawnCooldown", 144000, 12000, Integer.MAX_VALUE));
        LARGE_CARAVAN_DESPAWN_DELAY = register(builder.defineInt("largeCaravanDespawnDelay", 36000, 12000, Integer.MAX_VALUE));

        SPAWN_DISTANCE = register(builder.defineInt("spawnDistance", 32, 0, Integer.MAX_VALUE));
        SPAWN_DISTANCE_VARIANCE = register(builder.defineInt("spawnDistanceVariance", 32, 0, Integer.MAX_VALUE));
        MAX_BLOCK_LIGHT = register(builder.defineInt("maxBlockLight", 4, 0, 15));
        VILLAGE_SAFE_ZONE = register(builder.defineInt("villageSafeZone", 3, 0, 64));

        ALLOW_FOLLOWER_TELEPORT = register(builder.defineBoolean("allowFollowerTeleport", true));
        ALLOW_LEASHED_TELEPORT = register(builder.defineBoolean("allowLeashedTeleport", true));

        builder.buildCommon(PillagerCaravans.MOD_ID);
    }
}
