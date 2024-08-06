/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.api.enums.BlockType
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "Eagle", description = "使你在方块边缘蹲下(例:快速蹲搭)",Chinese="自动蹲搭", category = ModuleCategory.LEGIT)
class Eagle : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        mc.gameSettings.keyBindSneak.pressed = mc.theWorld!!.getBlockState(WBlockPos(thePlayer.posX, thePlayer.posY - 1.0, thePlayer.posZ)).block == classProvider.getBlockEnum(BlockType.AIR)
    }

    override fun onDisable() {
        if (mc.thePlayer == null)
            return

        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSneak))
            mc.gameSettings.keyBindSneak.pressed = false
    }
}
