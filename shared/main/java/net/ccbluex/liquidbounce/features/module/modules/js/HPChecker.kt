package net.ccbluex.liquidbounce.features.module.modules.js

import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.script.api.global.Chat

/**
 * JS TO KT
 * By Recallqwq
 */



@ModuleInfo(name = "HPChecker", description = "Check ur hp",Chinese="检测你的血量", category = ModuleCategory.JS)
class HPChecker : Module() {
    //减少的血量
    var HP3 = 0
    //上一次的血量
    var HP = 20
    fun onUpdate(event: UpdateEvent){
        //实时血量
        val HP2 = mc.thePlayer?.health?.toInt()
        if(HP > HP2!!){
            HP3 = HP - HP2
            Chat.print("你减少了"+ HP3.toString() +"点血")
            HP = HP2
        }
        if(HP < HP2){
            HP3 = HP2 - HP
            Chat.print("你恢复了"+ HP3.toString() +"点血")
            HP = HP2
        }
        if(mc.thePlayer!!.isDead || HP2 <= 0){
            HP = 20
            HP3 = 0
        }
    }
}