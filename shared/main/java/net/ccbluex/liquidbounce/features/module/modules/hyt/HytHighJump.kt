package net.ccbluex.liquidbounce.features.module.modules.hyt

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils

@ModuleInfo(name = "HytLongJump", description = "Fixed by Paimonqwq 20221022 一把只能一次", Chinese="花雨庭高挑",category = ModuleCategory.HYT)
class HytHighJump : Module() {


    private var jumped = false

    private var canBoost = false
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {

        val thePlayer = mc.thePlayer ?: return

        if (jumped) {
            if (thePlayer.onGround || thePlayer.capabilities.isFlying) {
                jumped = false
                thePlayer.motionX = 0.0
                thePlayer.motionZ = 0.0
                return
            }
            run{
                MovementUtils.strafe(MovementUtils.speed * if (canBoost) 1.17f else 1f)
                canBoost = false
            }
        }
        if (thePlayer.onGround && MovementUtils.isMoving) {
            jumped = true
            thePlayer.jump()
        }
    }
    @EventTarget
    fun onMove(event: MoveEvent) {
        val thePlayer = mc.thePlayer ?: return
        if (!MovementUtils.isMoving && jumped) {
            thePlayer.motionX = 0.0
            thePlayer.motionZ = 0.0
            event.zeroXZ()
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onJump(event: JumpEvent) {
        jumped = true
        canBoost = true
        event.motion = event.motion *1.11f
    }
    override val tag: String
        get() = "Bypass"
}