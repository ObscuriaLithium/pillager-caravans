package dev.obscuria.caravans.config;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.fragmentum.config.*;

public final class CommonConfig {

    public static final ConfigValue<Boolean> ENABLED;
    public static final ConfigValue<Boolean> SHOW_ENCOUNTER_TOAST;
    public static final ConfigValue<Boolean> TOAST_LIGHT_MODE;

    public static final ConfigValue<Double> SPAWN_COOLDOWN_MULTIPLIER;
    public static final ConfigValue<Integer> SPAWN_WORLD_AGE_REQUIRED;
    public static final ConfigValue<Integer> SPAWN_FAILED_COOLDOWN;
    public static final ConfigValue<Integer> SPAWN_DISTANCE;
    public static final ConfigValue<Integer> SPAWN_DISTANCE_VARIANCE;

    public static void init() {}

    static {
        final var builder = new ConfigBuilder("obscuria/pillager_caravans-common.toml");

        ENABLED = builder
                .comment("Whether Pillager Caravans should be active at all.")
                .defineBoolean("enabled", true);
        SHOW_ENCOUNTER_TOAST = builder
                .comment("Whether to show a toast when a player encounters a caravan.")
                .defineBoolean("showEncounterToast", true);
        TOAST_LIGHT_MODE = builder
                .comment("Whether to use light mode for the toast.")
                .defineBoolean("toastLightMode", false);

        builder.push("SpawnOptions");
        SPAWN_COOLDOWN_MULTIPLIER = builder
                .comment(
                        "Additional multiplier applied to the base cooldowns defined in the datapack.",
                        "Larger caravans usually have longer cooldowns after spawning.",
                        "This value scales all existing cooldowns globally (1.0 = 100%).")
                .defineDouble("spawnCooldownMultiplier", 1.0, 0.0, 1000.0);
        SPAWN_WORLD_AGE_REQUIRED = builder
                .comment(
                        "Minimum world age in ticks required before caravans can start spawning.",
                        "24'000 ticks = 1 Minecraft day.")
                .defineInt("spawnWorldAgeRequired", 24000, 0, Integer.MAX_VALUE);
        SPAWN_FAILED_COOLDOWN = builder
                .comment("Cooldown in ticks after a failed caravan spawn attempt before trying again.")
                .defineInt("spawnFailedCooldown", 1200, 0, Integer.MAX_VALUE);
        SPAWN_DISTANCE = builder
                .comment("Base distance from the player where caravans will spawn.")
                .defineInt("spawnDistance", 32, 0, Integer.MAX_VALUE);
        SPAWN_DISTANCE_VARIANCE = builder
                .comment("Random variance added to the base spawn distance (0~value).")
                .defineInt("spawnDistanceVariance", 32, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.buildCommon(PillagerCaravans.MODID);
    }
}
