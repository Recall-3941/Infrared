package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.init.Items
//0231自写
@ModuleInfo(name = "AutoSave",  description = "合法自救平台 0231自写",Chinese="自动自救平台", category = ModuleCategory.PLAYER)
class AutoSave : Module() {
    private val debug =BoolValue("Debug",true)
    override fun onEnable() {
        ClientUtils.displayChatMessage("§6[AutoSave]本模块用于自动自救平台，稳定性未知！-0231")
    }
    @EventTarget
    open fun isInVoid(): Boolean {
        for (i in 0..128) {
            if (MovementUtils.isOnGround(i.toDouble())) {
                return false
            }
        }
        return true
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (isInVoid()) {
            var BLAZERODInHotbar = InventoryUtils.findItem2(36, 45, Items.BLAZE_ROD)
            if(BLAZERODInHotbar != -1) {
                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(BLAZERODInHotbar - 36))
                mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.heldItem))
                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(mc.thePlayer!!.inventory.currentItem))
            }
            if (debug.get()){
                ClientUtils.displayChatMessage("[AutoSave]检测到你处于虚空，开始使用自救平台!")
            }

        }
    }
}