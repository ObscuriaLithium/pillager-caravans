package dev.obscuria.caravans.forge;

import dev.obscuria.caravans.PillagerCaravans;
import net.minecraftforge.fml.common.Mod;

@Mod(PillagerCaravans.MODID)
public class ForgePillagerCaravans {

    public ForgePillagerCaravans() {
        PillagerCaravans.init();
    }
}