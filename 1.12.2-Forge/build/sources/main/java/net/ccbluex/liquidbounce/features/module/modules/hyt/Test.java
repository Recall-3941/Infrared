package net.ccbluex.liquidbounce.features.module.modules.hyt;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;

/**
 * Skid by Paimon.
 *
 * @Date 2022/8/9
 */


@ModuleInfo(name="Test",description = "SB",Chinese ="Test",category= ModuleCategory.HYT)
public class Test extends Module {

    @Override
    public void onEnable() {
        if(LiquidBounce.INSTANCE.getRank() != "Owner"){
            ClientUtils.displayChatMessage("Need Owner!");
            super.onDisable();
        }
    }

    public Test() {


    }
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ClientUtils.displayChatMessage(mc.getCurrentScreen().getClass().getName());
        Fonts.font40.drawStringWithShadow(mc.getCurrentScreen().getClass().getName(),0,0,-1);
    }

}
