/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "Reach", description = "增加攻击距离",Chinese="长臂猿",category = ModuleCategory.LEGIT)
class Reach : Module() {

    val combatReachValue = FloatValue("CombatReach", 3.5f, 3f, 7f)
    val buildReachValue = FloatValue("BuildReach", 5f, 4.5f, 7f)

    val maxRange: Float
        get() {
            val combatRange = combatReachValue.get()
            val buildRange = buildReachValue.get()

            return if (combatRange > buildRange) combatRange else buildRange
        }
}
