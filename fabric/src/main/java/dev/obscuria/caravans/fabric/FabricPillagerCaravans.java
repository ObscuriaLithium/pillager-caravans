package dev.obscuria.caravans.fabric;

import dev.obscuria.caravans.PillagerCaravans;
import net.fabricmc.api.ModInitializer;

public class FabricPillagerCaravans implements ModInitializer {

    @Override
    public void onInitialize() {
        PillagerCaravans.init();
    }
}