package dev.obscuria.caravans.mixin.goal;

import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.caravans.content.Hooks;
import net.minecraft.world.entity.monster.PatrollingMonster;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatrollingMonster.LongDistancePatrolGoal.class)
public abstract class MixinLongDistancePatrolGoal<T extends PatrollingMonster> {

    @Shadow @Final private T mob;

    @ModifyArg(method = "findPatrolCompanions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(D)Lnet/minecraft/world/phys/AABB;"))
    private double modifyScanArea(double value) {
        return 48.0;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/PatrollingMonster;setPatrolTarget(Lnet/minecraft/core/BlockPos;)V"))
    private void injectTeleportAbility(CallbackInfo ci, @Local(ordinal = 0) PatrollingMonster companion) {
        if (!CommonConfig.ALLOW_FOLLOWER_TELEPORT.get()) return;
        if (Hooks.passIfNotCaravanMember(companion)) return;
        if (companion.distanceToSqr(this.mob) < 512) return;
        Hooks.teleportTo(companion, this.mob);
    }
}
