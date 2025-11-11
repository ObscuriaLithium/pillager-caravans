package dev.obscuria.caravans.config;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.fragmentum.config.ConfigLayout;
import dev.obscuria.fragmentum.config.ConfigOptions;
import dev.obscuria.fragmentum.config.ConfigRegistry;

public final class CaravanConfig {

    public static final Layout common = new Layout();

    public static void init() {
        final var fileName = "obscuria/pillager_caravans-common.toml";
        ConfigRegistry.registerCommon(PillagerCaravans.MODID, fileName, common, CaravanConfig::update);
    }

    private static void update(Layout layout) {}

    public static class Layout implements ConfigLayout {

        @ConfigOptions.Section("Spawning")
        public final Spawning spawning = new Spawning();

        public static final class Spawning {

            @ConfigOptions.Value("worldAgeRequired")
            @ConfigOptions.Range(min = 0)
            @ConfigOptions.Comment("Minimum world age in ticks required before caravans can start spawning."
                    + "//24000 ticks = 1 Minecraft day.")
            public int worldAgeRequired = 24000;

            @ConfigOptions.Value("cooldownMultiplier")
            @ConfigOptions.Range(min = 0.1, max = 100.0)
            @ConfigOptions.Comment("Additional multiplier applied to the base cooldowns defined in the datapack."
                    + "//Larger caravans usually have longer cooldowns after spawning."
                    + "//This value scales all existing cooldowns globally (1.0 = 100%).")
            public double cooldownMultiplier = 1.0;

            @ConfigOptions.Value("failedCooldown")
            @ConfigOptions.Range(min = 200)
            @ConfigOptions.Comment("Cooldown in ticks after a failed caravan spawn attempt before trying again.")
            public int failedCooldown = 1200;

            @ConfigOptions.Value("distance")
            @ConfigOptions.Range(min = 8, max = 256)
            @ConfigOptions.Comment("Base distance from the player where caravans will spawn.")
            public int distance = 32;

            @ConfigOptions.Value("distanceVariance")
            @ConfigOptions.Range(min = 0, max = 256)
            @ConfigOptions.Comment("Random variance added to the base spawn distance (0~value).")
            public int distanceVariance = 32;
        }
    }
}
