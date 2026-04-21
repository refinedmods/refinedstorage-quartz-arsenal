package com.refinedmods.refinedstorage.quartzarsenal.common;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;

import java.util.function.Consumer;

import net.minecraft.world.item.ItemStack;

public final class CreativeModeTabItems {
    private CreativeModeTabItems() {
    }

    public static void addItems(final Consumer<ItemStack> consumer) {
        consumer.accept(Items.INSTANCE.getWirelessCraftingGrid().getDefaultInstance());
        if (RefinedStorageApi.INSTANCE.isEnergyRequired()) {
            consumer.accept(Items.INSTANCE.getWirelessCraftingGrid().createAtEnergyCapacity());
            consumer.accept(Items.INSTANCE.getCreativeWirelessCraftingGrid().getDefaultInstance());
        }
    }
}
