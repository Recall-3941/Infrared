/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.potion.Potion
import oh.yalan.NativeClass
import oh.yalan.NativeMethod
import kotlin.math.cos
import kotlin.math.sin
@NativeClass
@ModuleInfo(name = "Velocity", description = "使你改变自己受到的击退", Chinese="减少击退",category = ModuleCategory.COMBAT)
class Velocity : Module() {


    /**
     * OPTIONS
     */
    private val horizontalValue = FloatValue("Horizontal", 0F, 0F, 1F)
    private val verticalValue = FloatValue("Vertical", 0F, 0F, 1F)
    private val modeValue = ListValue("Mode", arrayOf("Motion","Motion2","HYT0204-Bug","HYTNew","HYTRandom","HytTick","Ground","GroundReduce","NoXYZ","Only-Y","Custom","Jump","JumpFix","AAC4","NewAAC4","AAC5Reduce","Packet","PacketFix",
            "Simple","SimpleFix","Vanilla"), "Vanilla")
    private val aac4XZReducerValue = FloatValue("AAC4XZReducer", 1.36F, 1F, 3F)
    private val newaac4XZReducerValue = FloatValue("NewAAC4XZReducer", 0.45F, 0F, 1F)

    private val velocityTickValue = IntegerValue("VelocityTick", 1, 0, 10)
    // Reverse
    private val reverseStrengthValue = FloatValue("ReverseStrength", 1F, 0.1F, 1F)
    private val reverse2StrengthValue = FloatValue("SmoothReverseStrength", 0.05F, 0.02F, 0.1F)

    private val hytpacketaset = FloatValue("HytPacketASet", 0.35F, 0.1F, 1F)
    private val hytpacketbset = FloatValue("HytPacketBSet", 0.5F, 1F, 1F)
    // AAC Push
    private val aacPushXZReducerValue = FloatValue("AACPushXZReducer", 2F, 1F, 3F)
    private val aacPushYReducerValue = BoolValue("AACPushYReducer", true)
    public var block: IBlock? = null

    private val noFireValue = BoolValue("noFire", false)
    private val cobwebValue = BoolValue("NoCobweb", true)

    private val hytGround = BoolValue("HytOnlyGround", true)
    private val health = FloatValue("health", 10F,0F,20F)

    //Custom
    private val customX = FloatValue("CustomX",0F,0F,1F)
    private val customYStart = BoolValue("CanCustomY",false)
    private val customY = FloatValue("CustomY",1F,1F,2F)
    private val customZ = FloatValue("CustomZ",0F,0F,1F)
    private val customC06FakeLag = BoolValue("CustomC06FakeLag",false)


    /**
     * VALUES
     */

    private var huayutingjumpflag = false
    private var velocityTimer = MSTimer()
    private var velocityInput = false
    private var canCleanJump = false
    private var velocityTick = 0
    // SmoothReverse
    private var reverseHurt = false

    // AACPush
    private var jump = false
    private var canCancelJump = false
    override val tag: String
        get() = modeValue.get()

    override fun onDisable() {
        mc.thePlayer?.speedInAir = 0.02F
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.isInWater || thePlayer.isInLava || thePlayer.isInWeb)
            return

        if (noFireValue.get() && mc.thePlayer!!.burning) return
        when (modeValue.get().toLowerCase()) {
            "ground"->{
                if (thePlayer.hurtTime > 0 && !thePlayer.onGround){
                    thePlayer.motionX /= 1
                    thePlayer.motionZ /=1
                }
            }
            "hytnew" -> {
                if(mc.thePlayer!!.hurtTime > 0 && velocityInput) {
                    if(mc.thePlayer!!.onGround) {
                        mc.thePlayer!!.motionX *= (mc.thePlayer!!.motionX * (0.56 * Math.random()))
                        mc.thePlayer!!.motionY *= (mc.thePlayer!!.motionX * (0.77 * Math.random()))
                        mc.thePlayer!!.motionZ *= (mc.thePlayer!!.motionX * (0.56 * Math.random()))
                        mc.thePlayer!!.onGround = false
                    } else {
                        mc.thePlayer!!.motionX *= (mc.thePlayer!!.motionX * (0.77 * Math.random()))
                        mc.thePlayer!!.onGround = true
                        mc.thePlayer!!.motionZ *= (mc.thePlayer!!.motionZ * (0.77 * Math.random()))
                    }
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SNEAKING))
                    velocityInput = false
                }
            }
            "hyt0204-bug" -> {
                if(mc.thePlayer!!.hurtTime > 9 && velocityInput) {
                    if(mc.thePlayer!!.onGround) {
                        mc.thePlayer!!.motionX *= (mc.thePlayer!!.motionX * (0.55 * Math.random()))
                        mc.thePlayer!!.motionY *= (mc.thePlayer!!.motionX * (0.64 * Math.random()))
                        mc.thePlayer!!.motionZ *= (mc.thePlayer!!.motionX * (0.55 * Math.random()))
                        mc.thePlayer!!.onGround = false
                    }
                    else {
                        mc.thePlayer!!.motionX *= (mc.thePlayer!!.motionX * (0.77 * Math.random()))
                        mc.thePlayer!!.onGround = true
                        mc.thePlayer!!.motionZ *= (mc.thePlayer!!.motionZ * (0.77 * Math.random()))
                    }
                    velocityInput = false
                }
                if (mc.thePlayer!!.hurtTime <= 4){
                    mc.thePlayer!!.motionX *= 0.700151164
                    mc.thePlayer!!.motionZ *= 0.700151164
                }
                if(mc.thePlayer!!.hurtTime in 5..9) {
                    mc.thePlayer!!.motionX *= 0.6001421204
                    mc.thePlayer!!.motionZ *= 0.6001421204
                }
            }
            "jumpfix" -> {
                if(mc.thePlayer!!.hurtTime > 0 && huayutingjumpflag) {
                    if(mc.thePlayer!!.onGround){
                        if (mc.thePlayer!!.hurtTime <= 6) {
                            mc.thePlayer!!.motionX *= 0.600151164
                            mc.thePlayer!!.motionZ *= 0.600151164
                        }
                        if (mc.thePlayer!!.hurtTime <= 4) {
                            mc.thePlayer!!.motionX *= 0.700151164
                            mc.thePlayer!!.motionZ *= 0.700151164
                        }
                    }else if(mc.thePlayer!!.hurtTime <= 9) {
                        mc.thePlayer!!.motionX *= 0.6001421204
                        mc.thePlayer!!.motionZ *= 0.6001421204
                    }
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SNEAKING))
                    huayutingjumpflag = false
                }
            }
            "jump" -> if (thePlayer.hurtTime > 0 && thePlayer.onGround) {
                thePlayer.motionY = 0.42

                val yaw = thePlayer.rotationYaw * 0.017453292F

                thePlayer.motionX -= sin(yaw) * 0.2
                thePlayer.motionZ += cos(yaw) * 0.2
            }

            "glitch" -> {
                thePlayer.noClip = velocityInput

                if (thePlayer.hurtTime == 7)
                    thePlayer.motionY = 0.4

                velocityInput = false
            }
            "feile"->{
                if(thePlayer.onGround){
                    canCleanJump = true
                    thePlayer.motionY = 1.5
                    thePlayer.motionZ = 1.2
                    thePlayer.motionX = 1.5
                    if(thePlayer.onGround && velocityTick > 2) {
                        velocityInput = false
                    }
                }
            }
            "aac5reduce" -> {
                if (thePlayer.hurtTime > 1 && velocityInput) {
                    thePlayer.motionX *= 0.81
                    thePlayer.motionZ *= 0.81
                }
                if (velocityInput && (thePlayer.hurtTime <5 || thePlayer.onGround) && velocityTimer.hasTimePassed(120L)) {
                    velocityInput = false
                }
            }

            "hyttick" -> {
                if(velocityTick > velocityTickValue.get()) {
                    if(thePlayer.motionY > 0) thePlayer.motionY = 0.0
                    thePlayer.motionX = 0.0
                    thePlayer.motionZ = 0.0
                    thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }
            "reverse" -> {
                if (!velocityInput)
                    return

                if (!thePlayer.onGround) {
                    MovementUtils.strafe(MovementUtils.speed * reverseStrengthValue.get())
                } else if (velocityTimer.hasTimePassed(80L))
                    velocityInput = false
            }

            "aac4" -> {
                if (!thePlayer.onGround) {
                    if (velocityInput) {
                        thePlayer.speedInAir = 0.02f
                        thePlayer.motionX *= 0.6
                        thePlayer.motionZ *= 0.6
                    }
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                    thePlayer.speedInAir = 0.02f
                }
            }
            "newaac4"->{
                if (thePlayer.hurtTime > 0 && !thePlayer.onGround){
                    val reduce = newaac4XZReducerValue.get()
                    thePlayer.motionX *= reduce
                    thePlayer.motionZ *= reduce
                }

            }

            "smoothreverse" -> {
                if (!velocityInput) {
                    thePlayer.speedInAir = 0.02F
                    return
                }

                if (thePlayer.hurtTime > 0)
                    reverseHurt = true

                if (!thePlayer.onGround) {
                    if (reverseHurt)
                        thePlayer.speedInAir = reverse2StrengthValue.get()
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                    reverseHurt = false
                }
            }
            "hyt" -> {
                if (thePlayer.hurtTime > 0 && !thePlayer.onGround) {
                    thePlayer.motionX *= RenderUtils.drawJelloShadow()
                    thePlayer.motionZ *= RenderUtils.drawImage4()
                }
            }

            "packet" ->{
                if(hytGround.get()){
                    if (thePlayer.hurtTime > 0 && !thePlayer.isDead && thePlayer.hurtTime <=5 && thePlayer.onGround) {
                        thePlayer.motionX *= 0.5
                        thePlayer.motionZ *= 0.5
                        thePlayer.motionY /= 1.781145F
                    }
                }else{
                    if (thePlayer.hurtTime > 0 && !thePlayer.isDead && thePlayer.hurtTime <=5 ) {
                        thePlayer.motionX *= 0.5
                        thePlayer.motionZ *= 0.5
                        thePlayer.motionY /= 1.781145F
                    }
                }

            }

            "motion" ->{
                if (thePlayer.hurtTime > 0 && !thePlayer.isDead && thePlayer.hurtTime <=5 && mc.thePlayer!!.onGround) {
                    thePlayer.motionX *= 0.4
                    thePlayer.motionZ *= 0.4
                    thePlayer.motionY *= 0.381145F
                    thePlayer.motionY /= 1.781145F
                }
            }
            "motion2" ->{
                if (thePlayer.hurtTime > 0 && !thePlayer.isDead && !thePlayer.onGround) {
                    if (!thePlayer.isPotionActive(classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                        thePlayer.motionX *= 0.451145F
                        thePlayer.motionZ *= 0.451145F
                    }
                }
            }
            "only-y"->{
                if(hytGround.get()){
                    if (thePlayer.hurtTime > 0 && !thePlayer.isDead && thePlayer.hurtTime <=5 && thePlayer.onGround) {
                        thePlayer.motionX *= 0.4
                        thePlayer.motionZ *= 0.4
                        thePlayer.motionY *= 0.01
                        thePlayer.motionY /= 1.4F
                    }
                }else{
                    if (thePlayer.hurtTime > 0 && !thePlayer.isDead && thePlayer.hurtTime <=5 ) {
                        thePlayer.motionX *= 0.4
                        thePlayer.motionZ *= 0.4
                        thePlayer.motionY *= 0.04
                        thePlayer.motionY /= 1.4F
                    }
                }
            }

            "custom" -> {
                if (thePlayer.hurtTime > 0 && !thePlayer.isDead && !mc.thePlayer!!.isPotionActive(classProvider.getPotionEnum(PotionType.MOVE_SPEED)) && !mc.thePlayer!!.isInWater) {
                    thePlayer.motionX *= customX.get()
                    thePlayer.motionZ *= customZ.get()
                    if(customYStart.get())thePlayer.motionY /= customY.get()
                    if (customC06FakeLag.get()) mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosLook(thePlayer.posX, thePlayer.posY, thePlayer.posZ, thePlayer.rotationYaw, thePlayer.rotationPitch, thePlayer.onGround))
                }
            }

            "aaczero" -> if (thePlayer.hurtTime > 0) {
                if (!velocityInput || thePlayer.onGround || thePlayer.fallDistance > 2F)
                    return

                thePlayer.motionY -= 1.0
                thePlayer.isAirBorne = true
                thePlayer.onGround = true
            } else
                velocityInput = false
        }


    }
    @EventTarget
    fun onBlockBB(event: BlockBBEvent) {
        block = event.block
    }
    @NativeMethod
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val thePlayer = mc.thePlayer ?: return

        val packet = event.packet

        if (classProvider.isSPacketEntityVelocity(packet)) {
            val packetEntityVelocity = packet.asSPacketEntityVelocity()


            if (noFireValue.get() && mc.thePlayer!!.burning) return
            if ((mc.theWorld?.getEntityByID(packetEntityVelocity.entityID) ?: return) != thePlayer)
                return

            velocityTimer.reset()

            when (modeValue.get().toLowerCase()) {
                "jumpfix" -> {
                    if(packet.unwrap() is SPacketEntityVelocity) {
                        huayutingjumpflag = true

                        if(mc.thePlayer!!.hurtTime != 0) {
                            event.cancelEvent()
                            packet.asSPacketEntityVelocity().motionX = 0
                            packet.asSPacketEntityVelocity().motionY = 0
                            packet.asSPacketEntityVelocity().motionZ = 0
                        }
                    }
                }
                "vanilla" -> {
                    event.cancelEvent()
                }
                "simple" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == 0F && vertical == 0F)
                        event.cancelEvent()

                    packetEntityVelocity.motionX = (packetEntityVelocity.motionX * horizontal).toInt()
                    packetEntityVelocity.motionY = (packetEntityVelocity.motionY * vertical).toInt()
                    packetEntityVelocity.motionZ = (packetEntityVelocity.motionZ * horizontal).toInt()
                }
                "packetfix"->{
                    if (thePlayer.hurtTime > 0 && !thePlayer.isDead && !mc.thePlayer!!.isPotionActive(classProvider.getPotionEnum(PotionType.MOVE_SPEED)) && !mc.thePlayer!!.isInWater) {
                        thePlayer.motionX *= 0.4
                        thePlayer.motionZ *= 0.4
                        thePlayer.motionY /= 1.45F
                    }
                    if (thePlayer.hurtTime < 1) {
                        packetEntityVelocity.motionY = 0
                    }
                    if (thePlayer.hurtTime < 5) {
                        packetEntityVelocity.motionX = 0
                        packetEntityVelocity.motionZ = 0
                    }
                }
                "noxyz" -> {
                    if (thePlayer.onGround) {
                        canCancelJump = false
                        packetEntityVelocity.motionX = (0.985114).toInt()
                        packetEntityVelocity.motionY = (0.885113).toInt()
                        packetEntityVelocity.motionZ = (0.785112).toInt()
                        thePlayer.motionX /= 1.75
                        thePlayer.motionZ /= 1.75
                    }
                }


                "groundreduce" -> {
                    if (thePlayer.onGround) {
                        velocityInput = true
                        val yaw = thePlayer.rotationYaw * 0.017453292F
                        packetEntityVelocity.motionX = (packetEntityVelocity.motionX * 0.75).toInt()
                        packetEntityVelocity.motionZ = (packetEntityVelocity.motionZ * 0.75).toInt()
                        thePlayer.motionX -= sin(yaw) * 0.2
                        thePlayer.motionZ += cos(yaw) * 0.2
                    }
                }

                "hytpacketa" -> {
                    packetEntityVelocity.motionX =
                        (packetEntityVelocity.motionX * hytpacketaset.get() / 1.5).toInt()
                    packetEntityVelocity.motionY = (0.7).toInt()
                    packetEntityVelocity.motionZ =
                        (packetEntityVelocity.motionZ * hytpacketaset.get() / 1.5).toInt()
                    event.cancelEvent()
                }

                "hytpacketb" -> {
                    packetEntityVelocity.motionX =
                        (packetEntityVelocity.motionX * hytpacketbset.get() / 2.5).toInt()
                    packetEntityVelocity.motionY =
                        (packetEntityVelocity.motionY * hytpacketbset.get() / 2.5).toInt()
                    packetEntityVelocity.motionZ =
                        (packetEntityVelocity.motionZ * hytpacketbset.get() / 2.5).toInt()
                }


                "aac4", "reverse", "smoothreverse", "aac5reduce","aaczero" -> velocityInput = true

                "hyttick" -> {
                    velocityInput = true
                    val horizontal = 0F
                    val vertical = 0F

                    event.cancelEvent()

                }

                "glitch" -> {
                    if (!thePlayer.onGround)
                        return

                    velocityInput = true
                    event.cancelEvent()
                }

                "hytcancel" -> {
                    event.cancelEvent()
                }
            }

        }
    }

    @EventTarget
    fun onJump(event: JumpEvent) {
        val thePlayer = mc.thePlayer

        if (thePlayer == null || thePlayer.isInWater || thePlayer.isInLava || thePlayer.isInWeb)
            return

        when (modeValue.get().toLowerCase()) {
            "aacpush" -> {
                jump = true

                if (!thePlayer.isCollidedVertically)
                    event.cancelEvent()
            }
            "aac4" -> {
                if (thePlayer.hurtTime > 0) {
                    event.cancelEvent()
                }
            }
            "aaczero" -> if (thePlayer.hurtTime > 0)
                event.cancelEvent()

        }

    }
}
