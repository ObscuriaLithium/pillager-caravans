package dev.obscuria.caravans.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.obscuria.caravans.content.ILivingExtension;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Llama.class)
public abstract class MixinLlama {

    @WrapOperation(method = "finalizeSpawn", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/horse/Llama;setRandomStrength(Lnet/minecraft/util/RandomSource;)V"))
    private void overrideInitialStrength(Llama llama, RandomSource random, Operation<Void> original) {
        if (ILivingExtension.isCaravanMember(llama)) {
            this.setStrength(5);
        } else {
            original.call(llama, random);
        }
    }

    @Shadow protected abstract void setStrength(int strength);
}
