package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.common.data.CaravanData;
import dev.obscuria.caravans.common.ILivingExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements ILivingExtension {

    @Unique private @Nullable CaravanData caravans$caravanData = null;

    @Override
    public @Nullable CaravanData caravans$getCaravanData() {
        return caravans$caravanData;
    }

    @Override
    public void caravans$setCaravanData(@Nullable CaravanData data) {
        this.caravans$caravanData = data;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void customAiStep(CallbackInfo info) {
        if (caravans$caravanData == null) return;
        var self = (LivingEntity) (Object) this;
        this.caravans$caravanData.aiStep(self);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addCustomSaveData(CompoundTag compound, CallbackInfo info) {
        CaravanData.save(caravans$caravanData, compound);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomSaveData(CompoundTag compound, CallbackInfo info) {
        caravans$caravanData = CaravanData.load(compound);
    }
}
