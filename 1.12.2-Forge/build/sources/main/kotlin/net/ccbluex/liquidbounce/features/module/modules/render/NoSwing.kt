/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "NoSwing", description = "禁用攻击/挖矿时手部摇摆动画",Chinese="禁用摇摆动画", category = ModuleCategory.RENDER)
class NoSwing : Module() {
    val serverSideValue = BoolValue("ServerSide", true)
}