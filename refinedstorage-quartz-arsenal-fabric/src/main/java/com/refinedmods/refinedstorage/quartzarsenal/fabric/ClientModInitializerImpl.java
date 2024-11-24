package com.refinedmods.refinedstorage.quartzarsenal.fabric;

import com.refinedmods.refinedstorage.common.grid.screen.CraftingGridScreen;
import com.refinedmods.refinedstorage.common.support.network.item.NetworkItemPropertyFunction;
import com.refinedmods.refinedstorage.quartzarsenal.common.AbstractClientModInitializer;
import com.refinedmods.refinedstorage.quartzarsenal.common.ContentNames;
import com.refinedmods.refinedstorage.quartzarsenal.common.Items;
import com.refinedmods.refinedstorage.quartzarsenal.common.KeyMappings;
import com.refinedmods.refinedstorage.quartzarsenal.common.Menus;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;

public class ClientModInitializerImpl extends AbstractClientModInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerKeyMappings();
        registerItemProperties();
        registerMenus();
    }

    private void registerKeyMappings() {
        KeyMappings.INSTANCE.setOpenWirelessCraftingGrid(KeyBindingHelper.registerKeyBinding(new KeyMapping(
            ContentNames.OPEN_WIRELESS_CRAFTING_GRID_TRANSLATION_KEY,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            ContentNames.MOD_TRANSLATION_KEY
        )));
        ClientTickEvents.END_CLIENT_TICK.register(client -> handleInputEvents());
    }

    private void registerItemProperties() {
        ItemProperties.register(
            Items.INSTANCE.getWirelessCraftingGrid(),
            NetworkItemPropertyFunction.NAME,
            new NetworkItemPropertyFunction()
        );
        ItemProperties.register(
            Items.INSTANCE.getCreativeWirelessCraftingGrid(),
            NetworkItemPropertyFunction.NAME,
            new NetworkItemPropertyFunction()
        );
    }

    private static void registerMenus() {
        MenuScreens.register(Menus.INSTANCE.getWirelessCraftingGrid(), CraftingGridScreen::new);
    }
}
