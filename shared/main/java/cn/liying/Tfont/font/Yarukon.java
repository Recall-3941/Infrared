//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\����2\MCP 1.8.9 (1)\MCP 1.8.9\mcp918"!

//Decompiled by Procyon!

package cn.liying.Tfont.font;

public class Yarukon
{
    public static Yarukon INSTANCE;
    public char[] chars;
    public char[] ascii_chars;
    
    public Yarukon() {
        this.chars = new char[65535];
        this.ascii_chars = new char[256];
        Yarukon.INSTANCE = this;
        for (int i = 0; i < this.chars.length; ++i) {
            this.chars[i] = (char)i;
        }
        for (int i = 0; i < this.ascii_chars.length; ++i) {
            this.ascii_chars[i] = (char)i;
        }
    }
}
