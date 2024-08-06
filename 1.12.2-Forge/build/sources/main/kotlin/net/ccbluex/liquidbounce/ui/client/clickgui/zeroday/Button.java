package net.ccbluex.liquidbounce.ui.client.clickgui.zeroday;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Translate;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;

public class Button {

    Module module;
    public IFontRenderer ListFont;
    public float x;
    public float y;
    public ArrayList<ValueButton> buttons;
    public ArrayList keyBindButtons;
    private Window parent;
    private float animationY;
    public Translate translate;

    public Button(Module module, float x, float y) {
        this.ListFont = Fonts.font30;
        this.buttons = new ArrayList();
        this.keyBindButtons = new ArrayList();
        this.translate = new Translate(0.0F, 0.0F);
        this.module = module;
        this.x = x;
        this.y = y;
        int y2 = (int) (y + 5.0F);

        for (Iterator<Value<?>> iterator = this.module.getValues().iterator(); iterator.hasNext(); y2 += 20) {
            Value v = iterator.next();

            this.buttons.add(new ValueButton(v, (int) x, (float) y2));
        }

        if (module.getKeyBind() != 0) {
            this.keyBindButtons.add(new KeyBindButton(module.getKeyBind(), Keyboard.getKeyName(module.getKeyBind()), x, (float) y2));
        }

    }

    public void render(int mouseX, int mouseY, Window window) {
        this.animationY = this.y;
        if (this.module.getState()) {
            this.parent.drawGradientRect((double) this.x, (double) (this.animationY - 7.0F), (double) (this.x + 125.0F), (double) (this.animationY + 17.0F), true, this.parent.leftColor.getRGB(), this.parent.RightColor.getRGB());
        }

        GlStateManager.resetColor();
        this.ListFont.drawString(this.module.getName(), this.x + 5.0F, this.animationY + 1.0F, -1);
        if (!this.module.getValues().isEmpty()) {
            drawAndRotateArrow(this.x + 110.0F - 0.2F, this.animationY + 3.0F + 1.0F, 8.0F, this.module.showSettings);
        }

        if (this.module.showSettings) {
            float modY = this.animationY + 25.0F;
            Iterator iterator = this.buttons.iterator();

            ValueButton keyBindButton;

            while (iterator.hasNext()) {
                keyBindButton = (ValueButton) iterator.next();
                keyBindButton.y = modY;
                keyBindButton.x = (int) this.x;
                if (keyBindButton.value instanceof BoolValue) {
                    modY += 25.0F;
                }

                if (keyBindButton.value instanceof IntegerValue) {
                    modY += 40.0F;
                }

                if (keyBindButton.value instanceof FloatValue) {
                    modY += 40.0F;
                }

                if (keyBindButton.value instanceof ListValue) {
                    if (((ListValue) keyBindButton.value).openList) {
                        modY += (float) (((ListValue) keyBindButton.value).getValues().length * 15);
                    }

                    modY += 25.0F;
                }
            }

            iterator = this.buttons.iterator();

            while (iterator.hasNext()) {
                keyBindButton = (ValueButton) iterator.next();
                keyBindButton.render(mouseX, mouseY, window);
            }

            if (this.module.getKeyBind() != 0) {
                KeyBindButton keyBindButton1;

                for (iterator = this.keyBindButtons.iterator(); iterator.hasNext(); keyBindButton1.x = this.x) {
                    keyBindButton1 = (KeyBindButton) iterator.next();
                    keyBindButton1.y = modY;
                }

                iterator = this.keyBindButtons.iterator();

                while (iterator.hasNext()) {
                    keyBindButton1 = (KeyBindButton) iterator.next();
                    keyBindButton1.render();
                }
            }
        }

    }

    public void click(int mouseX, int mouseY, int Button) {
        if (this.isHovered(this.parent.x, this.parent.y + 25.0F, this.parent.x + 125.0F, this.parent.y + 25.0F + this.parent.height, mouseX, mouseY)) {
            if (Button == 0 && this.isHovered(this.x, this.animationY - 5.0F, this.x + 125.0F, this.animationY + 15.0F, mouseX, mouseY)) {
                this.module.setState(!this.module.getState());
            }

            if (Button == 1 && this.isHovered(this.x, this.animationY - 5.0F, this.x + 125.0F, this.animationY + 15.0F, mouseX, mouseY) && !this.module.getValues().isEmpty()) {
                this.module.showSettings = !this.module.showSettings;
            }

            this.buttons.forEach((valueButton) -> {
                valueButton.click(mouseX, mouseY, Button);
            });
        }

    }



    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
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

    public void setPanel(Window window) {
        this.parent = window;
    }
}
