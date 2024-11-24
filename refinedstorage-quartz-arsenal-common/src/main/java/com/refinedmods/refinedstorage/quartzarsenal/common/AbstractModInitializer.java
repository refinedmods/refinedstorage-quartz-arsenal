package com.refinedmods.refinedstorage.quartzarsenal.common;

import com.refinedmods.refinedstorage.common.content.ExtendedMenuTypeFactory;
import com.refinedmods.refinedstorage.common.content.RegistryCallback;
import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridContainerMenu;
import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridData;
import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridState;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.createQuartzArsenalIdentifier;

public abstract class AbstractModInitializer {
    protected static boolean allowComponentsUpdateAnimation(final ItemStack oldStack, final ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }

    protected final void registerMenus(final RegistryCallback<MenuType<?>> callback,
                                       final ExtendedMenuTypeFactory extendedMenuTypeFactory) {
        Menus.INSTANCE.setWirelessCraftingGrid(callback.register(
            ContentIds.WIRELESS_CRAFTING_GRID,
            () -> extendedMenuTypeFactory.create(
                WirelessCraftingGridContainerMenu::new,
                WirelessCraftingGridData.STREAM_CODEC
            )
        ));
    }

    protected final void registerDataComponents(final RegistryCallback<DataComponentType<?>> callback) {
        DataComponents.INSTANCE.setWirelessCraftingGridState(callback.register(
            createQuartzArsenalIdentifier("wireless_crafting_grid_state"),
            () -> DataComponentType.<WirelessCraftingGridState>builder()
                .persistent(WirelessCraftingGridState.CODEC)
                .networkSynchronized(WirelessCraftingGridState.STREAM_CODEC)
                .build()));
    }
}
