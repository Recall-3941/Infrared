package net.ccbluex.liquidbounce.features.module.modules.hyt

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.server.SPacketPlayerPosLook

@ModuleInfo(name = "HytVelocity",description = "Skid SuperLag", Chinese = "HYT反击退", category = ModuleCategory.HYT)
class HytVelocity:Module() {
    private var canCancel = false
    private val modeValue = ListValue("Mode", arrayOf("a", "b"), "a")

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (classProvider.isSPacketEntityVelocity(event.packet)) {
            event.cancelEvent()
            canCancel = true
        }
        when (modeValue.get().toLowerCase()) {
            "a" -> {
                //A
                if (event.packet is SPacketPlayerPosLook) {
                    val packet = event.packet
                    event.cancelEvent()
                    mc.netHandler.addToSendQueue(
                        classProvider.createCPacketPlayerPosLook(
                            packet.x,
                            packet.y,
                            packet.z,
                            packet.yaw,
                            packet.pitch,
                            mc.thePlayer!!.onGround
                        )
                    )
                    canCancel = false
                }
            }
            "b" -> {
                if (classProvider.isSPacketPlayerPosLook(event.packet) && canCancel) {
                    val packet = event.packet.asSPacketPosLook()
                    event.cancelEvent()
                    mc.netHandler.addToSendQueue(
                        classProvider.createCPacketPlayerLook(
                            packet.yaw,
                            packet.pitch,
                            mc.thePlayer!!.onGround
                        )
                    )
                    canCancel = false
                }
            }
        }
    }
}