package dev.obscuria.caravans.forge;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.forge.client.ForgePillagerCaravansClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(PillagerCaravans.MODID)
public final class ForgePillagerCaravans {

    public ForgePillagerCaravans() {
        PillagerCaravans.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ForgePillagerCaravansClient::init);
    }
}