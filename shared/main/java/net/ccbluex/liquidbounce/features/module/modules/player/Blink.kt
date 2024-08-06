package net.ccbluex.liquidbounce.features.module.modules.player

//import Yuan.Shen.liquidbounce.event.*
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.client.*
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

@ModuleInfo(name = "Blink", description = "Suspends all movement packets.", category = ModuleCategory.PLAYER, Chinese = "暂停发包")
class Blink : Module() {
    private val packets = LinkedBlockingQueue<Packet<*>>()
    private val spackets = LinkedBlockingQueue<Packet<INetHandlerPlayClient>>()
    private var fakePlayer: EntityOtherPlayerMP? = null
    private var disableLogger = false
    private val positions = LinkedList<DoubleArray>()
    private val pulseValue = BoolValue("Pulse", false)
    private val pulseDelayValue = IntegerValue("PulseDelay", 1000, 500, 5000)
    private val nowhenhurt = BoolValue("NoWhenHurt",false)
    private val hurttime = IntegerValue("HurtTime",0,0,100)
    private val pulseTimer = MSTimer()
    var blinking = false
    var pre = false
    private var delay = 0
    private var hurt = false
    override fun onEnable() {
        blinking = true
        if (mc2.player == null) return
        synchronized(positions) {
            positions.add(
                doubleArrayOf(
                    mc2.player.posX,
                    mc2.player.entityBoundingBox.minY + mc2.player.eyeHeight / 2,
                    mc2.player.posZ
                )
            )
            positions.add(doubleArrayOf(mc2.player.posX, mc2.player.entityBoundingBox.minY, mc2.player.posZ))
        }
        pulseTimer.reset()
    }

    override fun onDisable() {
        if (mc2.player == null) return
        blink()
        if (fakePlayer != null) {
            mc.theWorld!!.removeEntityFromWorld(fakePlayer!!.entityId)
            fakePlayer = null
        }
        blinking = false
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if (mc2.player == null || disableLogger) return
        if (packet is CPacketPlayer ||
            packet is CPacketPlayerDigging ||
            packet is CPacketHeldItemChange ||
            packet is CPacketEntityAction || packet is CPacketConfirmTransaction
        ) {
            event.cancelEvent()
            packets.add(packet)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        synchronized(positions) {
            positions.add(
                doubleArrayOf(
                    mc2.player.posX,
                    mc2.player.entityBoundingBox.minY,
                    mc2.player.posZ
                )
            )
        }
        if(mc2.player.hurtTime > 0 && nowhenhurt.get()){
            hurt = true
        }
        if(hurt){
            delay ++
            blink()
            pulseTimer.reset()
            if(delay > hurttime.get()){
                delay = 0
                hurt = false
            }
        }
        if (pulseValue.get() && pulseTimer.hasTimePassed(pulseDelayValue.get().toLong())) {
            blink()
            pulseTimer.reset()
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent){
        pre = event.eventState == EventState.PRE
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        val breadcrumbs = LiquidBounce.moduleManager.getModule(Breadcrumbs::class.java) as Breadcrumbs?
        val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
        val color = if (breadcrumbs!!.colorRainbow.get()) rainbow() else Color(
            hud.r.get(),hud.g.get(),hud.b.get()
        )
        synchronized(positions) {
            GL11.glPushMatrix()
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            mc.entityRenderer.disableLightmap()
            GL11.glBegin(GL11.GL_LINE_STRIP)
            RenderUtils.glColor(color)
            val renderPosX = mc.renderManager.viewerPosX
            val renderPosY = mc.renderManager.viewerPosY
            val renderPosZ = mc.renderManager.viewerPosZ
            for (pos in positions) GL11.glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ)
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
            GL11.glEnd()
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glPopMatrix()
        }
    }

    override val tag: String
        get() = packets.size.toString()

    private fun blink() {
        try {
            disableLogger = true
            blinking = false
            while (!packets.isEmpty()) {
                mc2.connection!!.networkManager.sendPacket(packets.take())
            }
            while (!spackets.isEmpty()){
                spackets.take().processPacket(mc2.connection)
            }
            blinking = true
            disableLogger = false
        } catch (e: Exception) {
            e.printStackTrace()
            disableLogger = false
        }
        synchronized(positions) { positions.clear() }
    }
}