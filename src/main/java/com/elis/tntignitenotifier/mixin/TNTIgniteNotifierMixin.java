package com.elis.tntignitenotifier.mixin;

import com.elis.tntignitenotifier.TNTIgniteNotifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TntBlock.class)
public class TNTIgniteNotifierMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("tnt-ignite-notifier-mixin");

    @Inject(method = "onUse", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ActionResult;success(Z)Lnet/minecraft/util/ActionResult;",
            shift = At.Shift.AFTER))
    private void init(BlockState state, World world, BlockPos pos, PlayerEntity player,
                      Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        LOGGER.info("POS:\t" + pos.toShortString());

        var block = state.getBlock();

        LOGGER.info("Player " + player.getName().getString()
                + "use " + block.getName().getString()
                + "] at " + pos.toShortString());

        notifyAdmins(player, pos);

		LOGGER.info("UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU-UwU");
    }

    @Unique
    private void notifyAdmins(PlayerEntity player, BlockPos pos) {
        var playerServer = player.getServer();
        if (playerServer == null) {
            LOGGER.info("Server is null");
            return;
        }

        var playerManager = playerServer.getPlayerManager();

        var playerList = playerManager.getPlayerList();
        // is it possible for playerList to be null?
        // but in any case it will not be superfluous
        if (playerList == null) {
            LOGGER.info("Player List is null");
            return;
        }

        playerList.forEach(
                admin -> {
                    LOGGER.info("Admin: {}", admin.getName().getString());
                    if (admin.hasPermissionLevel(3)) {
                        admin.sendMessage(Text.literal("Player" + player.getName().getString()
								+ " ignited TNT at" + pos.toShortString()), false);
                    }
                }
        );
    }
}