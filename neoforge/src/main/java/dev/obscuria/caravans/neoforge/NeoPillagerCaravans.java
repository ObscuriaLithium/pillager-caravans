package dev.obscuria.caravans.neoforge;

import dev.obscuria.caravans.PillagerCaravans;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(PillagerCaravans.MODID)
public class NeoPillagerCaravans {

    public NeoPillagerCaravans(IEventBus bus) {
        PillagerCaravans.init();
    }
}