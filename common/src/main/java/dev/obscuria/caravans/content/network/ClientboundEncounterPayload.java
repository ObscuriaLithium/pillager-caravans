package dev.obscuria.caravans.content.network;

import dev.obscuria.caravans.PillagerCaravans;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ClientboundEncounterPayload(ResourceLocation caravanType) implements CustomPacketPayload {

    public static final Type<ClientboundEncounterPayload> TYPE;
    public static final StreamCodec<FriendlyByteBuf, ClientboundEncounterPayload> CODEC;

    public static ClientboundEncounterPayload forCaravan(ResourceLocation caravanType) {
        return new ClientboundEncounterPayload(caravanType);
    }

    public static void handle(Player player, ClientboundEncounterPayload payload) {
        ClientNetworkHandler.handle(payload, player);
    }

    @Override
    public Type<ClientboundEncounterPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(PillagerCaravans.key("encounter"));
        CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, ClientboundEncounterPayload::caravanType,
                ClientboundEncounterPayload::forCaravan);
    }
}
