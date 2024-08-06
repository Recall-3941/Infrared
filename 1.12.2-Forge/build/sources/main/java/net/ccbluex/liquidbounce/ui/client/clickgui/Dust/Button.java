/*
 * Decompiled with CFR 0.136.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.Dust;

import cn.liying.Tfont.CFontRenderer;
import cn.liying.Tfont.FontLoaders;
import com.google.common.collect.Lists;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class Button {
    public Module cheat;
    public Window parent;
    public int x;
    public int y;
    public int index;
    public int remander;
    public double opacity = 0.0;
    public ArrayList<ValueButton> buttons = Lists.newArrayList();
    public boolean expand;

    public Button(Module cheat, int x2, int y2) {
        this.cheat = cheat;
        this.x = x2;
        this.y = y2;
        int y22 = y2 + 14;
        if (cheat == LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class)) {
            this.buttons.add(new ColorValueButton(x2 + 15, y22));
        }
        for (Value v2 : cheat.getValues()) {
            this.buttons.add(new ValueButton(v2, x2 + 5, y22));
            y22 += 15;
        }
        this.buttons.add(new KeyBindButton(cheat, x2 + 5, y22));
    }

    public void render(int mouseX, int mouseY) {
        if (this.cheat.getValues().size() + (this.cheat == LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class) ? 2 : 1) != this.buttons.size()) {
            this.buttons.clear();
            int y2 = this.y + 14;
            if (this.cheat == LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class)) {
                this.buttons.add(new ColorValueButton(this.x + 15, y2));
            }
            for (Value v2 : this.cheat.getValues()) {
                this.buttons.add(new ValueButton(v2, this.x + 5, y2));
                y2 += 15;
            }
            this.buttons.add(new KeyBindButton(this.cheat, this.x + 5, y2));
        }
        if (this.index != 0) {
            Button b22 = this.parent.buttons.get(this.index - 1);
            this.y = b22.y + 15 + (b22.expand ? 15 * b22.buttons.size() : 0);
        }
        int i2 = 0;
        while (i2 < this.buttons.size()) {
            this.buttons.get((int)i2).y = this.y + 14 + 15 * i2;
            this.buttons.get((int)i2).x = this.x + 5;
            ++i2;
        }
        CFontRenderer font = FontLoaders.sf_ui_display_regular14;
        font.drawStringWithShadow(this.cheat.getName(), this.x+38-font.getStringWidth(this.cheat.getName())/2, this.y, new Color(180,180,180).getRGB());
        if (this.cheat.getState()) {
            Gui.drawRect(this.x-5, this.y-6, this.x +85, this.y + font.getStringHeight(this.cheat.getName())+5 ,new Color(0,0,0,80).getRGB());
            font.drawStringWithShadow(this.cheat.getName(), this.x+38-font.getStringWidth(this.cheat.getName())/2, this.y, -1);
        }

        if (!this.expand && this.buttons.size() > 1) {
            Gui.drawRect(this.x-5, this.y - 5, this.x-4, this.y + font.getStringHeight(this.cheat.getName())+3, new Color(new Color(200, 200, 200,0).getRGB()).getRGB());
        }
        if (this.expand) {
            this.buttons.forEach(b2 -> b2.render(mouseX, mouseY));
        }
    }

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b2 -> b2.key(typedChar, keyCode));
    }

    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.sf_ui_display_regular18.getStringHeight(this.cheat.getName()) + 4) {
            if (button == 0) {
                this.cheat.setState(!this.cheat.getState());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
                this.expand = !this.expand;
                boolean bl2 = this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b2 -> b2.click(mouseX, mouseY, button));
        }
    }

    public void setParent(Window parent) {
        this.parent = parent;
        int i2 = 0;
        while (i2 < this.parent.buttons.size()) {
            if (this.parent.buttons.get(i2) == this) {
                this.index = i2;
                this.remander = this.parent.buttons.size() - i2;
                break;
            }
            ++i2;
        }
    }
}

