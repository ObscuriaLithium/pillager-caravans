package dev.obscuria.caravans.content;

import com.google.common.collect.ImmutableList;
import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.caravans.content.caravan.CaravanSpawner;
import dev.obscuria.caravans.content.network.ClientboundEncounterPayload;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import net.minecraft.server.level.ServerPlayer;
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

    static void onLivingTick(LivingEntity entity) {
        if (!CommonConfig.SHOW_ENCOUNTER_TOAST.get()) return;
        if (entity.level().isClientSide()) return;
        if (entity.tickCount % 40 != 0) return;
        final @Nullable var caravanType = ILivingExtension.getCaravanType(entity);
        if (caravanType == null) return;

        final var playerTracker = ILivingExtension.playerTracker(entity);
        final var players = entity.level().getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(32));
        if (players.isEmpty()) return;
        for (var player : players) {
            if (playerTracker.contains(player.getUUID())) continue;
            FragmentumNetworking.sendTo(player, ClientboundEncounterPayload.forCaravan(caravanType));
            playerTracker.add(player.getUUID());
        }
    }

    static void onLeashTick(Entity entity) {
        if (!(entity instanceof Mob mob)) return;
        final @Nullable var holder = mob.getLeashHolder();
        if (holder == null || !holder.isRemoved()) return;
        if (holder.getRemovalReason() != Entity.RemovalReason.DISCARDED) return;
        if (!(holder instanceof LivingEntity living) || !ILivingExtension.isCaravanMember(living)) return;
        mob.dropLeash(false, false);
        mob.discard();
    }
}
