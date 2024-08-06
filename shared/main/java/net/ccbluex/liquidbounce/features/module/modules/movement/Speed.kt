package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.player.Blink
import net.minecraft.entity.Entity
import net.minecraft.entity.item.*
import net.minecraft.entity.projectile.*
import kotlin.math.cos
import kotlin.math.sin


/**
 *@author ycx_mulin
 *
 * P.S：无法联系原作者，但根据市面上现有的源代码并综合考量，我们判断可以将此代码放出，如侵权可删除
 */

@ModuleInfo(name = "Speed", description = "CCBlueX", category = ModuleCategory.MOVEMENT, Chinese = "加速")
class Speed : Module() {
    //原作者的话：
    /**
     * 对的，严格反作弊在玩家碰撞到实体的时候，
     * 会减弱对模拟的检测.
     *
     * 我们可以通过这个特性在玩家与任意实体碰撞时，
     * 增加0.08的速度.
     *
     * 仅适用于客户端版本1.9及以上.
     */

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val blink = LiquidBounce.moduleManager.getModule(Blink::class.java)
        if(blink.state) return
        val player = mc2.player ?: return
        if (player.moveForward == 0.0f && player.moveStrafing == 0.0f) return
        var collisions = 0
        val box = player.entityBoundingBox.expand(1.0, 1.0, 1.0)
        for (entity in mc2.world.loadedEntityList) {
            val entityBox = entity.entityBoundingBox
            if (canCauseSpeed(entity) && box.intersects(entityBox)) {
                collisions++
            }
        }

        // 严格反作弊给出0.08的最大包涵度
        val yaw = Math.toRadians(getMoveYaw().toDouble())
        val boost = 0.08 * collisions
        player.addVelocity(-sin(yaw) * boost, 0.0, cos(yaw) * boost)
    }


    private fun canCauseSpeed(entity: Entity) =
        // 这个我不清楚
        entity != mc2.player && entity !is EntityArmorStand && entity !is EntityItem && entity !is EntityArrow && entity !is EntitySnowball &&
                entity !is EntityFireball && entity !is EntityEgg && entity !is EntityFireworkRocket && entity !is EntityPotion && entity !is EntityFishHook && entity !is EntityLlamaSpit
                && entity !is EntityShulkerBullet && entity !is EntityThrowable && entity !is EntityTNTPrimed && entity !is EntityMinecartTNT && !entity.isDead


    // 抄strafe的
    private fun getMoveYaw(): Float {
        var moveYaw = mc.thePlayer!!.rotationYaw
        if (mc.thePlayer!!.moveForward != 0F && mc.thePlayer!!.moveStrafing == 0F) {
            moveYaw += if (mc.thePlayer!!.moveForward > 0) 0 else 180
        } else if (mc.thePlayer!!.moveForward != 0F && mc.thePlayer!!.moveStrafing != 0F) {
            if (mc.thePlayer!!.moveForward > 0) {
                moveYaw += if (mc.thePlayer!!.moveStrafing > 0) -45 else 45
            } else {
                moveYaw -= if (mc.thePlayer!!.moveStrafing > 0) -45 else 45
            }
            moveYaw += if (mc.thePlayer!!.moveForward > 0) 0 else 180
        } else if (mc.thePlayer!!.moveStrafing != 0F && mc.thePlayer!!.moveForward == 0F) {
            moveYaw += if (mc.thePlayer!!.moveStrafing > 0) -90 else 90
        }
        return moveYaw
    }
}