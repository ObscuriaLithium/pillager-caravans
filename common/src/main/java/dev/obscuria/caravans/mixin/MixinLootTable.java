package dev.obscuria.caravans.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.obscuria.caravans.content.caravans.CaravanMember;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LootTable.class)
public abstract class MixinLootTable {

    @WrapOperation(method = "fill", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;)V"))
    private void suppressWarn(Logger instance, String warn, Operation<Void> original) {
        if (CaravanMember.generatingCaravanCargo) return;
        original.call(instance, warn);
    }
}
