package com.refinedmods.refinedstorage.quartzarsenal.common;

import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridContainerMenu;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.world.inventory.MenuType;

import static java.util.Objects.requireNonNull;

public final class Menus {
    public static final Menus INSTANCE = new Menus();

    @Nullable
    private Supplier<MenuType<WirelessCraftingGridContainerMenu>> wirelessCraftingGrid;

    private Menus() {
    }

    public void setWirelessCraftingGrid(final Supplier<MenuType<WirelessCraftingGridContainerMenu>> supplier) {
        this.wirelessCraftingGrid = supplier;
    }

    public MenuType<WirelessCraftingGridContainerMenu> getWirelessCraftingGrid() {
        return requireNonNull(wirelessCraftingGrid).get();
    }
}
