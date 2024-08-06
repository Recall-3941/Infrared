/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.packets;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.PostDisabler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SPacketConfirmTransaction.class)
public class MixinS32PacketConfirmTransaction {

    @Shadow
    private int windowId;
    @Shadow
    private short actionNumber;
    @Shadow
    private boolean accepted;

    /**
     * @author genshin
     */

    @Overwrite
    public void readPacketData(PacketBuffer buf) {
        PostDisabler disabler2 = (PostDisabler) LiquidBounce.moduleManager.getModule(PostDisabler.class);
        this.windowId = buf.readUnsignedByte();
        this.actionNumber = buf.readShort();
        this.accepted = buf.readBoolean();
        if (disabler2.getGrimPost() && this.actionNumber < 0) {
            PostDisabler.getPingPackets().add((int)this.actionNumber);
        }
    }
}