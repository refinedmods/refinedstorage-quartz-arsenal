package com.refinedmods.refinedstorage.quartzarsenal.common;

public interface Config {
    WirelessCraftingGridEntry getWirelessCraftingGrid();

    interface WirelessCraftingGridEntry {
        long getEnergyCapacity();

        long getOpenEnergyUsage();

        long getCraftingEnergyUsage();

        long getAutocraftingEnergyUsage();

        long getClearMatrixEnergyUsage();

        long getRecipeTransferEnergyUsage();
    }
}
