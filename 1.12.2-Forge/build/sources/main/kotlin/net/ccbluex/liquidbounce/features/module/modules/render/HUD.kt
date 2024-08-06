/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "HUD", description = "切换屏幕上的可视内容", Chinese="游戏内界面",category = ModuleCategory.RENDER, array = false)
class HUD : Module() {

    val hideHotBarValue = BoolValue("HideHotbar", false)
    val blackHotbarValue = BoolValue("BlackHotbar", true)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    private val blurValue = BoolValue("Blur", false)

    val Breathinglamp = BoolValue("Breathinglamp", true)
    private val hudblurValue = BoolValue("HudBlur", false)
    val fontChatValue = BoolValue("FontChat", false)
    @JvmField
    val r = IntegerValue("r",0,0,255)
    @JvmField
    val g = IntegerValue("g",0,0,255)
    @JvmField
    val b = IntegerValue("b",0,0,255)
    public val rainbowStartValue = FloatValue("RainbowStart", 0.41f, 0f, 1f)
    public val rainbowStopValue = FloatValue("RainbowStop", 0.58f, 0f, 1f)

    public val rainbowSaturationValue = FloatValue("RainbowSaturation", 0.7f, 0f, 1f)
    public val rainbowBrightnessValue = FloatValue("RainbowBrightness", 1f, 0f, 1f)
    public val rainbowSpeedValue = IntegerValue("RainbowSpeed", 1500, 500, 7000)

    private var hotBarX = 0F
    fun getrainbowStartValue(): Float {
        return rainbowStartValue.get()
    }

    fun getrainbowStopValue(): Float {
        return rainbowStopValue.get()
    }
    fun getAnimPos(pos: Float): Float {
        if (state && blackHotbarValue.get()) hotBarX = AnimationUtils.animate(pos, hotBarX, 0.02F * RenderUtils.deltaTime.toFloat())
        else hotBarX = pos

        return hotBarX
    }

    fun getrainbowSaturationValue(): Float {
        return rainbowSaturationValue.get()
    }

    fun getrainbowBrightnessValue(): Float {
        return rainbowBrightnessValue.get()
    }

    fun getrainbowSpeedValue(): Int {
        return rainbowSpeedValue.get()
    }


    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return

        LiquidBounce.hud.render(false)
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        LiquidBounce.hud.update()
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }

    @EventTarget(ignoreCondition = true)
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) return
        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.guiScreen != null &&
                !(classProvider.isGuiChat(event.guiScreen) || classProvider.isGuiHudDesigner(event.guiScreen))) mc.entityRenderer.loadShader(classProvider.createResourceLocation("loserline" + "/blur.json")) else if (mc.entityRenderer.shaderGroup != null &&
                mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("loserline/blur.json")) mc.entityRenderer.stopUseShader()
    }

    init {
        state = true
    }

}