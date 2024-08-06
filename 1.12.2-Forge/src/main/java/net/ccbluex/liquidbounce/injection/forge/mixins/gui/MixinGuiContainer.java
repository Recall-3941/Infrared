package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends MixinGuiScreen implements IMixinGuiContainer {

    @Shadow
    protected int xSize;
    @Shadow
    protected int ySize;
    private float progress = 0.0f;
    private long lastMS = 0;


    @Shadow
    protected abstract void handleMouseClick(Slot p_handleMouseClick_1_, int p_handleMouseClick_2_, int p_handleMouseClick_3_, ClickType p_handleMouseClick_4_);


    @Inject(method = {"drawScreen"}, at = {@At("HEAD")},cancellable = true)
    protected void drawScreenHead(CallbackInfo callbackInfo) {
        ChestStealer stealer = (ChestStealer) LiquidBounce.moduleManager.getModule(ChestStealer.class);
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GuiScreen guiScreen = mc.currentScreen;
        if (stealer.getState() && stealer.getSlienceValue().get() && guiScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) guiScreen;
            if (!(stealer.getChestTitleValue().get() && (chest.lowerChestInventory == null || !chest.lowerChestInventory.getName().contains(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("minecraft:chest"))).getDisplayName())))) {
                // mouse focus
                mc.setIngameFocus();
                mc.currentScreen = guiScreen;
                // hide GUI
                if (stealer.getSilentTitleValue().get()) {
                    RenderUtils.drawConnectCircle(scaledResolution.getScaledWidth() / 2,scaledResolution.getScaledHeight()/2);
                    Fonts.minecraftFont.drawString("stealing...", width / 2 - 15, (height / 2) + 30, 0xffffffff, true);
                    //RendererExtensionKt.drawCenteredString((FontRenderer) Fonts.misans35, "stealing...", width / 2, (height / 2) + 30, 0xffffffff, true);
                    callbackInfo.cancel();
                }
            }
        } else {
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            } else {
                this.progress = ((float) (System.currentTimeMillis() - this.lastMS)) / 300.0f;
            }

            double trueAnim = EaseUtils.easeOutQuart(this.progress);

            switch (Animations.guiAnimations.get()) {
                case "Zoom":
                    GL11.glTranslated((1 - trueAnim) * (width / 2D), (1 - trueAnim) * (height / 2D), 0D);
                    GL11.glScaled(trueAnim, trueAnim, trueAnim);
                    break;
                case "HSlide":
                    GL11.glTranslated((1 - trueAnim) * -width, 0D, 0D);
                    break;
                case "VSlide":
                    GL11.glTranslated(0D, (1 - trueAnim) * -height, 0D);
                    break;
                case "HVSlide":
                    GL11.glTranslated((1 - trueAnim) * -width, (1 - trueAnim) * -height, 0D);
                    break;
            }
            RenderUtils.drawGradientSideways(0, 0, this.xSize, this.ySize, 0, 0);
            GL11.glPushMatrix();
        }
    }

    @Inject(method = {"drawScreen"}, at = {@At("RETURN")}, cancellable = true)
    protected void drawScreenReturn(CallbackInfo callbackInfo) {
        GL11.glPopMatrix();
    }

    @Override
    public void publicHandleMouseClick(Slot slot, int slotNumber, int clickedButton, ClickType clickType) {

        this.handleMouseClick(slot, slotNumber, clickedButton, clickType);
    }
}
