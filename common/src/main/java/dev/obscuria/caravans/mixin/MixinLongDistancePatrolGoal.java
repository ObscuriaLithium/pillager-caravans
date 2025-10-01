package dev.obscuria.caravans.mixin;

import net.minecraft.world.entity.monster.PatrollingMonster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PatrollingMonster.LongDistancePatrolGoal.class)
public abstract class MixinLongDistancePatrolGoal {

    @ModifyArg(method = "findPatrolCompanions", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/AABB;inflate(D)Lnet/minecraft/world/phys/AABB;"))
    private double modifyScanArea(double value) {
        return 48.0;
    }
}
