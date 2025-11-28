package dev.obscuria.caravans.client;

import dev.obscuria.caravans.config.CommonConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record CaravanEncounterToast(ResourceLocation caravanType) implements Toast {

    private static final ResourceLocation DARK_SPRITE;
    private static final ResourceLocation LIGHT_SPRITE;
    private static final int DISPLAY_TIME;
    private static final ItemStack ICON;
    private static final Component TITLE;

    public Visibility render(GuiGraphics graphics, ToastComponent component, long timeSinceLastVisible) {
        final var lightMode = CommonConfig.TOAST_LIGHT_MODE.get();
        graphics.blitSprite(lightMode ? LIGHT_SPRITE : DARK_SPRITE, 0, 0, width(), height());
        final var caravanName = Component.translatable(caravanType.toLanguageKey("caravan"));
        graphics.drawString(component.getMinecraft().font, TITLE, 30, 7, lightMode ? 11141290 : 16755200, false);
        graphics.drawString(component.getMinecraft().font, caravanName, 30, 18, lightMode ? -16777216 : -1, false);
        graphics.renderFakeItem(ICON, 8, 8);
        final var displayTime = DISPLAY_TIME * component.getNotificationDisplayTimeMultiplier();
        return timeSinceLastVisible >= displayTime ? Visibility.HIDE : Visibility.SHOW;
    }

    static {
        DARK_SPRITE = ResourceLocation.withDefaultNamespace("toast/advancement");
        LIGHT_SPRITE = ResourceLocation.withDefaultNamespace("toast/recipe");
        DISPLAY_TIME = 5000;
        ICON = Items.CROSSBOW.getDefaultInstance();
        TITLE = Component.translatable("toast.caravans.encounter");
    }
}
