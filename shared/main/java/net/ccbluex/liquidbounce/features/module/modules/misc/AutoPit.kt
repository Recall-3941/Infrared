/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.TextValue
import java.util.*

@ModuleInfo(name = "AutoPit", description = "0231 skid",Chinese="自动回城", category = ModuleCategory.COMBAT)
class AutoPit : Module() {
    var health = FloatValue("Health", 5F, 0F, 20F)
    private val keepArmor = BoolValue("KeepArmor",false)
    private val message = BoolValue("AutoHubChatText",false)
    private val messages = TextValue("AutoHubChatText","[Fantasy Beta #1225] 别听谣言四起，就想与我为敌^^")
    var autoArmorhealth = FloatValue("AutoArmorHealth", 20F, 0F, 20F)
    init {
        state = true
    }
    var wating = true
    var wating2 = true
    val timer = Timer()

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        var autoArmor =LiquidBounce.moduleManager[AutoArmor::class.java] as AutoArmor
        if (mc.thePlayer!!.health > autoArmorhealth.get()) {
            autoArmor.state = true
        }
        if (mc.thePlayer!!.health <= health.value) {
            if (keepArmor.get()) {
                for (i in 0..3) {
                    val armorSlot = 3 - i
                    move(8 - armorSlot, true)
                }


                if (mc.thePlayer!!.totalArmorValue < 4 && wating2) {
                    mc.thePlayer!!.sendChatMessage("/hub")
                    wating2 = false
                }
            } else {
                if (wating2) {
                    mc.thePlayer!!.sendChatMessage("/hub")
                    wating2 = false
                }
            }
            if (message.get() && wating) {
                mc.thePlayer!!.sendChatMessage(messages.get())
                wating = false
            }

            LiquidBounce.moduleManager[KillAura::class.java].state = false
            LiquidBounce.moduleManager[Velocity::class.java].state = false
        }
    }

    private fun move(item: Int, isArmorSlot: Boolean) {
        if (item != -1) {
            val openInventory = !classProvider.isGuiInventory(mc.currentScreen)
            if (openInventory) {
                classProvider.createCPacketEntityAction(
                    mc.thePlayer!!,
                    ICPacketEntityAction.WAction.OPEN_INVENTORY
                )
            }

            mc.playerController.windowClick(
                mc.thePlayer!!.inventoryContainer.windowId,
                if (isArmorSlot) item else if (item < 9) item + 36 else item,
                0,
                1,
                mc.thePlayer!!
            )

            if (openInventory) {
                mc.netHandler.addToSendQueue(classProvider.createCPacketCloseWindow())
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        wating = true
        wating2 = true
    }

    override val tag: String
        get() = "HuaYuTingPit"
}