package com.refinedmods.refinedstorage.quartzarsenal.common;

import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridItem;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public final class Items {
    public static final Items INSTANCE = new Items();

    @Nullable
    private Supplier<WirelessCraftingGridItem> wirelessCraftingGrid;
    @Nullable
    private Supplier<WirelessCraftingGridItem> creativeWirelessCraftingGrid;

    private Items() {
    }

    public WirelessCraftingGridItem getWirelessCraftingGrid() {
        return requireNonNull(wirelessCraftingGrid).get();
    }

    public void setWirelessCraftingGrid(final Supplier<WirelessCraftingGridItem> supplier) {
        this.wirelessCraftingGrid = supplier;
    }

    public WirelessCraftingGridItem getCreativeWirelessCraftingGrid() {
        return requireNonNull(creativeWirelessCraftingGrid).get();
    }

    public void setCreativeWirelessCraftingGrid(final Supplier<WirelessCraftingGridItem> supplier) {
        this.creativeWirelessCraftingGrid = supplier;
    }
}
