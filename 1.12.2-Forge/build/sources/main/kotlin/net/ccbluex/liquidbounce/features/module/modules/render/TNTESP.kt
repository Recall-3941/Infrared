/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import java.awt.Color

@ModuleInfo(name = "TNTESP", description = "允许你穿墙看到点燃的TNT", Chinese="炸弹透视",category = ModuleCategory.RENDER)
class TNTESP : Module() {

    @EventTarget
    fun onRender3D(event : Render3DEvent) {
        mc.theWorld!!.loadedEntityList.filter(classProvider::isEntityTNTPrimed).forEach { RenderUtils.drawEntityBox(it, Color.RED, false) }
    }
}