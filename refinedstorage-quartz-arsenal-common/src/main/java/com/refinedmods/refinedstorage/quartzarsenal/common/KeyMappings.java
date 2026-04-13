package com.refinedmods.refinedstorage.quartzarsenal.common;

import net.minecraft.client.KeyMapping;
import org.jspecify.annotations.Nullable;

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
