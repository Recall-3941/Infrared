package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "OldHitting", description = "faq",Chinese="防砍动画", category = ModuleCategory.RENDER)
class OldHitting : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Vanilla", "slide", "1.8","Remix","Push","WindMill"), "1.8")

    fun getModeValue(): ListValue? {
        return modeValue
    }

}