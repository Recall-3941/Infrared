/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 * 
 * This code belongs to WYSI-Foundation. Please give credits when using this in your repository.
 */
package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper

object BlurUtils : MinecraftInstance() {




    private var lastFactor = 0
    private var lastWidth = 0
    private var lastHeight = 0
    private var lastWeight = 0

    private var lastX = 0F
    private var lastY = 0F
    private var lastW = 0F
    private var lastH = 0F

    private var lastStrength = 5F




    @JvmStatic
    fun blur(posX: Float, posY: Float, posXEnd: Float, posYEnd: Float, blurStrength: Float, displayClipMask: Boolean, triggerMethod: () -> Unit) {
        if (!OpenGlHelper.isFramebufferEnabled()) return


    }


    @JvmStatic
    fun blurArea(x: Float, y: Float, x2: Float, y2: Float, blurStrength: Float) = blur(x, y, x2, y2, blurStrength, false) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.quickDrawRect(x, y, x2, y2)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    @JvmStatic
    fun blurAreaRounded(x: Float, y: Float, x2: Float, y2: Float, rad: Float, blurStrength: Float) = blur(x, y, x2, y2, blurStrength, false) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun sizeHasChanged(scaleFactor: Int, width: Int, height: Int): Boolean = (lastFactor != scaleFactor || lastWidth != width || lastHeight != height)
}


