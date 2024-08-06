package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.potion.Potion;

import java.util.Objects;

@ModuleInfo(name = "AntiDebuff", description = "Anti the debuff",Chinese ="自动移除负面效果", category = ModuleCategory.PLAYER)
public class AntiDebuff extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc2.player.removeActivePotionEffect(Potion.getPotionById(9));
        mc2.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionFromResourceLocation("blindness")));
        mc2.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionFromResourceLocation("slowness")));
    }
}
