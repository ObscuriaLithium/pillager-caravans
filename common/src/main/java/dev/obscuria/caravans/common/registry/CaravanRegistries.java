package dev.obscuria.caravans.common.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.common.CaravanPlacement;
import dev.obscuria.caravans.common.CaravanVariation;
import dev.obscuria.caravans.common.data.CaravanData;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.fragmentum.content.registry.DelegatedRegistry;
import dev.obscuria.fragmentum.content.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.content.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class CaravanRegistries {

    private static final Registrar REGISTRAR = FragmentumRegistry.registrar(PillagerCaravans.MOD_ID);

    public static final DelegatedRegistry<MapCodec<? extends CaravanData>> DATA_TYPE = REGISTRAR.createRegistry(Keys.DATA_TYPE);

    public static final class Keys {

        public static final ResourceKey<Registry<MapCodec<? extends CaravanData>>> DATA_TYPE = create("data_type");
        public static final ResourceKey<Registry<CaravanVariation>> VARIATION = create("variation");
        public static final ResourceKey<Registry<CaravanPlacement>> PLACEMENT = create("placement");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(PillagerCaravans.id(name));
        }
    }

    public static void init() {
        CaravanData.bootstrap(BootstrapContext.create(REGISTRAR, Keys.DATA_TYPE, PillagerCaravans::id));
        REGISTRAR.createDataRegistry(Keys.VARIATION, () -> CaravanVariation.DIRECT_CODEC);
        REGISTRAR.createDataRegistry(Keys.PLACEMENT, () -> CaravanPlacement.DIRECT_CODEC);
    }
}