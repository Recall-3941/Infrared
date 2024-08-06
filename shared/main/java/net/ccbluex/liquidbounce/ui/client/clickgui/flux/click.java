package net.ccbluex.liquidbounce.ui.client.clickgui.flux;

import cn.liying.utils.Opacity;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.Translate;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

    public class click extends WrappedGuiScreen {
    int startX;
    int startY;
    public ScaledResolution sr;
    public Minecraft mc = Minecraft.getMinecraft();
    private ModuleCategory modulecategory=ModuleCategory.COMBAT;
    boolean dragged;
    int mouseX2,mouseY2;
    boolean mouseClicked;
    private Translate translate = new Translate(0F,0F);
    public final Opacity smooth = new Opacity(0);
    public ArrayList<ModuleList> moduleLists=new ArrayList<>();
    public EmptyInputBox emptyInputBox=null;
    public boolean loading=true ;
    public int mouseWheel;
    boolean close;
    public float minY = -100;
    float barHeight=20;
    public float lastPercent;
    public float percent;
    public float percent2;
    public float outro;
    public float lastOutro;
    public float lastPercent2;
    public float animationPosition;
    public float ani=10;
    public Translate Trani = new Translate(0F,0F);
    @Override
    public void initGui() {
        loading=true;
        sr = new ScaledResolution(mc);
        float width = sr.getScaledWidth();
        float height = sr.getScaledHeight();
        startX = (int) (width / 2 - (150 / 2f));
        startY = (int)((height/2)-150);
        moduleLists.clear();
        for (ModuleCategory moduleCategory : ModuleCategory.values()){
            moduleLists.add(new ModuleList(moduleCategory));
            loadClickGui();
        }
        loadCategory();
        super.initGui();
    }

    public static float smoothTrans(double current, double last) {
        return (float) (current + (last - current) / (Minecraft.getDebugFPS() / 10));
    }
    HashMap<Module, Integer> hashMap=new HashMap<Module, Integer>();

    public void drawScreen(int mouseX, int mouseY, float p_drawScreen_3_) {
                if (dragged) {
                    startX = mouseX2 + mouseX;
                    startY = mouseY2 + mouseY;
                }
                if (isHovered(startX-160,startY-24,startX+355,startY+36, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    dragged = true;
                    mouseX2 = (int) (startX - mouseX);
                    mouseY2 = (int) (startY - mouseY);
                } else {
                    dragged = false;
                }
        float w=0;
        minY=280;
        RenderUtils.drawRoundRect(startX-160,startY-24,startX+355,startY+320,new Color(255,160,0).getRGB());
        RenderUtils.drawRoundRect(startX-160,startY-20,startX+355,startY+320,new Color(35,34,41).getRGB());
        int modulecategorX=startX-130;
       //Fonts.flux50.drawStringWithShadow("q",startX-145,startY-10,-1);
        //Fonts.com40.drawString("Loserline",startX-140,startY-1,-1);
        for (int i=0;i<=9;i++){
            if (ModuleCategory.values()[i]==modulecategory) {
                Fonts.font35.drawStringWithShadow(ModuleCategory.values()[i].getDisplayName(), modulecategorX, startY + 4, new Color(255,255,255).getRGB());
                Fonts.flux.drawStringWithShadow(ModuleCategory.values()[i].getIcon(), modulecategorX - 10, startY + 5, new Color(255,255,255).getRGB());
            }else{
                Fonts.font35.drawStringWithShadow(ModuleCategory.values()[i].getDisplayName(), modulecategorX, startY + 4, new Color(153, 153, 153).getRGB());
                Fonts.flux.drawStringWithShadow(ModuleCategory.values()[i].getIcon(), modulecategorX - 10, startY + 5, new Color(153, 153, 153).getRGB());
            }
            if (isHovered(modulecategorX, startY + 4,modulecategorX+Fonts.font35.getStringWidth(ModuleCategory.values()[i].getDisplayName()),startY+13,mouseX,mouseY)){
                if (Mouse.isButtonDown(0)) {
                    if (!mouseClicked)
                    modulecategory= ModuleCategory.values()[i];
                    mouseClicked = true;
                } else mouseClicked = false;
            }
            modulecategorX+=25+ Fonts.font35.getStringWidth(ModuleCategory.values()[i].getDisplayName());
        }
        RenderUtils.drawRect(startX-160,startY+35,startX+355,startY+36,new Color(53,53,59).getRGB());
        RenderUtils.drawRect(startX-50,startY+36,startX-49,startY+320,new Color(53,53,59).getRGB());
        int wheel=Mouse.getDWheel();
        for (ModuleList moduleList : moduleLists){
            if (moduleList.modulecategory == modulecategory) {
                moduleList.draw(startX, startY, mouseX, mouseY);
                moduleList.setMouseWheel(wheel);
            }
        }
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.doGlScissor(startX-160,startY+35,startX+300,285);
        if (LiquidBounce.INSTANCE.getModule()!=null && LiquidBounce.INSTANCE.getModule().getLoading()){
            loadWheel();
            LiquidBounce.INSTANCE.getModule().setLoading(false);
        }
        if (LiquidBounce.INSTANCE.getModule()!=null && LiquidBounce.INSTANCE.getModule().getSave() && !LiquidBounce.INSTANCE.getModule().getLoading()){
            SaveMouseWheel();
            LiquidBounce.INSTANCE.getModule().setSave(false);
        }
        if (LiquidBounce.INSTANCE.getModule()!=null && LiquidBounce.INSTANCE.getModule().getStart()){

            LiquidBounce.INSTANCE.getModule().setStart(false);
        }
        if (LiquidBounce.INSTANCE.getModule()!=null && !LiquidBounce.INSTANCE.getModule().getStart()){
         //   this.animationPosition = 75;
        }
        if (LiquidBounce.INSTANCE.getModule()!=null){
            Module module = LiquidBounce.INSTANCE.getModule();
            float valueY=startY+55+translate.getY();
            Fonts.font30.drawStringWithShadow(module.getName(),startX-41, (int) (valueY-15),-1);
            for (Value value : module.getValues()){
                value.getValueTranslate().interpolate(0, valueY, 0.1);
                float valuePosY = value.getValueTranslate().getY();
                if (value instanceof BoolValue){
                    RenderUtils.drawBorderedRect(startX-40,valuePosY,startX-32,valuePosY+8,1.5f,new Color(185,184,190).getRGB(),new Color(185,184,190).getRGB());
                    Fonts.font30.drawStringWithShadow(value.getName(),startX-26, (int) (valuePosY+1),new Color(185,184,190).getRGB());
                    if (((BoolValue) value).get()){
                        Fonts.flux.drawString("v",startX-40,valuePosY+3,new Color(57,57,59).getRGB());
                    }
                    if (isHovered(startX-40,valuePosY,startX-31,valuePosY+8,mouseX,mouseY)){
                        if (Mouse.isButtonDown(0)) {
                            if (!mouseClicked)
                                ((BoolValue) value).toggle();
                            mouseClicked = true;
                        } else mouseClicked = false;
                    }
                    w+=15;
                    valueY+=15;
                }
                if (value instanceof TextValue){
                    if (loading) {
                        ((TextValue) value).setEmptyInputBox(new EmptyInputBox(4, mc.fontRenderer, startX-42, 150, startX+300, 8, new Color(145,145,145),Fonts.font30));
                        loading=false;
                    }
                    emptyInputBox=Objects.requireNonNull(((TextValue) value).getEmptyInputBox());
                    emptyInputBox.xPosition=startX-42;
                    emptyInputBox.yPosition= (int) (valuePosY+20);
                    Fonts.font30.drawStringWithShadow(value.getName(),startX-41, (int) (valuePosY+6),new Color(185,184,190).getRGB());
                    RenderUtils.drawRoundRect(startX-42,valuePosY+14,startX+266,valuePosY+31,new Color(58,58,58).getRGB());
                    RenderUtils.drawRoundRect(startX-41,valuePosY+15,startX+265,valuePosY+30,new Color(22,22,22).getRGB());
                    if (!emptyInputBox.isFocused()){
                        emptyInputBox.setText(((TextValue) value).get());
                    }else{
                        value.set(emptyInputBox.getText());
                    }
                    emptyInputBox.drawTextBox();
                    w+=35;
                    valueY+=35;
                }
                if (value instanceof ListValue){
                    Fonts.font30.drawStringWithShadow(value.getName(),startX-41, (int) (valuePosY+7),new Color(185,184,190).getRGB());
                    RenderUtils.drawRoundRect(startX-42,valuePosY+14,startX+266,valuePosY+31,new Color(49,49,49).getRGB());
                    RenderUtils.drawRoundRect(startX-41,valuePosY+15,startX+265,valuePosY+30,new Color(22,22,22).getRGB());
                    Fonts.font30.drawStringWithShadow(((ListValue) value).get(),startX-38, (int) (valuePosY+20),new Color(200,200,200).getRGB());
                    drawAndRotateArrow(startX + 255 - 5.0f, valuePosY+21 - 0.5f, 6.0f,((ListValue) value).openList);
                    if (isHovered(startX-41,valuePosY+15,startX+265,valuePosY+30,mouseX,mouseY)){
                        if (Mouse.isButtonDown(1)) {
                            if (!mouseClicked)
                                ((ListValue) value).openList=!((ListValue) value).openList;

                            mouseClicked = true;
                        } else mouseClicked = false;
                    }

                    if (((ListValue) value).openList){
                        if (((ListValue) value).getOpen()){
                            ((ListValue) value).setAnim(mouseWheel);
                            ((ListValue) value).setOpen(false);
                        }
                        float valueBoxHeight = valuePosY+32;
                        for (String values : ((ListValue) value).getValues()) {
                            if (values != ((ListValue) value).get()) {
                                RenderUtils.drawRect(startX - 42, valueBoxHeight, startX + 265, valueBoxHeight + 20, new Color(22, 22, 22).getRGB());
                                Fonts.font30.drawStringWithShadow(values, startX - 38, (int) (valueBoxHeight + 8), new Color(200, 200, 200).getRGB());
                                if (isHovered(startX - 42, valueBoxHeight, startX + 265, valueBoxHeight + 20, mouseX, mouseY)) {
                                    RenderUtils.drawRect(startX - 42, valueBoxHeight, startX + 265, valueBoxHeight + 20, new Color(255, 255, 255, 100).getRGB());
                                }
                                if (isHovered(startX - 42, valueBoxHeight, startX + 265, valueBoxHeight + 20, mouseX, mouseY)) {
                                    if (Mouse.isButtonDown(0)) {
                                        if (!mouseClicked)
                                            value.set(values);
                                        mouseWheel = ((ListValue) value).getAnim();
                                        ((ListValue) value).setOpen(true);
                                        ((ListValue) value).openList = false;
                                        mouseClicked = true;
                                    } else mouseClicked = false;
                                }
                                w += 20;
                                valueY += 20;
                                valueBoxHeight += 20;
                            }
                        }
                    }
                    w+=35;
                    valueY+=35;
                }
                if (value instanceof IntegerValue){
                    float posX = startX-50;
                    final double max = Math.max(0.0, (mouseX - (posX + 8)) / 300.0);
                    IntegerValue optionInt = (IntegerValue) value;
                    Fonts.font30.drawString(optionInt.getName()+":",startX-42, valuePosY, new Color(160,160,160).getRGB());
                    optionInt.getTranslate().interpolate((300F * (optionInt.get() > optionInt.getMaximum() ? optionInt.getMaximum() : optionInt.get() < optionInt.getMinimum() ? 0 : optionInt.get() - optionInt.getMinimum()) / (optionInt.getMaximum() - optionInt.getMinimum()) + 8), 0, 0.1);
                    RenderUtils.drawRoundedRect(startX-42, valuePosY+11, 307, 7,2f,(new Color(45,45,45)).getRGB(),.5f,(new Color(45,45,45)).getRGB());
                    RenderUtils.drawRoundedRect(posX+9, valuePosY + 11, (optionInt.getTranslate().getX()-2), 7,2f, (new Color(255,255,255)).getRGB(),.5F,(new Color(255,255,255)).getRGB());
                    RenderUtils.drawRoundedRect((posX + optionInt.getTranslate().getX()+1),  (valuePosY + 8F), 6,13F,3f, new Color(255,255,255).getRGB(),.5f, new Color(255,255,255).getRGB());

                    Fonts.font30.drawString(optionInt.get().toString(), startX-40+Fonts.font30.getStringWidth(optionInt.getName()+":"), valuePosY, new Color(255,255,255).getRGB());
                    if (this.isHovered(posX + 8, valuePosY + 9, posX + 308, valuePosY + 16, mouseX, mouseY) && Mouse.isButtonDown(0))
                        optionInt.set(Math.toIntExact(Math.round(optionInt.getMinimum() + (optionInt.getMaximum() - optionInt.getMinimum()) * Math.min(max, 1.0))));
                    valueY+=25;
                    w+=25;
                }
                if (value instanceof FloatValue){
                    float posX = startX-50;
                    final double max = Math.max(0.0, (mouseX - (posX + 8)) / 305.0);
                    FloatValue optionInt = (FloatValue) value;
                    Fonts.font30.drawString(optionInt.getName()+":",startX-42, valuePosY, new Color(160,160,160).getRGB());
                    optionInt.getTranslate().interpolate((305F * (optionInt.get() > optionInt.getMaximum() ? optionInt.getMaximum() : optionInt.get() < optionInt.getMinimum() ? 0 : optionInt.get() - optionInt.getMinimum()) / (optionInt.getMaximum() - optionInt.getMinimum()) + 8), 0, 0.1);
                    RenderUtils.drawRoundedRect(startX-42, valuePosY+11, 307, 7,2f,(new Color(45,45,45)).getRGB(),.5f,(new Color(45,45,45)).getRGB());
                    RenderUtils.drawRoundedRect(posX+9, valuePosY + 11, (optionInt.getTranslate().getX()-2), 7,2f, (new Color(255,255,255)).getRGB(),.5F,(new Color(255,255,255)).getRGB());
                    RenderUtils.drawRoundedRect((posX + optionInt.getTranslate().getX()+1),  (valuePosY + 8F), 6,13F,3f, new Color(255,255,255).getRGB(),.5f, new Color(255,255,255).getRGB());
                    Fonts.font30.drawString(optionInt.get().toString(), startX-40+Fonts.font30.getStringWidth(optionInt.getName()+":"), valuePosY, new Color(255,255,255).getRGB());
                    if (this.isHovered(posX + 8, valuePosY + 9, posX + 313, valuePosY + 16,  mouseX, mouseY) && Mouse.isButtonDown(0) )
                        optionInt.set(Math.round((optionInt.getMinimum() + (optionInt.getMaximum() - optionInt.getMinimum()) * Math.min(max, 1.0)) * 100.0) / 100.0);
                    valueY+=25;
                    w+=25;
                }
            };
            float moduleHeight = valueY  - translate.getY();
            if (Mouse.hasWheel() && isHovered(startX-50,startY+36,startX+355,startY+320,mouseX,mouseY)) {
                if (wheel > 0 && mouseWheel<0) {
                    mouseWheel += 40;
                }
                if (wheel < 0 && Math.abs(mouseWheel) < (moduleHeight)-(startY - 15)-335) {
                    mouseWheel -= 40;
                }
            }

            minY -= w;
            if (((moduleHeight)-(startY - 15)-335)>0) {
                float viewable = 281;
                float progress = Math.min(translate.getY() / this.minY, 1);
                float ratio = (viewable / w) * viewable;//获取x位置
                this.barHeight = Math.max(ratio, 20f);
                float position = progress * (viewable - barHeight);//获取x2位置
                RenderUtils.drawRect(startX+354, translate.getY() + 37 + .5f, startX+355, this.startY + 37 + 285 - .5f, 0xff2d2d2d);
                RenderUtils.drawRect(startX+354, this.startY + 37 + position, startX+355, (this.startY + 37 + position + barHeight), new Color(255, 160, 0).getRGB());
            }
            translate.interpolate(0, mouseWheel, 0.8F);
            hashMap.put(LiquidBounce.INSTANCE.getModule(), mouseWheel);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }
    GuiScreen GuiScreen;

    public static void drawAndRotateArrow(float x, float y, float size, boolean rotate) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glBegin(4);

        if (rotate) {
            GL11.glVertex2f(size, (size / 2.0f));
            GL11.glVertex2f((size / 2.0f), 0.0f);
            GL11.glVertex2f(0.0f, (size / 2.0f));
        } else {
            GL11.glVertex2f(0.0f, 0.0f);
            GL11.glVertex2f((size / 2.0f), (size / 2.0f));
            GL11.glVertex2f(size, 0.0f);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public void drawGradientSideways(float left, float top, float right, float bottom, int startColor, int endColor) {
        int zLevel = 0;
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        ITessellator tessellator = (ITessellator) Tessellator.getInstance();
        IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, (IVertexFormat) DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(right, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case Keyboard.KEY_ESCAPE:
                mc.displayGuiScreen(GuiScreen);
                return;
        }
        if (emptyInputBox!=null) {
            if (emptyInputBox.isFocused()) {
                emptyInputBox.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (emptyInputBox!=null) {
            emptyInputBox.mouseClicked(mouseX, mouseY, mouseButton);
        }
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
        Minecraft.getMinecraft().getTextureManager().bindTexture((ResourceLocation) classProvider.createResourceLocation(("liying/"+image+".png")));
        Gui.drawModalRectWithCustomSizedTexture((int)x,  (int)y, 0, 0,(int)width, (int)height, width, height);
    }

    public void SaveConfig() {
        File file = new File(LiquidBounce.fileManager.ClickguiDir + "/gui.txt");
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            PrintWriter printWriter = new PrintWriter(file);
            for (ModuleList menu : moduleLists) {
                printWriter.print(menu.modulecategory.getDisplayName() + ":" + menu.startX + ":" + menu.startY +":"+menu.mouseWheel + "\n");
            }
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SaveCategory() {
        String name="";
        File file = new File(LiquidBounce.fileManager.ClickguiDir + "/Categorygui.txt");
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            PrintWriter printWriter = new PrintWriter(file);
            if (LiquidBounce.INSTANCE.getModule()!=null){
                name=":"+LiquidBounce.INSTANCE.getModule().getName();
            }else{
                name="";
            }
            printWriter.print(modulecategory.getDisplayName()+":"+startX+":"+startY+name+"\n");
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadCategory() {
        File file = new File(LiquidBounce.fileManager.ClickguiDir + "/Categorygui.txt");
            try {
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String len;
                String name1 = "";
                while ((len = bufferedReader.readLine()) != null) {
                    String str = len;
                    String name = str.toString().split(":")[0];
                    if (str.toString().split(":").length > 3) {
                        name1 = str.toString().split(":")[3];
                    }
                    for (ModuleCategory modulecategory : ModuleCategory.values()) {
                        if (Objects.equals(modulecategory.getDisplayName(), name)) {
                            this.modulecategory = modulecategory;
                        }
                    }
                    for (Module module : LiquidBounce.moduleManager.getModules()) {
                        if (Objects.equals(module.getName(), name1)) {
                            LiquidBounce.INSTANCE.setModule(module);
                        }
                    }
                    this.startX = Integer.parseInt(str.toString().split(":")[1]);
                    this.startY = Integer.parseInt(str.toString().split(":")[2]);
                }
                bufferedReader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveMouseWheel() {
        File file = new File(LiquidBounce.fileManager.ClickguiDir + "/Wheelgui.txt");
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

    public void loadClickGui(){
        File file = new File(LiquidBounce.fileManager.ClickguiDir + "/gui.txt");
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String len;
            while ((len=bufferedReader.readLine())!=null) {
                String str = len;
                String moduleCatrgory=str.toString().split(":")[0];
                for (ModuleList menu : moduleLists) {
                    if (moduleCatrgory.equals(menu.modulecategory.getDisplayName())) {
                        int newx = Integer.parseInt(str.toString().split(":")[1]);
                        int newy = Integer.parseInt(str.toString().split(":")[2]);
                        int newwheel = Integer.parseInt(str.toString().split(":")[3]);
                        menu.startX = newx;
                        menu.startY = newy;
                        menu.mouseWheel = newwheel;
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
        SaveConfig();
        SaveCategory();
        SaveMouseWheel();
        this.smooth.setOpacity(0.0F);
    }

    public void loadWheel(){
        File file = new File(LiquidBounce.fileManager.ClickguiDir + "/Wheelgui.txt");
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
                        mouseWheel = Integer.parseInt(str.toString().split(":")[1]);
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

    public void start(){

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);
        if (!this.close) {
            if (this.percent > 0.981D) {
                GlStateManager.translate((scaledResolution.getScaledWidth() / 2), (scaledResolution.getScaledHeight() / 2), 0.0F);
                GlStateManager.scale(this.percent, this.percent, 0.0F);
            } else {
                this.percent2 = smoothTrans(this.percent2, this.lastPercent2);
                GlStateManager.translate((scaledResolution.getScaledWidth() / 2), (scaledResolution.getScaledHeight() / 2), 0.0F);
                GlStateManager.scale(this.percent2, this.percent2, 0.0F);
            }
        } else {
            GlStateManager.translate((scaledResolution.getScaledWidth() / 2), (scaledResolution.getScaledHeight() / 2), 0.0F);
            GlStateManager.scale(this.percent, this.percent, 0.0F);
        }
        GlStateManager.translate((-scaledResolution.getScaledWidth() / 2), (-scaledResolution.getScaledHeight() / 2), 0.0F);

        if (this.percent <= 1.5D && this.close) {
            this.percent = smoothTrans(this.percent, 12.0D);
        }

        if (this.percent >= 1.4D && this.close) {
            this.mc.currentScreen = null;
            this.mc.mouseHelper.grabMouseCursor();
            this.mc.inGameHasFocus = true;
        }
    }
}
