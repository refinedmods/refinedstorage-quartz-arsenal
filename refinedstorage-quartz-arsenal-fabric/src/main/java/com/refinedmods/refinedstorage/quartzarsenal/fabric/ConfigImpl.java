package com.refinedmods.refinedstorage.quartzarsenal.fabric;

import com.refinedmods.refinedstorage.quartzarsenal.common.Config;
import com.refinedmods.refinedstorage.quartzarsenal.common.DefaultEnergyUsage;
import com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@me.shedaniel.autoconfig.annotation.Config(name = QuartzArsenalIdentifierUtil.QUARTZ_ARSENAL_MOD_ID)
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "CanBeFinal"})
public class ConfigImpl implements ConfigData, Config {
    @ConfigEntry.Gui.CollapsibleObject
    private WirelessCraftingGridEntryEntryImpl wirelessCraftingGrid = new WirelessCraftingGridEntryEntryImpl();

    public static ConfigImpl get() {
        return AutoConfig.getConfigHolder(ConfigImpl.class).getConfig();
    }

    @Override
    public WirelessCraftingGridEntry getWirelessCraftingGrid() {
        return wirelessCraftingGrid;
    }

    private static class WirelessCraftingGridEntryEntryImpl implements WirelessCraftingGridEntry {
        private long energyCapacity = DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_CAPACITY;
        private long openEnergyUsage = DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_OPEN;
        private long craftingEnergyUsage = DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_CRAFTING;
        private long autocraftingEnergyUsage = DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_AUTOCRAFTING;
        private long clearMatrixEnergyUsage = DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_CLEAR_MATRIX;
        private long recipeTransferEnergyUsage = DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_RECIPE_TRANSFER;

        @Override
        public long getEnergyCapacity() {
            return energyCapacity;
        }

        @Override
        public long getOpenEnergyUsage() {
            return openEnergyUsage;
        }

        @Override
        public long getCraftingEnergyUsage() {
            return craftingEnergyUsage;
        }

        @Override
        public long getAutocraftingEnergyUsage() {
            return autocraftingEnergyUsage;
        }

        @Override
        public long getClearMatrixEnergyUsage() {
            return clearMatrixEnergyUsage;
        }

        @Override
        public long getRecipeTransferEnergyUsage() {
            return recipeTransferEnergyUsage;
        }
    }
}
