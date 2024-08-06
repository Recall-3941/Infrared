package net.ccbluex.liquidbounce.ui.client.clickgui.zeroday;

import cn.liying.utils.VisualUtils2;
import com.google.common.collect.Lists;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Translate;
import net.ccbluex.liquidbounce.utils.VisualUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Window {

    public ModuleCategory moduleCategory;
    public ArrayList<net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button> buttons = Lists.newArrayList();
    private IFontRenderer TitleFont;
    public float x;
    public float y;
    private int pngWidth;
    private int pngHeight;
    private int pngX;
    private int pngY;
    private int wheel;
    public int mouseWheel;
    public float height;
    public Translate translate;
    public Color leftColor;
    public Color RightColor;
    private boolean dragged;
    private int mouseX2;
    private int mouseY2;

    public Window(ModuleCategory category, float x, float y) {
        this.TitleFont = Fonts.font40;
        this.translate = new Translate(0.0F, 0.0F);
        this.moduleCategory = category;
        this.x = x;
        this.y = y;
        float moduleY = 0.0F;

        Iterator iterator;

        for (iterator = LiquidBounce.INSTANCE.getModuleManager().getModuleInCategory(this.moduleCategory).iterator(); iterator.hasNext(); moduleY += 20.0F) {
            Module button = (Module) iterator.next();

            this.buttons.add(new net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button(button, x, y + 30.0F + moduleY));
        }

        iterator = this.buttons.iterator();

        while (iterator.hasNext()) {
            net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button button1 = (net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button) iterator.next();

            button1.setPanel(this);
        }

    }

    public void render(int tick, int mouseX, int mouseY) {
        if (this.dragged) {
            this.x = (float) (this.mouseX2 + mouseX);
            this.y = (float) (this.mouseY2 + mouseY);

            net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button moduleHeight;

            for (Iterator modY = this.buttons.iterator(); modY.hasNext(); moduleHeight.x = this.x) {
                moduleHeight = (net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button) modY.next();
            }
        }

        if (this.isHovered(this.x, this.y, this.x + 125.0F, this.y + 25.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.dragged = true;
            this.mouseX2 = (int) (this.x - (float) mouseX);
            this.mouseY2 = (int) (this.y - (float) mouseY);
        } else {
            this.dragged = false;
        }

        if (this.moduleCategory == ModuleCategory.COMBAT) {
            this.pngWidth = 16;
            this.pngHeight = 16;
            this.pngX = 5;
            this.pngY = 4;
        }

        if (this.moduleCategory == ModuleCategory.PLAYER) {
            this.pngWidth = 15;
            this.pngHeight = 18;
            this.pngX = 5;
            this.pngY = 3;
        }

        if (this.moduleCategory == ModuleCategory.MOVEMENT) {
            this.pngWidth = 16;
            this.pngHeight = 11;
            this.pngX = 5;
            this.pngY = 6;
        }

        if (this.moduleCategory == ModuleCategory.RENDER) {
            this.pngWidth = 16;
            this.pngHeight = 18;
            this.pngX = 5;
            this.pngY = 3;
        }

        if (this.moduleCategory == ModuleCategory.WORLD) {
            this.pngWidth = 15;
            this.pngHeight = 18;
            this.pngX = 6;
            this.pngY = 3;
        }

        float modY1 = this.y + 30.0F;

        net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button button;
        Iterator moduleHeight1;

        for (moduleHeight1 = this.buttons.iterator(); moduleHeight1.hasNext(); modY1 += 24.0F) {
            button = (net.ccbluex.liquidbounce.ui.client.clickgui.zeroday.Button) moduleHeight1.next();
            button.translate.interpolate(0.0F, modY1, 0.1D);
            float iY = button.translate.getY();

            button.y = iY + this.translate.getY();
            if (button.module.showSettings) {
                Iterator iterator = button.module.getValues().iterator();

                while (iterator.hasNext()) {
                    Value value = (Value) iterator.next();

                    if (value instanceof BoolValue) {
                        modY1 += 25.0F;
                    }

                    if (value instanceof IntegerValue) {
                        modY1 += 40.0F;
                    }

                    if (value instanceof FloatValue) {
                        modY1 += 40.0F;
                    }

                    if (value instanceof ListValue) {
                        if (((ListValue) value).openList) {
                            modY1 += (float) (((ListValue) value).getValues().length * 15);
                        }

                        modY1 += 25.0F;
                    }
                }

                if (button.module.getKeyBind() != 0) {
                    modY1 += 30.0F;
                }
            }
        }

        this.height = this.buttons.size() * 20 > 300 ? 250.0F : (float) (this.buttons.size() * 20);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glEnable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Stencil.write(false);
        Stencil.erase(false);
        this.drawShadow(this.x, this.y, 125.0F, this.height + 25.0F);
        Stencil.dispose();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glEnable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Stencil.write(false);
        Stencil.erase(true);
        Stencil.dispose();
        this.leftColor = ColorUtils.rainbow(tick);
        this.RightColor = ColorUtils.rainbow(tick + 1);
        this.drawGradientRect((double) this.x, (double) this.y, (double) (this.x + 125.0F), (double) (this.y + 25.0F), false, ColorUtils.rainbow(tick).getRGB(), ColorUtils.rainbow(tick + 1).getRGB());
        RenderUtils.drawRect(this.x, this.y, this.x + 26.0F, this.y + 25.0F, (new Color(0, 0, 0, 60)).getRGB());
        RenderUtils.drawImage(
                LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("liying/zeroday/" + this.moduleCategory.getDisplayName().toLowerCase() + ".png"), (int) (this.x + (float) this.pngX), (int) (this.y + (float) this.pngY), this.pngWidth, this.pngHeight);
        this.TitleFont.drawString(this.moduleCategory.getDisplayName(), this.x + 31.0F, this.y + 5.0F, -1);
        GlStateManager.pushMatrix();
        GL11.glEnable(3089);
        VisualUtils2.doGlScissor((int) this.x, (int) this.y + 25, (int) (this.x + 125.0F), (int) this.height);
        RenderUtils.drawRect(this.x, this.y + 25.0F, this.x + 125.0F, this.y + 25.0F + this.height, (new Color(56, 56, 56)).getRGB());
        moduleHeight1 = this.buttons.iterator();

        while (moduleHeight1.hasNext()) {
            button = (Button) moduleHeight1.next();
            button.render(mouseX, mouseY, this);
        }

        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glEnable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Stencil.write(false);
        Stencil.erase(false);
        this.drawShadow(this.x, this.y + 25.0F, 125.0F, 0.0F);
        Stencil.dispose();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glEnable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Stencil.write(false);
        Stencil.erase(true);
        Stencil.dispose();
        GL11.glDisable(3089);
        GlStateManager.popMatrix();
        if (this.isHovered(this.x, this.y + 25.0F, this.x + 125.0F, this.y + 25.0F + this.height, mouseX, mouseY)) {
            if (this.wheel > 0 && this.mouseWheel < 0) {
                this.mouseWheel += 10;
            }

            if (this.wheel < 0 && (float) Math.abs(this.mouseWheel) < modY1 - this.y - this.height - 30.0F) {
                this.mouseWheel -= 10;
            }
        }

        if (Mouse.hasWheel() && (float) Math.abs(this.mouseWheel) > modY1 - this.y - this.height - 30.0F) {
            this.mouseWheel = (int) (-(modY1 - this.y - this.height - 30.0F));
        }

        this.translate.interpolate(0.0F, (float) this.mouseWheel, 0.1D);
    }

    public void setWheel(int wheel) {
        this.wheel = wheel;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
    }


    public void drawShadow(float x, float y, float width, float height) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        this.drawTexturedRect(x - 9.0F, y - 9.0F, 9.0F, 9.0F, "paneltopleft", sr);
        this.drawTexturedRect(x - 9.0F, y + height, 9.0F, 9.0F, "panelbottomleft", sr);
        this.drawTexturedRect(x + width, y + height, 9.0F, 9.0F, "panelbottomright", sr);
        this.drawTexturedRect(x + width, y - 9.0F, 9.0F, 9.0F, "paneltopright", sr);
        this.drawTexturedRect(x - 9.0F, y, 9.0F, height, "panelleft", sr);
        this.drawTexturedRect(x + width, y, 9.0F, height, "panelright", sr);
        this.drawTexturedRect(x, y - 9.0F, width, 9.0F, "paneltop", sr);
        this.drawTexturedRect(x, y + height, width, 9.0F, "panelbottom", sr);
    }

    public void drawShadowDown(float x, float y, float width, float height) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        this.drawTexturedRect(x - 9.0F, y + height, 9.0F, 9.0F, "panelbottomleft", sr);
        this.drawTexturedRect(x + width, y + height, 9.0F, 9.0F, "panelbottomright", sr);
        this.drawTexturedRect(x, y + height, width, 9.0F, "panelbottom", sr);
    }

    public void drawTexturedRect(float x, float y, float width, float height, String image, ScaledResolution sr) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("liying/" + image + ".png"));
        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0.0F, 0.0F, (int) width, (int) height, width, height);
    }

    public void click(int mouseX, int mouseY, int Button) {
        this.buttons.forEach((button) -> {
            button.click(mouseX, mouseY, Button);
        });
    }



    public void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        VisualUtils.glColor(startColor);
        if (sideways) {
            GL11.glVertex2d(left, top);
            GL11.glVertex2d(left, bottom);
            VisualUtils.glColor(endColor);
            GL11.glVertex2d(right, bottom);
            GL11.glVertex2d(right, top);
        } else {
            GL11.glVertex2d(left, top);
            VisualUtils.glColor(endColor);
            GL11.glVertex2d(left, bottom);
            GL11.glVertex2d(right, bottom);
            VisualUtils.glColor(startColor);
            GL11.glVertex2d(right, top);
        }

        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
    }
}
