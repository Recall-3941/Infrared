package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider
import net.ccbluex.liquidbounce.utils.MinecraftInstance.mc
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager

class hotbarutil {
    val translate = Translate2(0f , 0f)
    var size = 1.0f

    fun renderHotbarItem(index: Int, xPos: Float, yPos: Float, partialTicks: Float) {

        val itemStack = MinecraftInstance.mc2.player.inventory.mainInventory[index]
        if (itemStack != null) {
            val lvt_7_1_ = itemStack.animationsToGo.toFloat() - partialTicks
            if (lvt_7_1_ > 0.0f) {
                GlStateManager.pushMatrix()
                val lvt_8_1_ = 1.0f + lvt_7_1_ / 5.0f
                GlStateManager.translate((xPos + 8).toFloat(), (yPos + 12).toFloat(), 0.0f)
                GlStateManager.scale(1.0f / lvt_8_1_, (lvt_8_1_ + 1.0f) / 2.0f, 1.0f)
                GlStateManager.translate((-(xPos + 8)).toFloat(), (-(yPos + 12)).toFloat(), 0.0f)
            }
            RenderUtils.drawTexturedRect((xPos - 7).toInt(), (yPos -7).toInt(), 30, 30,"hotbar",  classProvider.createScaledResolution(mc));
            RenderUtils.drawTexturedRect((xPos - 7).toInt(), (yPos -7).toInt(), 30, 30,"hotbar",  classProvider.createScaledResolution(mc));

        }
    }
}