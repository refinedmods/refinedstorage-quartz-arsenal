package com.refinedmods.refinedstorage.quartzarsenal.neoforge;

import com.refinedmods.refinedstorage.quartzarsenal.common.Config;
import com.refinedmods.refinedstorage.quartzarsenal.common.DefaultEnergyUsage;

import net.neoforged.neoforge.common.ModConfigSpec;

import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.createQuartzArsenalTranslationKey;

public class ConfigImpl implements Config {
    private final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    private final ModConfigSpec spec;
    private final WirelessCraftingGridEntry wirelessCraftingGrid;

    public ConfigImpl() {
        wirelessCraftingGrid = new WirelessCraftingGridEntryImpl();
        spec = builder.build();
    }

    public ModConfigSpec getSpec() {
        return spec;
    }

    @Override
    public WirelessCraftingGridEntry getWirelessCraftingGrid() {
        return wirelessCraftingGrid;
    }

    private static String translationKey(final String value) {
        return createQuartzArsenalTranslationKey("text.autoconfig", "option." + value);
    }

    private class WirelessCraftingGridEntryImpl implements WirelessCraftingGridEntry {
        private final ModConfigSpec.LongValue energyCapacity;
        private final ModConfigSpec.LongValue openEnergyUsage;
        private final ModConfigSpec.LongValue craftingEnergyUsage;
        private final ModConfigSpec.LongValue autocraftingEnergyUsage;
        private final ModConfigSpec.LongValue clearMatrixEnergyUsage;
        private final ModConfigSpec.LongValue recipeTransferEnergyUsage;

        WirelessCraftingGridEntryImpl() {
            builder.translation(translationKey("wirelessCraftingGrid")).push("wirelessCraftingGrid");
            energyCapacity = builder
                .translation(translationKey("wirelessCraftingGrid.energyCapacity"))
                .defineInRange("energyCapacity", DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_CAPACITY, 0, Long.MAX_VALUE);
            openEnergyUsage = builder
                .translation(translationKey("wirelessCraftingGrid.openEnergyUsage"))
                .defineInRange("openEnergyUsage", DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_OPEN, 0, Long.MAX_VALUE);
            craftingEnergyUsage = builder
                .translation(translationKey("wirelessCraftingGrid.craftingEnergyUsage"))
                .defineInRange("craftingEnergyUsage", DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_CRAFTING, 0,
                    Long.MAX_VALUE);
            autocraftingEnergyUsage = builder
                .translation(translationKey("wirelessCraftingGrid.autocraftingEnergyUsage"))
                .defineInRange("autocraftingEnergyUsage", DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_AUTOCRAFTING, 0,
                    Long.MAX_VALUE);
            clearMatrixEnergyUsage = builder
                .translation(translationKey("wirelessCraftingGrid.clearMatrixEnergyUsage"))
                .defineInRange("clearMatrixEnergyUsage", DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_CLEAR_MATRIX, 0,
                    Long.MAX_VALUE);
            recipeTransferEnergyUsage = builder
                .translation(translationKey("wirelessCraftingGrid.recipeTransferEnergyUsage"))
                .defineInRange("recipeTransferEnergyUsage", DefaultEnergyUsage.WIRELESS_CRAFTING_GRID_RECIPE_TRANSFER,
                    0, Long.MAX_VALUE);
            builder.pop();
        }

        @Override
        public long getEnergyCapacity() {
            return energyCapacity.get();
        }

        @Override
        public long getOpenEnergyUsage() {
            return openEnergyUsage.get();
        }

        @Override
        public long getCraftingEnergyUsage() {
            return craftingEnergyUsage.get();
        }

        @Override
        public long getAutocraftingEnergyUsage() {
            return autocraftingEnergyUsage.get();
        }

        @Override
        public long getClearMatrixEnergyUsage() {
            return clearMatrixEnergyUsage.get();
        }

        @Override
        public long getRecipeTransferEnergyUsage() {
            return recipeTransferEnergyUsage.get();
        }
    }
}
