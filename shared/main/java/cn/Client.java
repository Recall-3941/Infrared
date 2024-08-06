package cn;



import cn.novoline.api.FontManager;
import cn.novoline.impl.SimpleFontManager;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class Client {
    public static double fontScaleOffset = 1;//round((double)1600/1080, 1) * s.getScaleFactor();//2.75;
    public static FontManager fontManager = SimpleFontManager.create();
    public static FontManager getFontManager() {
        return fontManager;
    }

    public static String name = "Vestige";
    public static String version = "240806";
    public static int THEME_RGB_COLOR = new Color(36, 240, 0).getRGB();

    public static Client instance = new Client();


    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

//  fxxking evo clickgui

}
