//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\����2\MCP 1.8.9 (1)\MCP 1.8.9\mcp918"!

//Decompiled by Procyon!

package cn.liying.Tfont.font;

import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Locale;

public class GlyphPageFontRenderer
{
    private float posX;
    private float posY;
    private final int[] colorCode;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    private final GlyphPage regularGlyphPage;
    private final GlyphPage boldGlyphPage;
    private final GlyphPage italicGlyphPage;
    private final GlyphPage boldItalicGlyphPage;
    
    public GlyphPageFontRenderer(final GlyphPage regularGlyphPage, final GlyphPage boldGlyphPage, final GlyphPage italicGlyphPage, final GlyphPage boldItalicGlyphPage) {
        this.colorCode = new int[32];
        this.regularGlyphPage = regularGlyphPage;
        this.boldGlyphPage = boldGlyphPage;
        this.italicGlyphPage = italicGlyphPage;
        this.boldItalicGlyphPage = boldItalicGlyphPage;
        for (int i = 0; i < 32; ++i) {
            final int j = (i >> 3 & 0x1) * 85;
            int k = (i >> 2 & 0x1) * 170 + j;
            int l = (i >> 1 & 0x1) * 170 + j;
            int i2 = (i & 0x1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i2 /= 4;
            }
            this.colorCode[i] = ((k & 0xFF) << 16 | (l & 0xFF) << 8 | (i2 & 0xFF));
        }
    }
    
    public static GlyphPageFontRenderer create(final Font font, final boolean allChars) {
        final GlyphPage regularPage = new GlyphPage(font, true, true);
        regularPage.generateGlyphPage(allChars ? Yarukon.INSTANCE.chars : Yarukon.INSTANCE.ascii_chars, allChars);
        return new GlyphPageFontRenderer(regularPage, regularPage, regularPage, regularPage);
    }
    
    public int drawCenteredString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color, false);
    }
    
    public int drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color, true);
    }
    
    public int drawStringWithShadow(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x, y, color, true);
    }
    
    public int drawString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x, y, color, false);
    }
    
    public int drawString(final String text, final float x, final float y, final int color, final boolean dropShadow) {
        GlStateManager.enableAlpha();
        this.resetStyles();
        int i;
        if (dropShadow) {
            i = this.renderString(text, x + 1.0f, y + 1.0f, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        }
        else {
            i = this.renderString(text, x, y, color, false);
        }
        return i;
    }
    
    private int renderString(final String text, final float x, final float y, int color, final boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        this.red = (color >> 16 & 0xFF) / 255.0f;
        this.blue = (color >> 8 & 0xFF) / 255.0f;
        this.green = (color & 0xFF) / 255.0f;
        this.alpha = (color >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(this.red, this.blue, this.green, this.alpha);
        this.posX = x * 2.0f;
        this.posY = y * 2.0f;
        this.renderStringAtPos(text, dropShadow);
        return (int)(this.posX / 4.0f);
    }
    
    private void renderStringAtPos(final String text, final boolean shadow) {
        GlyphPage glyphPage = this.getCurrentGlyphPage();
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableTexture2D();
        glyphPage.bindTexture();
        GL11.glTexParameteri(3553, 10240, 9729);
        for (int i = 0; i < text.length(); ++i) {
            final char c0 = text.charAt(i);
            if (c0 == '1' && i + 1 < text.length()) {
                int i2 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i2 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (i2 < 0) {
                        i2 = 15;
                    }
                    if (shadow) {
                        i2 += 16;
                    }
                    final int j1 = this.colorCode[i2];
                    this.textColor = j1;
                    GlStateManager.color((j1 >> 16) / 255.0f, (j1 >> 8 & 0xFF) / 255.0f, (j1 & 0xFF) / 255.0f, this.alpha);
                }
                else if (i2 == 16) {
                    this.randomStyle = true;
                }
                else if (i2 == 17) {
                    this.boldStyle = true;
                }
                else if (i2 == 18) {
                    this.strikethroughStyle = true;
                }
                else if (i2 == 19) {
                    this.underlineStyle = true;
                }
                else if (i2 == 20) {
                    this.italicStyle = true;
                }
                else {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
            }
            else {
                glyphPage = this.getCurrentGlyphPage();
                glyphPage.bindTexture();
                final float f = glyphPage.drawChar(c0, this.posX, this.posY);
                this.doDraw(f, glyphPage);
            }
        }
        glyphPage.unbindTexture();
        GL11.glPopMatrix();
    }
    
    private void doDraw(final float f, final GlyphPage glyphPage) {
        if (this.strikethroughStyle) {
            final ITessellator tessellator = (ITessellator) Tessellator.getInstance();
            final IWorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, (IVertexFormat) DefaultVertexFormats.POSITION);
            worldrenderer.pos((double)this.posX, (double)(this.posY + glyphPage.getMaxFontHeight() / 2), 0.0).endVertex();
            worldrenderer.pos((double)(this.posX + f), (double)(this.posY + glyphPage.getMaxFontHeight() / 2), 0.0).endVertex();
            worldrenderer.pos((double)(this.posX + f), (double)(this.posY + glyphPage.getMaxFontHeight() / 2 - 1.0f), 0.0).endVertex();
            worldrenderer.pos((double)this.posX, (double)(this.posY + glyphPage.getMaxFontHeight() / 2 - 1.0f), 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        if (this.underlineStyle) {
            final ITessellator tessellator2 = (ITessellator) Tessellator.getInstance();
            final IWorldRenderer worldrenderer2 = tessellator2.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer2.begin(7, (IVertexFormat) DefaultVertexFormats.POSITION);
            final int l = this.underlineStyle ? -1 : 0;
            worldrenderer2.pos((double)(this.posX + l), (double)(this.posY + glyphPage.getMaxFontHeight()), 0.0).endVertex();
            worldrenderer2.pos((double)(this.posX + f), (double)(this.posY + glyphPage.getMaxFontHeight()), 0.0).endVertex();
            worldrenderer2.pos((double)(this.posX + f), (double)(this.posY + glyphPage.getMaxFontHeight() - 1.0f), 0.0).endVertex();
            worldrenderer2.pos((double)(this.posX + l), (double)(this.posY + glyphPage.getMaxFontHeight() - 1.0f), 0.0).endVertex();
            tessellator2.draw();
            GlStateManager.enableTexture2D();
        }
        this.posX += f;
    }
    
    private GlyphPage getCurrentGlyphPage() {
        if (this.boldStyle && this.italicStyle) {
            return this.boldItalicGlyphPage;
        }
        if (this.boldStyle) {
            return this.boldGlyphPage;
        }
        if (this.italicStyle) {
            return this.italicGlyphPage;
        }
        return this.regularGlyphPage;
    }
    
    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }
    
    public int getFontHeight() {
        return this.regularGlyphPage.getMaxFontHeight() / 2;
    }
    
    public int getStringWidth(final String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        final int size = text.length();
        boolean on = false;
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '1') {
                on = true;
            }
            else if (on && character >= '0' && character <= 'r') {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                else if (colorIndex == 17) {
                    this.boldStyle = true;
                }
                else if (colorIndex == 20) {
                    this.italicStyle = true;
                }
                else if (colorIndex == 21) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                ++i;
                on = false;
            }
            else {
                if (on) {
                    --i;
                }
                character = text.charAt(i);
                final GlyphPage currentPage = this.getCurrentGlyphPage();
                width += (int)(currentPage.getWidth(character) - 8.0f);
            }
        }
        return width / 2;
    }
    
    public String trimStringToWidth(final String text, final int width) {
        return this.trimStringToWidth(text, width, false);
    }
    
    public String trimStringToWidth(final String text, final int maxWidth, final boolean reverse) {
        final StringBuilder stringbuilder = new StringBuilder();
        boolean on = false;
        final int j = reverse ? (text.length() - 1) : 0;
        final int k = reverse ? -1 : 1;
        int width = 0;
        for (int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            char character = text.charAt(i);
            if (character == '1') {
                on = true;
            }
            else if (on && character >= '0' && character <= 'r') {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                else if (colorIndex == 17) {
                    this.boldStyle = true;
                }
                else if (colorIndex == 20) {
                    this.italicStyle = true;
                }
                else if (colorIndex == 21) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                ++i;
                on = false;
            }
            else {
                if (on) {
                    --i;
                }
                character = text.charAt(i);
                final GlyphPage currentPage = this.getCurrentGlyphPage();
                width += (int)((currentPage.getWidth(character) - 8.0f) / 2.0f);
            }
            if (i > width) {
                break;
            }
            if (reverse) {
                stringbuilder.insert(0, character);
            }
            else {
                stringbuilder.append(character);
            }
        }
        return stringbuilder.toString();
    }
}
