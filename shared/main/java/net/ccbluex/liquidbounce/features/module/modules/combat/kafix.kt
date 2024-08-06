package net.ccbluex.liquidbounce.features.module.modules.combat
/*
          By 0231-Online
          哔哩哔哩的本人不是作者，作者拜托我开源的，有问题也别找我
          寄语：希望某些人别把这东西当个宝贝一样，以为有了就不会ban了，还沾沾自喜。
          当然你要对号入座也行。希望你能给作者一点尊重，如果你在抄的时候把作者去掉了，
          那你的妈妈随之也会在世界上消失了：）
 */
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "Killaurafix", description = "fix ur ka",Chinese="杀戮修复", category = ModuleCategory.COMBAT)
class kafix : Module() {
    private val hurttime: IntegerValue =  IntegerValue("hurttime", 9, 1, 10)
    private val hurttime2: IntegerValue = IntegerValue("hurttime2", 10, 1, 10)
    private val AirRange: FloatValue = FloatValue("AirRange", 3f, 0f, 5f)
    private val GroundRange: FloatValue = FloatValue("GroundRange", 3.5f, 0f, 5f)
    private val Debug= BoolValue("Debug", true)
    var ticks=0
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer!!.isAirBorne){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.rangeValue.set(AirRange.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka range "+AirRange.value.toString())            }

        }
        if (mc.thePlayer!!.onGround){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.rangeValue.set(GroundRange.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka range "+GroundRange.value.toString())
            }
        }
        ticks++
        if (ticks ==1){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.hurtTimeValue.set(hurttime.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime "+hurttime.value.toString())
            }
        }
        if (ticks ==2){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.hurtTimeValue.set(hurttime2.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime "+hurttime2.value.toString())
            }
        }
        if (ticks ==3){
            ticks=0
        }
    }
}