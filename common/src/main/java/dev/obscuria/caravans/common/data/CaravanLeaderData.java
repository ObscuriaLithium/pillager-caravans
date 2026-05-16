package dev.obscuria.caravans.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.caravans.common.network.ClientboundEncounterPayload;
import dev.obscuria.caravans.config.CommonConfig;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import lombok.Getter;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public final class CaravanLeaderData extends CaravanData {

    public static final ResourceLocation INVALID_TYPE = PillagerCaravans.identifier("invalid");
    public static final Codec<CaravanLeaderData> CODEC;

    @Getter private final HashSet<UUID> trackedPlayers;
    @Getter private final ResourceLocation caravanType;

    public static CaravanLeaderData create(@Nullable ResourceLocation caravanType, int despawnDelay) {
        caravanType = caravanType == null ? INVALID_TYPE : caravanType;
        return new CaravanLeaderData(List.of(), caravanType, despawnDelay);
    }

    private CaravanLeaderData(List<UUID> trackedPlayers, ResourceLocation caravanType, int despawnDelay) {
        super(despawnDelay);
        this.trackedPlayers = new HashSet<>(trackedPlayers);
        this.caravanType = caravanType;
    }

    @Override
    public Codec<CaravanLeaderData> codec() {
        return CODEC;
    }

    @Override
    public void aiStep(LivingEntity entity) {
        super.aiStep(entity);
        if (!CommonConfig.ENABLE_ENCOUNTER_TOAST.get()) return;
        if (entity.level().isClientSide()) return;
        if (entity.tickCount % 40 != 0) return;

        var players = entity.level().getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(32));
        if (players.isEmpty()) return;
        for (var player : players) {
            if (!trackedPlayers.add(player.getUUID())) continue;
            FragmentumNetworking.sendTo(player, ClientboundEncounterPayload.forCaravan(caravanType));
        }
    }

    private List<UUID> trackedPlayersList() {
        return List.copyOf(trackedPlayers);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                UUIDUtil.STRING_CODEC.listOf().fieldOf("tracked_players").forGetter(CaravanLeaderData::trackedPlayersList),
                ResourceLocation.CODEC.fieldOf("caravan_type").forGetter(CaravanLeaderData::getCaravanType),
                Codec.INT.fieldOf("despawn_delay").forGetter(CaravanLeaderData::getDespawnDelay)
        ).apply(codec, CaravanLeaderData::new));
    }
}
