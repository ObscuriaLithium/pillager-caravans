package dev.obscuria.caravans.common.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.common.CaravanPlacement;
import dev.obscuria.caravans.common.CaravanVariation;
import dev.obscuria.caravans.common.data.CaravanData;
import dev.obscuria.fragmentum.v2.api.common.registry.BootstrapContext;
import dev.obscuria.fragmentum.v2.api.common.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.v2.api.common.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface CaravanRegistries {

    Registrar REGISTRAR = FragmentumRegistry.registrar(PillagerCaravans.MOD_ID);

    Registry<MapCodec<? extends CaravanData>> DATA_TYPE = REGISTRAR.createRegistry(Keys.DATA_TYPE);

    interface Keys {

        ResourceKey<Registry<MapCodec<? extends CaravanData>>> DATA_TYPE = create("data_type");
        ResourceKey<Registry<CaravanVariation>> VARIATION = create("variation");
        ResourceKey<Registry<CaravanPlacement>> PLACEMENT = create("placement");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(PillagerCaravans.id(name));
        }
    }

    static void init() {
        CaravanGameRules.init();
        CaravanData.bootstrap(BootstrapContext.create(REGISTRAR, Keys.DATA_TYPE, PillagerCaravans::id));
        REGISTRAR.createDataRegistry(Keys.VARIATION, () -> CaravanVariation.DIRECT_CODEC);
        REGISTRAR.createDataRegistry(Keys.PLACEMENT, () -> CaravanPlacement.DIRECT_CODEC);
    }
}
