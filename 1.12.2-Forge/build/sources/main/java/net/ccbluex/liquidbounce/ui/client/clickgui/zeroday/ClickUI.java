package net.ccbluex.liquidbounce.ui.client.clickgui.zeroday;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import org.lwjgl.input.Mouse;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ClickUI extends WrappedGuiScreen {

    ArrayList windows = new ArrayList();
    private final IFontRenderer TitleFont;

    public ClickUI() {
        this.TitleFont = Fonts.font40;
        float x = 60.0F;
        float y = 40.0F;

        for (int e = 0; e < 8; ++e) {
            ModuleCategory line = ModuleCategory.values()[e];

            this.windows.add(new Window(line, x, y));
            x += 135.0F;
        }

        try {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(LiquidBounce.INSTANCE.getFileManager().ClickguiDir + "\\clickgui.txt")));

            String s;

            while ((s = bufferedreader.readLine()) != null) {

                for (Object o : this.windows) {
                    Window window = (Window) o;

                    if (s.split(":")[0].equals(window.moduleCategory.getDisplayName())) {
                        window.x = Float.parseFloat(s.split(":")[1]);
                        window.y = Float.parseFloat(s.split(":")[2]);
                        window.mouseWheel = (int) Float.parseFloat(s.split(":")[3]);

                        Button b4;

                        for (Iterator iterator1 = window.buttons.iterator(); iterator1.hasNext(); b4.y = window.y) {
                            b4 = (Button) iterator1.next();
                            b4.x = window.x;
                        }
                    }
                }
            }

            bufferedreader.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int tick = 1;
        int wheel = Mouse.getDWheel();

        for (Iterator iterator = this.windows.iterator(); iterator.hasNext(); tick += 2) {
            Window window = (Window) iterator.next();

            window.setWheel(wheel);
            window.render(tick, mouseX, mouseY);
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Iterator iterator = this.windows.iterator();

        while (iterator.hasNext()) {
            Window window = (Window) iterator.next();

            window.click(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void onGuiClosed() {
        try {
            BufferedWriter e = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LiquidBounce.INSTANCE.getFileManager().ClickguiDir + "\\clickgui.txt")));
            Iterator iterator = this.windows.iterator();

            while (iterator.hasNext()) {
                Window window = (Window) iterator.next();

                e.write(window.moduleCategory.getDisplayName() + ":" + window.x + ":" + window.y + ":" + window.mouseWheel);
                e.newLine();
                e.flush();
            }

            e.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        super.onGuiClosed();
    }
}
