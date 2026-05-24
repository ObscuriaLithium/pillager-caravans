package dev.obscuria.caravans.common.network;

import dev.obscuria.caravans.PillagerCaravans;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record ClientboundEncounterPayload(Identifier caravanType) implements CustomPacketPayload {

    public static final Type<ClientboundEncounterPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundEncounterPayload> STREAM_CODEC;

    public static ClientboundEncounterPayload forCaravan(Identifier caravanType) {
        return new ClientboundEncounterPayload(caravanType);
    }

    public static void handle(Player player, ClientboundEncounterPayload payload) {
        ClientNetworkHandler.handle(payload, player);
    }

    @Override public Type<ClientboundEncounterPayload> type() {
        return TYPE;
    }

    static {
        TYPE = new Type<>(PillagerCaravans.id("encounter"));
        STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, ClientboundEncounterPayload::caravanType,
                ClientboundEncounterPayload::new);
    }
}
