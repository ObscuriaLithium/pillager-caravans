package dev.obscuria.caravans.world;

import net.minecraft.world.entity.LivingEntity;

public interface ILivingExtension {

    void caravans$setCaravanMember(boolean value);

    boolean caravans$isCaravanMember();

    static void setCaravanMember(LivingEntity entity, boolean value) {
        ((ILivingExtension)entity).caravans$setCaravanMember(value);
    }

    static boolean isCaravanMember(LivingEntity entity) {
        return ((ILivingExtension)entity).caravans$isCaravanMember();
    }
}
