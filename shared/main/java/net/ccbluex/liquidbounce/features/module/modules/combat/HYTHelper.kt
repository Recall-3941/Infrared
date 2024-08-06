package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.legit.LegitSpeed
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.player.Blink
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.network.play.client.CPacketKeepAlive

@ModuleInfo(name = "HYTHelper", description = "旧时代的产物",Chinese="花雨庭帮助器", category = ModuleCategory.COMBAT)
class HYTHelper : Module() {
    private val scaffoldhelper: BoolValue =  BoolValue("ScaffoldHelper", false)
    private val velocityhelper: BoolValue =  BoolValue("VelocityHelper", false)
    private val lagbackhelper: BoolValue = BoolValue("LagBackHelper",false)
    private val aurasprinthelper: BoolValue = BoolValue("AuraKeepSprintHelper",false)
    private val packetfixer: BoolValue = BoolValue("PacketFixer",false)
    private val fixBlinkAndFreecam = BoolValue("Fix-Blink3Y", false)
    private val fixItemSwap = BoolValue("Fix-Scaffold14D", false)
    private val flagchecker = BoolValue("LagBackCheck",true)
    private val flagreason = BoolValue("LagReason-Tips",true)
    //private val autofight = BoolValue("AutoWalkToTarget",false)
    private val debug= BoolValue("Debug", false)


    private var prevSlot = -1
    private var ticks = 0
    private var a = 0
    private var b=0
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (fixBlinkAndFreecam.get() && (LiquidBounce.moduleManager.getModule(Blink::class.java)!!.state || LiquidBounce.moduleManager.getModule(
                FreeCam::class.java
            )!!.state) && packet is CPacketKeepAlive && packetfixer.get()
        ) {
            event.cancelEvent()
        }

        if (flagchecker.get()) {
            if (classProvider.isSPacketPlayerPosLook(packet) && ticks == 20) {
                ClientUtils.displayChatMessage("[FlagChecker]Flag Detected!")
                if (flagreason.get()) {
                    val KillAura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
                    val Velocity = LiquidBounce.moduleManager.getModule(Velocity::class.java) as Velocity
                    val Speed = LiquidBounce.moduleManager.getModule(Speed::class.java) as Speed
                    val Scaffold = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
                    val Blink = LiquidBounce.moduleManager.getModule(Blink::class.java) as Blink
                    val LegitSpeed = LiquidBounce.moduleManager.getModule(LegitSpeed::class.java) as LegitSpeed
                    if (KillAura.state == true) {
                        if (Velocity.state == true) {
                            LiquidBounce.hud.addNotification(
                                Notification(
                                    "FlagReason",
                                    "Killaura or Velocity",
                                    NotifyType.INFO
                                )
                            )
                        } else {
                            LiquidBounce.hud.addNotification(Notification("FlagReason", "Killaura", NotifyType.INFO))
                        }
                    }
                    if (Speed.state == true || LegitSpeed.state == true) {
                        LiquidBounce.hud.addNotification(Notification("FlagReason", "Speed", NotifyType.INFO))
                    }
                    if (Scaffold.state == true) {
                        LiquidBounce.hud.addNotification(Notification("FlagReason", "Scaffold", NotifyType.INFO))
                    }
                    if (Blink.state == true) {
                        LiquidBounce.hud.addNotification(Notification("FlagReason", "Blink", NotifyType.INFO))
                    }
                }
            }
        }

        if (!mc2.isSingleplayer() && fixItemSwap.get() && packet is CPacketHeldItemChange && packetfixer.get()) {
            if (packet.getSlotId() == prevSlot) {
                event.cancelEvent()
            } else {
                prevSlot = packet.getSlotId()
            }
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        //if(autofight.get()){

        //}
        ticks++
        if(ticks == 20){
            ticks = 0
        }
        if (scaffoldhelper.get()) {
            val KillAura = LiquidBounce.moduleManager.getModule(KillAura::class.java)
            val Scaffold = LiquidBounce.moduleManager.getModule(Scaffold::class.java)
            if (Scaffold.state) {
                LiquidBounce.moduleManager.getModule(KillAura::class.java).state = false
                if(debug.get()){
                    ClientUtils.displayChatMessage("[ScaffoldHelper]Close ur Killaura")
                }
            }
        }
        if (velocityhelper.get()) {
            val velocity_module = LiquidBounce.moduleManager.getModule(Velocity::class.java)
            velocity_module.state = mc.thePlayer!!.onGround
        }
        if (lagbackhelper.get())  {
            val Killaura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
            if(ticks > 250){
                if(mc.thePlayer!!.isOnLadder&&mc.gameSettings.keyBindJump.pressed){
                    mc.thePlayer!!.motionY=0.11
                }
            }
            if(ticks > 500){
                ticks = 0
            }else{
                ticks++
            }
        }
        if(aurasprinthelper.get()){
            val KillAura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
            if(mc.thePlayer!!.onGround){
                if(b == 0){
                    KillAura.keepSprintValue.set(true)
                    b++
                }
            }else{
                b = 0
                if(a == 0){
                    KillAura.keepSprintValue.set(false)
                    a++
                }
            }
        }
    }
    override val tag: String
        get() =
            if (lagbackhelper.get())
                "LagCheck"
            else
                "Cheat+"
}