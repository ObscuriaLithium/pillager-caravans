package dev.obscuria.caravans.neoforge;

import dev.obscuria.caravans.PillagerCaravans;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(PillagerCaravans.MOD_ID)
public class NeoForgePillagerCaravans {

    public NeoForgePillagerCaravans(IEventBus bus) {
        PillagerCaravans.init();
    }
}