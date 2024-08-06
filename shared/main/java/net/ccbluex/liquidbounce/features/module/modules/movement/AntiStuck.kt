package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.server.SPacketPlayerPosLook

@ModuleInfo(name = "AntiStuck", Chinese = "防卡方块",description="L", category = ModuleCategory.MOVEMENT)
class AntiStuck : Module() {
    private val flagsValue = IntegerValue("Flags", 5, 1, 10)

    private val timer = MSTimer()
    private val reduceTimer = MSTimer()
    private var flagsTime = 0
    private var stuck = false

    private fun reset() {
        stuck = false
        flagsTime = 0
        timer.reset()
        reduceTimer.reset()
    }

    override fun onEnable() {
        reset()
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        reset()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (stuck) {
            val freeze = LiquidBounce.moduleManager[Freeze::class.java]!!
            freeze.state = true

            if (timer.hasTimePassed(1500)) {
                stuck = false
                flagsTime = 0
                freeze.state = false
                timer.reset()
                reduceTimer.reset()
            }
        } else {
            if (flagsTime > flagsValue.get()) {
                timer.reset()
                reduceTimer.reset()
                flagsTime = 0
                stuck = true
                LiquidBounce.hud.addNotification(
                    Notification(
                        "AntiStuck","Trying to unstuck you", NotifyType.INFO
                    )
                )
            }
            if (timer.hasTimePassed(1500) && reduceTimer.hasTimePassed(500) && flagsTime > 0) {
                flagsTime -= 1
                reduceTimer.reset()
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is SPacketPlayerPosLook) {
            flagsTime++
            reduceTimer.reset()
            if (!stuck) {
                timer.reset()
            }
        }
        if (stuck && packet is CPacketPlayer) {
            event.cancelEvent()
        }
    }
}
