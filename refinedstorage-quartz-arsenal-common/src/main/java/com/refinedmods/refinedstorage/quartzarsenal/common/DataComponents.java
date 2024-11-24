package com.refinedmods.refinedstorage.quartzarsenal.common;

import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridState;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.core.component.DataComponentType;

import static java.util.Objects.requireNonNull;

public final class DataComponents {
    public static final DataComponents INSTANCE = new DataComponents();

    @Nullable
    private Supplier<DataComponentType<WirelessCraftingGridState>> wirelessCraftingGridState;

    private DataComponents() {
    }

    public DataComponentType<WirelessCraftingGridState> getWirelessCraftingGridState() {
        return requireNonNull(wirelessCraftingGridState).get();
    }

    public void setWirelessCraftingGridState(
        @Nullable final Supplier<DataComponentType<WirelessCraftingGridState>> supplier
    ) {
        this.wirelessCraftingGridState = supplier;
    }
}
