package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import com.refinedmods.refinedstorage.api.grid.operations.GridExtractMode;
import com.refinedmods.refinedstorage.api.grid.operations.GridInsertMode;
import com.refinedmods.refinedstorage.api.grid.operations.GridOperations;
import com.refinedmods.refinedstorage.api.grid.watcher.GridWatcherManager;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemContext;

class WirelessCraftingGridOperations implements GridOperations {
    private final GridOperations delegate;
    private final NetworkItemContext context;
    private final GridWatcherManager watchers;

    WirelessCraftingGridOperations(final GridOperations delegate,
                                   final NetworkItemContext context,
                                   final GridWatcherManager watchers) {
        this.delegate = delegate;
        this.context = context;
        this.watchers = watchers;
    }

    @Override
    public boolean extract(final ResourceKey resource,
                           final GridExtractMode extractMode,
                           final InsertableStorage destination) {
        final boolean success = delegate.extract(resource, extractMode, destination);
        if (success) {
            drain(Platform.INSTANCE.getConfig().getWirelessGrid().getExtractEnergyUsage());
        }
        return success;
    }

    @Override
    public boolean insert(final ResourceKey resource,
                          final GridInsertMode insertMode,
                          final ExtractableStorage source) {
        final boolean success = delegate.insert(resource, insertMode, source);
        if (success) {
            drain(Platform.INSTANCE.getConfig().getWirelessGrid().getInsertEnergyUsage());
        }
        return success;
    }

    private void drain(final long amount) {
        final boolean wasActive = context.isActive();
        context.drainEnergy(amount);
        final boolean isActive = context.isActive();
        if (wasActive != isActive) {
            watchers.activeChanged(isActive);
        }
    }
}
