package net.ccbluex.liquidbounce.features.module.modules.hyt;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Ghost;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;

@ModuleInfo(name="FuckerHelper",description = "8月初绕Fucker",Chinese ="自动挖床辅助",category= ModuleCategory.HYT)
public class FuckerHelper extends Module {
    public FuckerHelper() {


    }


public final void onEnable(){

    super.onEnable();
}

    //sh1tc0de
    //我是个傻逼 6666 no shit
    @EventTarget
    public final void onUpdate(UpdateEvent event){
        Fucker fucker = (Fucker) LiquidBounce.INSTANCE.getModuleManager().getModule(Fucker.class);
        KillAura killAura = (KillAura) LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        Ghost ghost= (Ghost) LiquidBounce.INSTANCE.getModuleManager().getModule(Ghost.class);

        Timer timer = (Timer) LiquidBounce.INSTANCE.getModuleManager().getModule(Timer.class);
        float tspeed = mc.getTimer().getTimerSpeed();
        if(fucker.getState()){
            killAura.setState(false);
            ghost.setState(true);
            //timer.getSpeedValue().set(0.1F);
            //mc.getTimer().setTimerSpeed(0.1F);
            //timer.setState(true);
        }else{
            //mc.getTimer().setTimerSpeed(tspeed);
            ghost.setState(false);
            killAura.setState(true);
            //ghost.setState(false);
            //timer.setState(false);
        }
    }

}
