package cn.liying.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;

import java.util.Random;

public class RandomImgUtils {
    private static long startTime = 0L;
    static Random random = new Random();
    static int count = random.nextInt(1);
    public static int count2 = random.nextInt(1);

    public static IResourceLocation getBackGround() {
        return LiquidBounce.wrapper.getClassProvider().createResourceLocation(("liying/bg/" + count + ".png"));
    }
}
