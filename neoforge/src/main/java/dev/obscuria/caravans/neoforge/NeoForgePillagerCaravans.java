package dev.obscuria.caravans.neoforge;

import dev.obscuria.caravans.PillagerCaravans;
import net.neoforged.fml.common.Mod;

@Mod(PillagerCaravans.MOD_ID)
public final class NeoForgePillagerCaravans {

    public NeoForgePillagerCaravans() {
        PillagerCaravans.init();
    }
}