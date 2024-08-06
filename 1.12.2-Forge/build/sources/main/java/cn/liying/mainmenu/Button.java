package cn.liying.mainmenu;


import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.GuiMainMenuButtonUtils.RenderUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.mc;

public class Button {
    String name;
    int x;
    int y;
    IGuiScreen guiScreen;
    public boolean isHovered = false;
    public float hoverAni = 0;
    String icon;
    public Button(String name, IGuiScreen guiScreen, String icon){
        this.name=name;
        this.guiScreen=guiScreen;
        this.icon=icon;
    }

    public void draw(int x,int y,int mouseX,int mouseY){
        this.x=x;
        this.y=y;
        isHovered=isHovered(x,y,x+120,y+18,mouseX,mouseY);
        hoverAni = (float) RenderUtils.getAnimationState(hoverAni, isHovered ? 255f : 0f, 500);
        float finalAni = clampValue(hoverAni / 100f, 0, 1);
        RenderUtils.drawRoundRect(x-1,y-1,x+121,y+19,new Color(62,61,64).getRGB());
        if(hoverAni > 1f) {
            RenderUtils.drawRoundRect(x-1,y-1,x+121,y+19, RenderUtils.reAlpha(Colors.getColor(new Color(255,165,0)), finalAni));
        }else{
            RenderUtils.drawRoundRect(x-1,y-1,x+121,y+19,new Color(62,61,64).getRGB());
        }
        RenderUtils.drawRoundRect(x,y,x+120,y+18,new Color(22,22,22).getRGB());
        Fonts.font35.drawStringWithShadow(name, (int) (x  + 120 / 2f - 30), (int) (y + (18 / 2f) - (Fonts.font20.getFontHeight() / 2f)), Color.WHITE.getRGB());

    }
    
    public void onClick() {
        if(isHovered) {
            if (guiScreen == null) {
                Minecraft.getMinecraft().shutdown();
            } else {
                mc.displayGuiScreen(guiScreen);
            }
        }
    }
    
    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public static float clampValue(final float value, final float floor, final float cap) {
        if (value < floor) {
            return floor;
        }
        return Math.min(value, cap);
    }
}
