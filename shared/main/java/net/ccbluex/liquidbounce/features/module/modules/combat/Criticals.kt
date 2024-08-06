/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.MovementUtils.isOnGround
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import oh.yalan.NativeClass

@NativeClass
@ModuleInfo(name = "Criticals", description = "自动打出暴击伤害",Chinese="刀刀暴只因", category = ModuleCategory.COMBAT)
class Criticals : Module() {

    val modeValue = ListValue("Mode", arrayOf("FunkNight","NewPacket","Packet", "NcpPacket", "NoGround", "Hop", "TPHop", "Jump", "LowJump","Visual","HytSpartan","ShenBi","AAC4Hyt"), "packet")
    val delayValue = IntegerValue("Delay", 0, 0, 500)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)

    private val lookValue = BoolValue("UseC06Packet", false)
    private var attacks = 0
    val msTimer = MSTimer()

    override fun onEnable() {
        if (modeValue.get().equals("NoGround", ignoreCase = true))
            mc.thePlayer!!.jump()
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (classProvider.isEntityLivingBase(event.targetEntity)) {
            val thePlayer = mc.thePlayer ?: return
            val entity = event.targetEntity!!.asEntityLivingBase()

            if (!thePlayer.onGround || thePlayer.isOnLadder || thePlayer.isInWeb || thePlayer.isInWater ||
                    thePlayer.isInLava || thePlayer.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() ||
                    LiquidBounce.moduleManager[Fly::class.java]!!.state || !msTimer.hasTimePassed(delayValue.get().toLong()))
                return

            val x = thePlayer.posX
            val y = thePlayer.posY
            val z = thePlayer.posZ
            var lowhopdelay = 0
            var n = 0
            fun sendCriticalPacket(xOffset: Double = 0.0, yOffset: Double = 0.0, zOffset: Double = 0.0, ground: Boolean) {
                val x = mc.thePlayer!!.posX + xOffset
                val y = mc.thePlayer!!.posY + yOffset
                val z = mc.thePlayer!!.posZ + zOffset
                if (lookValue.get()) {
                    mc.netHandler.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(x, y, z, mc.thePlayer!!.rotationYaw, mc.thePlayer!!.rotationPitch, ground))
                } else {
                    mc.netHandler.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, ground))
                }
            }
            when (modeValue.get().toLowerCase()) {
                "funknight"->{
                    /**
                     * JS TO KT
                     * BY RECALLQWQ
                     */
                    lowhopdelay++;
                    if(mc.thePlayer!!.onGround || isOnGround(0.5)){
                        if(lowhopdelay > 50){
                            mc.thePlayer?.motionY = 0.06
                        }
                    }
                }
                "newpacket" -> {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.05250000001304, z, true))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.00150000001304, z, false))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.01400000001304, z, false))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.00150000001304, z, false))
                    thePlayer.onCriticalHit(entity)
                }
                "packet" -> {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.0625, z, true))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y, z, false))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 1.1E-5, z, false))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y, z, false))
                    thePlayer.onCriticalHit(entity)
                }

                "ncppacket" -> {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.11, z, false))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.1100013579, z, false))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.0000013579, z, false))
                    thePlayer.onCriticalHit(entity)
                }
                "hytspartan" -> {
                    n = this.attacks;
                    this.attacks = n + 1;
                    if (this.attacks <= 6){

                    }else{
                        mc.netHandler.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.01, z, false));
                        mc.netHandler.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 1.0E-10, z, false));
                        mc.netHandler.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.114514, z, false));
                        this.attacks = 0;
                    }
                }
                "hop" -> {
                    thePlayer.motionY = 0.1
                    thePlayer.fallDistance = 0.1f
                    thePlayer.onGround = false
                }

                "tphop" -> {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.02, z, false))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.01, z, false))
                    thePlayer.setPosition(x, y + 0.01, z)
                }
                "shenbi" -> {
                    attacks++

                    if(attacks > 6) {
                        if (thePlayer.onGround) {
                            thePlayer.motionY = 0.25
                            attacks = 0
                        }
                    }

                }
                "aac4hyt"->{
                    attacks++
                    if (attacks > 5) {
                        sendCriticalPacket(yOffset = 0.0114514, ground = false)
                        sendCriticalPacket(yOffset = 0.0019 ,ground = false)
                        sendCriticalPacket(yOffset = 0.000001 ,ground = false)
                        attacks = 0
                    }
                }

                "jump" -> thePlayer.motionY = 0.42
                "visual" -> thePlayer.onCriticalHit(entity)
                "lowjump" -> thePlayer.motionY = 0.3425
            }

            msTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (classProvider.isCPacketPlayer(packet) && modeValue.get().equals("NoGround", ignoreCase = true))
            packet.asCPacketPlayer().onGround = false
    }

    override val tag: String?
        get() = modeValue.get()
}
