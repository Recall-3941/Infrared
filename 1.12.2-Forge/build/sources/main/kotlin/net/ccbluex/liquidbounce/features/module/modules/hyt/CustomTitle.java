package net.ccbluex.liquidbounce.features.module.modules.hyt;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.opengl.Display;

/**
 * Skid by Paimon.
 *
 * @Date 2022/8/9
 */


@ModuleInfo(name="CustomTitle",description = "自定义窗口标题",Chinese ="自定义窗口标题",category= ModuleCategory.HYT)
public class CustomTitle extends Module {
    private final TextValue Title = new TextValue("Title","白治军客户端 | 白治军:别逼我开户 | 白治军已死亡：");
    public CustomTitle() {


    }
    private int S;
    private int HM;
    private int M;
    private int H;

    public final int getS() {
        return this.S;
    }


    public final int getM() {
        return this.M;
    }

    public final int getH() {
        return this.H;
    }
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        ++this.HM;
        if (this.HM == 20) {
            ++this.S;
            this.HM = 0;
        }
        if (this.S == 60) {
            ++this.M;
            this.S = 0;
        }
        if (this.M == 60) {
            ++this.H;
            this.M = 0;
        }
        Display.setTitle((String)this.Title.get() + this.H +"时 "+this.M+"分 "+this.S+"秒");
    }

}
