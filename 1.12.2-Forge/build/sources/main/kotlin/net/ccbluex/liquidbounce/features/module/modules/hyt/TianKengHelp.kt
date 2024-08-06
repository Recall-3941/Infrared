package net.ccbluex.liquidbounce.features.module.modules.hyt

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.network.play.server.SPacketChat
import java.util.regex.Pattern

@ModuleInfo(name = "TianKengHelp" , description = "HytTkAutoStart00000000 By Sock",Chinese="天坑助手", category = ModuleCategory.HYT)
class TianKengHelp : Module(){
    private var tickget = IntegerValue("Tick",40,0,60)
    private var speedvalue = BoolValue("Speed",true)
    private var customerservicetips = BoolValue("CustomerServiceTips",false)
    private var bantips = BoolValue("BanTips",false)
    private var debugtickget = IntegerValue("DebugTick",5,0,20)
    var hurt = false
    var tick = 0
    var toggle = false
    var onn = false
    var delaytick = 0
    var bannumber = 0
    var shouldrun = false
    var master = true
    var customerservicenumber = 0
    var health = 0F
    var dehealth = 0

    override fun onDisable() {
        if(speedvalue.get()){
            LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
        }

        customerservicenumber = 0
        bannumber = 0
    }

    override fun onEnable() {
        customerservicenumber = 0
        bannumber = 0
    }

    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet.unwrap()

        if(customerservicetips.get()) {
            if (packet is SPacketChat) {
                if(packet.chatComponent.unformattedText.contains("有一名玩家因为作弊已被踢出，祝您游戏愉快!!请大家遵守游戏规则，善待自己的游戏帐号。")){
                    customerservicenumber += 1
                    LiquidBounce.hud.addNotification(Notification("Tips","客服已Ban:$customerservicenumber" + "人",NotifyType.WARNING))
                }
                if(customerservicenumber > 3){
                    LiquidBounce.hud.addNotification(Notification("Tips","客服已Ban3个以上,建议尽快离开天坑",NotifyType.ERROR))
                }
            }
        }

        if(bantips.get()){
            if (packet is SPacketChat) {
                val matcher = Pattern.compile("玩家(.*?)在本局游戏中行为异常").matcher(packet.chatComponent.unformattedText)
                if(matcher.find()){
                    bannumber += 1
                    val banname = matcher.group(1)
                    mc.thePlayer!!.sendChatMessage("[Loserline]主播开不明白是不是 被Ban主播:$banname" + ",目前已Ban:$bannumber" + "个")
                }
            }
        }

        if(mc.thePlayer!!.burning){
            return
        }
        if(!toggle){
            if(mc.thePlayer!!.hurtTime > 0){
                if(mc.thePlayer!!.health == health){
                    if(speedvalue.get()){
                        LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
                    }
                    return
                }
                shouldrun = true
            }else{
                hurt = false
            }
            health = mc.thePlayer!!.health
        }
        hurt = mc.thePlayer!!.hurtTime > 0
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(mc.thePlayer!!.burning){
            if(speedvalue.get()){
                LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
            }
            return
        }
        if(shouldrun){
            delaytick += 1
            if(debugtickget.get() == delaytick){
                shouldrun = false
                delaytick = 0
                hurt = true
                toggle = true
                if(speedvalue.get()){
                    LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = true
                }
            }
        }
        if(toggle){
            if(!hurt){
                tick += 1
            }else{
                tick = 0
            }
        }
        if(tick == tickget.get()){
            tick=0
            toggle=false
            if(speedvalue.get()){
                LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
            }
        }
    }
}