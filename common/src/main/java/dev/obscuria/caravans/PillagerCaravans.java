package dev.obscuria.caravans;

import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.caravans.common.network.ClientboundEncounterPayload;
import dev.obscuria.caravans.common.registry.CaravanRegistries;
import dev.obscuria.caravans.server.CaravanCommand;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import dev.obscuria.fragmentum.content.packs.BuiltInPackBuilder;
import dev.obscuria.fragmentum.server.FragmentumServerRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PillagerCaravans {

    public static final String MOD_ID = "caravans";
    public static final String MOD_NAME = "Pillager Caravans";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_CARAVAN_SPAWNING;

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
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
        registrar.registerClientbound(ClientboundEncounterPayload.class,
                ClientboundEncounterPayload.TYPE,
                ClientboundEncounterPayload.CODEC,
                ClientboundEncounterPayload::handle);
    }

    private static void registerBuiltInPacks() {
        BuiltInPackBuilder.dataPack("packs/classic_caravans")
                .displayName(Component.literal("Classic Caravans"))
                .packSource(PackSource.BUILT_IN)
                .register(PillagerCaravans.class, MOD_ID);
    }

    static {
        RULE_DO_CARAVAN_SPAWNING = GameRules.register("doCaravanSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
    }
}
