package com.refinedmods.refinedstorage.quartzarsenal.neoforge;

import com.refinedmods.refinedstorage.common.grid.screen.CraftingGridScreen;
import com.refinedmods.refinedstorage.quartzarsenal.common.AbstractClientModInitializer;
import com.refinedmods.refinedstorage.quartzarsenal.common.ContentNames;
import com.refinedmods.refinedstorage.quartzarsenal.common.KeyMappings;
import com.refinedmods.refinedstorage.quartzarsenal.common.Menus;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;

import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.createQuartzArsenalIdentifier;

public class ClientModInitializer extends AbstractClientModInitializer {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent e) {
        NeoForge.EVENT_BUS.addListener(ClientModInitializer::onKeyInput);
        NeoForge.EVENT_BUS.addListener(ClientModInitializer::onMouseInput);
    }

    @SubscribeEvent
    public static void onKeyInput(final InputEvent.Key e) {
        handleInputEvents();
    }

    @SubscribeEvent
    public static void onMouseInput(final InputEvent.MouseButton.Pre e) {
        handleInputEvents();
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(final RegisterMenuScreensEvent e) {
        e.register(Menus.INSTANCE.getWirelessCraftingGrid(), CraftingGridScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(final RegisterKeyMappingsEvent e) {
        final KeyMapping.Category category = new KeyMapping.Category(createQuartzArsenalIdentifier("keymappings"));
        e.registerCategory(category);

        final KeyMapping openWirelessCraftingGrid = new KeyMapping(
            ContentNames.OPEN_WIRELESS_CRAFTING_GRID_TRANSLATION_KEY,
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            category
        );
        e.register(openWirelessCraftingGrid);
        KeyMappings.INSTANCE.setOpenWirelessCraftingGrid(openWirelessCraftingGrid);
    }
}
