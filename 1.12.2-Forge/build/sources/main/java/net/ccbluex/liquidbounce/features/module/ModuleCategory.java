package net.ccbluex.liquidbounce.features.module;

import cn.liying.utils.Drag;
import cn.liying.utils.Main;
import cn.liying.utils.Scroll;

public enum ModuleCategory {

    COMBAT("Combat","a"),
    PLAYER("Player","d"),
    MOVEMENT("Movement","b"),
    RENDER("Render","c"),
    WORLD("World","e"),
    MISC("Misc","c"),
    EXPLOIT("Exploit","a"),
    LEGIT("Legit","q"),
    HYT("Hyt","a"),
    GUI("Gui","c"),
    JS("JS","a");//此分类为将远古时期的JavaScript转为此项，但基本没什么用
    public final String namee;
    public final String icon;
    public final String displayName;
    public final int posX;
    public final boolean expanded;
    private final int colour = 0;

    public int posY = 20;
    public final boolean canSeeInTab = false;
    private final Scroll scroll = new Scroll();
    private final Drag drag;
    ModuleCategory(String a1,String a) {
        namee = a1;
        icon = a;
        posX = 40 + (Main.categoryCount * 120);
        drag = new Drag(posX, posY);
        displayName = namee;
        expanded = true;
    }
    public int getColour() {
        return colour;
    }
    public Drag getDrag() {
        return drag;
    }
    public Scroll getScroll() {
        return scroll;
    }
    public Scroll setMaxScroll() {
        return scroll;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getIcon() {
        return icon;
    }
}
