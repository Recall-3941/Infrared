package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import org.lwjgl.input.Keyboard

@ModuleInfo(name = "KeyBindManager",description="FDP",Chinese="按键绑定管理   ", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RMENU, canEnable = false)
class KeyBindManager : Module() {
    override fun onEnable() {
        mc2.displayGuiScreen(LiquidBounce.keyBindManager)
    }
}