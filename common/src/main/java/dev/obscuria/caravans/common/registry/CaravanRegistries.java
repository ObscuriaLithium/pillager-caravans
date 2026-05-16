package dev.obscuria.caravans.common.registry;

import com.mojang.serialization.Codec;
import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.common.data.CaravanData;
import dev.obscuria.caravans.common.CaravanPlacement;
import dev.obscuria.caravans.common.CaravanVariation;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.fragmentum.registry.DelegatedRegistry;
import dev.obscuria.fragmentum.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface CaravanRegistries {

    Registrar REGISTRAR = FragmentumRegistry.registrar(PillagerCaravans.MODID);

    DelegatedRegistry<Codec<? extends CaravanData>> DATA_TYPE = REGISTRAR.createRegistry(Keys.DATA_TYPE);

    interface Keys {

        ResourceKey<Registry<Codec<? extends CaravanData>>> DATA_TYPE = create("data_type");
        ResourceKey<Registry<CaravanVariation>> VARIATION = create("variation");
        ResourceKey<Registry<CaravanPlacement>> PLACEMENT = create("placement");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(PillagerCaravans.identifier(name));
        }
    }

    static void init() {
        CaravanData.bootstrap(BootstrapContext.create(REGISTRAR, Keys.DATA_TYPE, PillagerCaravans::identifier));
        REGISTRAR.createDataRegistry(Keys.VARIATION, () -> CaravanVariation.DIRECT_CODEC);
        REGISTRAR.createDataRegistry(Keys.PLACEMENT, () -> CaravanPlacement.DIRECT_CODEC);
    }
}
