package net.ccbluex.liquidbounce.features.module.modules.render;

import cn.hanabi.gui.cloudmusic.ui.MusicPlayerUI;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

/**
 * Skid by Paimon.
 *
 * @Date 2022/8/16/016
 */

@ModuleInfo(name = "MusicPlayer", description = "Hanabi", Chinese="网易云播放器",category = ModuleCategory.RENDER,canEnable = false)
public class MusicPlayer extends Module {

    public MusicPlayer(){

    }

    @Override
    public void onEnable() {
        mc2.displayGuiScreen(new MusicPlayerUI());
        super.onEnable();
    }
}
