package dev.obscuria.caravans.content.caravans;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.caravans.content.registry.CaravanRegistries;
import dev.obscuria.caravans.content.IWeighted;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record CaravanPlacement(
        int weight,
        HolderSet<Biome> biomes,
        @Unmodifiable List<CaravanPool> pools
) implements IWeighted {

    public static final Codec<Holder<CaravanPlacement>> CODEC;
    public static final Codec<CaravanPlacement> DIRECT_CODEC;

    public boolean isFor(Holder<Biome> biome) {
        return biomes.contains(biome);
    }

    public @Nullable Holder<CaravanVariation> getRandomVariation(RandomSource random) {
        final @Nullable var pool = IWeighted.pickRandom(pools, random);
        return pool == null ? null : pool.getRandomVariation(random);
    }

    static {
        CODEC = RegistryFixedCodec.create(CaravanRegistries.Keys.PLACEMENT);
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("weight").forGetter(CaravanPlacement::weight),
                RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(CaravanPlacement::biomes),
                CaravanPool.CODEC.listOf().fieldOf("pools").forGetter(CaravanPlacement::pools)
        ).apply(codec, CaravanPlacement::new));
    }
}
