package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.common.ILivingExtension;
import dev.obscuria.caravans.common.data.CaravanData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    private void addCustomSaveData(ValueOutput output, CallbackInfo ci) {
        CaravanData.save(caravans$caravanData, output);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomSaveData(ValueInput input, CallbackInfo ci) {
        caravans$caravanData = CaravanData.load(input);
    }
}
