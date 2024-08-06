/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "NoSlowBreak", description = "允许你挖掘时无减速.", Chinese="禁用挖掘减速",category = ModuleCategory.WORLD)
class NoSlowBreak : Module() {
    val airValue = BoolValue("Air", true)
    val waterValue = BoolValue("Water", false)
}
