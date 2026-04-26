package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;
import com.refinedmods.refinedstorage.common.grid.GridData;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record WirelessCraftingGridData(GridData gridData, PlayerSlotReference playerSlotReference) {
    public static final StreamCodec<RegistryFriendlyByteBuf, WirelessCraftingGridData> STREAM_CODEC =
        StreamCodec.composite(
            GridData.STREAM_CODEC, WirelessCraftingGridData::gridData,
            PlayerSlotReference.STREAM_CODEC, WirelessCraftingGridData::playerSlotReference,
            WirelessCraftingGridData::new
        );
}
