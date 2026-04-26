package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;
import com.refinedmods.refinedstorage.common.grid.GridData;
import com.refinedmods.refinedstorage.common.support.containermenu.ExtendedMenuProvider;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class WirelessCraftingGridExtendedMenuProvider implements ExtendedMenuProvider<WirelessCraftingGridData> {
    private final Component name;
    private final CraftingGrid craftingGrid;
    private final PlayerSlotReference playerSlotReference;

    WirelessCraftingGridExtendedMenuProvider(final Component name,
                                             final CraftingGrid craftingGrid,
                                             final PlayerSlotReference playerSlotReference) {
        this.name = name;
        this.craftingGrid = craftingGrid;
        this.playerSlotReference = playerSlotReference;
    }

    @Override
    public WirelessCraftingGridData getMenuData() {
        return new WirelessCraftingGridData(
            GridData.of(craftingGrid),
            playerSlotReference
        );
    }

    @Override
    public StreamEncoder<RegistryFriendlyByteBuf, WirelessCraftingGridData> getMenuCodec() {
        return WirelessCraftingGridData.STREAM_CODEC;
    }

    @Override
    public Component getDisplayName() {
        return name;
    }

    @Override
    public AbstractContainerMenu createMenu(final int syncId, final Inventory inventory, final Player player) {
        return new WirelessCraftingGridContainerMenu(syncId, inventory, craftingGrid, playerSlotReference);
    }
}
