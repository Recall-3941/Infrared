package net.ccbluex.liquidbounce.ui.client.clickgui.zeroday;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

import java.awt.*;

public class KeyBindButton {

    int key;
    String keyName;
    public float x;
    public float y;

    public KeyBindButton(int key, String keyName, float x, float y) {
        this.key = key;
        this.keyName = keyName;
        this.x = x;
        this.y = y;
    }

    public void render() {
        RenderUtils.drawRect(this.x + 5.0F, this.y - 5.0F, this.x + 120.0F, this.y + 15.0F, new Color(46, 46, 46));
        RenderUtils.drawRect(this.x + 106.0F, this.y, this.x + 116.0F, this.y + 10.0F, (new Color(56, 56, 56)).getRGB());
        Fonts.font20.drawStringWithShadow("KeyBind", (int) (this.x + 8.0F), (int) ((double) this.y + 2.5D), -1);
        Fonts.font20.drawStringWithShadow(this.keyName, (int) (this.x + 108.0F), (int) ((double) this.y + 2.5D), -1);
    }
}
