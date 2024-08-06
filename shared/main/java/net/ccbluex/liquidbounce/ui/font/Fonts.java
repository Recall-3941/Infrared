/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts extends MinecraftInstance {

    @FontDetails(fontName = "Minecraft Font")
    public static final IFontRenderer minecraftFont = mc.getFontRendererObj();
    private static final HashMap<FontInfo, IFontRenderer> CUSTOM_FONT_RENDERERS = new HashMap<>();


    @FontDetails(fontName = "notif", fontSize = 60)
    public static IFontRenderer n80;

    @FontDetails(fontName = "jello", fontSize = 14)
    public static IFontRenderer jello14;
    @FontDetails(fontName = "jello", fontSize = 18)
    public static IFontRenderer jello18;
    @FontDetails(fontName = "jello", fontSize = 35)
    public static IFontRenderer jello35;
    @FontDetails(fontName = "flux", fontSize = 35)
    public static IFontRenderer flux;

    @FontDetails(fontName = "flux", fontSize = 50)
    public static IFontRenderer flux50;
    @FontDetails(fontName = "flux", fontSize = 14)
    public static IFontRenderer FluxIcon14;
    @FontDetails(fontName = "flux", fontSize = 16)
    public static IFontRenderer flux16;
    @FontDetails(fontName = "Posteraaa", fontSize = 35)
    public static IFontRenderer Posterama35;

    @FontDetails(fontName = "comfortaa", fontSize = 14)
    public static IFontRenderer com14;
    @FontDetails(fontName = "comfortaa", fontSize = 18)
    public static IFontRenderer com18;
    @FontDetails(fontName = "comfortaa", fontSize = 35)
    public static IFontRenderer com35;
    @FontDetails(fontName = "comfortaa", fontSize = 40)
    public static IFontRenderer com40;
    @FontDetails(fontName = "comfortaa", fontSize = 30)
    public static IFontRenderer com30;
    @FontDetails(fontName = "comfortaa", fontSize = 96)
    public static IFontRenderer com96;
    @FontDetails(fontName = "comfortaa", fontSize = 120)
    public static IFontRenderer com120;
    @FontDetails(fontName = "MiSans", fontSize = 14)
    public static IFontRenderer font14;
    @FontDetails(fontName = "MiSans", fontSize = 18)
    public static IFontRenderer font18;

    @FontDetails(fontName = "MiSans", fontSize = 16)
    public static IFontRenderer font16;
    @FontDetails(fontName = "MiSans", fontSize = 20)
    public static IFontRenderer font20;
    @FontDetails(fontName = "MiSans", fontSize = 30)
    public static IFontRenderer font30;
    @FontDetails(fontName = "MiSans", fontSize = 35)
    public static IFontRenderer font35;
    @FontDetails(fontName = "MiSans", fontSize = 40)
    public static IFontRenderer font40;
    @FontDetails(fontName = "MiSans", fontSize = 128)
    public static IFontRenderer font128;
    @FontDetails(fontName = "Roboto Medium", fontSize = 25)
    public static IFontRenderer font25;
    @FontDetails(fontName = "Roboto Bold", fontSize = 180)
    public static IFontRenderer fontBold180;

    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts.");

        downloadFonts();

        n80 = classProvider.wrapFontRenderer(new GameFontRenderer(getDeobf(60)));
        com14 = classProvider.wrapFontRenderer(new GameFontRenderer(getCom(14)));
        com18 = classProvider.wrapFontRenderer(new GameFontRenderer(getCom(18)));
        com35 = classProvider.wrapFontRenderer(new GameFontRenderer(getCom(35)));

        com96 = classProvider.wrapFontRenderer(new GameFontRenderer(getCom(72)));
        com120 = classProvider.wrapFontRenderer(new GameFontRenderer(getCom(120)));

        com30 = classProvider.wrapFontRenderer(new GameFontRenderer(getCom(30)));
        com40 = classProvider.wrapFontRenderer(new GameFontRenderer(getCom(40)));
        jello14 = classProvider.wrapFontRenderer(new GameFontRenderer(getJello(14)));
        jello18 = classProvider.wrapFontRenderer(new GameFontRenderer(getJello(18)));
        jello35 = classProvider.wrapFontRenderer(new GameFontRenderer(getJello(35)));
        Posterama35 = classProvider.wrapFontRenderer(new GameFontRenderer(getPosterama(35)));
        FluxIcon14 = classProvider.wrapFontRenderer(new GameFontRenderer(getFlux(14)));
        flux16 = classProvider.wrapFontRenderer(new GameFontRenderer(getFlux(16)));
        flux = classProvider.wrapFontRenderer(new GameFontRenderer(getFlux(30)));

        flux50 = classProvider.wrapFontRenderer(new GameFontRenderer(getFlux(50)));
        font16 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 16)));
        font25 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(25)));
        font14 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 14)));
        font18 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 18)));

        font20 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 20)));
        font30 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 30)));
        font35 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 35)));
        font40 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 40)));

        font128 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("MiSans.ttf", 128)));

        fontBold180 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("Roboto-Bold.ttf", 180)));

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.fontsDir, "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    Font font = getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt());

                    CUSTOM_FONT_RENDERERS.put(new FontInfo(font), classProvider.wrapFontRenderer(new GameFontRenderer(font)));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }
    private static Font getSFUI(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("pride/font/sfuidisplayregular.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getPosterama(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("storyline/font/PosteramaText-Regular.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }

    private static void downloadFonts() {
        try {
            final File outputFile = new File(LiquidBounce.fileManager.fontsDir, "roboto.zip");

            if (!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                HttpUtils.download( "https://github.91chi.fun/https://github.com/xiguatianbutian/LiquidBounce-1.12/releases/download/FONT/roboto.zip", outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                extractZip(outputFile.getPath(), LiquidBounce.fileManager.fontsDir.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IFontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                Object o = field.get(null);

                if (o instanceof IFontRenderer) {
                    FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (IFontRenderer) o;
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return CUSTOM_FONT_RENDERERS.getOrDefault(new FontInfo(name, size), minecraftFont);
    }

    public static FontInfo getFontDetails(final IFontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<FontInfo, IFontRenderer> entry : CUSTOM_FONT_RENDERERS.entrySet()) {
            if (entry.getValue() == fontRenderer)
                return entry.getKey();
        }

        return null;
    }

    public static List<IFontRenderer> getFonts() {
        final List<IFontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof IFontRenderer) fonts.add((IFontRenderer) fontObj);
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS.values());

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }
    private static Font getFlux(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("loserline/font/fluxicon.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getCom(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("loserline/font/comfortaa.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getDeobf(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("loserline/font/nico.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getJello(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("loserline/font/jello.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }


    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if (!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return name;
        }

        public int getFontSize() {
            return fontSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FontInfo fontInfo = (FontInfo) o;

            if (fontSize != fontInfo.fontSize) return false;
            return Objects.equals(name, fontInfo.name);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + fontSize;
            return result;
        }
    }

}