package net.ccbluex.liquidbounce.ui.client.clickgui.Jello;

import cn.liying.Tfont.FontLoaders;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

;

public class Category {
    ModuleCategory moduleCategory;
    int x;
    int y;
    public boolean dragged;
    int mouseX2,mouseY2;
    int width;
    int height;
    int wheel;
    int mouseWheel;
    boolean mouseClicked;
    int slider;
    boolean mouseClicked2;
    public final Translate translate = new Translate(0F, 0F);
    public final Translate RECTtranslate = new Translate(0F, 0F);

    public Category(ModuleCategory moduleCategory,int x,int y,int width,int height){
        this.moduleCategory=moduleCategory;
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
}

    public void draw(int mouseX,int mouseY){
        if(dragged) {
            x = mouseX2 + mouseX;
            y = mouseY2 + mouseY;
        }
       if (isHovered(x,y,x+width,y+height,mouseX,mouseY) && Mouse.isButtonDown(0)){
            dragged = true;
            mouseX2 = (int) (x - mouseX);
            mouseY2 = (int) (y - mouseY);
        } else {
            dragged=false;
       }
        Stencil.write(false);
        Stencil.erase(false);
        drawShadow(x,y,width,height); //85 25
        Stencil.dispose();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glEnable(3042);
        GL11.glColor4f(1, 1, 1, 1);
        Stencil.write(false);
        Stencil.erase(true);
        Stencil.dispose();
        GlStateManager.resetColor();
        RenderUtils.drawRect(x,y,x+width,y+30,new Color(230,230,230).getRGB());
        RenderUtils.drawRect(x,y+30,x+width,y+height,-1);
        FontLoaders.JelloTitle20.drawString(moduleCategory.getDisplayName(),x+6,y+12,new Color(55,55,55).getRGB(),true);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtils.doGlScissor(x,y+30,x+width,height-30);
        float moduleY=y+35+translate.getY();
        for (Module module : LiquidBounce.moduleManager.getModuleInCategory(moduleCategory)) {

            module.getModuleTranslate().interpolate(0, moduleY, 0.1);
            float modulePosY = module.getModuleTranslate().getY();
            float hoverOpacity;
            float rectHoved;
            RenderUtils.drawRect(x, modulePosY - 5, x + width, modulePosY + 10, ClientUtils.reAlpha(Colors.getColor(65, 155, 255), module.getHoverOpacity()));
            if (Mouse.hasWheel() && isHovered(x, y + 30, x + width, y + height, mouseX, mouseY)) {
                RenderUtils.drawRoundedRect(x + width - 10, y+30,x+width-5,y+height, 6,new Color(100, 100, 100).getRGB());
            }
            if (this.isHovered(x, modulePosY - 5, x + width, modulePosY + 10, mouseX, mouseY)) {
                rectHoved = (float) RenderUtils.getAnimationState(module.getRectHoved(), 0.25f, 1.0f);
                module.setRectHoved(rectHoved);
                if (LiquidBounce.INSTANCE.getModule()==null) {
                RenderUtils.drawRect(x, modulePosY - 5, x + width, modulePosY + 10, ClientUtils.reAlpha(Colors.BLACK.c, module.getRectHoved()));
                }
            }
            if (module.getState()) {
                hoverOpacity = (float) RenderUtils.getAnimationState(module.getHoverOpacity(), 1f, 1.0f);
                module.setHoverOpacity(hoverOpacity);
                FontLoaders.JelloTitle18.drawString(module.getName(), x + 8, modulePosY, -1);
            } else {
                hoverOpacity = (float) RenderUtils.getAnimationState(module.getHoverOpacity(), 0.0f, 1.5f);
                module.setHoverOpacity(hoverOpacity);
                FontLoaders.JelloTitle18.drawString(module.getName(), x + 6, modulePosY, new Color(1, 1, 1).getRGB());
            }

            if (isHovered(x, y + 30, x + width, y + height, mouseX, mouseY)) {
                if (this.isHovered(x, modulePosY - 5, x + width, modulePosY + 10, mouseX, mouseY)) {
                    if (Mouse.isButtonDown(0)) {
                        if (!mouseClicked)
                            if (Objects.equals(module.getCategory(), moduleCategory)) {
                                module.toggle();
                                module.setHovered(!module.getHovered());
                            }
                        mouseClicked = true;
                    } else mouseClicked = false;
                }
            }
            if (isHovered(x, y + 30, x + width, y + height, mouseX, mouseY)) {
                if (this.isHovered(x, modulePosY - 5, x + width, modulePosY + 10, mouseX, mouseY)) {
                    if (Mouse.isButtonDown(1)) {
                        if (!mouseClicked2)
                            if (!mouseClicked2 && module.getValues().size() > 0) {
                                for (Module mod : LiquidBounce.moduleManager.getModules()) {
                                    if (mod != LiquidBounce.INSTANCE.getModule()) {
                                        if (!module.showSettings && mod.showSettings)
                                            mod.showSettings = false;
                                    }
                                }
                                    if (LiquidBounce.INSTANCE.getModule()==null) {
                                        LiquidBounce.INSTANCE.setModule(module);
                                        Objects.requireNonNull(LiquidBounce.INSTANCE.getModule()).showSettings = !LiquidBounce.INSTANCE.getModule().showSettings;
                                        if (!module.getHovvv()) {
                                            module.setHovvv(true);
                                        }
                                    }
                            }
                        mouseClicked2 = true;
                    } else mouseClicked2 = false;
                }
            }
            moduleY+=15;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        float moduleHeight = moduleY - translate.getY();
        float rectHeight   = moduleY-RECTtranslate.getY();
        if (Mouse.hasWheel() && isHovered(x,y+30,x+width,y+height,mouseX,mouseY)) {
            if (wheel > 0) {
                if (mouseWheel<0) {
                    mouseWheel += 10;
                }
                if(slider<0) {
                    slider += 10;
                }
            }
            if (wheel < 0) {
                if (Math.abs(mouseWheel)<(moduleHeight-(y-16)-200)) {
                    mouseWheel -= 10;
                }
                if (Math.abs(slider)<rectHeight-50) {
                    slider -= 10;
                }
            }
        }
        translate.interpolate(0, mouseWheel, 0.15F);
        RECTtranslate.interpolate(0, slider, 0.15F);
    }
    
    public void setWheel(int Wheel){
        this.wheel=Wheel;
    }
    
    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public void drawShadow(float x, float y, float width, float height){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        drawTexturedRect(x - 9, y - 9, 9, 9, "paneltopleft", sr);
        drawTexturedRect(x - 9, y +  height, 9, 9, "panelbottomleft", sr);
        drawTexturedRect(x + width, y +  height, 9, 9, "panelbottomright", sr);
        drawTexturedRect(x + width, y - 9, 9, 9, "paneltopright", sr);
        drawTexturedRect(x - 9, y, 9, height, "panelleft", sr);
        drawTexturedRect(x + width, y, 9, height, "panelright", sr);
        drawTexturedRect(x, y - 9, width, 9, "paneltop", sr);
        drawTexturedRect(x, y + height, width, 9, "panelbottom", sr);
    }
    
    public void drawTexturedRect(float x, float y, float width, float height, String image, ScaledResolution sr) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("liying/jello/"+image+".png"));
        Gui.drawModalRectWithCustomSizedTexture((int)x,  (int)y, 0, 0,(int)width, (int)height, width, height);
    }
}
