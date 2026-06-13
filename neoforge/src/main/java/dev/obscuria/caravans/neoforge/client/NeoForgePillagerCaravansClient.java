package dev.obscuria.caravans.neoforge.client;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.config.ConfigBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = PillagerCaravans.MOD_ID, dist = Dist.CLIENT)
public class NeoForgePillagerCaravansClient {

    public NeoForgePillagerCaravansClient(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                NeoForgePillagerCaravansClient::createConfigScreen);
    }

    private static Screen createConfigScreen(ModContainer container, Screen parent) {
        return ConfigBuilder.createConfigScreen(parent);
    }
}