package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.content.Hooks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Leashable.class)
public interface MixinLeashable {

    @Inject(method = "tickLeash", at = @At("HEAD"))
    private static void onLeashTick(Entity entity, CallbackInfo info) {
        Hooks.onLeashTick(entity);
    }
}
