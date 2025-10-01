package dev.obscuria.caravans.registry;

import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.world.caravans.CaravanPlacement;
import dev.obscuria.caravans.world.caravans.CaravanVariation;
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
        REGISTRAR.createSyncedDataRegistry(Keys.VARIATION, () -> CaravanVariation.DIRECT_CODEC);
        REGISTRAR.createSyncedDataRegistry(Keys.PLACEMENT, () -> CaravanPlacement.DIRECT_CODEC);
    }
}
