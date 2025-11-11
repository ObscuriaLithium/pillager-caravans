package dev.obscuria.caravans.content;

import com.google.common.collect.ImmutableList;
import dev.obscuria.caravans.content.caravans.CaravanSpawner;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.CustomSpawner;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Hooks {

    static ImmutableList<CustomSpawner> injectSpawners(List<CustomSpawner> spawners) {
        return ImmutableList.<CustomSpawner>builder().addAll(spawners).add(CaravanSpawner.INSTANCE).build();
    }

    static void onLeashTick(Mob mob) {
        final @Nullable var holder = mob.getLeashHolder();
        if (holder == null || !holder.isRemoved()) return;
        if (holder.getRemovalReason() != Entity.RemovalReason.DISCARDED) return;
        if (!(holder instanceof LivingEntity living) || !ILivingExtension.isCaravanMember(living)) return;
        mob.dropLeash(false, false);
        mob.discard();
    }
}
