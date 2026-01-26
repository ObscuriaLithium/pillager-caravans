package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.caravans.content.Hooks;
import net.minecraft.world.entity.PathfinderMob;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PathfinderMob.class)
public abstract class MixinPathfinderMob {

    @ModifyVariable(method = "tickLeash", name = "f", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;onLeashDistance(F)V"))
    private float injectTeleportAbility(float value) {
        if (!CommonConfig.ALLOW_LEASHED_TELEPORT.get()) return value;
        var entity = (PathfinderMob) (Object) this;
        if (Hooks.passIfNotCaravanMember(entity)) return value;
        @Nullable var owner = entity.getLeashHolder();
        if (owner == null || Hooks.passIfNotCaravanMember(owner)) return value;
        Hooks.onLeashDistance(entity, value);
        return Math.min(value, 8F);
    }
}
