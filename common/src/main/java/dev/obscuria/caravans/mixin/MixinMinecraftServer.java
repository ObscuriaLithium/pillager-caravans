package dev.obscuria.caravans.mixin;

import dev.obscuria.caravans.content.Hooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.CustomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @ModifyVariable(method = "createLevels", at = @At(value = "STORE", ordinal = 0))
    private List<CustomSpawner> injectSpawners(List<CustomSpawner> spawners) {
        return Hooks.injectSpawners(spawners);
    }
}
