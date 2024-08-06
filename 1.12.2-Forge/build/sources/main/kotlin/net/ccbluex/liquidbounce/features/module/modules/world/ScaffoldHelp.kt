package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.SafeWalk
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "ScaffoldHelp", description = "自动搭路帮助", Chinese = "自动搭路帮助", category = ModuleCategory.WORLD)
class ScaffoldHelp : Module() {

    private val modValue = ListValue("AutoJumpMode", arrayOf("Ground","Parkour"),"Ground")
    private val safewalkValue = BoolValue("SafeWalk",true)
    private val autojumpValue = BoolValue("AutoJump",true)


    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        val safeWalk = LiquidBounce.moduleManager.getModule(SafeWalk::class.java) as SafeWalk
        val scaffold = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
        safeWalk.state = safewalkValue.get()

        scaffold.state = !thePlayer.onGround
        if (autojumpValue.get()) {
            when (modValue.get().toLowerCase()) {
                "Ground" -> {
                    if (thePlayer.onGround) {
                        if (MovementUtils.isMoving)
                            thePlayer.jump()
                    }
                }
                "Parkour" -> {
                    if (MovementUtils.isMoving && thePlayer.onGround && !thePlayer.sneaking && !mc.gameSettings.keyBindSneak.isKeyDown && !mc.gameSettings.keyBindJump.isKeyDown && mc.theWorld!!.getCollidingBoundingBoxes(
                            thePlayer,
                            thePlayer.entityBoundingBox.offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)
                        ).isEmpty()
                    ) {
                        thePlayer.jump()
                    }
                }
            }
        }
    }
    override fun onDisable() {
        val scaffold = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
        val safeWalk = LiquidBounce.moduleManager.getModule(SafeWalk::class.java) as SafeWalk
        scaffold.state = false
        safeWalk.state = false
    }
}