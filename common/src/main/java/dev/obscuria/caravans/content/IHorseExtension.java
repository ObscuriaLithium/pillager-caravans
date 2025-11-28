package dev.obscuria.caravans.content;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public interface IHorseExtension {

    void caravans$createInventory();

    SimpleContainer caravans$getInventory();

    static void createInventory(AbstractHorse entity) {
        ((IHorseExtension)entity).caravans$createInventory();
    }

    static SimpleContainer getInventory(AbstractHorse entity) {
        return ((IHorseExtension)entity).caravans$getInventory();
    }
}
