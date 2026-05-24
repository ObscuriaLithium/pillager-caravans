package dev.obscuria.caravans.common;

import com.mojang.serialization.Codec;
import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.fragmentum.v2.api.config.ConfigValue;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum CaravanTier implements StringRepresentable {
    SMALL(
            CommonConfig.SMALL_CARAVAN_SPAWN_WEIGHT,
            CommonConfig.SMALL_CARAVAN_SPAWN_COOLDOWN,
            CommonConfig.SMALL_CARAVAN_DESPAWN_DELAY),
    MEDIUM(
            CommonConfig.MEDIUM_CARAVAN_SPAWN_WEIGHT,
            CommonConfig.MEDIUM_CARAVAN_SPAWN_COOLDOWN,
            CommonConfig.MEDIUM_CARAVAN_DESPAWN_DELAY),
    LARGE(
            CommonConfig.LARGE_CARAVAN_SPAWN_WEIGHT,
            CommonConfig.LARGE_CARAVAN_SPAWN_COOLDOWN,
            CommonConfig.LARGE_CARAVAN_DESPAWN_DELAY);

    public static final Codec<CaravanTier> CODEC;

    private final ConfigValue<Integer> spawnWeightValue;
    private final ConfigValue<Integer> spawnCooldownValue;
    private final ConfigValue<Integer> despawnDelayValue;

    CaravanTier(ConfigValue<Integer> spawnWeightValue,
                ConfigValue<Integer> spawnCooldownValue,
                ConfigValue<Integer> despawnDelayValue
    ) {
        this.spawnWeightValue = spawnWeightValue;
        this.spawnCooldownValue = spawnCooldownValue;
        this.despawnDelayValue = despawnDelayValue;
    }

    public int spawnWeight() {
        return spawnWeightValue.get();
    }

    public int spawnCooldown() {
        return spawnCooldownValue.get();
    }

    public int despawnDelay() {
        return despawnDelayValue.get();
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    static {
        CODEC = StringRepresentable.fromEnum(CaravanTier::values);
    }
}
