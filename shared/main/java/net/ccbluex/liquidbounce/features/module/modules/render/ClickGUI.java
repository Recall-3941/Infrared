/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.Jello.Jello;
import net.ccbluex.liquidbounce.ui.client.clickgui.flux.click;
import net.ccbluex.liquidbounce.ui.client.clickgui.novoline.ClickyUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidBounceStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.ClickUI;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", description = "可点击UI.",Chinese="点击界面", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUI extends Module {

    public static final ListValue mode = new ListValue("Mode", new String[] {"Dust","Novoline","Jello","Flux","Zeroday", "LiquidBounce"}, "LiquidBounce");
    private final ListValue styleValue = new ListValue("Style", new String[] {"LiquidBounce", "Null", "Slowly"}, "Null") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };

    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    public static final BoolValue cnMode = new BoolValue("ChineseMode(Need Reload)", false);
    private static final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);



    public static BoolValue getCNMode() {
        return cnMode;
    }
    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        updateStyle();

        switch(mode.get()) {
            case "LiquidBounce":
                updateStyle();
                mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
                break;
            case "Flux":
                mc.displayGuiScreen(classProvider.wrapGuiScreen( new click()));
                break;

            case "Dust":
                mc.displayGuiScreen(classProvider.wrapGuiScreen( new net.ccbluex.liquidbounce.ui.client.clickgui.Dust.ClickyUI()));
                break;
            case "Novoline":
                mc.displayGuiScreen(classProvider.wrapGuiScreen( new ClickyUI()));
                break;
            case "Jello":
                mc.displayGuiScreen(classProvider.wrapGuiScreen( new Jello()));
                break;
            case "Zeroday":
                mc.displayGuiScreen(classProvider.wrapGuiScreen( new ClickUI()));
                break;
        }
    }

    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {
            case "liquidbounce":
                LiquidBounce.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LiquidBounce.clickGui.style = new NullStyle();
                break;
            case "slowly":
                LiquidBounce.clickGui.style = new SlowlyStyle();
                break;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final IPacket packet = event.getPacket();

        if (classProvider.isSPacketCloseWindow(packet) && classProvider.isClickGui(mc.getCurrentScreen())) {
            event.cancelEvent();
        }
    }
}
