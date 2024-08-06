/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue
@ModuleInfo(name = "HitBox", description = "让目标的碰撞箱更大",Chinese="碰撞箱增大", category = ModuleCategory.COMBAT)

class HitBox : Module() {

    val sizeValue = FloatValue("Size", 0.4F, 0F, 1F)

}