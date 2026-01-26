package dev.obscuria.caravans.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ICaravanMember {

    List<UUID> caravans$playerTracker();

    void caravans$setCaravanType(@Nullable ResourceLocation type);

    @Nullable ResourceLocation caravans$getCaravanType();

    void caravans$setCaravanMember(boolean value);

    boolean caravans$isCaravanMember();

    static List<UUID> playerTracker(LivingEntity entity) {
        return ((ICaravanMember)entity).caravans$playerTracker();
    }

    static void setCaravanType(LivingEntity entity, @Nullable ResourceLocation type) {
        ((ICaravanMember)entity).caravans$setCaravanType(type);
    }

    static @Nullable ResourceLocation getCaravanType(LivingEntity entity) {
        return ((ICaravanMember)entity).caravans$getCaravanType();
    }

    static void setCaravanMember(LivingEntity entity, boolean value) {
        ((ICaravanMember)entity).caravans$setCaravanMember(value);
    }

    static boolean isCaravanMember(LivingEntity entity) {
        return ((ICaravanMember)entity).caravans$isCaravanMember();
    }
}
