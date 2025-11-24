package dev.obscuria.caravans.content.registry;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.content.caravan.CaravanPlacement;
import dev.obscuria.caravans.content.caravan.CaravanVariation;
import dev.obscuria.fragmentum.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface CaravanRegistries {

    Registrar REGISTRAR = FragmentumRegistry.registrar(PillagerCaravans.MODID);

    interface Keys {

        ResourceKey<Registry<CaravanVariation>> VARIATION = create("variation");
        ResourceKey<Registry<CaravanPlacement>> PLACEMENT = create("placement");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(PillagerCaravans.key(name));
        }
    }

    static void init() {
        REGISTRAR.createDataRegistry(Keys.VARIATION, () -> CaravanVariation.DIRECT_CODEC);
        REGISTRAR.createDataRegistry(Keys.PLACEMENT, () -> CaravanPlacement.DIRECT_CODEC);
    }
}
