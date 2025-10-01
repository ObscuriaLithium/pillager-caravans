package dev.obscuria.caravans.world;

import com.google.common.collect.ImmutableList;
import dev.obscuria.caravans.world.caravans.CaravanSpawner;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.CustomSpawner;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
public interface Hooks {

    static ImmutableList<CustomSpawner> injectSpawners(List<CustomSpawner> spawners) {
        return ImmutableList.<CustomSpawner>builder().addAll(spawners).add(new CaravanSpawner()).build();
    }

    static void onLeashTick(Mob mob) {
        final @Nullable var holder = mob.getLeashHolder();
        if (holder == null || !holder.isRemoved()) return;
        if (holder.getRemovalReason() != Entity.RemovalReason.DISCARDED) return;
        mob.dropLeash(false, false);
        mob.discard();
    }
}
