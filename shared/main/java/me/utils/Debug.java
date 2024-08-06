package me.utils;

import net.ccbluex.liquidbounce.LiquidBounce;

import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

public class Debug extends MinecraftInstance {



    public static Boolean thePlayerisBlocking = false;
    public static void render(ScaledResolution sr, int itemX, float partialTicks) {
        GL11.glPushMatrix();
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        GL11.glPopMatrix();

        int middleScreen = sr.getScaledWidth() / 2;


//        RenderUtils.drawRect(itemX, sr.getScaledHeight() - 23, itemX + 22, sr.getScaledHeight() - 22, new Color(0, 165, 255));
    }
}
