package dev.obscuria.caravans.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class CaravanMemberData extends CaravanData {

    public static final Codec<CaravanMemberData> CODEC;

    public static CaravanMemberData create(int despawnDelay) {
        return new CaravanMemberData(despawnDelay);
    }

    private CaravanMemberData(int despawnDelay) {
        super(despawnDelay);
    }

    @Override
    public Codec<CaravanMemberData> codec() {
        return CODEC;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("despawn_delay").forGetter(CaravanMemberData::getDespawnDelay)
        ).apply(codec, CaravanMemberData::new));
    }
}
