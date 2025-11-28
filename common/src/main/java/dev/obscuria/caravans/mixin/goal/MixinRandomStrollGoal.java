package dev.obscuria.caravans.mixin.goal;

import dev.obscuria.caravans.content.ILivingExtension;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomStrollGoal.class)
public abstract class MixinRandomStrollGoal {

    @Shadow @Final protected PathfinderMob mob;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void modifyCanUse(CallbackInfoReturnable<Boolean> info) {
        if (!ILivingExtension.isCaravanMember(mob)) return;
        if (!mob.isLeashed()) return;
        info.setReturnValue(false);
    }
}
