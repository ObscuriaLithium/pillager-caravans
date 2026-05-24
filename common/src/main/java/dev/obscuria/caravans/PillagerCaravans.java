package dev.obscuria.caravans;

import dev.obscuria.caravans.common.network.ClientboundEncounterPayload;
import dev.obscuria.caravans.common.registry.CaravanRegistries;
import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.caravans.server.CaravanCommand;
import dev.obscuria.fragmentum.v2.api.common.network.FragmentumNetworking;
import dev.obscuria.fragmentum.v2.api.common.packs.BuiltInPackBuilder;
import dev.obscuria.fragmentum.v2.api.server.FragmentumServerRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PillagerCaravans {

    public static final String MOD_ID = "caravans";
    public static final String DISPLAY_NAME = "Pillager Caravans";
    public static final Logger LOG = LoggerFactory.getLogger(DISPLAY_NAME);

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        CommonConfig.init();
        CaravanRegistries.init();
        FragmentumServerRegistry.registerCommand(CaravanCommand::register);
        registerPayloads();
        registerBuiltInPacks();
    }

    private static void registerPayloads() {
        final var registrar = FragmentumNetworking.registrar(MOD_ID);
        registrar.registerClientbound(
                ClientboundEncounterPayload.class,
                ClientboundEncounterPayload.TYPE,
                ClientboundEncounterPayload.STREAM_CODEC,
                ClientboundEncounterPayload::handle);
    }

    private static void registerBuiltInPacks() {
        BuiltInPackBuilder.dataPack("packs/classic_caravans")
                .displayName(Component.literal("Classic Caravans"))
                .packSource(PackSource.BUILT_IN)
                .register(PillagerCaravans.class, PillagerCaravans.MOD_ID);
    }
}
