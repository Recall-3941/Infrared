///*
// * LiquidBounce Hacked Client
// * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
// * https://github.com/CCBlueX/LiquidBounce/
// */
//package net.ccbluex.liquidbounce.features.module.modules.combat
//
//import net.ccbluex.liquidbounce.LiquidBounce
//import net.ccbluex.liquidbounce.api.enums.EnumFacingType
//import net.ccbluex.liquidbounce.api.enums.WEnumHand
//import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
//import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
//import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
//import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity
//import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType
//import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
//import net.ccbluex.liquidbounce.api.minecraft.util.WVec3
//import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings
//import net.ccbluex.liquidbounce.event.*
//import net.ccbluex.liquidbounce.features.module.Module
//import net.ccbluex.liquidbounce.features.module.ModuleCategory
//import net.ccbluex.liquidbounce.features.module.ModuleInfo
//import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot
//import net.ccbluex.liquidbounce.features.module.modules.misc.Teams
//import net.ccbluex.liquidbounce.features.module.modules.player.Blink
//import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam
//import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
//import net.ccbluex.liquidbounce.injection.backend.Backend
//import net.ccbluex.liquidbounce.utils.*
//import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
//import net.ccbluex.liquidbounce.utils.misc.RandomUtils
//import net.ccbluex.liquidbounce.utils.render.RenderUtils
//import net.ccbluex.liquidbounce.utils.timer.MSTimer
//import net.ccbluex.liquidbounce.utils.timer.TimeUtils
//import net.ccbluex.liquidbounce.value.BoolValue
//import net.ccbluex.liquidbounce.value.FloatValue
//import net.ccbluex.liquidbounce.value.IntegerValue
//import net.ccbluex.liquidbounce.value.ListValue
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.item.ItemAxe
//import net.minecraft.item.ItemPickaxe
//import net.minecraft.item.ItemSword
//import org.lwjgl.opengl.GL11
//import java.awt.Color
//import java.awt.Robot
//import java.awt.event.InputEvent
//import java.util.*
//import kotlin.math.*
//
//@ModuleInfo(name = "KillAura2", description = "Automatically attacks targets around you.",
//        category = ModuleCategory.COMBAT, Chinese = "杀戮光环2")
//class KillAura2 : Module() {
//
//    /**
//     * OPTIONS
//     */
//
//    // CPS - Attack speed
//    private val maxCPS: IntegerValue = object : IntegerValue("MaxCPS", 8, 1, 30) {
//        override fun onChanged(oldValue: Int, newValue: Int) {
//            val i = minCPS.get()
//            if (i > newValue) set(i)
//
//            attackDelay = getAttackDelay(minCPS.get(), this.get())
//        }
//    }
//
//    private val minCPS: IntegerValue = object : IntegerValue("MinCPS", 5, 1, 20) {
//        override fun onChanged(oldValue: Int, newValue: Int) {
//            val i = maxCPS.get()
//            if (i < newValue) set(i)
//
//            attackDelay = getAttackDelay(this.get(), maxCPS.get())
//        }
//    }
//    private val StopHurtWhenHurt = BoolValue("StopHurtWhenHurt", false)
//    private val StopHurtWhenAttack = BoolValue("StopHurtWhenAttack", false)
//
//    private val combatDelayValue = BoolValue("1.9CombatDelay", false)
//    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
//    private val switchDelayValue = IntegerValue("SwitchDelay", 700, 0, 2000)
//
//    // Range
//    val rangeValue = object : FloatValue("Range", 3.7f, 1f, 8f){
//        override fun onChanged(oldValue: Float, newValue: Float) {
//            val d = discoverRangeValue.get()
//            if (d < newValue) set(d)
//        }
//    }
//    private val throughWallsRangeValue = object : FloatValue("ThroughWallsRange", 1.5f, 0f, 8f) {
//        override fun onChanged(oldValue: Float, newValue: Float) {
//            val d = discoverRangeValue.get()
//            if (d < newValue) set(d)
//        }
//    }
//    private val discoverRangeValue = FloatValue("DiscoverRange", 6f, 0f, 15f)
//    private val rangeSprintReducementValue = FloatValue("RangeSprintReducement", 0f, 0f, 0.4f)
//
//    // Modes
//    private val priorityValue = ListValue("Priority", arrayOf("Health", "Distance", "Direction", "LivingTime"), "Distance")
//    private val targetModeValue = ListValue("TargetMode", arrayOf("Single", "Switch", "Multi"), "Switch")
//
//    // Bypass
//    private val swingValue = BoolValue("Swing", true)
//    public val noSwingValue = BoolValue("NoSwing", false)
//    public val serverSideNoSwingValue = BoolValue("ServerSideNoSwing", false)
//    val keepSprintValue = BoolValue("KeepSprint", true)
//
//    // AutoBlock
//    private val autoBlockValue = ListValue("AutoBlock", arrayOf("Off", "Packet", "AfterTick", "Right","MoonX","NCP","Hypixel","Old1.8.9", "Hyt1.12.2"), "Packet")
//    private val interactAutoBlockValue = BoolValue("InteractAutoBlock", true)
//    private val delayedBlockValue = BoolValue("DelayedBlock", true)
//    private val blockRate = IntegerValue("BlockRate", 100, 1, 100)
//
//    // Raycast
//    private val raycastValue = BoolValue("RayCast", true)
//    private val raycastIgnoredValue = BoolValue("RayCastIgnored", false)
//    private val livingRaycastValue = BoolValue("LivingRayCast", true)
//
//    // Bypass
//    private val aacValue = BoolValue("AAC", false)
//
//    // Turn Speed
//    private val maxTurnSpeed: FloatValue = object : FloatValue("MaxTurnSpeed", 180f, 0f, 180f) {
//        override fun onChanged(oldValue: Float, newValue: Float) {
//            val v = minTurnSpeed.get()
//            if (v > newValue) set(v)
//        }
//    }
//
//    private val minTurnSpeed: FloatValue = object : FloatValue("MinTurnSpeed", 180f, 0f, 180f) {
//        override fun onChanged(oldValue: Float, newValue: Float) {
//            val v = maxTurnSpeed.get()
//            if (v < newValue) set(v)
//        }
//    }
//
//    private val rotationSmoothValue = FloatValue("CustomSmooth", 2f, 1f, 10f)
//    private val rotationSmoothModeValue = ListValue("SmoothMode", arrayOf("Custom", "Line", "Quad", "Sine","Tine","QuadSine"), "Sine")
//    private val rotations = ListValue("RotationMode", arrayOf("Vanilla","Xinst","Better","TIGSMA", "Other","BackTrack", "LiquidSensePlus","114Sense","Test","HytRotation"), "Better")
//    private val noHitCheck = BoolValue("NoHitCheck", false)
//    private val blinkCheck = BoolValue("BlinkCheck", false)
//    private val noScaffValue = BoolValue("NoScaffold", true)
//    private val silentRotationValue = BoolValue("SilentRotation", true)
//    private val rotationStrafeValue = ListValue("Strafe", arrayOf("Off", "Strict", "Silent"), "Silent")
//    private val randomCenterValue = BoolValue("RandomCenter", true)
//    private val outborderValue = BoolValue("Outborder", false)
//    private val fovValue = FloatValue("FOV", 180f, 0f, 360f)
//    private val attackTimingValue = ListValue("AttackTiming", arrayOf("All", "Pre", "Post", "Both"), "All")
//    private val randomCenterModeValue = ListValue("RandomCenter", arrayOf("Off", "Cubic", "Horizonal", "Vertical"), "Off")
//    private val randomCenRangeValue = FloatValue("RandomRange", 0.0f, 0.0f, 1.2f)
//    // Predict
//    private val predictValue = BoolValue("Predict", true)
//
//    private val maxPredictSize: FloatValue = object : FloatValue("MaxPredictSize", 1f, 0.1f, 5f) {
//        override fun onChanged(oldValue: Float, newValue: Float) {
//            val v = minPredictSize.get()
//            if (v > newValue) set(v)
//        }
//    }
//
//    private val minPredictSize: FloatValue = object : FloatValue("MinPredictSize", 1f, 0.1f, 5f) {
//        override fun onChanged(oldValue: Float, newValue: Float) {
//            val v = maxPredictSize.get()
//            if (v < newValue) set(v)
//        }
//    }
//
//    // Bypass
//    private val failRateValue = FloatValue("FailRate", 0f, 0f, 100f)
//    private val fakeSwingValue = BoolValue("FakeSwing", true)
//    private val noInventoryAttackValue = BoolValue("NoInvAttack", false)
//    private val noInventoryDelayValue = IntegerValue("NoInvDelay", 200, 0, 500)
//    private val limitedMultiTargetsValue = IntegerValue("LimitedMultiTargets", 0, 0, 50)
//
//    // Visuals
//    private val markValue = ListValue("Mark", arrayOf("Block","Jello","None"),"Block")
//    private val fakeSharpValue = BoolValue("FakeSharp", true)
//    private val circleValue = BoolValue("Circle",true)
//    private val circleRedValue = IntegerValue("CircleRed", 255, 0, 255)
//    private val circleGreenValue = IntegerValue("CircleGreen", 255, 0, 255)
//    private val circleBlueValue = IntegerValue("CircleBlue", 255, 0, 255)
//    private val circleAlphaValue = IntegerValue("CircleAlpha", 255, 0, 255)
//    private val circleThicknessValue = FloatValue("CircleThickness", 2F, 1F, 5F)
//
//    /**
//     * MODULE
//     */
//
//    // Target
//    var target: IEntityLivingBase? = null
//    private var currentTarget: IEntityLivingBase? = null
//    private var hitable = false
//    private val prevTargetEntities = mutableListOf<Int>()
//    private val inRangeDiscoveredTargets = mutableListOf<EntityLivingBase>()
//
//    // Attack delay
//    private val attackTimer = MSTimer()
//    private val switchTimer = MSTimer()
//    private val swingTimer = MSTimer()
//    private var attackDelay = 0L
//    private var Hurt = 0
//    private var swingDelay = 0L
//    private var clicks = 0
//
//    public var noSwingState = false
//
//    // Container Delay
//    private var containerOpen = -1L
//
//    // Fake block status
//    var blockingStatus = false
//
//
//    /**
//     * Enable kill aura module
//     */
//    override fun onEnable() {
//        mc.thePlayer ?: return
//        mc.theWorld ?: return
//
//        updateTarget()
//    }
//
//    /**
//     * Disable kill aura module
//     */
//    override fun onDisable() {
//        mc.gameSettings.keyBindUseItem.pressed = false
//        Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
//        target = null
//        currentTarget = null
//        hitable = false
//        prevTargetEntities.clear()
//        attackTimer.reset()
//        clicks = 0
//
//        stopBlocking()
//    }
//
//    private fun getAttackDelay(minCps: Int, maxCps: Int):Long{
//        var delay=TimeUtils.randomClickDelay(minCps.coerceAtMost(maxCps), minCps.coerceAtLeast(maxCps))
//        if(combatDelayValue.get()){
//            var value=4.0
//            if(mc.thePlayer?.inventory?.getCurrentItemInHand() !=null){
//                val currentItem= mc.thePlayer!!.inventory.getCurrentItemInHand()?.item
//                if(currentItem is ItemSword){
//                    value-=2.4
//                }else if(currentItem is ItemPickaxe){
//                    value-=2.8
//                }else if(currentItem is ItemAxe){
//                    value-=3
//                }
//            }
//            delay=delay.coerceAtLeast((1000 / value).toLong())
//        }
//        return delay
//    }
//
//    /**
//     * Motion event
//     */
//
//
//
//    @EventTarget
//    fun onMotion(event: MotionEvent) {
//        if (StopHurtWhenHurt.get()){
//            if (mc.thePlayer?.hurtTime!! > 0){
//                Hurt = 100
//                if (Hurt > 0) {
//                    mc.gameSettings.keyBindSprint.pressed = false
//                    mc.thePlayer?.sprinting = false
//                }
//            }
//            Hurt--
//        }
//        if (event.eventState == EventState.POST) {
//            target ?: return
//            currentTarget ?: return
//
//            // Update hitable
//            updateHitable()
//
//            // AutoBlock
//            if (autoBlockValue.get().equals("Old1.8.9", true))
//                mc.netHandler.addToSendQueue(
//                        classProvider.createCPacketPlayerBlockPlacement(
//                                WBlockPos(-1, -1, -1),
//                                255,
//                                null,
//                                0.0f,
//                                0.0f,
//                                0.0f
//                        )
//                )
//            if (autoBlockValue.get().equals("NCP", true))
//                mc.netHandler.addToSendQueue(
//                        classProvider.createCPacketPlayerBlockPlacement(
//                                WBlockPos(-1, -1, -1),
//                                255,
//                                null,
//                                0.0f,
//                                0.0f,
//                                0.0f
//                        )
//                )
//            if(autoBlockValue.get().equals("Hypixel",true))
//                mc.netHandler.addToSendQueue(
//                        classProvider.createCPacketPlayerBlockPlacement(
//                                WBlockPos(-1.25, -1.25, -1.25),
//                                255,
//                                null,
//                                0.0f,
//                                0.0f,
//                                0.0f
//                        )
//                )
//
//            if (autoBlockValue.get().equals("Hyt1.12.2", true))
//
//                if (autoBlockValue.get().equals("Right", true))
//                    Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
//
//            if (autoBlockValue.get().equals("AfterTick", true) && canBlock)
//                startBlocking(currentTarget!!, hitable)
//
//            return
//        }
//
//        if (mc.thePlayer?.isRiding == true)
//            return
//        if ((attackTimingValue.equals("Pre") && event.eventState == EventState.PRE) ||
//                (attackTimingValue.equals("Post") && event.eventState == EventState.POST) ||
//                attackTimingValue.equals("Both") || attackTimingValue.equals("All")) {
//            runAttackLoop()
//        }
//        if (event.eventState !== EventState.PRE) {
//            target ?: return
//            currentTarget ?: return
//
//            // Update hitable
//            updateHitable()
//            return
//        }
//        // AutoBlock
//
//        if (rotationStrafeValue.get().equals("Off", true))
//            update()
//
//        if (target != null && currentTarget != null) {
//            while (clicks > 0) {
//                runAttack()
//                clicks--
//            }
//        }
//    }
//
//    /**
//     * Check if player is able to block
//     */
//    private val canBlock: Boolean
//        inline get() = mc.thePlayer!!.heldItem != null && classProvider.isItemSword(mc.thePlayer!!.heldItem!!.item)
//
//
//    /**
//     * Strafe event
//     */
//    @EventTarget
//    fun onStrafe(event: StrafeEvent) {
//        if (rotationStrafeValue.get().equals("Off", true))
//            return
//
//        update()
//
//        if (currentTarget != null && RotationUtils.targetRotation != null) {
//            when (rotationStrafeValue.get().toLowerCase()) {
//                "strict" -> {
//                    val (yaw) = RotationUtils.targetRotation ?: return
//                    var strafe = event.strafe
//                    var forward = event.forward
//                    val friction = event.friction
//
//                    var f = strafe * strafe + forward * forward
//
//                    if (f >= 1.0E-4F) {
//                        f = sqrt(f)
//
//                        if (f < 1.0F)
//                            f = 1.0F
//
//                        f = friction / f
//                        strafe *= f
//                        forward *= f
//
//                        val yawSin = sin((yaw * Math.PI / 180F).toFloat())
//                        val yawCos = cos((yaw * Math.PI / 180F).toFloat())
//
//                        val player = mc.thePlayer!!
//
//                        player.motionX += strafe * yawCos - forward * yawSin
//                        player.motionZ += forward * yawCos + strafe * yawSin
//                    }
//                    event.cancelEvent()
//                }
//                "silent" -> {
//                    update()
//
//                    RotationUtils.targetRotation.applyStrafeToPlayer(event)
//                    event.cancelEvent()
//                }
//            }
//        }
//    }
//
//    fun update() {
//        if (cancelRun || (noInventoryAttackValue.get() && (classProvider.isGuiContainer(mc.currentScreen) ||
//                        System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())))
//            return
//
//        // Update target
//        updateTarget()
//
//        if (target == null) {
//            stopBlocking()
//            return
//        }
//
//        // Target
//        currentTarget = target
//
//        if (!targetModeValue.get().equals("Switch", ignoreCase = true) && isEnemy(currentTarget))
//            target = currentTarget
//    }
//
//    /**
//     * Update event
//     */
//    @EventTarget
//    fun onUpdate(event: UpdateEvent) {
//        if (StopHurtWhenAttack.get()){
//            if (mc.thePlayer?.hurtTime!! > 0){
//                Hurt = 100
//                if (Hurt > 0) {
//                    mc.gameSettings.keyBindSprint.pressed = false
//                    mc.thePlayer?.sprinting = false
//                }
//            }
//            Hurt--
//        }
//
//        if (cancelRun) {
//            target = null
//            currentTarget = null
//            hitable = false
//            stopBlocking()
//            return
//        }
//
//        if(noSwingValue.get()) {
//            noSwingState = true
//        }
//
//        if (noInventoryAttackValue.get() && (classProvider.isGuiContainer(mc.currentScreen) ||
//                        System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())) {
//            target = null
//            currentTarget = null
//            hitable = false
//            if (classProvider.isGuiContainer(mc.currentScreen)) containerOpen = System.currentTimeMillis()
//            return
//        }
//        if (mc.thePlayer?.isRiding == true) {
//            update()
//        }
//
//        if (target != null && currentTarget != null) {
//            while (clicks > 0) {
//                runAttack()
//                clicks--
//            }
//        }
//    }
//
//    /**
//     * Render event
//     */
//    @EventTarget
//    fun onRender3D(event: Render3DEvent) {
//        if (circleValue.get()) {
//            GL11.glPushMatrix()
//            GL11.glTranslated(
//                    mc.thePlayer!!.lastTickPosX + (mc.thePlayer!!.posX - mc.thePlayer!!.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
//                    mc.thePlayer!!.lastTickPosY + (mc.thePlayer!!.posY - mc.thePlayer!!.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY,
//                    mc.thePlayer!!.lastTickPosZ + (mc.thePlayer!!.posZ - mc.thePlayer!!.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
//            )
//            GL11.glEnable(GL11.GL_BLEND)
//            GL11.glEnable(GL11.GL_LINE_SMOOTH)
//            GL11.glDisable(GL11.GL_TEXTURE_2D)
//            GL11.glDisable(GL11.GL_DEPTH_TEST)
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
//
//            GL11.glLineWidth(circleThicknessValue.get())
//            GL11.glColor4f(circleRedValue.get().toFloat() / 255.0F, circleGreenValue.get().toFloat() / 255.0F, circleBlueValue.get().toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
//            GL11.glRotatef(90F, 1F, 0F, 0F)
//            GL11.glBegin(GL11.GL_LINE_STRIP)
//
//            for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
//                GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * discoverRangeValue.get(), (sin(i * Math.PI / 180.0).toFloat() * discoverRangeValue.get()))
//            }
//
//            GL11.glEnd()
//
//            GL11.glDisable(GL11.GL_BLEND)
//            GL11.glEnable(GL11.GL_TEXTURE_2D)
//            GL11.glEnable(GL11.GL_DEPTH_TEST)
//            GL11.glDisable(GL11.GL_LINE_SMOOTH)
//
//            GL11.glPopMatrix()
//        }
//
//        if (cancelRun) {
//            target = null
//            currentTarget = null
//            hitable = false
//            stopBlocking()
//            return
//        }
//
//        if (noInventoryAttackValue.get() && (classProvider.isGuiContainer(mc.currentScreen) ||
//                        System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())) {
//            target = null
//            currentTarget = null
//            hitable = false
//            if (classProvider.isGuiContainer(mc.currentScreen)) containerOpen = System.currentTimeMillis()
//            return
//        }
//
//        target ?: return
//        when (markValue.get().toLowerCase()) {
//            "block" -> {
//                val bb = target!!.entityBoundingBox
//                target!!.entityBoundingBox = bb.expand(0.2, 0.2, 0.2)
//                RenderUtils.drawEntityBox(target!!, if (target!!.hurtTime <= 0) Color.GREEN else Color.RED, true)
//                target!!.entityBoundingBox = bb
//            }
//
//            "jello" -> {
//                val drawTime = (System.currentTimeMillis() % 2000).toInt()
//                val drawMode = drawTime > 1000
//                var drawPercent = drawTime / 1000.0
//                //true when goes up
//                if (!drawMode) {
//                    drawPercent = 1 - drawPercent
//                } else {
//                    drawPercent -= 1
//                }
//                drawPercent = EaseUtils.easeInOutQuad(drawPercent)
//                val points = mutableListOf<WVec3>()
//                val bb = target!!.entityBoundingBox
//                val radius = bb.maxX - bb.minX
//                val height = bb.maxY - bb.minY
//                val posX = target!!.lastTickPosX + (target!!.posX - target!!.lastTickPosX) * mc.timer.renderPartialTicks
//                var posY = target!!.lastTickPosY + (target!!.posY - target!!.lastTickPosY) * mc.timer.renderPartialTicks
//                if (drawMode) {
//                    posY -= 0.5
//                } else {
//                    posY += 0.5
//                }
//                val posZ = target!!.lastTickPosZ + (target!!.posZ - target!!.lastTickPosZ) * mc.timer.renderPartialTicks
//                for (i in 0..360 step 7) {
//                    points.add(WVec3(posX - sin(i * Math.PI / 180F) * radius, posY + height * drawPercent, posZ + cos(i * Math.PI / 180F) * radius))
//                }
//                points.add(points[0])
//                //draw
//                mc.entityRenderer.disableLightmap()
//                GL11.glPushMatrix()
//                GL11.glDisable(GL11.GL_TEXTURE_2D)
//                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
//                GL11.glEnable(GL11.GL_LINE_SMOOTH)
//                GL11.glEnable(GL11.GL_BLEND)
//                GL11.glDisable(GL11.GL_DEPTH_TEST)
//                GL11.glBegin(GL11.GL_LINE_STRIP)
//                val baseMove = (if (drawPercent > 0.5) {
//                    1 - drawPercent
//                } else {
//                    drawPercent
//                }) * 2
//                val min = (height / 60) * 20 * (1 - baseMove) * (if (drawMode) {
//                    -1
//                } else {
//                    1
//                })
//                for (i in 0..20) {
//                    var moveFace = (height / 60F) * i * baseMove
//                    if (drawMode) {
//                        moveFace = -moveFace
//                    }
//                    val firstPoint = points[0]
//                    GL11.glVertex3d(
//                            firstPoint.xCoord - mc.renderManager.viewerPosX, firstPoint.yCoord - moveFace - min - mc.renderManager.viewerPosY,
//                            firstPoint.zCoord - mc.renderManager.viewerPosZ
//                    )
//                    GL11.glColor4f(1F, 1F, 1F, 0.7F * (i / 20F))
//                    for (vec3 in points) {
//                        GL11.glVertex3d(
//                                vec3.xCoord - mc.renderManager.viewerPosX, vec3.yCoord - moveFace - min - mc.renderManager.viewerPosY,
//                                vec3.zCoord - mc.renderManager.viewerPosZ
//                        )
//                    }
//                    GL11.glColor4f(0F, 0F, 0F, 0F)
//                }
//                GL11.glEnd()
//                GL11.glEnable(GL11.GL_DEPTH_TEST)
//                GL11.glDisable(GL11.GL_LINE_SMOOTH)
//                GL11.glDisable(GL11.GL_BLEND)
//                GL11.glEnable(GL11.GL_TEXTURE_2D)
//                GL11.glPopMatrix()
//            }
//        }
//
//        if (cancelRun) {
//            target = null
//            currentTarget = null
//            hitable = false
//            stopBlocking()
//            inRangeDiscoveredTargets.clear()
//        }
//
//        if (currentTarget != null && attackTimer.hasTimePassed(attackDelay) &&
//                currentTarget!!.hurtTime <= hurtTimeValue.get()) {
//            clicks++
//            attackTimer.reset()
//            attackDelay = TimeUtils.randomClickDelay(minCPS.get(), maxCPS.get())
//        }
//    }
//
//    /**
//     * Handle entity move
//     */
//    @EventTarget
//    fun onEntityMove(event: EntityMovementEvent) {
//        val movedEntity = event.movedEntity
//
//        if (target == null || movedEntity != currentTarget)
//            return
//
//        updateHitable()
//    }
//
//    /**
//     * Attack enemy
//     */
//    private fun runAttack() {
//        target ?: return
//        currentTarget ?: return
//        val thePlayer = mc.thePlayer ?: return
//        val theWorld = mc.theWorld ?: return
//
//        // Settings
//        val failRate = failRateValue.get()
//        val swing = swingValue.get()
//        val multi = targetModeValue.get().equals("Multi", ignoreCase = true)
//        val openInventory = aacValue.get() && classProvider.isGuiContainer(mc.currentScreen)
//        val failHit = failRate > 0 && Random().nextInt(100) <= failRate
//
//        // Close inventory when open
//        if (openInventory)
//            mc.netHandler.addToSendQueue(classProvider.createCPacketCloseWindow())
//
//        // Check is not hitable or check failrate
//
//        if (!hitable || failHit) {
//            if (swing && (fakeSwingValue.get() || failHit))
//                thePlayer.swingItem()
//        } else {
//            // Attack
//            if (!multi) {
//                attackEntity(currentTarget!!)
//            } else {
//                var targets = 0
//
//                for (entity in theWorld.loadedEntityList) {
//                    val distance = thePlayer.getDistanceToEntityBox(entity)
//
//                    if (classProvider.isEntityLivingBase(entity) && isEnemy(entity) && distance <= discoverRangeValue.get()) {
//                        attackEntity(entity.asEntityLivingBase())
//
//                        targets += 1
//
//                        if (limitedMultiTargetsValue.get() != 0 && limitedMultiTargetsValue.get() <= targets)
//                            break
//                    }
//                }
//            }
//            if(switchTimer.hasTimePassed(switchDelayValue.get().toLong()) || targetModeValue.get() != "Switch") {
//                prevTargetEntities.add(if (aacValue.get()) target!!.entityId else currentTarget!!.entityId)
//                switchTimer.reset()
//            }
//
//            prevTargetEntities.add(if (aacValue.get()) target!!.entityId else currentTarget!!.entityId)
//
//            if (target == currentTarget)
//                target = null
//        }
//
//        // Open inventory
//        if (openInventory)
//            mc.netHandler.addToSendQueue(createOpenInventoryPacket())
//    }
//
//    /**
//     * Update current target
//     */
//    private fun updateTarget() {
//        // Reset fixed target to null
//        target = null
//
//        // Settings
//        val hurtTime = hurtTimeValue.get()
//        val fov = fovValue.get()
//        val switchMode = targetModeValue.get().equals("Switch", ignoreCase = true)
//
//        // Find possible targets
//        val targets = mutableListOf<IEntityLivingBase>()
//
//        val theWorld = mc.theWorld!!
//        val thePlayer = mc.thePlayer!!
//
//        for (entity in theWorld.loadedEntityList) {
//            if (!classProvider.isEntityLivingBase(entity) || !isEnemy(entity) || (switchMode && prevTargetEntities.contains(entity.entityId)))
//                continue
//
//            val distance = thePlayer.getDistanceToEntityBox(entity)
//            val entityFov = RotationUtils.getRotationDifference(entity)
//
//            if (distance <= discoverRangeValue.get() && (fov == 180F || entityFov <= fov) && entity.hurtResistantTime <= hurtTime)
//                updateRotations(entity)
//            targets.add(entity.asEntityLivingBase())
//
//            if(distance >= discoverRangeValue.get()){
//                if (mc.gameSettings.keyBindUseItem.pressed) {
//                    Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
//                }
//            }
//        }
//
//        // Sort targets by priority
//        when (priorityValue.get().toLowerCase()) {
//            "distance" -> targets.sortBy { thePlayer.getDistanceToEntityBox(it) } // Sort by distance
//            "health" -> targets.sortBy { it.health } // Sort by health
//            "direction" -> targets.sortBy { RotationUtils.getRotationDifference(it) } // Sort by FOV
//            "livingtime" -> targets.sortBy { -it.ticksExisted } // Sort by existence
//        }
//
//        inRangeDiscoveredTargets.clear()
//
//        // Cleanup last targets when no targets found and try again
//        if (inRangeDiscoveredTargets.isEmpty()&&prevTargetEntities.isNotEmpty()) {
//            prevTargetEntities.clear()
//            updateTarget()
//            return
//        }
//
//        // Find best target
//        for (entity in targets) {
//            // Update rotations to current target
//            if (!updateRotations(entity)) // when failed then try another target
//                continue
//
//            // Set target to current entity
//            if(mc.thePlayer!!.getDistanceToEntityBox(entity) < maxRange) {
//                target = entity
//                return
//            }
//        }
//    }
//
//    /**
//     * Check if [entity] is selected as enemy with current target options and other modules
//     */
//    private fun isEnemy(entity: IEntity?): Boolean {
//        if (classProvider.isEntityLivingBase(entity) && entity != null && (EntityUtils.targetDead || isAlive(entity.asEntityLivingBase())) && entity != mc.thePlayer) {
//            if (!EntityUtils.targetInvisible && entity.invisible)
//                return false
//
//            if (EntityUtils.targetPlayer && classProvider.isEntityPlayer(entity)) {
//                if (entity.asEntityPlayer().spectator || AntiBot.isBot(entity.asEntityLivingBase()))
//                    return false
//
//                if (EntityUtils.isFriend(entity) && !LiquidBounce.moduleManager[NoFriends::class.java].state)
//                    return false
//
//                val teams = LiquidBounce.moduleManager[Teams::class.java] as Teams
//
//                return !teams.state || !teams.isInYourTeam(entity.asEntityLivingBase())
//            }
//
//            return EntityUtils.targetMobs && EntityUtils.isMob(entity) || EntityUtils.targetAnimals &&
//                    EntityUtils.isAnimal(entity)
//        }
//
//        return false
//    }
//
//    /**
//     * Attack [entity]
//     */
//    private fun attackEntity(entity: IEntityLivingBase) {
//        // Stop blocking
//        val thePlayer = mc.thePlayer!!
//        if (thePlayer.isBlocking || blockingStatus)
//            stopBlocking()
//
//        if (thePlayer.isBlocking || blockingStatus) {
//            mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
//                    WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)))
//            blockingStatus = false
//        }
//
//
//        // Call attack event
//        LiquidBounce.eventManager.callEvent(AttackEvent(entity))
//        // Attack target
//        if (swingValue.get() && Backend.MINECRAFT_VERSION_MINOR == 8)
//            thePlayer.swingItem()
//
//        mc.netHandler.addToSendQueue(classProvider.createCPacketUseEntity(entity, ICPacketUseEntity.WAction.ATTACK))
//
//        if (swingValue.get() && Backend.MINECRAFT_VERSION_MINOR != 8)
//            thePlayer.swingItem()
//
//        if (keepSprintValue.get()) {
//            // Critical Effect
//            if (thePlayer.fallDistance > 0F && !thePlayer.onGround && !thePlayer.isOnLadder &&
//                    !thePlayer.isInWater && !thePlayer.isPotionActive(classProvider.getPotionEnum(PotionType.BLINDNESS)) && !thePlayer.isRiding)
//                thePlayer.onCriticalHit(entity)
//
//            // Enchant Effect
//            if (functions.getModifierForCreature(thePlayer.heldItem, entity.creatureAttribute) > 0F)
//                thePlayer.onEnchantmentCritical(entity)
//        } else {
//            if (mc.playerController.currentGameType != IWorldSettings.WGameType.SPECTATOR)
//                thePlayer.attackTargetEntityWithCurrentItem(entity)
//        }
//        // Start blocking after attack
//        if (autoBlockValue.get().equals("Packet", true) && (thePlayer.isBlocking || canBlock)) {
//            startBlocking(entity, interactAutoBlockValue.get())
//            if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
//                return
//
//            if (delayedBlockValue.get())
//                return
//
//            startBlocking(entity, interactAutoBlockValue.get())
//        }
//        if (mc.thePlayer!!.isBlocking || (!autoBlockValue.get().equals("off", true) && canBlock)) {
//            if (autoBlockValue.get().equals("AfterTick", true))
//                return
//
//            if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
//                return
//
//            startBlocking(entity, interactAutoBlockValue.get())
//
//            if (autoBlockValue.get().equals("MoonX", true) || canBlock) {
//                mc.gameSettings.keyBindUseItem.pressed = true
//                mc.gameSettings.keyBindUseItem.pressed = false
//            }
//            if (autoBlockValue.get().equals("Right", true) || canBlock) {
//                Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
//                Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
//            }
//            if (autoBlockValue.get().equals("Hyt1.12.2", true) || canBlock) {
//                mc.gameSettings.keyBindUseItem.pressed = true
//                mc.gameSettings.keyBindUseItem.pressed = false
//            }
//
//
//        }
//
//        // Extra critical effects
//        val criticals = LiquidBounce.moduleManager[Criticals::class.java] as Criticals
//
//        for (i in 0..2) {
//            // Critical Effect
//            if (thePlayer.fallDistance > 0F && !thePlayer.onGround && !thePlayer.isOnLadder && !thePlayer.isInWater && !thePlayer.isPotionActive(classProvider.getPotionEnum(PotionType.BLINDNESS)) && thePlayer.ridingEntity == null || criticals.state && criticals.msTimer.hasTimePassed(criticals.delayValue.get().toLong()) && !thePlayer.isInWater && !thePlayer.isInLava && !thePlayer.isInWeb)
//                thePlayer.onCriticalHit(target!!)
//
//            // Enchant Effect
//            if (functions.getModifierForCreature(thePlayer.heldItem, target!!.creatureAttribute) > 0.0f || fakeSharpValue.get())
//                thePlayer.onEnchantmentCritical(target!!)
//        }
//    }
//
//    /**
//     * Update killaura rotations to enemy
//     */
//    private fun updateRotations(entity: IEntity): Boolean {
//        var boundingBox = entity.entityBoundingBox
//        if (rotations.get().equals("Vanilla", ignoreCase = true)){
//            if (maxTurnSpeed.get() <= 0F)
//                return true
//
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//
//            val (vec, rotation) = RotationUtils.searchCenter(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation,
//                    (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get())
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
//            else
//                limitedRotation.toPlayer(mc.thePlayer!!)
//
//            return true
//        }/*
//        if (rotations.get().equals("BackTrack", ignoreCase = true)) {
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//            val (_, rotation) = RotationUtils.searchCenter(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//            //debug
//            // ClientUtils.displayChatMessage((mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get()).toString())
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
//                rotation,
//                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get()) {
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
//            }else {
//                limitedRotation.toPlayer(mc.thePlayer!!)
//                return true
//            }
//        }*/
//
//        if (rotations.get().equals("BackTrack", ignoreCase = true)) {
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
//                    RotationUtils.OtherRotation(boundingBox,RotationUtils.getCenter(entity.entityBoundingBox), predictValue.get(),
//                            mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),maxRange), (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get()) {
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
//            }else {
//                limitedRotation.toPlayer(mc.thePlayer!!)
//                return true
//            }
//        }
//        if (rotations.get().equals("Test", ignoreCase = true)) {
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//            val (_, rotation) = RotationUtils.lockView(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//            //debug
//            // ClientUtils.displayChatMessage((mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get()).toString())
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
//                    rotation,
//                    (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get()) {
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
//            }else {
//                limitedRotation.toPlayer(mc.thePlayer!!)
//                return true
//            }
//        }
//        if (rotations.get().equals("114Sense", ignoreCase = true)) {
//            if (maxTurnSpeed.get() <= 0F)
//                return true
//
//            var boundingBox = entity.entityBoundingBox
//
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX - (mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY - (mc.thePlayer!!.posY - mc.thePlayer!!.prevPosY)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ - (mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//
//            val (_, rotation) = RotationUtils.searchCenter(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox),false),(Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get())
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 10 else 0)
//            else
//                limitedRotation.toPlayer(mc.thePlayer!!)
//
//            return true
//        }
//        if (rotations.get().equals("HytRotation", ignoreCase = true)) {
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//            val (_, rotation) = RotationUtils.lockView(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
//                    rotation,
//                    (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//            if (silentRotationValue.get())
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
//            else
//                limitedRotation.toPlayer(mc.thePlayer!!)
//            return true
//        }
//        if (rotations.get().equals("TIGSMA", ignoreCase = true)) {
//            if (maxTurnSpeed.get() <= 0F)
//                return true
//
//            var boundingBox = entity.entityBoundingBox
//
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX - (mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY - (mc.thePlayer!!.posY - mc.thePlayer!!.prevPosY)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ - (mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//
//            val (_, rotation) = RotationUtils.searchCenter(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,RotationUtils.getNCPRotations(RotationUtils.getCenter(entity.entityBoundingBox),false), (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get())
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 10 else 0)
//            else
//                limitedRotation.toPlayer(mc.thePlayer!!)
//
//            return true
//        }
//        if (rotations.get().equals("Xinst", ignoreCase = true)) {
//            if (maxTurnSpeed.get() <= 0F)
//                return true
//
//            var boundingBox = entity.entityBoundingBox
//
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX - (mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY - (mc.thePlayer!!.posY - mc.thePlayer!!.prevPosY)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ - (mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//
//            val (_, rotation) = RotationUtils.searchCenter(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,RotationUtils.getNCPRotations(RotationUtils.getCenter(entity.entityBoundingBox),true), (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get())
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 10 else 0)
//            else
//                limitedRotation.toPlayer(mc.thePlayer!!)
//
//            return true
//        }
//        if (rotations.get().equals("LiquidSensePlus", ignoreCase = true)) {
//            if (maxTurnSpeed.get() <= 0F)
//                return true
//
//            var boundingBox = entity.entityBoundingBox
//
//            if (predictValue.get())
//                boundingBox = boundingBox.offset(
//                        (entity.posX - entity.prevPosX - (mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posY - entity.prevPosY - (mc.thePlayer!!.posY - mc.thePlayer!!.prevPosY)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
//                        (entity.posZ - entity.prevPosZ - (mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
//                )
//
//            val (_, rotation) = RotationUtils.searchCenter(
//                    boundingBox,
//                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
//                    randomCenterValue.get(),
//                    predictValue.get(),
//                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
//                    maxRange
//            ) ?: return false
//
//            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation,
//                    (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
//
//            if (silentRotationValue.get())
//                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
//            else
//                limitedRotation.toPlayer(mc.thePlayer!!)
//
//            return true
//        }
//        return true
//    }
//
//    /**
//     * Check if enemy is hitable with current rotations
//     */
//
//    private fun updateHitable() {
//        // Disable hitable check if turn speed is zero
//        if (maxTurnSpeed.get() <= 0F) {
//            hitable = true
//            return
//        }
//
//        val reach = min(maxRange.toDouble(), mc.thePlayer!!.getDistanceToEntityBox(target!!)) + 1
//
//        if (raycastValue.get()) {
//            val raycastedEntity = RaycastUtils.raycastEntity(reach, object : RaycastUtils.EntityFilter {
//                override fun canRaycast(entity: IEntity?): Boolean {
//                    return (!livingRaycastValue.get() || (classProvider.isEntityLivingBase(entity) && !classProvider.isEntityArmorStand(entity))) &&
//                            (isEnemy(entity) || raycastIgnoredValue.get() || aacValue.get() && mc.theWorld!!.getEntitiesWithinAABBExcludingEntity(entity, entity!!.entityBoundingBox).isNotEmpty())
//                }
//
//            })
//
//
//            if (raycastValue.get() && raycastedEntity != null && classProvider.isEntityLivingBase(raycastedEntity)
//                    && (LiquidBounce.moduleManager[NoFriends::class.java].state || !EntityUtils.isFriend(raycastedEntity)))
//                currentTarget = raycastedEntity.asEntityLivingBase()
//
//            hitable = if (maxTurnSpeed.get() > 0F) currentTarget == raycastedEntity else true
//        } else
//            hitable = RotationUtils.isFaced(currentTarget, reach)
//    }
//
//    private fun runAttackLoop() {
//        if (clicks <= 0 && swingTimer.hasTimePassed(swingDelay)) {
//            swingTimer.reset()
//            swingDelay = getAttackDelay(minCPS.get(), maxCPS.get())
//            runSwing()
//            return
//        }
//    }
//    private fun runSwing() {
//        mc.thePlayer?.swingItem()
//    }
//
//    /**
//     * Start blocking
//     */
//    private fun startBlocking(interactEntity: IEntity, interact: Boolean) {
//        if (interact) {
//            val positionEye = mc.renderViewEntity?.getPositionEyes(1F)
//
//            val expandSize = interactEntity.collisionBorderSize.toDouble()
//            val boundingBox = interactEntity.entityBoundingBox.expand(expandSize, expandSize, expandSize)
//
//            val (yaw, pitch) = RotationUtils.targetRotation ?: Rotation(mc.thePlayer!!.rotationYaw, mc.thePlayer!!.rotationPitch)
//            val yawCos = cos(-yaw * 0.017453292F - Math.PI.toFloat())
//            val yawSin = sin(-yaw * 0.017453292F - Math.PI.toFloat())
//            val pitchCos = -cos(-pitch * 0.017453292F)
//            val pitchSin = sin(-pitch * 0.017453292F)
//            val range = min(maxRange.toDouble(), mc.thePlayer!!.getDistanceToEntityBox(interactEntity)) + 1
//            val lookAt = positionEye!!.addVector(yawSin * pitchCos * range, pitchSin * range, yawCos * pitchCos * range)
//
//            val movingObject = boundingBox.calculateIntercept(positionEye, lookAt) ?: return
//            val hitVec = movingObject.hitVec
//
//            mc.netHandler.addToSendQueue(classProvider.createCPacketUseEntity(interactEntity, WVec3(
//                    hitVec.xCoord - interactEntity.posX,
//                    hitVec.yCoord - interactEntity.posY,
//                    hitVec.zCoord - interactEntity.posZ)
//            ))
//            mc.netHandler.addToSendQueue(classProvider.createCPacketUseEntity(interactEntity, ICPacketUseEntity.WAction.INTERACT))
//        }
//
//        mc.netHandler.addToSendQueue(createUseItemPacket(mc.thePlayer!!.inventory.getCurrentItemInHand(), WEnumHand.MAIN_HAND))
//        blockingStatus = false
//    }
//
//
//    /**
//     * Stop blocking
//     */
//    private fun stopBlocking() {
//        if (blockingStatus) {
//            mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)))
//            blockingStatus = false
//        }
//    }
//
//    /**
//     * Check if run should be cancelled
//     */
//    private val cancelRun: Boolean
//        inline get() = mc.thePlayer!!.spectator || !isAlive(mc.thePlayer!!)
//                || (blinkCheck.get() && LiquidBounce.moduleManager[Blink::class.java]!!.state) || LiquidBounce.moduleManager[FreeCam::class.java].state ||
//                (noScaffValue.get() && LiquidBounce.moduleManager[Scaffold::class.java]!!.state)
//
//    /**
//     * Check if [entity] is alive
//     */
//    private fun isAlive(entity: IEntityLivingBase) = entity.entityAlive && entity.health > 0 ||
//            aacValue.get() && entity.hurtTime > 5
//
//
//
//    /**
//     * Range
//     */
//    private val maxRange: Float
//        get() = max(rangeValue.get(), throughWallsRangeValue.get())
//
//    private fun getRange(entity: IEntity) =
//            (if (mc.thePlayer!!.getDistanceToEntityBox(entity) >= throughWallsRangeValue.get()) rangeValue.get() else throughWallsRangeValue.get()) - if (mc.thePlayer!!.sprinting) rangeSprintReducementValue.get() else 0F
//    //cancelRun
//    /**
//     * HUD Tag
//     */
//    override val tag: String?
//        get() = targetModeValue.get()
//}