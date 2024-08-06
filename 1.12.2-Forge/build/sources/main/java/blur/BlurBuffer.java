package blur;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.gui.Blur;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

import java.awt.*;

public class BlurBuffer {

    public static void blurArea(float x, float y, float width, float height) {
        final Blur hud =(Blur) LiquidBounce.moduleManager.getModule(Blur.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(hud.getRadius().getValue().floatValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurAreacustomradius(float x, float y, float width, float height ,float radius) {
        final Blur hud =(Blur) LiquidBounce.moduleManager.getModule(Blur.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }
    public static void blurRoundArea(float x, float y, float width, float height, int radius) {

        final Blur hud =(Blur) LiquidBounce.moduleManager.getModule(Blur.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect(x, y, x + width, y + height, radius, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(hud.getRadius().getValue().floatValue());
        StencilUtil.uninitStencilBuffer();
    }
}
