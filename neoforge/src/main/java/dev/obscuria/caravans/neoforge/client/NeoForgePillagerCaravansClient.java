package dev.obscuria.caravans.neoforge.client;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.config.ConfigBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = PillagerCaravans.MOD_ID, dist = Dist.CLIENT)
public final class NeoForgePillagerCaravansClient {

    public NeoForgePillagerCaravansClient() {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                NeoForgePillagerCaravansClient::createConfigFactory);
    }

    private static IConfigScreenFactory createConfigFactory() {
        return (_, parent) -> ConfigBuilder.createConfigScreen(parent);
    }
}
