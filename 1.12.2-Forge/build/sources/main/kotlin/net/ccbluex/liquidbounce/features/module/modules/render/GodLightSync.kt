package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.IntegerValue


/**
 *
 * Skid by Paimon.
 * @Date 2022/10/6
 */
@ModuleInfo(name="GodLightSync", description = "你好", category = ModuleCategory.RENDER, canEnable = false, Chinese = "大神光同步")
class GodLightSync : Module(){

    public val r = IntegerValue("Red", 229, 0, 255)
    public val g = IntegerValue("Green", 100, 0, 255)
    public val b = IntegerValue("Blue", 173, 0, 255)
    val h = IntegerValue("H", 255, 0, 255)
    public val r2 = IntegerValue("Red2", 109, 0, 255)
    public val g2= IntegerValue("Green2",255, 0, 255)
    public val b2 = IntegerValue("Blue2", 255, 0, 255)
}