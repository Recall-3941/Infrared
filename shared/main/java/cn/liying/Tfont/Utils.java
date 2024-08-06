//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\����2\MCP 1.8.9 (1)\MCP 1.8.9\mcp918"!

//Decompiled by Procyon!

package cn.liying.Tfont;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils
{
    public static boolean fuck;
    private static Minecraft mc;
    
    public static boolean isContainerEmpty(final Container container) {
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }
    
    public static Minecraft getMinecraft() {
        return Utils.mc;
    }
    
    public static String getMD5(final String input) {
        final StringBuilder res = new StringBuilder();
        try {
            final MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            final byte[] arrby;
            final byte[] md5 = arrby = algorithm.digest();
            for (final byte aMd5 : arrby) {
                final String tmp = Integer.toHexString(0xFF & aMd5);
                if (tmp.length() == 1) {
                    res.append("0").append(tmp);
                }
                else {
                    res.append(tmp);
                }
            }
        }
        catch (NoSuchAlgorithmException ex) {}
        return res.toString();
    }

    public static int add(final int number, final int add, final int max) {
        return (number + add > max) ? max : (number + add);
    }
    
    public static int remove(final int number, final int remove, final int min) {
        return (number - remove < min) ? min : (number - remove);
    }
    
    static {
        Utils.fuck = true;
        Utils.mc = Minecraft.getMinecraft();
    }
}
