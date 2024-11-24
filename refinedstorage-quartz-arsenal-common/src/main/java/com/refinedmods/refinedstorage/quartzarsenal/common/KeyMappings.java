package com.refinedmods.refinedstorage.quartzarsenal.common;

import javax.annotation.Nullable;

import net.minecraft.client.KeyMapping;

public final class KeyMappings {
    public static final KeyMappings INSTANCE = new KeyMappings();

    @Nullable
    private KeyMapping openWirelessCraftingGrid;

    private KeyMappings() {
    }

    public void setOpenWirelessCraftingGrid(final KeyMapping openWirelessCraftingGrid) {
        this.openWirelessCraftingGrid = openWirelessCraftingGrid;
    }

    @Nullable
    public KeyMapping getOpenWirelessCraftingGrid() {
        return openWirelessCraftingGrid;
    }
}
