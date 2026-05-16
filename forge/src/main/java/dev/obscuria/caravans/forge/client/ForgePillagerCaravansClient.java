package dev.obscuria.caravans.forge.client;

import dev.obscuria.caravans.config.ConfigBuilder;
import lombok.experimental.UtilityClass;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

@UtilityClass
public final class ForgePillagerCaravansClient {

    public static void init() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                ForgePillagerCaravansClient::createConfigFactory);
    }

    private static ConfigScreenHandler.ConfigScreenFactory createConfigFactory() {
        return new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> ConfigBuilder.createConfigScreen(parent));
    }
}
