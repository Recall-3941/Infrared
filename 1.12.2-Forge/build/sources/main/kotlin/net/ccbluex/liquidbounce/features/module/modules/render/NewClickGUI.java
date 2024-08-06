package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.bzdhyp.BzdhypGUI;

/**
 * Skid by Paimon.
 *
 * @Date 2022/8/16/016
 */

@ModuleInfo(name = "NewClickGUI", description = "更好的ClickGUI", Chinese="更好的界面",category = ModuleCategory.RENDER,canEnable = false)
public class NewClickGUI extends Module {

    public NewClickGUI(){

    }

    @Override
    public void onEnable() {
        mc2.displayGuiScreen(new BzdhypGUI());
        super.onEnable();
    }
}
