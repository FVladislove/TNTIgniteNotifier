package com.elis.tntignitenotifier;

import com.mojang.authlib.properties.Property;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TNTIgniteNotifier implements ModInitializer {
    public static final String MOD_ID = "tnt-ignite-notifier";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register(this::onUseBlock);
        LOGGER.info("Initialized mod");
    }

    private ActionResult onUseBlock(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
        var pos = blockHitResult.getBlockPos();
        LOGGER.info("POS:\t" + pos.toShortString());

        var blockState = world.getBlockState(pos);
        LOGGER.info("BlockState:\t" + blockState.toString());

        var block = blockState.getBlock();
        LOGGER.info("Block:\t" + block.toString());

        LOGGER.info("Player " + playerEntity.getName().getString() + "use " + block.getName().getString() + "] at " + pos.toShortString());

        var stateManager = block.getStateManager();
        LOGGER.info("StateManager:\t" + stateManager.toString());

        if (block == Blocks.TNT) {
            LOGGER.info("TNT Entity instance");
            LOGGER.info("BlockState:\t" + blockState.toString());
//			if (block.)
            if (playerEntity.getMainHandStack().getItem() == Items.FLINT_AND_STEEL ||
                    playerEntity.getMainHandStack().getItem() == Items.FIRE_CHARGE) {
                notifyAdmins(playerEntity, pos);
            }
        }
        LOGGER.info("============================================");
        return ActionResult.PASS;
    }


    private void notifyAdmins(PlayerEntity player, BlockPos pos) {
        var playerServer = player.getServer();
        if (playerServer == null) {
            LOGGER.info("Server is null");
            return;
        }
        LOGGER.info("Player Server: {}", playerServer.getName());

        var playerManager = playerServer.getPlayerManager();
        LOGGER.info("Player Manager: {}", playerManager);

        var playerList = playerManager.getPlayerList();
        LOGGER.info("Player List: {}", playerList);
//		Objects.requireNonNull(player.getServer()).getPlayerManager().getPlayerList().forEach(
        playerList.forEach(
                admin -> {
                    LOGGER.info("Admin: {}", admin.getName().getString());
                    if (admin.hasPermissionLevel(2)) {
                        admin.sendMessage(Text.literal("Player" + player.getName().getString() + " ignited TNT at" + pos.toShortString()), false);
                    }
                }
        );
    }
}