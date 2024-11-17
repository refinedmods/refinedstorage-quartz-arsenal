package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.grid.AbstractCraftingGridContainerMenu;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;
import com.refinedmods.refinedstorage.quartzarsenal.common.Menus;

import net.minecraft.world.entity.player.Inventory;

public class WirelessCraftingGridContainerMenu extends AbstractCraftingGridContainerMenu {
    public WirelessCraftingGridContainerMenu(final int syncId,
                                             final Inventory playerInventory,
                                             final WirelessCraftingGridData gridData) {
        super(Menus.INSTANCE.getWirelessCraftingGrid(), syncId, playerInventory, gridData.gridData());
        this.disabledSlot = gridData.slotReference();
        resized(0, 0, 0);
    }

    WirelessCraftingGridContainerMenu(final int syncId,
                                      final Inventory playerInventory,
                                      final CraftingGrid craftingGrid,
                                      final SlotReference slotReference) {
        super(Menus.INSTANCE.getWirelessCraftingGrid(), syncId, playerInventory, craftingGrid);
        this.disabledSlot = slotReference;
        resized(0, 0, 0);
    }
}
