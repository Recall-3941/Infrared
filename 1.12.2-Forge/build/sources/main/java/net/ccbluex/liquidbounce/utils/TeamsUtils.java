package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.utils.MinecraftInstance.*;

import net.ccbluex.liquidbounce.utils.MinecraftInstance.*;

import java.util.Objects;

public class TeamsUtils {
    public static boolean checkEnemyNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        return !getEntityNameColor((EntityLivingBase) Objects.requireNonNull(MinecraftInstance.mc.getThePlayer())).equals(getEntityNameColor(entity));
    }
    public static String getEntityNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        if (name.contains("§")) {
            if (name.contains("§1")) {
                return "§1";
            }
            if (name.contains("§2")) {
                return "§2";
            }
            if (name.contains("§3")) {
                return "§3";
            }
            if (name.contains("§4")) {
                return "§4";
            }
            if (name.contains("§5")) {
                return "§5";
            }
            if (name.contains("§6")) {
                return "§6";
            }
            if (name.contains("§7")) {
                return "§7";
            }
            if (name.contains("§8")) {
                return "§8";
            }
            if (name.contains("§9")) {
                return "§9";
            }
            if (name.contains("§0")) {
                return "§0";
            }
            if (name.contains("§e")) {
                return "§e";
            }
            if (name.contains("§d")) {
                return "§d";
            }
            if (name.contains("§a")) {
                return "§a";
            }
            if (name.contains("§b")) {
                return "§b";
            }
            if (name.contains("§c")) {
                return "§c";
            }
            if (name.contains("§f")) {
                return "§f";
            }
        }
        return "null";
    }
}
