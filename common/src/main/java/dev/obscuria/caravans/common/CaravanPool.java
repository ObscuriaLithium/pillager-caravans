package dev.obscuria.caravans.common;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public record CaravanPool(
        int weight,
        List<Entry> variations
) implements IWeighted {

    public static final Codec<CaravanPool> CODEC;

    public @Nullable Holder<CaravanVariation> getRandomVariation(RandomSource random) {
        @Nullable var entry = IWeighted.pickRandom(variations, random);
        return entry == null ? null : entry.variation();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("weight").forGetter(CaravanPool::weight),
                Entry.CODEC.listOf().fieldOf("variations").forGetter(CaravanPool::variations)
        ).apply(codec, CaravanPool::new));
    }

    private record Entry(Holder<CaravanVariation> variation) implements IWeighted {

        public static final Codec<Entry> CODEC;
        private static final Codec<Entry> V1_CODEC; // Legacy format
        private static final Codec<Entry> v2_CODEC; // Actual format

        @Override
        public int weight() {
            return variation.value().spawnWeight();
        }

        private static Entry unwrap(Either<Entry, Entry> either) {
            return either.map(Function.identity(), Function.identity());
        }

        static {
            V1_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    CaravanVariation.CODEC.fieldOf("variation").forGetter(Entry::variation)
            ).apply(codec, Entry::new));
            v2_CODEC = CaravanVariation.CODEC.xmap(Entry::new, Entry::variation);
            CODEC = Codec.either(V1_CODEC, v2_CODEC).xmap(Entry::unwrap, Either::left);
        }
    }
}