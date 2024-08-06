package net.ccbluex.liquidbounce.features.module.modules.misc


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.hyt.utils.PacketUtils
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.injection.backend.wrap
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.GuiDownloadTerrain
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraft.network.login.server.SPacketEncryptionRequest
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.network.play.server.*
import net.minecraft.network.status.server.SPacketPong
import net.minecraft.network.status.server.SPacketServerInfo
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.CopyOnWriteArrayList

/*
    来源：ZeroSense-慢性&&……
    作者：未知
 */
@ModuleInfo(name = "PostDisabler", description = "PostDisabler", category = ModuleCategory.MISC, Chinese = "禁用器")
class PostDisabler : Module() {
    val modeValue = ListValue("Mode", arrayOf("GrimAC","No"), "GrimAC")
    val post =BoolValue("Post",true)
    private val fastbreak  = BoolValue("Fastbreak ",true)
    private val AutoBlockFix = BoolValue("RotaionPlace",true)
    val badpacketA= BoolValue("BadPacket A",false)
    private val debug = BoolValue("Debug",false)
    private var delayValue = IntegerValue("LogDelay", 40, 1, 1000)
    private var lastSlot: Int = -1
    var timer = MSTimer()

    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet.unwrap()
        val connection = mc.unwrap().connection ?: return
        val our = classProvider.isItemFood(mc.thePlayer!!.heldItem?.item) || classProvider.isItemPotion(mc.thePlayer!!.heldItem?.item) || classProvider.isItemBucketMilk(mc.thePlayer!!.heldItem?.item) || classProvider.isItemBow(mc.thePlayer!!.heldItem?.item)
        val killAura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
        if (packet is CPacketPlayerTryUseItemOnBlock && ( killAura.currentTarget != null || our) && AutoBlockFix.get()) {
            event.cancelEvent()
            if (debug.get()) {
                val timer = MSTimer()
                if (timer.hasTimePassed(delayValue.get().toLong() * 5000)) {
                    debugMessage("Cancel RotaionPlace vl")
                    timer.reset()
                }
            }
        }
        if(badpacketA.get() && packet is CPacketHeldItemChange){
            val slot: Int = packet.slotId
            if (slot == this.lastSlot && slot != -1) {
                event.cancelEvent()
                if (debug.get()) {
                    val timer = MSTimer()
                    if (timer.hasTimePassed(delayValue.get().toLong() * 5000)) {
                        debugMessage("Cancel badPacketA vl")
                        timer.reset()
                    }
                }
            }
            this.lastSlot = packet.slotId

        }
        //fastbreak
        if (fastbreak.get() && (packet is CPacketPlayerDigging && packet.action == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
            connection.sendPacket(CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK,
                packet.position.add(0, 500, 0), packet.facing))
            if (debug.get()) {
                val timer = MSTimer()
                if (timer.hasTimePassed(delayValue.get().toLong() * 5000)) {
                    debugMessage("Cancel fastbreak vl")
                    timer.reset()
                }
            }
            timer.reset()
        }
    }
    @EventTarget
    fun onWorld(event: WorldEvent) {
        lastSlot = -1
    }
    companion object {

        // Static
        @JvmStatic
        var storedPackets: MutableList<Packet<INetHandler>> = CopyOnWriteArrayList()
        @JvmStatic
        var pingPackets: ConcurrentLinkedDeque<Int> = ConcurrentLinkedDeque()
        @JvmStatic
        private var lastResult = false
    }
    fun getGrimPost(): Boolean {
        val postDisabler = LiquidBounce.moduleManager.getModule(PostDisabler::class.java) as PostDisabler
        val result = postDisabler.state && modeValue.get()=="GrimAC" && post.get()
                && mc.thePlayer != null
                && mc.thePlayer!!.entityAlive
                && mc.thePlayer!!.ticksExisted >= 10
                && mc.currentScreen !is GuiDownloadTerrain

        if (lastResult && !result) {
            lastResult = false
            mc2.addScheduledTask { processPackets() }
        }
        return result.also { lastResult = it }
    }

    fun processPackets() {
        if (storedPackets.isNotEmpty()) {
            for (packet in storedPackets) {
                val event = PacketEvent(packet.wrap())
                LiquidBounce.eventManager.callEvent(event)
                if (event.isCancelled) {
                    continue
                }
                packet.processPacket(mc2.connection as INetHandler)
            }
            storedPackets.clear()
        }
    }

    fun grimPostDelay(packet: Packet<*>): Boolean {
        if (mc.thePlayer == null) {
            return false
        }
        if (mc.currentScreen is GuiDownloadTerrain) {
            return false
        }
        if (packet is SPacketServerInfo) {
            return false
        }
        if (packet is SPacketEncryptionRequest) {
            return false
        }
        if (packet is SPacketPlayerListItem) {
            return false
        }
        if (packet is SPacketDisconnect) {
            return false
        }
        if (packet is SPacketChunkData) {
            return false
        }
        if (packet is SPacketPong) {
            return false
        }
        if (packet is SPacketWorldBorder) {
            return false
        }
        if (packet is SPacketJoinGame) {
            return false
        }
        if (packet is SPacketEntityHeadLook) {
            return false
        }
        if (packet is SPacketTeams) {
            return false
        }
        if (packet is SPacketChat) {
            return false
        }
        if (packet is SPacketSetSlot) {
            return false
        }
        if (packet is SPacketEntityMetadata) {
            return false
        }
        if (packet is SPacketEntityProperties) {
            return false
        }
        if (packet is SPacketUpdateTileEntity) {
            return false
        }
        if (packet is SPacketTimeUpdate) {
            return false
        }
        if (packet is SPacketPlayerListHeaderFooter) {
            return false
        }
        if (packet is SPacketEntityVelocity) {
            val sPacketEntityVelocity: SPacketEntityVelocity = packet
            return sPacketEntityVelocity.entityID == mc.thePlayer!!.entityId
        }
        return packet is SPacketExplosion
                || packet is SPacketConfirmTransaction
                || packet is SPacketPlayerPosLook
                //  || packet is SPacketEntityTeleport
                //  || packet is SPacketEntityStatus
                || packet is SPacketEntityEquipment
                || packet is SPacketBlockChange
                || packet is SPacketMultiBlockChange
                //   || packet is SPacketDestroyEntities
                || packet is SPacketKeepAlive
                || packet is SPacketUpdateHealth
                || packet is SPacketEntity
                || packet is SPacketSpawnMob
                || packet is SPacketCustomPayload
    }
    fun fixC0F(packet: CPacketConfirmTransaction) {
        val id: Int = packet.uid.toInt()
        if (id >= 0 || pingPackets.isEmpty()) {
            PacketUtils.sendPacketNoEvent(packet)
        } else {
            do {
                val current: Int = pingPackets.first
                PacketUtils.sendPacketNoEvent(CPacketConfirmTransaction(packet.windowId, current.toShort(), true))
                pingPackets.pollFirst()
                if (current == id) {
                    break
                }
            } while (!pingPackets.isEmpty())
        }
    }
    fun debugMessage(str: String) {
        if (debug.get()) {
            ClientUtils.displayChatMessage("§7[Disabler] $str")
        }
    }
}