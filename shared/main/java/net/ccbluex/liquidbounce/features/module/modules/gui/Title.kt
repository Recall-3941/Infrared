package net.ccbluex.liquidbounce.features.module.modules.gui

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import oh.yalan.NativeClass
import java.awt.Color



@NativeClass
@ModuleInfo(name = "Title",Chinese="游戏内标题",description=":)", category = ModuleCategory.GUI)
class Title : Module() {

    private val info = TextValue("Info", "im legit")
    private val Clientname = TextValue("Clientname",LiquidBounce.CLIENT_NAME)
    private val colorModeValue = ListValue("Color", arrayOf("Custom", "GodLightSync","novo","rainbow","skyrainbow","anptherrainbow"), "Custom")
    private val RedValue = IntegerValue("R", 255, 0, 255)
    private val GreenValue = IntegerValue("G", 255, 0, 255)
    private val BlueValue = IntegerValue("B", 255, 0, 255)
    private val AlphaValue = IntegerValue("A", 255,0,255)
    private val rainbowSpeed = IntegerValue("RainbowSpeed", 10, 1, 10)
    private val rainbowIndex = IntegerValue("RainbowIndex", 1, 1, 20)





    @EventTarget
    fun onRender(event: Render2DEvent){

        val TextColor = when (colorModeValue.get().toLowerCase()) {
            "novo" -> ColorUtils.novoRainbow(40).rgb
            "rainbow" -> ColorUtils.hslRainbow(rainbowIndex.get(), indexOffset = 100 * rainbowSpeed.get()).rgb
            "skyrainbow" -> RenderUtils.skyRainbow(rainbowIndex.get(), 1F, 1F).rgb
            "anotherrainbow" -> ColorUtils.fade(Color(RedValue.get(), GreenValue.get(), BlueValue.get(), AlphaValue.get()), 100, rainbowIndex.get()).rgb
            "godlightsync"->ColorUtils.GodLight(40).rgb
            else -> Color(RedValue.get(), GreenValue.get(), BlueValue.get(), 1).rgb
        }
        Fonts.com96.drawStringWithShadow(Clientname.get(),10,15, TextColor)
        Fonts.com35.drawStringWithShadow(LiquidBounce.CLIENT_VERSION.toString(),Fonts.com96.getStringWidth(LiquidBounce.CLIENT_NAME) + 13, 14,TextColor)
        Fonts.com35.drawStringWithShadow(info.get(),Fonts.com96.getStringWidth(LiquidBounce.CLIENT_NAME) + 13, 24,TextColor)
    }

}
