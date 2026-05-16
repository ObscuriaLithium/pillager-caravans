package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.common.Hooks;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MixinMob {

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void customBaseTick(CallbackInfo info) {
        Hooks.onMobTick((Mob) (Object) this);
    }
}
