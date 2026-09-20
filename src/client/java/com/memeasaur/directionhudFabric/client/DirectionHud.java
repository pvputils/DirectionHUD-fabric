package com.memeasaur.directionhudFabric.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

final class DirectionHud {
    private static final Identifier COMPASS = Identifier.fromNamespaceAndPath("directionhud", "textures/gui/compass.png");

    static int heading(float yaw) {
        return Mth.floor(yaw * 256.0F / 360.0F + 0.5D) & 255;
    }

    static void render(GuiGraphicsExtractor graphics) {
        Minecraft mc = Minecraft.getInstance();
        HudConfig c = DirectionhudFabricClient.config;
        if (!c.enabled || mc.player == null || mc.gui.hud.isHidden() || mc.getDebugOverlay().showDebugScreen()
                || (mc.gui.screen() != null && !(mc.gui.screen() instanceof ChatScreen && c.showInChat))) return;
        int x = c.alignMode.endsWith("center") ? graphics.guiWidth() / 2 - 32 + (c.applyXOffsetToCenter ? c.xOffset : 0)
                : c.alignMode.endsWith("right") ? graphics.guiWidth() - 65 - c.xOffset : c.xOffset;
        int y = c.alignMode.startsWith("middle") ? graphics.guiHeight() / 2 - 6 + (c.applyYOffsetToMiddle ? c.yOffset : 0)
                : c.alignMode.startsWith("bottom") ? graphics.guiHeight() - 12 - (c.alignMode.equals("bottomcenter") ? c.yOffsetBottomCenter : c.yOffset)
                : c.yOffset;
        int direction = heading(mc.player.getYRot());
        int u = direction & 127;
        int v = c.compassIndex * 24 + (direction >= 128 ? 12 : 0);
        graphics.blit(RenderPipelines.GUI_TEXTURED, COMPASS, x, y, u, v, 65, 12, 256, 256);
        String marker = "\u00a7" + c.markerColor.toLowerCase(java.util.Locale.ROOT) + "|";
        graphics.text(mc.font, marker, x + 32, y + 1, 0xffffffff, false);
        graphics.text(mc.font, marker, x + 32, y + 5, 0xffffffff, false);
    }
}

