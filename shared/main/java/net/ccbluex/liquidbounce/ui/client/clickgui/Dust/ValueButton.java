package net.ccbluex.liquidbounce.ui.client.clickgui.Dust;


import cn.liying.Tfont.CFontRenderer;
import cn.liying.Tfont.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ValueButton {
   public Value value;
   public String name;
   public boolean custom = false;
   public boolean change;
   public int x;
   public int y;
   public double opacity = 0.0D;

   public ValueButton(Value value, int x, int y) {
      this.value = value;
      this.x = x;
      this.y = y;
      this.name = "";
      if(this.value instanceof BoolValue) {
         this.change = ((Boolean)((BoolValue)this.value).getValue()).booleanValue();
      } else if(this.value instanceof ListValue) {
         this.name = "" + ((ListValue)this.value).getValue();
      } else if(value instanceof IntegerValue) {
         IntegerValue v = (IntegerValue)value;
         this.name = String.valueOf(this.name) + (((Number)v.getValue()).doubleValue());
      }

      this.opacity = 0.0D;
   }

   public void render(int mouseX, int mouseY) {
       CFontRenderer font = FontLoaders.sf_ui_display_regular14;
      if(!this.custom) {
         if(mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.sf_ui_display_regular18.getStringHeight(this.value.getName()) + 5) {
            if(this.opacity + 10.0D < 200.0D) {
               this.opacity += 10.0D;
            } else {
               this.opacity = 200.0D;
            }
         } else if(this.opacity - 6.0D > 0.0D) {
            this.opacity -= 6.0D;
         } else {
            this.opacity = 0.0D;
         }
         if(this.change) {
            Gui.drawRect(this.x +67	,this.y - 3,this.x +77,this.y + font.getStringHeight(this.value.getName())+4, new Color(255,255,255).getRGB());
         }

         IntegerValue v1;
         double render;
         if(this.value instanceof BoolValue) {
            this.change = ((Boolean)((BoolValue)this.value).getValue()).booleanValue();
         } else if(this.value instanceof ListValue) {
            this.name = "" + ((ListValue)this.value).getValue();
         } else if(this.value instanceof IntegerValue) {
            v1 = (IntegerValue)this.value;
            this.name = "" + (((Number)v1.getValue()).doubleValue());
            if(mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + font.getStringHeight(this.value.getName()) + 5 && Mouse.isButtonDown(0)) {
               render = v1.getMinimum();
               double max = v1.getMaximum();
               double inc = 0;
               double valAbs = (double)mouseX - ((double)this.x + 1.0D);
               double perc = valAbs / 68.0D;
               perc = Math.min(Math.max(0.0D, perc), 1.0D);
               double valRel = (max - render) * perc;
               double val = render + valRel;
               val = (double)Math.round(val * (1.0D / inc)) / (1.0D / inc);
               v1.setValue((int) val);
            }
         }
         if(this.value instanceof IntegerValue) {
            v1 = (IntegerValue)this.value;
            render = (double)(75.0F * (((Number)v1.getValue()).floatValue()+ v1.getMinimum()) / (v1.getMaximum() - v1.getMinimum()));
            RenderUtils.drawRect((float)this.x, (float)(this.y + font.getStringHeight(this.value.getName()) + 3), (float)((double)this.x + render), (float)(this.y + font.getStringHeight(this.value.getName()) + 4), (new Color(255, 255, 255)).getRGB());
         }
         RenderUtils.drawRect(0.0F, 0.0F, 0.0F, 0.0F, 0);
         font.drawStringWithShadow(this.value.getName(), (double)this.x, (double)this.y, -1);
         font.drawStringWithShadow(this.name, (double)(this.x + 75 - FontLoaders.sf_ui_display_regular18.getStringWidth(this.name)), (double)this.y, -1);
      }

   }

   public void key(char typedChar, int keyCode) {
   }

   public void click(int mouseX, int mouseY, int button) {
      if(!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y +FontLoaders.sf_ui_display_regular18.getStringHeight(this.value.getName()) + 5) {
         if(this.value instanceof BoolValue) {
            BoolValue m1 = (BoolValue)this.value;
            m1.setValue(Boolean.valueOf(!((Boolean)m1.getValue()).booleanValue()));
            return;
         }

         if (value instanceof ListValue) {
            ListValue m = (ListValue) value;
            if ((button == 0 || button == 1)) {
               String current = m.get();
               this.value.set(m.getValues()[m.getModeListNumber(current) + 1 >= m.getValues().length?0:m.getModeListNumber(current) + 1]);
         }
      }
      }
   }
}
