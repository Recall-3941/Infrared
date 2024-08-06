package net.ccbluex.liquidbounce.features.module.modules.hyt

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.server.SPacketChat
import java.util.regex.Pattern


/**
 *
 * Skid by Paimon.
 * @Date 2022/8/15/015
 */

@ModuleInfo(name="NoFucker",description = "L",Chinese="不挖自己床", category= ModuleCategory.HYT)
class NoFucker: Module() {
    private val debugValue = BoolValue("Debug", false)
    private val autoValue = BoolValue("auto", false)
    var x = 0.0
    var y = 0.0
    var z = 0.0
    var a = 0

    @EventTarget
    fun onUpdate(event: PacketEvent) {
        if (autoValue.get() == true) { //判断自动是否为true
            val packet = event.packet.unwrap()
            if (packet is SPacketChat) {
                val gameplay = Pattern.compile("游戏开始(.*?)\\(").matcher(packet.chatComponent.unformattedText)
                if (gameplay.find()) {
                    x = mc.thePlayer?.posX ?: x
                    y = mc.thePlayer?.posY ?: y
                    z = mc.thePlayer?.posZ ?: z
                    ClientUtils.displayChatMessage("§8[§c§6Loserline§8]§c§d已经标记您的床 您的Fucker不会挖你家的床")
                }
            }
        }

        if (debugValue.get()) {
            ClientUtils.displayChatMessage("" + mc.thePlayer?.getDistance(x, y, z))
        }

        Fucker.state = mc.thePlayer?.getDistance(x, y, z)!! >= 20

    }

    override fun onEnable() {
        if (autoValue.get() == false) { //判断自动是否为false
            x = mc.thePlayer?.posX ?: x
            y = mc.thePlayer?.posY ?: y
            z = mc.thePlayer?.posZ ?: z
            ClientUtils.displayChatMessage("§8[§c§6Loserline§8]§c§d已经标记您的床 您的Fucker不会挖你家的床")
        }
    }

    override fun onDisable() {
        x = 0.0
        y = 0.0
        z = 0.0
    }
}