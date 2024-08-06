package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.ServerUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import java.awt.Color

@ModuleInfo(name = "BetterHotBar",Chinese="更好的物品栏",description="Skid", category = ModuleCategory.RENDER)
class BetterHotBar : Module() {
    private val modeValue = ListValue("bar", arrayOf("Black","Lite", "Off"), "Off")
    private val fontvalue = ListValue("Info", arrayOf("Info", "Off"),"Off")
    private val girlvalue = ListValue("girl", arrayOf("Crygirl","Happygirl","Bluecat", "Liuli", "Off"),"Off")
    private val offsetValue = IntegerValue("Y-Offset", 36, -50, 100)


    private var Black = classProvider.createResourceLocation("loserline/hud/betterhud1.png")
    private var Lite = classProvider.createResourceLocation("loserline/hud/betterhud2.png")
    private var crygirl = classProvider.createResourceLocation("loserline/girls/crygirl.png")
    private var happygirl = classProvider.createResourceLocation("loserline/girls/happygirl.png")
    private var bluecat = classProvider.createResourceLocation("loserline/girls/bluecat.png")
    private var liuli = classProvider.createResourceLocation("loserline/girls/liuli.png")




    @EventTarget
    fun onRender(event: Render2DEvent) {
        when (modeValue.get()) {
            "Black" -> drawhud(Black)
            "Lite" -> drawhud(Lite)
        }
        when(fontvalue.get()) {
            "Info" -> fonts()
        }
        when(girlvalue.get()) {
            "Crygirl" -> drawgirl(crygirl)
            "Happygirl" -> drawgirl(happygirl)
            "Bluecat" -> drawgirl(bluecat)
            "Liuli" -> drawgirl(liuli)
        }

    }

    private fun fonts() {
        Fonts.com35.drawString("FPS:" + Minecraft.getDebugFPS(),20F,495F + offsetValue.get(),-1)
        Fonts.flux.drawString("k",7F,496F + offsetValue.get(), Color(11,143,180).rgb)

        Fonts.com35.drawString("Ping:" + EntityUtils.getPing(mc.thePlayer),143F,495F + offsetValue.get(),-1)
        Fonts.flux.drawString("k",130F,496F + offsetValue.get(), Color(11,143,180).rgb)

        Fonts.com35.drawString("BPS:" + calculateBPS(),270F,495F + offsetValue.get(),-1)
        Fonts.flux.drawString("k",257F,496F + offsetValue.get(), Color(11,143,180).rgb)

        Fonts.com35.drawString("ServerIP:" + ServerUtils.getRemoteIp(),780F,495F + offsetValue.get(),-1)
        Fonts.flux.drawString("k",767F,496F + offsetValue.get(), Color(11,143,180).rgb)

    }
    fun calculateBPS(): Double {
        if(mc.thePlayer != null) {
            val bps = Math.hypot(
                mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
                mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
            ) * mc.timer.timerSpeed * 20
            return Math.round(bps * 100.0) / 100.0
        }else{
            return 0.00
        }

    }
    fun drawhud(resource: IResourceLocation) = RenderUtils.drawImage(resource,-8,487 + offsetValue.get(),1920,45)
    fun drawgirl(resource: IResourceLocation) = RenderUtils.drawImage(resource,570,415 + offsetValue.get(),100,100)

}

