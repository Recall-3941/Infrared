/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.modules.misc.PostDisabler;
import net.ccbluex.liquidbounce.injection.backend.PacketImplKt;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.network.play.server.SPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Shadow
    private Channel channel;
    @Shadow
    private INetHandler packetListener;
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(PacketImplKt.wrap(packet));
        LiquidBounce.eventManager.callEvent(event);

        if (event.isCancelled())
            callback.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(PacketImplKt.wrap(packet));
        LiquidBounce.eventManager.callEvent(event);

        if (event.isCancelled())
            callback.cancel();
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_ ) {
        if (this.channel.isOpen()) {
            try {
                Packet<INetHandler> packet = (Packet<INetHandler>) p_channelRead0_2_;
                PostDisabler disabler2 = (PostDisabler) LiquidBounce.moduleManager.getModule(PostDisabler.class);
                if (p_channelRead0_2_ instanceof SPacketCustomPayload) {
                    final PacketEvent event = new PacketEvent(PacketImplKt.wrap(p_channelRead0_2_));
                    LiquidBounce.eventManager.callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    packet.processPacket(this.packetListener);

                } else if (disabler2.getGrimPost() && disabler2.grimPostDelay(p_channelRead0_2_)) {
                    Minecraft.getMinecraft().addScheduledTask(() -> PostDisabler.getStoredPackets().add(packet));
                } else {
                    final PacketEvent event = new PacketEvent(PacketImplKt.wrap(p_channelRead0_2_));
                    LiquidBounce.eventManager.callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    packet.processPacket(this.packetListener);
                }
            } catch (ThreadQuickExitException ignored) {
            }
        }
    }
}