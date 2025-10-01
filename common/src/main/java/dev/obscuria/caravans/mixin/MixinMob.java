package dev.obscuria.caravans.mixin;

import dev.obscuria.caravans.world.Hooks;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MixinMob {

    @Inject(method = "tickLeash", at = @At("HEAD"))
    private void onLeashTick(CallbackInfo info) {
        Hooks.onLeashTick((Mob) (Object) this);
    }
}
