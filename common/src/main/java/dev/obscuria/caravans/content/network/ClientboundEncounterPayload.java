package dev.obscuria.caravans.content.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ClientboundEncounterPayload(ResourceLocation caravanType) {

    public static ClientboundEncounterPayload forCaravan(ResourceLocation caravanType) {
        return new ClientboundEncounterPayload(caravanType);
    }

    public static void encode(ClientboundEncounterPayload payload, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(payload.caravanType);
    }

    public static ClientboundEncounterPayload decode(FriendlyByteBuf buffer) {
        return new ClientboundEncounterPayload(buffer.readResourceLocation());
    }

    public static void handle(Player player, ClientboundEncounterPayload payload) {
        ClientNetworkHandler.handle(payload, player);
    }
}
