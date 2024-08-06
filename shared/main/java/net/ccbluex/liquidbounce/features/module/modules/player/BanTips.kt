package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.SPacketChat
import java.util.regex.Pattern

@ModuleInfo(name = "BanTips" , description = "tttttiips",Chinese="ban提示", category = ModuleCategory.HYT)
class BanTips : Module() {
    private var CustomLOLText = TextValue("AutoSendText", "[Vestige]主播是不是开不明白LLL")
    private var staffbanplayerscount = BoolValue("CountStaffBans",false)
    var staffbans = 0
    var acbans = 0
    fun onPacket(event: PacketEvent){
        val packet = event.packet.unwrap()
        if(staffbanplayerscount.get()) {
            if (packet is SPacketChat) {
                if(packet.chatComponent.unformattedText.contains("有一名玩家因为作弊已被踢出，祝您游戏愉快!!请大家遵守游戏规则，善待自己的游戏帐号。")){
                    staffbans += 1
                    LiquidBounce.hud.addNotification(Notification("Tips","客服在您游玩的时间里Ban了:$staffbans" + "名玩家",NotifyType.WARNING))
                }
                if(staffbans > 5){
                    LiquidBounce.hud.addNotification(Notification("Tips","客服有点多，建议别玩了",NotifyType.WARNING))
                }
            }
        }
        if (packet is SPacketChat) {
            val matcher = Pattern.compile("玩家(.*?)在本局游戏中行为异常").matcher(packet.chatComponent.unformattedText)
                if(matcher.find()){
                    acbans += 1
                    val banname = matcher.group(1)
                    mc.thePlayer!!.sendChatMessage(CustomLOLText.get() + "||刚刚被ban的人：$banname" + "||客服ban了:$staffbans" + "个" + "||开不明白的有：$acbans" + "个")
                }
            }
        }
}