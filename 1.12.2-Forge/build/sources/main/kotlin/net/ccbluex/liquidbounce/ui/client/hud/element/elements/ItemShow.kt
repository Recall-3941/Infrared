//package net.ccbluex.liquidbounce.ui.client.hud.element.elements
//
//
//import net.ccbluex.liquidbounce.ui.client.hud.element.Border
//import net.ccbluex.liquidbounce.ui.client.hud.element.Element
//import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
//import net.ccbluex.liquidbounce.ui.font.Fonts
//import net.ccbluex.liquidbounce.utils.render.RenderUtils
//import net.ccbluex.liquidbounce.value.IntegerValue
//import net.minecraft.item.*
//import java.awt.Color
//
//@ElementInfo(name = "ItemShow", disableScale = true, priority = 1)
//class ItemShow(x: Double = 5.0, y: Double = 130.0) : Element(x, y) {
//    private val xValue = IntegerValue("X", 125, 0, 1000)
//    private val yValue = IntegerValue("Y", 125, 0, 1000)
//    private val rectWidthValue = IntegerValue("RectWidth", 110, 0, 200)
//    private val rectHeightValue = IntegerValue("RectHeight", 40, 0, 200)
//    private var Item = ""
//    private var i = 0
//    override fun drawElement(): Border? {
//        val CurItem = mc.thePlayer!!.heldItem!!.item
//        RenderUtils.drawBorderedRect(
//            xValue.get().toFloat(),
//            yValue.get().toFloat(),
//            (xValue.get() + Fonts.font35.getStringWidth(mc.thePlayer!!.heldItem!!.displayName)+48).toFloat(),
//            (yValue.get() + rectHeightValue.get()).toFloat(),
//            1f,
//            Color(0, 0, 0, 140).rgb,
//            Color(0,0,0,100).rgb
//        )
//        if (mc.thePlayer!!.heldItem != null) {
//            val CurItem = mc.thePlayer!!.heldItem!!.item
//            Item = if (CurItem is ItemSword) {
//                "Sword"
//            } else {
//                if (CurItem is ItemFood || CurItem is ItemBucketMilk) {
//                    "Food"
//                } else {
//                    if (CurItem is ItemBlock) {
//                        "Block"
//                    } else {
//                        if (CurItem is ItemBow) {
//                            "Bow"
//                        } else {
//                            if (CurItem is ItemTool) {
//                                "Tool"
//                            } else {
//                                if (CurItem is ItemPotion) {
//                                    "Potion"
//                                } else {
//                                    if (CurItem is ItemMap || CurItem is ItemEmptyMap) {
//                                        "Map"
//                                    } else {
//                                        "Misc"
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            val ItemName = mc.thePlayer!!.heldItem!!.displayName
//            Fonts.font35.drawString(
//                "Name:$ItemName",
//                (xValue.get() + 4).toFloat(),
//                (yValue.get() + 5).toFloat(),
//                Color(255, 255, 255).rgb,
//                true
//            )
//            Fonts.font35.drawString(
//                "Item:$Item",
//                xValue.get() + rectWidthValue.get() / 2.8f,
//                (yValue.get() + rectHeightValue.get() / 2).toFloat(),
//                Color(255, 255, 255).rgb,
//                true
//            )
//            mc.renderItem.renderItemIntoGUI(
//                mc.thePlayer!!.heldItem!!,
//                xValue.get() + rectWidthValue.get() / 8,
//                yValue.get() + rectHeightValue.get() / 3
//            )
//
//
//        }
//        return Border(xValue.get().toFloat(),
//            yValue.get().toFloat(),
//            (xValue.get() + Fonts.font35.getStringWidth(mc.thePlayer!!.heldItem!!.displayName)+48).toFloat(),
//            (yValue.get() + rectHeightValue.get()).toFloat())
//    }
//}
