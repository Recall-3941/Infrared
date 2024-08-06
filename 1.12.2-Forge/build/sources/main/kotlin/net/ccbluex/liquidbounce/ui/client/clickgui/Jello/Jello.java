package net.ccbluex.liquidbounce.ui.client.clickgui.Jello;

import cn.liying.Tfont.FontLoaders;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Jello extends WrappedGuiScreen {
    ArrayList<Category> categories = new ArrayList<>();
    public float lastPercent;
    public float percent;
    public float percent2;
    public final Opacity smooth = new Opacity(0);
    public float outro;
    public final Translate translate = new Translate(0F, 0F);
    public float lastOutro;
    public float lastPercent2;
    boolean close;
    int mouseWheel;
    boolean mouseClicked;
    boolean mouseClicked2;
    double ani;
    HashMap<Module, Integer> hashMap=new HashMap<Module, Integer>();
    public Jello() {
        int x=20;
        net.minecraft.client.gui.ScaledResolution scaledResolution = new net.minecraft.client.gui.ScaledResolution(Minecraft.getMinecraft());
        int width=scaledResolution.getScaledWidth();
        int height=scaledResolution.getScaledHeight();
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            categories.add(new Category(moduleCategory, x, 20,100,175));
            x+=110;
            loadClickGui();
        }
        Savevalue();
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        start();
        int wheel= Mouse.getDWheel();
        categories.forEach(category -> category.setWheel(wheel));
        categories.forEach(category -> category.draw(mouseX, mouseY));
        if (!isHovered(representedScreen.getWidth() / 2.0f - 100.0f, representedScreen.getHeight() / 2.0f - 130.0f, representedScreen.getWidth() / 2.0f + 100.0f, representedScreen.getHeight() / 2.0f + 130.0f, mouseX, mouseY) && LiquidBounce.INSTANCE.getModule()!=null && Mouse.isButtonDown(0)) {
            SaveMouseWheel();
            LiquidBounce.INSTANCE.getModule().showSettings=false;
            LiquidBounce.INSTANCE.setModule(null);
        }
        if (LiquidBounce.INSTANCE.getModule()!=null && LiquidBounce.INSTANCE.getModule().showSettings) {
            RenderUtils.drawRect(0.0f, 0.0f, representedScreen.getWidth(), representedScreen.getHeight(), ClientUtils.reAlpha(Colors.BLACK.c, 0.45f));
        }
        if (LiquidBounce.INSTANCE.getModule()!=null && LiquidBounce.INSTANCE.getModule().showSettings){
            net.minecraft.client.gui.ScaledResolution scaledResolution = new net.minecraft.client.gui.ScaledResolution(Minecraft.getMinecraft());
            int width=scaledResolution.getScaledWidth();
            int height=scaledResolution.getScaledHeight();
            if (LiquidBounce.INSTANCE.getModule().getHovvv()){
                loadWheel();
                LiquidBounce.INSTANCE.getModule().setHovvv(false);
            };
            RenderUtils.drawBorderedRect(width / 2.0f - 100.0f, height / 2.0f - 130.0f, width / 2.0f + 100.0f, height / 2.0f + 130.0f, (int) 2.0f, Colors.WHITE.c, Colors.WHITE.c);
            FontLoaders.JelloTitle18.drawString(LiquidBounce.INSTANCE.getModule().getName(),width / 2.0f - 100.0f, height / 2.0f - 140.0f,-1);
            float y=height / 2.0f - 130.0f;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.doGlScissor((int) (width / 2.0f - 100.0f), (int)(height / 2.0f - 130.0f), (int)(width / 2.0f + 100.0f), (int)(height / 2.0f + 10F));
            float valueY=y+5+translate.getY();

            for (Value value : LiquidBounce.INSTANCE.getModule().getValues()) {
                value.getValueTranslate().interpolate(0, valueY, 0.1);
                float valuePosY = value.getValueTranslate().getY();

                float modeOffset = valuePosY+7;
                float fontOffset = modeOffset+1;
                if (value instanceof ListValue){
                    FontLoaders.JelloList16.drawString(((ListValue) value).getName(),width / 2.0f - 99.0f,valuePosY,new Color(0,0,0).getRGB());
                    RenderUtils.drawBorderedRect((int)(width / 2.0f + 60f),valuePosY-2F,(int)(width / 2.0f + 99f),valuePosY+6F,2F,new Color(100,100,100).getRGB(),new Color(100,100,100).getRGB());
                    FontLoaders.JelloList16.drawString(((ListValue) value).get(),(int)(width / 2.0f + 60f),valuePosY,-1);
                    if (isHovered(width / 2.0f - 100.0f, height / 2.0f - 130.0f, width / 2.0f + 100.0f, height / 2.0f + 130.0f,mouseX,mouseY)) {//判断鼠标是否在value窗口
                        if (isHovered((int)(width / 2.0f + 60f),valuePosY-2F,(int)(width / 2.0f + 99f),valuePosY+6F, mouseX, mouseY)) {
                            if (Mouse.isButtonDown(1)) {
                                if (!mouseClicked)
                                    ((ListValue) value).listtoggle();
                                mouseClicked = true;
                            } else mouseClicked = false;
                        }
                    }
                    if (!((ListValue) value).openList){
                        Stencil.write(false);
                        Stencil.erase(false);
                        drawTexturedRect((int)(width / 2.0f + 93.0f),(int)valuePosY+1,4,4,"selectedAltTriangle",scaledResolution);
                        Stencil.dispose();
                        GlStateManager.disableAlpha();
                        GlStateManager.enableBlend();
                        GL11.glEnable(3042);
                        GL11.glColor4f(1, 1, 1, 1);
                        Stencil.write(false);
                        Stencil.erase(true);
                        Stencil.dispose();
                    }else{
                        for (String str : ((ListValue) value).getValues()) {
                            ani = RenderUtils.getAnimationState((float) ani,7*((ListValue) value).getValues().length, 100);
                            RenderUtils.drawRect((int)(width / 2.0f + 60f),modeOffset,(int)(width / 2.0f + 99f), (float) (modeOffset+ani),new Color(100,100,100).getRGB());
                            if (!Objects.equals(str, ((ListValue) value).get())) {
                                FontLoaders.JelloList16.drawString(str, (int) (width / 2.0f + 60f), fontOffset, -1);
                                if (isHovered((int) (width / 2.0f + 60f),fontOffset-5,(int) (width / 2.0f + 99f),fontOffset+5,mouseX,mouseY)){
                                    if (Mouse.isButtonDown(0)) {
                                        if (!mouseClicked2)
                                            value.set(str);
                                        ((ListValue) value).openList=false;
                                        mouseClicked2 = true;
                                    } else mouseClicked2 = false;
                                }
                                fontOffset += 12;
                                modeOffset += 12;
                                valueY += 12;
                            }
                        }
                    }

                }
                if (value instanceof BoolValue){
                    if (((BoolValue) value).get()){
                        RenderUtils.drawImage(LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("liying/jello/checked.png"),(int)(width / 2.0f + 85.0f),(int)valuePosY-3,12,12);
                    }else{
                        RenderUtils.drawImage(LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("liying/jello/unchecked.png"),(int)(width / 2.0f + 85.0f),(int)valuePosY-3,12,12);
                    }
                    if (isHovered(width / 2.0f - 100.0f, height / 2.0f - 130.0f, width / 2.0f + 100.0f, height / 2.0f + 130.0f,mouseX,mouseY)) {//判断鼠标是否在value窗口
                        if (isHovered((int) (width / 2.0f + 85.0f), valuePosY - 5, (int) (width / 2.0f + 100.0f), valuePosY + 10, mouseX, mouseY)) {
                            if (Mouse.isButtonDown(0)) {
                                if (!mouseClicked)
                                    ((BoolValue) value).toggle();
                                mouseClicked = true;
                            } else mouseClicked = false;
                        }
                    }
                    FontLoaders.JelloList16.drawString(((BoolValue) value).getName(),width / 2.0f - 99.0f,valuePosY,new Color(0,0,0).getRGB());
                }
                if (value instanceof IntegerValue){
                    float posX = width / 2.0f + 35.0f;
                    final double max = Math.max(0.0, (mouseX - (posX + 8)) / 52.0);
                    FontLoaders.JelloList16.drawString(value.getName(),width / 2.0f - 99.0f,valuePosY,new Color(0,0,0).getRGB());
                    IntegerValue optionDouble = (IntegerValue)value;
                    optionDouble.getTranslate().interpolate((float) (52F * (optionDouble.get() > optionDouble.getMaximum() ? optionDouble.getMaximum() : optionDouble.get() < optionDouble.getMinimum() ? 0 : optionDouble.get() - optionDouble.getMinimum()) / (optionDouble.getMaximum() - optionDouble.getMinimum()) + 8), 0, 0.1);
                    RenderUtils.drawRect(posX + 8, valuePosY + 1, posX + 60, valuePosY + 3,  new Color(Colors.GREY.c).brighter().brighter().getRGB());
                    RenderUtils.drawRect(posX + 8, valuePosY + 1, (posX + optionDouble.getTranslate().getX()), valuePosY + 3, new Color(-14848033).brighter().getRGB());
                    RenderUtils.drawCircle((posX + optionDouble.getTranslate().getX()),  (valueY + 2.5F), 2.0F, new Color(-14848033).brighter().getRGB());
                    FontLoaders.Sans18.drawString(optionDouble.get().toString(), posX + optionDouble.getTranslate().getX()-2,valuePosY+6, new Color(0,0,0).getRGB());
                    if (this.isHovered(posX + 8, valuePosY + 1, posX + 60, valuePosY + 4, mouseX, mouseY) && !mouseClicked && Mouse.isButtonDown(0)) optionDouble.set(Math.round((optionDouble.getMinimum() + (optionDouble.getMaximum() - optionDouble.getMinimum()) * Math.min(max, 1.0)) * 100.0) / 100.0);                }
                if (value instanceof FloatValue){
                    float posX = width / 2.0f + 35.0f;
                    final double max = Math.max(0.0, (mouseX - (posX + 8)) / 52.0);
                    FontLoaders.JelloList16.drawString(value.getName(),width / 2.0f - 99.0f,valuePosY,new Color(0,0,0).getRGB());
                    FloatValue optionDouble = (FloatValue)value;
                    optionDouble.getTranslate().interpolate((float) (52F * (optionDouble.get() > optionDouble.getMaximum() ? optionDouble.getMaximum() : optionDouble.get() < optionDouble.getMinimum() ? 0 : optionDouble.get() - optionDouble.getMinimum()) / (optionDouble.getMaximum() - optionDouble.getMinimum()) + 8), 0, 0.1);
                    RenderUtils.drawRect(posX + 8, valuePosY + 1, posX + 60, valuePosY + 3,  new Color(Colors.GREY.c).brighter().brighter().getRGB());
                    RenderUtils.drawRect(posX + 8, valuePosY + 1, (posX + optionDouble.getTranslate().getX()), valuePosY + 3, new Color(-14848033).brighter().getRGB());
                    RenderUtils.drawCircle((posX + optionDouble.getTranslate().getX()),  (valueY + 2.5F), 2.0F, new Color(-14848033).brighter().getRGB());
                    FontLoaders.Sans18.drawString(optionDouble.get().toString(), posX + optionDouble.getTranslate().getX()-2,valuePosY+6, new Color(0,0,0).getRGB());
                    if (this.isHovered(posX + 8, valuePosY + 1, posX + 60, valuePosY + 4, mouseX, mouseY) && !mouseClicked && Mouse.isButtonDown(0)) optionDouble.set(Math.round((optionDouble.getMinimum() + (optionDouble.getMaximum() - optionDouble.getMinimum()) * Math.min(max, 1.0)) * 100.0) / 100.0);
                }
                valueY+=20;
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
            float moduleHeight = valueY - translate.getY();
            if (Mouse.hasWheel() && isHovered(width / 2.0f - 100.0f, height / 2.0f - 130.0f, width / 2.0f + 100.0f, height / 2.0f + 130.0f,mouseX,mouseY)) {
                if (wheel > 0) {
                    if (mouseWheel<0) {
                        mouseWheel += 10;
                    }
                }
                if (wheel < 0) {
                    if (Math.abs(mouseWheel)<(moduleHeight-(height / 2.0f - 130.0f-16)-200)) {
                        mouseWheel -= 10;
                    }
                }
            }
            translate.interpolate(0, mouseWheel, 0.15F);
            hashMap.put(LiquidBounce.INSTANCE.getModule(),mouseWheel);
        }
    }
    
    public void start(){

        IScaledResolution sr = classProvider.createScaledResolution(MinecraftInstance.mc);
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);
        if (!this.close) {
            if (this.percent > 0.981D) {
                GlStateManager.translate((sr.getScaledWidth() / 2), (sr.getScaledHeight() / 2), 0.0F);
                GlStateManager.scale(this.percent, this.percent, 0.0F);
            } else {
                this.percent2 = smoothTrans(this.percent2, this.lastPercent2);
                GlStateManager.translate((sr.getScaledWidth() / 2), (sr.getScaledHeight() / 2), 0.0F);
                GlStateManager.scale(this.percent2, this.percent2, 0.0F);
            }
        } else {
            GlStateManager.translate((sr.getScaledWidth() / 2), (sr.getScaledHeight() / 2), 0.0F);
            GlStateManager.scale(this.percent, this.percent, 0.0F);
        }
        GlStateManager.translate((-sr.getScaledWidth() / 2), (-sr.getScaledHeight() / 2), 0.0F);

        if (this.percent <= 1.5D && this.close) {
            this.percent = smoothTrans(this.percent, 12.0D);
        }

        if (this.percent >= 1.4D && this.close) {
            mc2.currentScreen = null;
            mc2.mouseHelper.grabMouseCursor();
            mc2.inGameHasFocus = true;
        }
    }
    
    public void onGuiClosed() {
        SaveConfig();
        this.smooth.setOpacity(0.0F);
        try {
            mc2.entityRenderer.stopUseShader();
        } catch (Throwable e) {
        }
    }
    
    public void mouseReleased(int mouseX, int mouseY, int Button) {
        SaveConfig();
    }
    
    public void SaveMouseWheel() {
        File file = new File(LiquidBounce.fileManager.dir.getName() + "/Wheelgui.txt");
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            PrintWriter printWriter = new PrintWriter(file);
            for (Map.Entry<Module,Integer> set : hashMap.entrySet()) {
                printWriter.print(set.getKey().getName() +":"+set.getValue() + "\n");
            }
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void SaveConfig() {
        File file = new File(LiquidBounce.fileManager.dir.getName() + "/gui.txt");
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            PrintWriter printWriter = new PrintWriter(file);
            for (Category menu : categories) {
                printWriter.print(menu.moduleCategory.getDisplayName() + ":" + menu.x + ":" + menu.y +":"+menu.mouseWheel+":"+menu.slider + "\n");
            }
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void loadValue() {

    }
    
    public void Savevalue() {
        File file = new File(LiquidBounce.fileManager.dir.getName() + "/valuex.txt");
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            PrintWriter printWriter = new PrintWriter(file);
            String len = null;
            String values = "";
            for (Module m : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                for (Value v : m.getValues()) {
                    values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
                }
            }
            save("Valuesx.txt", values, false);
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public static void save(final String file, final String content, final boolean append) {
        try {
            final File f = new File(LiquidBounce.INSTANCE.getFileManager().dir.getName(), file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileWriter writer = new FileWriter(f, append);
                try {
                    writer.write(content);
                }
                finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadWheel(){
        File file = new File(LiquidBounce.fileManager.dir.getName()+ "/Wheelgui.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String len;
            String all = null;
            while ((len=bufferedReader.readLine())!=null) {
                String str = len;
                all+=str;
                if (LiquidBounce.INSTANCE.getModule()!=null) {
                    String name=str.toString().split(":")[0];
                    if (!name.equals(LiquidBounce.INSTANCE.getModule().getName())) {
                        continue;
                    }
                    if (name.equals(LiquidBounce.INSTANCE.getModule().getName())){
                        mouseWheel= Integer.parseInt(str.toString().split(":")[1]);
                        break;
                    }
                }
            }
            if (all!=null) {
                if (!all.contains(LiquidBounce.INSTANCE.getModule().getName())) {
                    mouseWheel=0;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadClickGui(){
        File file = new File(LiquidBounce.fileManager.dir.getName() + "/gui.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String len;
            while ((len=bufferedReader.readLine())!=null) {
                String str = len;
                String moduleCatrgory=str.toString().split(":")[0];
                for (Category menu : categories) {
                    if (moduleCatrgory.equals(menu.moduleCategory.getDisplayName())) {
                        int newx = Integer.parseInt(str.toString().split(":")[1]);
                        int newy = Integer.parseInt(str.toString().split(":")[2]);
                        int newwheel = Integer.parseInt(str.toString().split(":")[3]);
                        int newslider=Integer.parseInt(str.toString().split(":")[4]);
                        menu.x = newx;
                        menu.y = newy;
                        menu.mouseWheel = newwheel;
                        menu.slider=newslider;
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void initGui() {
        if (MinecraftInstance.mc.getTheWorld() != null) {
            ((EntityRenderer) mc2.entityRenderer).loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        super.initGui();
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;

    }
    
    public static float smoothTrans(double current, double last) {
        return (float) (current + (last - current) / (Minecraft.getDebugFPS() / 10));
    }
    
    public void drawShadow(float x, float y, float width, float height){
        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(Minecraft.getMinecraft());
        drawTexturedRect(x - 9, y - 9, 9, 9, "paneltopleft", sr);
        drawTexturedRect(x - 9, y +  height, 9, 9, "panelbottomleft", sr);
        drawTexturedRect(x + width, y +  height, 9, 9, "panelbottomright", sr);
        drawTexturedRect(x + width, y - 9, 9, 9, "paneltopright", sr);
        drawTexturedRect(x - 9, y, 9, height, "panelleft", sr);
        drawTexturedRect(x + width, y, 9, height, "panelright", sr);
        drawTexturedRect(x, y - 9, width, 9, "paneltop", sr);
        drawTexturedRect(x, y + height, width, 9, "panelbottom", sr);
    }
    
    public void drawTexturedRect(float x, float y, float width, float height, String image, net.minecraft.client.gui.ScaledResolution sr) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("liying/jello/"+image+".png"));
        Gui.drawModalRectWithCustomSizedTexture((int)x,  (int)y, 0, 0,(int)width, (int)height, width, height);
    }
}
