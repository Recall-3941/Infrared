package net.ccbluex.liquidbounce.ui.client.clickgui.zeroday;

import cn.liying.utils.ColorUtil2;
import cn.liying.utils.VisualUtils2;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Translate;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ValueButton {

    public final Value value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public IFontRenderer ValueFont;
    public float y;
    private float animationY;
    public Translate translate;
    public float listY;

    public ValueButton(Value value, int x, float y) {
        this.ValueFont = Fonts.font30;
        this.translate = new Translate(0.0F, 0.0F);
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public void render(int mouseX, int mouseY, Window parent) {
        this.animationY = this.y;
        int left = ColorUtil2.getColor(parent.leftColor, 150);
        int right = ColorUtil2.getColor(parent.RightColor, 150);

        if (this.value instanceof BoolValue) {
            RenderUtils.drawRect((float) (this.x + 5), this.animationY - 5.0F, (float) (this.x + 120), this.animationY + 15.0F, new Color(46, 46, 46));
            this.ValueFont.drawString(this.value.getName(), (float) (this.x + 8), (float) ((double) this.animationY + 2.5D), -1);
            if (((BoolValue) this.value).get()) {
                parent.drawGradientRect((double) (this.x + 106), (double) this.animationY, (double) (this.x + 116), (double) (this.animationY + 10.0F), true, parent.leftColor.getRGB(), parent.RightColor.getRGB());
            } else {
                parent.drawGradientRect((double) (this.x + 106), (double) this.animationY, (double) (this.x + 116), (double) (this.animationY + 10.0F), true, (new Color(56, 56, 56)).getRGB(), (new Color(56, 56, 56)).getRGB());
            }
        }

        GlStateManager.resetColor();
        float posX;
        double max;

        if (this.value instanceof IntegerValue) {
            RenderUtils.drawRect((float) (this.x + 5), this.animationY - 5.0F, (float) (this.x + 120), this.animationY + 30.0F, new Color(46, 46, 46));
            this.ValueFont.drawString(this.value.getName(), (float) (this.x + 11), this.animationY + 2.0F, -1);
            posX = (float) (this.x + 14);
            max = Math.max(0.0D, (double) (((float) mouseX - posX) / 90.0F));
            IntegerValue value1 = (IntegerValue) this.value;

            value1.getTranslate().interpolate(90.0F * (float) (value1.get() > value1.getMaximum() ? value1.getMaximum() : (value1.get() < value1.getMinimum() ? 0 : value1.get() - value1.getMinimum())) / (float) (value1.getMaximum() - value1.getMinimum()), 0.0F, 0.1D);
            RenderUtils.drawRect(posX, this.animationY + 15.0F, posX + 90.0F, this.animationY + 19.0F, (new Color(56, 56, 56)).getRGB());
            parent.drawGradientRect((double) posX, (double) (this.animationY + 15.0F), (double) (posX + (value1.getTranslate().getX() - 2.0F)), (double) (this.animationY + 19.0F), true, left, right);
            parent.drawGradientRect((double) (posX + (value1.getTranslate().getX() - 1.0F)), (double) (this.animationY + 12.0F), (double) (posX + value1.getTranslate().getX() + 3.0F), (double) (this.animationY + 22.0F), true, parent.leftColor.getRGB(), parent.RightColor.getRGB());
            this.ValueFont.drawString(String.valueOf((Integer) this.value.get()), posX + value1.getTranslate().getX() + 5.0F, this.animationY + 15.0F, -1);
            if (this.isHovered(posX, this.animationY + 15.0F, posX + 90.0F, this.animationY + 19.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                value1.set((Number) Math.toIntExact(Math.round((double) value1.getMinimum() + (double) (value1.getMaximum() - value1.getMinimum()) * Math.min(max, 1.0D))));
            }
        }

        if (this.value instanceof FloatValue) {
            RenderUtils.drawRect((float) (this.x + 5), this.animationY - 5.0F, (float) (this.x + 120), this.animationY + 30.0F, new Color(46, 46, 46));
            this.ValueFont.drawString(this.value.getName(), (float) (this.x + 11), this.animationY + 2.0F, -1);
            posX = (float) (this.x + 14);
            max = Math.max(0.0D, (double) (((float) mouseX - posX) / 90.0F));
            FloatValue floatvalue = (FloatValue) this.value;

            floatvalue.getTranslate().interpolate(90.0F * ((Float) floatvalue.get() > floatvalue.getMaximum() ? floatvalue.getMaximum() : (((Float) floatvalue.get()).floatValue() < floatvalue.getMinimum() ? 0.0F : ((Float) floatvalue.get()).floatValue() - floatvalue.getMinimum())) / (floatvalue.getMaximum() - floatvalue.getMinimum()), 0.0F, 0.1D);
            RenderUtils.drawRect(posX, this.animationY + 15.0F, posX + 90.0F, this.animationY + 19.0F, (new Color(56, 56, 56)).getRGB());
            parent.drawGradientRect((double) posX, (double) (this.animationY + 15.0F), (double) (posX + (floatvalue.getTranslate().getX() - 2.0F)), (double) (this.animationY + 19.0F), true, left, right);
            parent.drawGradientRect((double) (posX + (floatvalue.getTranslate().getX() - 1.0F)), (double) (this.animationY + 12.0F), (double) (posX + floatvalue.getTranslate().getX() + 3.0F), (double) (this.animationY + 22.0F), true, parent.leftColor.getRGB(), parent.RightColor.getRGB());
            this.ValueFont.drawString(String.valueOf((Float) this.value.get()), posX + floatvalue.getTranslate().getX() + 5.0F, this.animationY + 15.0F, -1);
            if (this.isHovered(posX, this.animationY + 15.0F, posX + 90.0F, this.animationY + 19.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                floatvalue.set((Number) ((double) Math.round(((double) floatvalue.getMinimum() + (double) (floatvalue.getMaximum() - floatvalue.getMinimum()) * Math.min(max, 1.0D)) * 100.0D) / 100.0D));
            }
        }

        if (this.value instanceof ListValue) {
            RenderUtils.drawRect((float) (this.x + 5), this.animationY - 5.0F, (float) (this.x + 120), this.animationY + 15.0F + (float) (((ListValue) this.value).openList ? ((ListValue) this.value).getValues().length * 15 : 0), new Color(46, 46, 46));
            Fonts.font30.drawString(this.value.getName(), (float) (this.x + 10), (float) ((double) this.animationY + 2.5D), -1);
            Fonts.font30.drawString((String) ((ListValue) this.value).get(), (float) (this.x + 120 - Fonts.font30.getStringWidth((String) ((ListValue) this.value).get()) - 20), (float) ((double) this.animationY + 2.5D), -1);
            drawAndRotateArrow((float) (this.x + 106) - 0.2F, this.animationY + 2.0F + 1.0F, 8.0F, ((ListValue) this.value).openList);
            if (((ListValue) this.value).openList) {
                this.listY = this.animationY + 16.0F;
                String[] astring = ((ListValue) this.value).getValues();
                int i = astring.length;

                for (String s : astring) {
                    Fonts.flux.drawString("n", (float) (this.x + 11), this.listY, parent.leftColor.getRGB());
                    Fonts.font30.drawString(s, (float) (this.x + 28), this.listY + 1.0F, -1);
                    if (s.equals(((ListValue) this.value).get())) {
                        VisualUtils2.drawRoundedRect((float) this.x + 13.2F, this.listY - 0.2F, 8.0F, 8.0F, 13.0F, parent.leftColor.getRGB(), 1.0F, parent.leftColor.getRGB());
                    }

                    this.listY += 15.0F;
                }
            }
        }

    }

    public static void drawAndRotateArrow(float x, float y, float size, boolean rotate) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(3553);
        GL11.glBegin(4);
        if (rotate) {
            GL11.glVertex2f(size, size / 2.0F);
            GL11.glVertex2f(size / 2.0F, 0.0F);
            GL11.glVertex2f(0.0F, size / 2.0F);
        } else {
            GL11.glVertex2f(0.0F, 0.0F);
            GL11.glVertex2f(size / 2.0F, size / 2.0F);
            GL11.glVertex2f(size, 0.0F);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public void click(int mouseX, int mouseY, int Button) {
        if (Button == 0) {
            if (this.isHovered((float) (this.x + 106), this.animationY, (float) (this.x + 116), this.animationY + 10.0F, mouseX, mouseY) && this.value instanceof BoolValue) {
                this.value.set(!(Boolean) ((BoolValue) this.value).get());
            }

            if (this.value instanceof ListValue && ((ListValue) this.value).openList) {
                this.listY = this.animationY + 16.0F;
                String[] astring = ((ListValue) this.value).getValues();
                int i = astring.length;

                for (String value1 : astring) {
                    if (this.isHovered((float) (this.x + 11), this.listY - 1.0F, (float) (this.x + 24), this.listY + 9.0F, mouseX, mouseY)) {
                        this.value.set(value1);
                    }

                    this.listY += 15.0F;
                }
            }
        }

        if (Button == 1 && this.isHovered((float) (this.x + 5), this.animationY - 5.0F, (float) (this.x + 120), this.animationY + 15.0F, mouseX, mouseY) && this.value instanceof ListValue) {
            ((ListValue) this.value).openList = !((ListValue) this.value).openList;
        }

    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
    }
}
