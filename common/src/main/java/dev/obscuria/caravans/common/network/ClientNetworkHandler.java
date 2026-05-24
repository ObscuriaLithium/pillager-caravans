package dev.obscuria.caravans.common.network;

import dev.obscuria.caravans.client.CaravanEncounterToast;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public interface ClientNetworkHandler {

    static void handle(ClientboundEncounterPayload payload, Player player) {
        Minecraft.getInstance().getToastManager().addToast(new CaravanEncounterToast(payload.caravanType()));
    }
}
