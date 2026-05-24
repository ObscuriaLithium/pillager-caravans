package dev.obscuria.caravans.client;

import dev.obscuria.caravans.config.CommonConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CaravanEncounterToast implements Toast {

    private static final Identifier DARK_SPRITE = Identifier.withDefaultNamespace("toast/advancement");
    private static final Identifier LIGHT_SPRITE = Identifier.withDefaultNamespace("toast/recipe");

    private static final int DISPLAY_TIME;
    private static final ItemStack ICON;
    private static final Component TITLE;

    private final Identifier caravanType;
    private Toast.Visibility wantedVisibility;

    public CaravanEncounterToast(Identifier caravanType) {
        this.caravanType = caravanType;
        this.wantedVisibility = Toast.Visibility.SHOW;
    }

    @Override public Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override public void update(ToastManager manager, long timer) {
        var displayTime = DISPLAY_TIME * manager.getNotificationDisplayTimeMultiplier();
        this.wantedVisibility = timer >= displayTime ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override public void extractRenderState(GuiGraphicsExtractor extractor, Font font, long timer) {
        var lightMode = CommonConfig.TOAST_LIGHT_MODE.get();
        var sprite = lightMode ? LIGHT_SPRITE : DARK_SPRITE;
        extractor.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, this.width(), this.height());
        var caravanName = Component.translatable(caravanType.toLanguageKey("caravan"));
        extractor.text(font, TITLE, 30, 7, lightMode ? 0xFFAA00AA : 0xFFFFAA00, false);
        extractor.text(font, caravanName, 30, 18, lightMode ? 0xFF000000 : 0xFFFFFFFF, false);
        extractor.fakeItem(ICON, 8, 8);
    }

    static {
        DISPLAY_TIME = 5000;
        ICON = Items.CROSSBOW.getDefaultInstance();
        TITLE = Component.translatable("toast.caravans.encounter");
    }
}
