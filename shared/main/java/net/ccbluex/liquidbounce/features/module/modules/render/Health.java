/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.ShaderEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "Health", description = "sb.",Chinese ="血量", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RSHIFT)
public class Health extends Module {

    private IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private IntegerValue  colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private IntegerValue  colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private BoolValue cColorValue = new BoolValue("CustomColor", false);
    private BoolValue cFontValue = new BoolValue("CustomFont", false);

    @EventTarget
    public void onShader(final ShaderEvent event) {

    }
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        IScaledResolution sr = classProvider.createScaledResolution(mc);
        float healthNum = (float)Math.round((mc.getThePlayer().getHealth() * 10.0) / 10.0F);
        float abNum = (float)Math.round((Minecraft.getMinecraft().player.getAbsorptionAmount() * 10.0) / 10.0F);

        //自定义字体时
        String abString = this.cFontValue.get() ? "§e" + abNum + "§r" :
                //我的世界原版字体时
                "§e" + abNum + "§e❤";

        if (Minecraft.getMinecraft().player.getAbsorptionAmount() <= 0.0F)
            abString = "";

        //自定义字体
        String text =  this.cFontValue.get() ? healthNum + "§r " + abString + "" :
                //我的世界原版字体
                healthNum + "§c❤ " + abString + "";

        int c = this.cColorValue.get() ? new Color(this.colorRedValue.get(), this.colorGreenValue.get(), this.colorBlueValue.get()).getRGB() : ColorUtils.INSTANCE.getHealthColor(mc.getThePlayer().getHealth(), mc.getThePlayer().getMaxHealth());
        //自定义字体
        if (this.cFontValue.get()) {
            Fonts.Posterama35.drawCenteredString(text, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 25, c, true);
            //我的世界原版字体
        } else {
            mc.getFontRendererObj().drawCenteredString(text, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 25, c, true);
        }
    }
}
