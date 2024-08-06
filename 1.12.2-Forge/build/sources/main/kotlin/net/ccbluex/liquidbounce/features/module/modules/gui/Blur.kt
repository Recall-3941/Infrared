package net.ccbluex.liquidbounce.features.module.modules.gui

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.value.FloatValue


/**
 *
 * Skid by Paimon.
 * @Date 2022/10/29
 */
@ModuleInfo(name="Blur", description = "L", Chinese = "L", category = ModuleCategory.GUI)
class Blur : Module(){

    public val Radius = FloatValue("HudBlurRadius", 20f, 1f, 30f)
    //private val scoreboardBlur = BoolValue("Scoreboard", false)

   // private val invBlur = BoolValue("Inventory3", false)

    private var currentElement: Element? = null

    var selectedElement: Element? = null

    var selectedElement2: Element? = null

    var selectedElement3: Element? = null
    @EventTarget
    fun onRender2D(event:Render2DEvent){
            for (element in LiquidBounce.hud.elements) {
                /*
                if(scoreboardBlur.get()){
                    if(element.name == "GameInfo"){
                        selectedElement = element
                        BlurBuffer.blurRoundArea(selectedElement!!.renderX.toFloat(), selectedElement!!.renderY.toFloat() + 11F,176F,75F,
                           6
                        )
                        //LiquidBounce.hud.render(false)
                    }
                }
                if(invBlur.get()){
                    if(element.name == "Inventory3"){
                        selectedElement2 = element
                        BlurBuffer.blurArea(selectedElement2!!.renderX.toFloat(), selectedElement2!!.renderY.toFloat(),174F,66F)
                        //LiquidBounce.hud.render(false)
                    }
                }
                */
            }
        //LiquidBounce.hud.render(false)

    }
}