package dev.obscuria.caravans.mixin.entity;

import dev.obscuria.caravans.content.IHorseExtension;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractHorse.class)
public abstract class MixinAbstractHorse implements IHorseExtension {

    @Shadow protected SimpleContainer inventory;

    @Shadow protected abstract void createInventory();

    @Override
    public void caravans$createInventory() {
        this.createInventory();
    }

    @Override
    public SimpleContainer caravans$getInventory() {
        return this.inventory;
    }
}
