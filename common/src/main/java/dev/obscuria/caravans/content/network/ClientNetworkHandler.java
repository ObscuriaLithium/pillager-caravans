package dev.obscuria.caravans.content.network;

import dev.obscuria.caravans.client.CaravanEncounterToast;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public interface ClientNetworkHandler {

    static void handle(ClientboundEncounterPayload payload, Player player) {
        Minecraft.getInstance().getToasts().addToast(new CaravanEncounterToast(payload.caravanType()));
    }
}
