package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.content.Hooks;
import dev.obscuria.caravans.content.ILivingExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements ILivingExtension {

    @Unique private final List<UUID> caravans$playerTracker = new ArrayList<>();
    @Unique private @Nullable ResourceLocation caravans$caravanType = null;
    @Unique private boolean caravans$caravanMember = false;

    @Override
    public List<UUID> caravans$playerTracker() {
        return caravans$playerTracker;
    }

    @Override
    public void caravans$setCaravanType(@Nullable ResourceLocation type) {
        this.caravans$caravanType = type;
    }

    @Override
    public @Nullable ResourceLocation caravans$getCaravanType() {
        return caravans$caravanType;
    }

    @Override
    public void caravans$setCaravanMember(boolean value) {
        this.caravans$caravanMember = value;
    }

    @Override
    public boolean caravans$isCaravanMember() {
        return caravans$caravanMember;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void customTick(CallbackInfo info) {
        Hooks.onLivingTick((LivingEntity) (Object) this);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addCustomSaveData(CompoundTag compound, CallbackInfo info) {
        compound.putBoolean("IsCaravanMember", caravans$caravanMember);
        if (caravans$caravanType == null) return;
        compound.putString("CaravanType", caravans$caravanType.toString());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomSaveData(CompoundTag compound, CallbackInfo info) {
        caravans$caravanMember = compound.getBoolean("IsCaravanMember");
        if (!compound.contains("CaravanType", Tag.TAG_STRING)) return;
        caravans$caravanType = ResourceLocation.tryParse(compound.getString("CaravanType"));
    }
}
