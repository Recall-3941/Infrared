/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.block.BlockUtils

@ModuleInfo(name = "AirLadder", description = "允许你不接触梯子/藤蔓就爬上他们", Chinese="隔空爬梯",category = ModuleCategory.MOVEMENT)
class AirLadder : Module() {
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (classProvider.isBlockLadder(BlockUtils.getBlock(WBlockPos(thePlayer.posX, thePlayer.posY + 1, thePlayer.posZ))) && thePlayer.isCollidedHorizontally ||
            classProvider.isBlockVine(BlockUtils.getBlock(WBlockPos(thePlayer.posX, thePlayer.posY, thePlayer.posZ))) ||
            classProvider.isBlockVine(BlockUtils.getBlock(WBlockPos(thePlayer.posX, thePlayer.posY + 1, thePlayer.posZ)))) {
            thePlayer.motionY = 0.15
            thePlayer.motionX = 0.0
            thePlayer.motionZ = 0.0
        }
    }
}