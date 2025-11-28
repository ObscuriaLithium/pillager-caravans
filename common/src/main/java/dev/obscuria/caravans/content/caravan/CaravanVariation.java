package dev.obscuria.caravans.content.caravan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.caravans.content.registry.CaravanRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record CaravanVariation(
        int cooldown,
        CaravanMember leader,
        @Unmodifiable List<CaravanMember> members
) {

    public static final Codec<Holder<CaravanVariation>> CODEC;
    public static final Codec<CaravanVariation> DIRECT_CODEC;

    public int actualCooldown() {
        return (int) (cooldown * CommonConfig.SPAWN_COOLDOWN_MULTIPLIER.get());
    }

    public int spawn(Holder<CaravanVariation> self, ServerLevel level, BlockPos position) {

        final var random = level.random;
        final var pos = position.mutable();
        pos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY());
        if (leader.spawn(self, level, pos, true) == null) return 0;

        final var entities = new ArrayList<Entity>();
        for (var member : members) {
            for (var i = 0; i < member.count(); i++) {
                pos.setX(pos.getX() + random.nextInt(3) - random.nextInt(3));
                pos.setZ(pos.getZ() + random.nextInt(3) - random.nextInt(3));
                pos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY());
                final @Nullable var entity = member.spawn(self, level, pos, false);
                if (entity != null) entities.add(entity);
            }
        }

        leashAnimals(entities, random);
        return 1 + entities.size();
    }

    private void leashAnimals(List<Entity> entities, RandomSource random) {

        @Nullable Animal lastAnimal = null;
        final var holders = entities.stream()
                .filter(this::isValidLeashHolder)
                .collect(Collectors.toCollection(ArrayList::new));

        for (var member : entities) {
            if (!(member instanceof Animal animal)) continue;

            if (lastAnimal != null && random.nextInt(4) < 3) {
                animal.setLeashedTo(lastAnimal, true);
            } else {
                Collections.shuffle(holders);
                final @Nullable var holder = holders.isEmpty() ? null : holders.get(0);
                if (holder != null) animal.setLeashedTo(holder, true);
            }

            lastAnimal = animal;
        }
    }

    private boolean isValidLeashHolder(Entity entity) {
        return entity instanceof AbstractIllager;
    }

    static {
        CODEC = RegistryFixedCodec.create(CaravanRegistries.Keys.VARIATION);
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("cooldown").forGetter(CaravanVariation::cooldown),
                CaravanMember.CODEC.fieldOf("leader").forGetter(CaravanVariation::leader),
                CaravanMember.CODEC.listOf().fieldOf("members").forGetter(CaravanVariation::members)
        ).apply(codec, CaravanVariation::new));
    }
}
