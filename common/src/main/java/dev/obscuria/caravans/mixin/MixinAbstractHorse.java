package dev.obscuria.caravans.mixin;

import dev.obscuria.caravans.content.IHorseExtension;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractHorse.class)
public abstract class MixinAbstractHorse implements IHorseExtension {

    @Shadow protected SimpleContainer inventory;

    @Shadow protected abstract void createInventory();

    @Shadow public abstract boolean isArmor(ItemStack stack);

    @Override
    public void caravans$createInventory() {
        this.createInventory();
    }

    @Override
    public SimpleContainer caravans$getInventory() {
        return this.inventory;
    }

    @Override
    public void caravans$equipArmor(ItemStack stack) {
        if (!this.isArmor(stack)) return;
        this.inventory.setItem(1, stack);
    }

}
