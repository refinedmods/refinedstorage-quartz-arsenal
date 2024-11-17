package com.refinedmods.refinedstorage.quartzarsenal.common;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractClientModInitializer {
    protected static void handleInputEvents() {
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final KeyMapping openWirelessCraftingGrid = KeyMappings.INSTANCE.getOpenWirelessCraftingGrid();
        while (openWirelessCraftingGrid != null && openWirelessCraftingGrid.consumeClick()) {
            RefinedStorageApi.INSTANCE.useSlotReferencedItem(
                player,
                Items.INSTANCE.getWirelessCraftingGrid(),
                Items.INSTANCE.getCreativeWirelessCraftingGrid()
            );
        }
    }
}
