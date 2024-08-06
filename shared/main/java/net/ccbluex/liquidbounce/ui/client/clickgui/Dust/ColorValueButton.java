/*
 * Decompiled with CFR 0.136.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.Dust;


import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ColorValueButton
extends ValueButton {
    private float[] hue = new float[]{0.0f};
    private int position;
    private int color = new Color(125, 125, 125).getRGB();

    public ColorValueButton(int x2, int y2) {
        super(null, x2, y2);
        this.custom = true;
        this.position = -1111;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float[] huee = new float[]{this.hue[0]};
        int i2 = this.x - 7;
        while (i2 < this.x - 7 + 85) {
            int color = Color.getHSBColor(huee[0] / 255.0f, 0.7f, 0.7f).getRGB();
            if (mouseX > i2 - 1 && mouseX < i2 + 1 && mouseY > this.y - 6 && mouseY < this.y + 12 && Mouse.isButtonDown(0)) {
                this.color = color;
                this.position = i2;
            }
            if (this.color == color) {
                this.position = i2;
            }
            Gui.drawRect(i2 - 1, this.y - 2, i2, this.y - 2 + 12, color);
            float[] arrf = huee;
            arrf[0] = arrf[0] + 4.0f;
            if (huee[0] > 255.0f) {
                huee[0] = huee[0] - 255.0f;
            }
            ++i2;
        }
        Gui.drawRect(this.position, this.y - 2, this.position + 1, this.y - 2 + 12, -1);
        if (this.hue[0] > 255.0f) {
            this.hue[0] = this.hue[0] - 255.0f;
        }
    }

    @Override
    public void key(char typedChar, int keyCode) {
    }

    @Override
    public void click(int mouseX, int mouseY, int button) {
    }
}

