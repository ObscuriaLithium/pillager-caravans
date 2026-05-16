package dev.obscuria.caravans;

import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.caravans.common.network.ClientboundEncounterPayload;
import dev.obscuria.caravans.common.registry.CaravanRegistries;
import dev.obscuria.caravans.server.CaravanCommand;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import dev.obscuria.fragmentum.packs.BuiltInPackBuilder;
import dev.obscuria.fragmentum.server.FragmentumServerRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PillagerCaravans {

    public static final String MODID = "caravans";
    public static final String DISPLAY_NAME = "Pillager Caravans";
    public static final Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_CARAVAN_SPAWNING;

    public static ResourceLocation identifier(String name) {
        return new ResourceLocation(MODID, name);
    }

    public static void init() {
        CommonConfig.init();
        CaravanRegistries.init();
        FragmentumServerRegistry.registerCommand(CaravanCommand::register);
        registerPayloads();
        registerBuiltInPacks();
    }

    private static void registerPayloads() {
        final var registrar = FragmentumNetworking.registrar(MODID);
        registrar.registerClientbound(ClientboundEncounterPayload.class,
                ClientboundEncounterPayload::encode,
                ClientboundEncounterPayload::decode,
                ClientboundEncounterPayload::handle);
    }

    private static void registerBuiltInPacks() {
        BuiltInPackBuilder.dataPack("packs/classic_caravans")
                .displayName(Component.literal("Classic Caravans"))
                .packSource(PackSource.BUILT_IN)
                .register(PillagerCaravans.MODID);
    }

    static {
        RULE_DO_CARAVAN_SPAWNING = GameRules.register("doCaravanSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
    }
}
