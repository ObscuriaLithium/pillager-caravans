package dev.obscuria.caravans.content.caravan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.caravans.content.IWeighted;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record CaravanPool(
        int weight,
        @Unmodifiable List<Entry> variations
) implements IWeighted {

    public static final Codec<CaravanPool> CODEC;

    public @Nullable Holder<CaravanVariation> getRandomVariation(RandomSource random) {
        final @Nullable var entry = IWeighted.pickRandom(variations, random);
        return entry == null ? null : entry.variation();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("weight").forGetter(CaravanPool::weight),
                Entry.CODEC.listOf().fieldOf("variations").forGetter(CaravanPool::variations)
        ).apply(codec, CaravanPool::new));
    }

    public record Entry(
            int weight,
            Holder<CaravanVariation> variation
    ) implements IWeighted {

        public static final Codec<Entry> CODEC;

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    Codec.INT.fieldOf("weight").forGetter(Entry::weight),
                    CaravanVariation.CODEC.fieldOf("variation").forGetter(Entry::variation)
            ).apply(codec, Entry::new));
        }
    }
}
