/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.util.ResourceLocation


@ModuleInfo(name = "Cape", description = "更好的披风",Chinese="披风", category = ModuleCategory.RENDER)
class Cape : Module() {

    val styleValue = ListValue("Style", arrayOf("Loserline","Lunar","Bilibili","LiquidBounce","Paimon","JiaRan","Stars","RQ","Bilibilitv","huoying","KatterSense"), "loserline")

    fun getCapeLocation(value: String): ResourceLocation {
        return try {
            CapeStyle.valueOf(value.toUpperCase()).location
        } catch (e: IllegalArgumentException) {
            CapeStyle.PAIMON.location
        }
    }

    enum class CapeStyle(val location: ResourceLocation) {
        LUNAR(ResourceLocation("loserline/cape/lunar.png")),
        BILIBILI(ResourceLocation("loserline/cape/biliBili.png")),
        LIQUIDBOUNCE(ResourceLocation("loserline/cape/liquidbounce.png")),
        PAIMON(ResourceLocation("loserline/cape/paimon.png")),
        JIARAN(ResourceLocation("loserline/cape/jiaran.png")),
        STAR(ResourceLocation("loserline/cape/star.png")),
        RQ(ResourceLocation("loserline/cape/rq.png")),
        BILIBILITV(ResourceLocation("loserline/cape/bilibilitv.png")),
        HUOYING(ResourceLocation("loserline/cape/huoying.png")),
        SKYRIM(ResourceLocation("loserline/cape/loserline.png")),
        KATTERSENSE(ResourceLocation("kattersense/cape/ks.png"))
    }

    override val tag: String
        get() = styleValue.get()
}