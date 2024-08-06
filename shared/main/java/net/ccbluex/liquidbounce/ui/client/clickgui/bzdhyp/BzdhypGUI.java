package net.ccbluex.liquidbounce.ui.client.clickgui.bzdhyp;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
/**
 * Skid by Paimon.
 *
 * @Date 2022/8/16/016
 */
public class BzdhypGUI extends GuiScreen {

    private boolean dragging;

    private int startX = 10;
    private int startY = 10;
    private int dragX = 0;
    private int dragY = 0;
    private final IResourceLocation hudIcon = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation("loserline" + "/custom_hud_icon.png");
    private final IResourceLocation bg = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation("loserline" + "/clickgui/bg.png");


    private final IResourceLocation bg1 = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation("loserline" + "/clickgui/bg1.png");
    private final IResourceLocation touxiang = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation("loserline" + "/clickgui/touxiang.png");
    private final IResourceLocation shuiyin = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation("loserline" + "/clickgui/shuiyin.png");
    private final IResourceLocation logo = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation("loserline" + "/clickgui/logo.png");
    private ModuleCategory currentCategory = ModuleCategory.COMBAT;

    private Module currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentCategory).get(0);
    @Override
    public void initGui() {
        float x =10;


        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX,int mouseY,float partialTicks) {


        ScaledResolution sr =new ScaledResolution(mc);
        int x =dragX;
        int y = dragY;

        RenderUtils.drawImage(bg,x,y,533,350);
        RenderUtils.drawImage(bg1,x,y,533,350);
        RenderUtils.drawImage(logo,x + 13,y + 15,107,45);
        RenderUtils.drawImage(touxiang,x + 13+35+5,y + 15+35+5,15,15);
        RenderUtils.drawImage(shuiyin,x + 13+5+35+13,y + 15+35,49,25);
        int yPos = 65;
        for (final ModuleCategory category : ModuleCategory.values()) {
            ModuleCategory[] categories = ModuleCategory.values();
                yPos+=20;
                Fonts.com40.drawString(category.getDisplayName(),x+25,y+yPos, Color.WHITE.getRGB());
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClickMove(int p_mouseClickMove_1_, int p_mouseClickMove_2_, int p_mouseClickMove_3_, long p_mouseClickMove_4_) {
        if(isHovered(startX+5,startY+5,startX+553,startY+100,p_mouseClickMove_1_,p_mouseClickMove_2_)){
            dragX = p_mouseClickMove_1_;
            dragY = p_mouseClickMove_2_;
        }

        super.mouseClickMove(p_mouseClickMove_1_, p_mouseClickMove_2_, p_mouseClickMove_3_, p_mouseClickMove_4_);
    }
    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
