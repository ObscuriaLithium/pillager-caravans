package dev.obscuria.caravans.mixin;

import dev.obscuria.caravans.world.ILivingExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements ILivingExtension {

    @Unique private boolean caravans$caravanMember = false;

    @Override
    public void caravans$setCaravanMember(boolean value) {
        this.caravans$caravanMember = value;
    }

    @Override
    public boolean caravans$isCaravanMember() {
        return caravans$caravanMember;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addCustomSaveData(CompoundTag compound, CallbackInfo info) {
        compound.putBoolean("IsCaravanMember", caravans$caravanMember);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomSaveData(CompoundTag compound, CallbackInfo info) {
        caravans$caravanMember = compound.getBoolean("IsCaravanMember");
    }
}
