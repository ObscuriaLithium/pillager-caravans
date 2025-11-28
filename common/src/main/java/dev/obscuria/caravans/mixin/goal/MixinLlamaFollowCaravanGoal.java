package dev.obscuria.caravans.mixin.goal;

import dev.obscuria.caravans.content.ILivingExtension;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LlamaFollowCaravanGoal.class)
public abstract class MixinLlamaFollowCaravanGoal {

    @Shadow @Final public Llama llama;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void modifyCanUse(CallbackInfoReturnable<Boolean> info) {
        if (!ILivingExtension.isCaravanMember(llama)) return;
        if (!llama.isLeashed()) return;
        info.setReturnValue(false);
    }
}
