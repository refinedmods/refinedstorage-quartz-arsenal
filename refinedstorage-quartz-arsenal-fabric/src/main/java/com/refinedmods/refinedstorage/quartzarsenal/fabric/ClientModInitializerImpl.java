package com.refinedmods.refinedstorage.quartzarsenal.fabric;

import com.refinedmods.refinedstorage.common.grid.screen.CraftingGridScreen;
import com.refinedmods.refinedstorage.quartzarsenal.common.AbstractClientModInitializer;
import com.refinedmods.refinedstorage.quartzarsenal.common.ContentNames;
import com.refinedmods.refinedstorage.quartzarsenal.common.KeyMappings;
import com.refinedmods.refinedstorage.quartzarsenal.common.Menus;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;

import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.createQuartzArsenalIdentifier;

public class ClientModInitializerImpl extends AbstractClientModInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerKeyMappings();
        registerMenus();
    }

    private void registerKeyMappings() {
        final KeyMapping.Category category = KeyMapping.Category.register(createQuartzArsenalIdentifier("keymappings"));

        KeyMappings.INSTANCE.setOpenWirelessCraftingGrid(KeyMappingHelper.registerKeyMapping(new KeyMapping(
            ContentNames.OPEN_WIRELESS_CRAFTING_GRID_TRANSLATION_KEY,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            category
        )));
        ClientTickEvents.END_CLIENT_TICK.register(client -> handleInputEvents());
    }

    private static void registerMenus() {
        MenuScreens.register(Menus.INSTANCE.getWirelessCraftingGrid(), CraftingGridScreen::new);
    }
}
