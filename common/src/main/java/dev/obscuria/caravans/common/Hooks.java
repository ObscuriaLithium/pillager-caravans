package dev.obscuria.caravans.common;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Hooks {

    static ImmutableList<CustomSpawner> injectSpawners(List<CustomSpawner> spawners) {
        return ImmutableList.<CustomSpawner>builder().addAll(spawners).add(CaravanSpawner.INSTANCE).build();
    }

    static boolean passIfNotCaravanMember(Entity entity) {
        return !(entity instanceof LivingEntity living) || !ILivingExtension.isCaravanMember(living);
    }

    static void onMobTick(Mob entity) {
        if (passIfNotCaravanMember(entity)) return;
        @Nullable var owner = entity.getLeashHolder();
        if (owner == null || passIfNotCaravanMember(owner)) return;
        entity.getLookControl().setLookAt(owner, 10.0F, (float) entity.getMaxHeadXRot());
    }

    static void onLeashDistance(PathfinderMob entity, float distance) {
        if (distance < 9.0F) return;
        @Nullable var owner = entity.getLeashHolder();
        if (owner == null) return;
        teleportTo(entity, owner);
    }

    static void teleportTo(Mob entity, Entity owner) {
        var pos = owner.blockPosition();
        for (int i = 0; i < 10; ++i) {
            var x = randomIntInclusive(entity, -3, 3);
            var y = randomIntInclusive(entity, -1, 1);
            var z = randomIntInclusive(entity, -3, 3);
            if (maybeTeleportTo(entity, owner, pos.getX() + x, pos.getY() + y, pos.getZ() + z)) return;
        }
    }

    private static boolean maybeTeleportTo(Mob entity, Entity owner, int x, int y, int z) {
        if (Math.abs(x - owner.getX()) < 2.0F && Math.abs(z - owner.getZ()) < 2.0F) {
            return false;
        } else if (!canTeleportTo(entity, new BlockPos(x, y, z))) {
            return false;
        } else {
            entity.moveTo(x + 0.5, y, z + 0.5, entity.getYRot(), entity.getXRot());
            entity.getNavigation().stop();
            return true;
        }
    }

    private static boolean canTeleportTo(Mob entity, BlockPos pos) {
        var blockPathType = WalkNodeEvaluator.getBlockPathTypeStatic(entity.level(), pos.mutable());
        if (blockPathType != BlockPathTypes.WALKABLE) return false;
        var state = entity.level().getBlockState(pos.below());
        if (!entity.isNoGravity() && state.getBlock() instanceof LeavesBlock) {
            return false;
        } else {
            var relative = pos.subtract(entity.blockPosition());
            return entity.level().noCollision(entity, entity.getBoundingBox().move(relative));
        }
    }

    private static int randomIntInclusive(Mob entity, int min, int max) {
        return entity.getRandom().nextInt(max - min + 1) + min;
    }
}
