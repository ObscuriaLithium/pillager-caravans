package dev.obscuria.caravans;

import dev.obscuria.caravans.config.CaravanConfig;
import dev.obscuria.caravans.content.network.ClientboundEncounterPayload;
import dev.obscuria.caravans.content.registry.CaravanRegistries;
import dev.obscuria.caravans.server.CaravanCommand;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import dev.obscuria.fragmentum.server.FragmentumServerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public final class PillagerCaravans {

    public static final String MODID = "caravans";
    public static final String DISPLAY_NAME = "Pillager Caravans";
    public static final Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);

    public static final ICaravanServices SERVICES;
    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_CARAVAN_SPAWNING;

    public static ResourceLocation key(String name) {
        return new ResourceLocation(MODID, name);
    }

    public static void init() {
        CaravanConfig.init();
        CaravanRegistries.init();
        FragmentumServerRegistry.registerCommand(CaravanCommand::register);
        registerPayloads();
    }

    private static void registerPayloads() {
        final var registrar = FragmentumNetworking.registrar(MODID);
        registrar.registerClientbound(ClientboundEncounterPayload.class,
                ClientboundEncounterPayload::encode,
                ClientboundEncounterPayload::decode,
                ClientboundEncounterPayload::handle);
    }

    static {
        SERVICES = ServiceLoader.load(ICaravanServices.class).findFirst().orElseThrow();
        RULE_DO_CARAVAN_SPAWNING = SERVICES.registerGameRule("doCaravanSpawning", GameRules.Category.SPAWNING, true);
    }
}
