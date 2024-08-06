//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\����2\MCP 1.8.9 (1)\MCP 1.8.9\mcp918"!

//Decompiled by Procyon!

package cn.liying.utils;

import java.awt.*;

public enum ColorUtil2
{
    BLACK(-16711423), 
    BLUE(-12028161), 
    DARKBLUE(-12621684), 
    GREEN(-9830551), 
    DARKGREEN(-9320847), 
    WHITE(-65794), 
    AQUA(-7820064), 
    DARKAQUA(-12621684), 
    GREY(-9868951), 
    DARKGREY(-14342875), 
    RED(-65536), 
    DARKRED(-8388608), 
    ORANGE(-29696), 
    DARKORANGE(-2263808), 
    YELLOW(-256), 
    DARKYELLOW(-2702025), 
    MAGENTA(-18751), 
    DARKMAGENTA(-2252579);
    
    public int c;
    
    private ColorUtil2(final int co) {
        this.c = co;
    }
    
    public static int getColor(final Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public static int getColor(final Color leftColor, final int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }
    
    public static int getColor(final int brightness, final int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }
    
    public static int getColor(final int red, final int green, final int blue) {
        return getColor(red, green, blue, 255);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        final byte color = 0;
        int color2 = color | alpha << 24;
        color2 |= red << 16;
        color2 |= green << 8;
        color2 |= blue;
        return color2;
    }
    
    public static int blendColours(final int[] colours, final double progress) {
        final int size = colours.length;
        if (progress == 1.0) {
            return colours[0];
        }
        if (progress == 0.0) {
            return colours[size - 1];
        }
        final double mulProgress = Math.max(0.0, (1.0 - progress) * (size - 1));
        final int index = (int)mulProgress;
        return fadeBetween(colours[index], colours[index + 1], mulProgress - index);
    }
    
    public static int fadeBetween(final int startColour, final int endColour, double progress) {
        if (progress > 1.0) {
            progress = 1.0 - progress % 1.0;
        }
        return fadeTo(startColour, endColour, progress);
    }
    
    public static int fadeBetween(final int startColour, final int endColour) {
        return fadeBetween(startColour, endColour, 0.0);
    }
    
    public static int fadeTo(final int startColour, final int endColour, final double progress) {
        final double invert = 1.0 - progress;
        final int r = (int)((startColour >> 16 & 0xFF) * invert + (endColour >> 16 & 0xFF) * progress);
        final int g = (int)((startColour >> 8 & 0xFF) * invert + (endColour >> 8 & 0xFF) * progress);
        final int b = (int)((startColour & 0xFF) * invert + (endColour & 0xFF) * progress);
        final int a = (int)((startColour >> 24 & 0xFF) * invert + (endColour >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
}
