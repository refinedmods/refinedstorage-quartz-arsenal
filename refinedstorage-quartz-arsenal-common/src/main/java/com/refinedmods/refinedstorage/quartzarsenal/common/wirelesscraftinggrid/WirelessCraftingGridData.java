package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReferenceFactory;
import com.refinedmods.refinedstorage.common.grid.GridData;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record WirelessCraftingGridData(GridData gridData, SlotReference slotReference) {
    public static final StreamCodec<RegistryFriendlyByteBuf, WirelessCraftingGridData> STREAM_CODEC =
        StreamCodec.composite(
            GridData.STREAM_CODEC, WirelessCraftingGridData::gridData,
            SlotReferenceFactory.STREAM_CODEC,
            WirelessCraftingGridData::slotReference,
            WirelessCraftingGridData::new
        );
}
