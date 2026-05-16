package dev.obscuria.caravans.common;

import dev.obscuria.caravans.common.data.CaravanData;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface ILivingExtension {

    @Nullable CaravanData caravans$getCaravanData();

    void caravans$setCaravanData(@Nullable CaravanData data);

    static @Nullable CaravanData getCaravanData(LivingEntity entity) {
        return ((ILivingExtension) entity).caravans$getCaravanData();
    }

    static void setCaravanData(LivingEntity entity, @Nullable CaravanData data) {
        ((ILivingExtension) entity).caravans$setCaravanData(data);
    }

    static boolean isCaravanMember(LivingEntity entity) {
        return getCaravanData(entity) != null;
    }
}
