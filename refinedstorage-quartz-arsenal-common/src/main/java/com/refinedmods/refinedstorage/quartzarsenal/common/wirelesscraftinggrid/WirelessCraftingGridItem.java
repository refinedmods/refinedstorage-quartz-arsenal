package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage.api.network.impl.energy.EnergyStorageImpl;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.security.SecurityHelper;
import com.refinedmods.refinedstorage.common.api.support.energy.AbstractNetworkEnergyItem;
import com.refinedmods.refinedstorage.common.api.support.energy.EnergyItemContext;
import com.refinedmods.refinedstorage.common.api.support.energy.EnergyItemHelper;
import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemContext;
import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemHelper;
import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;
import com.refinedmods.refinedstorage.common.security.BuiltinPermission;
import com.refinedmods.refinedstorage.quartzarsenal.common.ContentIds;
import com.refinedmods.refinedstorage.quartzarsenal.common.ContentNames;
import com.refinedmods.refinedstorage.quartzarsenal.common.Platform;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNullElse;

public class WirelessCraftingGridItem extends AbstractNetworkEnergyItem {
    private final boolean creative;

    public WirelessCraftingGridItem(final boolean creative, final EnergyItemHelper energyItemHelper,
                                    final NetworkItemHelper networkItemHelper) {
        super(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM,
                creative ? ContentIds.CREATIVE_WIRELESS_CRAFTING_GRID : ContentIds.WIRELESS_CRAFTING_GRID)),
            energyItemHelper, networkItemHelper);
        this.creative = creative;
    }

    public static EnergyStorage createEnergyStorage(final ItemStack stack, final EnergyItemContext context) {
        final EnergyStorage energyStorage = new EnergyStorageImpl(
            Math.clamp(
                Platform.getConfig().getWirelessCraftingGrid().getEnergyCapacity(),
                1,
                Long.MAX_VALUE
            )
        );
        return RefinedStorageApi.INSTANCE.createItemEnergyStorage(energyStorage, stack, context);
    }

    @Override
    protected void use(@Nullable final Component name,
                       final ServerPlayer player,
                       final PlayerSlotReference playerSlotReference,
                       final NetworkItemContext context) {
        final boolean isAllowed = context.resolveNetwork()
            .map(network -> SecurityHelper.isAllowed(player, BuiltinPermission.OPEN, network))
            .orElse(true);
        if (!isAllowed) {
            RefinedStorageApi.INSTANCE.sendNoPermissionToOpenMessage(player, ContentNames.WIRELESS_CRAFTING_GRID);
            return;
        }
        final CraftingGrid craftingGrid = new WirelessCraftingGrid(player, context, playerSlotReference);
        final Component correctedName = requireNonNullElse(
            name,
            creative ? ContentNames.CREATIVE_WIRELESS_CRAFTING_GRID : ContentNames.WIRELESS_CRAFTING_GRID
        );
        final var provider = new WirelessCraftingGridExtendedMenuProvider(correctedName, craftingGrid,
            playerSlotReference);
        com.refinedmods.refinedstorage.common.Platform.INSTANCE.getMenuOpener().openMenu(player, provider);
    }
}
