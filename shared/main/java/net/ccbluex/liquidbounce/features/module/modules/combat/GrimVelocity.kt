/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.minecraft.network.play.client.CPacketAnimation
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraft.util.EnumHand

@ModuleInfo(name = "GrimNoXZ", description = "noxz", category = ModuleCategory.COMBAT, Chinese = "严格反作弊-无击退")
class GrimVelocity : Module() {

    private var a = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        for (entity in mc2.world.loadedEntityList) {
            val killaura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            if (killaura.target != null) {
                if (entity.entityId == killaura.target!!.entityId) {
                    if (mc2.player.hurtTime == 9) {
                        if (!mc2.player.serverSprintState && !a) {
                            mc2.connection!!.sendPacket(
                                CPacketEntityAction(
                                    mc2.player,
                                    CPacketEntityAction.Action.START_SPRINTING
                                )
                            )
                            a = true
                        }
                        repeat(5) {
                            mc2.connection!!.sendPacket(CPacketUseEntity(entity))
                            mc2.connection!!.sendPacket(CPacketAnimation(EnumHand.MAIN_HAND))
                            mc2.player.motionX *= 0.6
                            mc2.player.motionZ *= 0.6
                        }
                        if (a && (mc2.gameSettings.keyBindLeft.isKeyDown || mc2.gameSettings.keyBindRight.isKeyDown || mc2.gameSettings.keyBindBack.isKeyDown)) {
                            mc2.connection!!.sendPacket(
                                CPacketEntityAction(
                                    mc2.player,
                                    CPacketEntityAction.Action.STOP_SPRINTING
                                )
                            )
                        }
                        break

                    }
                }
            }
        }
    }
    @EventTarget
    fun onPacket(event:PacketEvent) {
        val packet = event.packet.unwrap()
        if (packet is CPacketEntityAction && packet.action.equals(CPacketEntityAction.Action.STOP_SPRINTING) || mc2.gameSettings.keyBindForward.isKeyDown) {
            a = false
        }
    }
}