package dev.obscuria.caravans.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.caravans.common.registry.CaravanRegistries;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class CaravanData {

    public static final int DESPAWN_DELAY_IN_COMBAT = 6000;
    public static final Codec<CaravanData> CODEC;

    private static final String TAG_NAME = "CaravanData";

    @Getter private int despawnDelay;

    protected CaravanData(int despawnDelay) {
        this.despawnDelay = despawnDelay;
    }

    public abstract MapCodec<? extends CaravanData> codec();

    public void aiStep(LivingEntity entity) {
        if (entity.level().isClientSide()) return;
        maybeDespawn(entity);
    }

    public static void save(@Nullable CaravanData data, CompoundTag tag) {
        if (data == null) return;
        @Nullable var packedData = CODEC.encodeStart(NbtOps.INSTANCE, data).result().orElse(null);
        if (packedData == null) return;
        tag.put(TAG_NAME, packedData);
    }

    public static @Nullable CaravanData load(@Nullable CompoundTag tag) {
        if (tag == null || !tag.contains(TAG_NAME, Tag.TAG_COMPOUND)) return null;
        return CODEC.parse(NbtOps.INSTANCE, tag.getCompound(TAG_NAME)).result().orElse(null);
    }

    public static void bootstrap(BootstrapContext<MapCodec<? extends CaravanData>> context) {
        context.register("leader", () -> CaravanLeaderData.CODEC);
        context.register("member", () -> CaravanMemberData.CODEC);
    }

    public boolean maybeDespawn(LivingEntity entity) {
        return maybeDespawn(entity, false, true);
    }

    public boolean maybeDespawn(LivingEntity entity, boolean ignoreDelay, boolean dropLeash) {
        if (!canDespawn(entity)) return false;
        if (entity.getCombatTracker().inCombat) {
            this.despawnDelay = Math.max(despawnDelay, DESPAWN_DELAY_IN_COMBAT);
        } else {
            this.despawnDelay -= 1;
        }
        if (despawnDelay > 0 && !ignoreDelay) return false;
        if (dropLeash && entity instanceof Mob mob) mob.dropLeash(true, false);
        entity.remove(Entity.RemovalReason.DISCARDED);
        return true;
    }

    private boolean canDespawn(LivingEntity entity) {
        return !entity.hasCustomName() && !entity.hasExactlyOnePlayerPassenger();
    }

    static {
        CODEC = CaravanRegistries.DATA_TYPE.byNameCodec().dispatch(CaravanData::codec, Function.identity());
    }
}
