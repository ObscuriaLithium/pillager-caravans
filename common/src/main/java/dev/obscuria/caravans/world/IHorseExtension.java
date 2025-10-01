package dev.obscuria.caravans.world;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;

public interface IHorseExtension {

    void caravans$createInventory();

    SimpleContainer caravans$getInventory();

    void caravans$equipArmor(ItemStack stack);

    static void createInventory(AbstractHorse entity) {
        ((IHorseExtension)entity).caravans$createInventory();
    }

    static SimpleContainer getInventory(AbstractHorse entity) {
        return ((IHorseExtension)entity).caravans$getInventory();
    }

    static void equipArmor(AbstractHorse horse, ItemStack stack) {
        ((IHorseExtension)horse).caravans$equipArmor(stack);
    }
}
