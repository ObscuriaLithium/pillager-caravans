package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.common.Hooks;
import dev.obscuria.caravans.config.CommonConfig;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.PathfinderMob;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Leashable.class)
public interface MixinPathfinderMob {

    @Inject(method = "leashTooFarBehaviour", at = @At("HEAD"), cancellable = true)
    private void modifyLeashTooFarBehavior(CallbackInfo ci) {
        if (!CommonConfig.ALLOW_LEASHED_TELEPORT.get()) return;
        if (!(this instanceof PathfinderMob entity)) return;
        if (Hooks.passIfNotCaravanMember(entity)) return;
        @Nullable var owner = entity.getLeashHolder();
        if (owner == null || Hooks.passIfNotCaravanMember(owner)) return;
        Hooks.leashTooFarBehavior(entity);
        ci.cancel();
    }
}
