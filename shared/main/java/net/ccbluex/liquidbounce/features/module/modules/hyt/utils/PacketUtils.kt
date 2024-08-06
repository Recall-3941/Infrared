package net.ccbluex.liquidbounce.features.module.modules.hyt.utils


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.misc.PostDisabler
import net.ccbluex.liquidbounce.utils.MathUtils

import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.minecraft.client.Minecraft
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.CPacketConfirmTransaction

object PacketUtils : MinecraftInstance() {
    private val packets = ArrayList<Packet<INetHandlerPlayServer>>()
    private val packets2 = ArrayList<Packet<*>>()
    private var postDisabler = LiquidBounce.moduleManager.getModule(PostDisabler::class.java) as PostDisabler
    @JvmStatic
    fun send(packet: Packet<*>?) {
        if (mc.thePlayer != null) {
            Minecraft.getMinecraft().connection!!.sendPacket(packet)
        }
    }
    @JvmStatic
    fun handleSendPacket(packet: Packet<*>): Boolean {
        if (packets.contains(packet)) {
            packets.remove(packet)
            return true
        }
        return false
    }
    fun sendPacketNoEvent2(packet: Packet<*>) {
        packets2.add(packet)
        mc2.connection!!.sendPacket(packet)
    }


    //这个是你需要手动发包，需要加c0f就引用这个
    @JvmStatic
    fun sendPacketC0F() {
        if (!postDisabler.getGrimPost()) {
            send(
                CPacketConfirmTransaction(
                    MathUtils.getRandomInRange(102, 1000024123),
                    MathUtils.getRandomInRange(102, 1000024123).toShort(), true
                ) as Packet<*>?
            )
        }
    }
    @JvmStatic
    fun sendPacketNoEvent(packet: Packet<INetHandlerPlayServer>) {
        packets.add(packet)
        mc2.connection!!.sendPacket(packet)
    }

    @JvmStatic
    fun getPacketType(packet: Packet<*>): PacketType {
        val className = packet.javaClass.simpleName
        if (className.startsWith("C", ignoreCase = true)) {
            return PacketType.CLIENTSIDE
        } else if (className.startsWith("S", ignoreCase = true)) {
            return PacketType.SERVERSIDE
        }
        return PacketType.UNKNOWN
    }

    enum class PacketType {
        SERVERSIDE,
        CLIENTSIDE,
        UNKNOWN
    }
}