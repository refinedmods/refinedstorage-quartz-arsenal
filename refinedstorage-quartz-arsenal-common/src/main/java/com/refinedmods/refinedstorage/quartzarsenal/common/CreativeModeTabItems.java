package com.refinedmods.refinedstorage.quartzarsenal.common;

import java.util.function.Consumer;

import net.minecraft.world.item.ItemStack;

public final class CreativeModeTabItems {
    private CreativeModeTabItems() {
    }

    public static void addItems(final Consumer<ItemStack> consumer) {
        consumer.accept(Items.INSTANCE.getWirelessCraftingGrid().getDefaultInstance());
        consumer.accept(Items.INSTANCE.getWirelessCraftingGrid().createAtEnergyCapacity());
        consumer.accept(Items.INSTANCE.getCreativeWirelessCraftingGrid().getDefaultInstance());
    }
}
