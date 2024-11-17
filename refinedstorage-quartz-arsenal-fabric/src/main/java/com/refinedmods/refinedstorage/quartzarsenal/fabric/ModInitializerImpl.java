package com.refinedmods.refinedstorage.quartzarsenal.fabric;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.content.DirectRegistryCallback;
import com.refinedmods.refinedstorage.common.content.ExtendedMenuTypeFactory;
import com.refinedmods.refinedstorage.common.content.RegistryCallback;
import com.refinedmods.refinedstorage.fabric.api.RefinedStoragePlugin;
import com.refinedmods.refinedstorage.fabric.support.energy.EnergyStorageAdapter;
import com.refinedmods.refinedstorage.quartzarsenal.common.AbstractModInitializer;
import com.refinedmods.refinedstorage.quartzarsenal.common.ContentIds;
import com.refinedmods.refinedstorage.quartzarsenal.common.CreativeModeTabItems;
import com.refinedmods.refinedstorage.quartzarsenal.common.Items;
import com.refinedmods.refinedstorage.quartzarsenal.common.Platform;
import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridItem;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

public class ModInitializerImpl extends AbstractModInitializer implements RefinedStoragePlugin, ModInitializer {
    @Override
    public void onApiAvailable(final RefinedStorageApi refinedStorageApi) {
        Platform.setConfigProvider(ConfigImpl::get);
        registerContent(refinedStorageApi);
        registerCapabilities();
        registerCreativeModeTabListener(refinedStorageApi);
    }

    private void registerContent(final RefinedStorageApi refinedStorageApi) {
        final DirectRegistryCallback<Item> itemRegistryCallback = new DirectRegistryCallback<>(BuiltInRegistries.ITEM);
        registerCustomItems(itemRegistryCallback, refinedStorageApi);
        registerMenus(new DirectRegistryCallback<>(BuiltInRegistries.MENU), new ExtendedMenuTypeFactory() {
            @Override
            public <T extends AbstractContainerMenu, D> MenuType<T> create(final MenuSupplier<T, D> supplier,
                                                                           final StreamCodec<RegistryFriendlyByteBuf, D>
                                                                               streamCodec) {
                return new ExtendedScreenHandlerType<>(supplier::create, streamCodec);
            }
        });
        registerDataComponents(new DirectRegistryCallback<>(BuiltInRegistries.DATA_COMPONENT_TYPE));
    }

    private void registerCustomItems(final RegistryCallback<Item> callback,
                                     final RefinedStorageApi refinedStorageApi) {
        Items.INSTANCE.setWirelessCraftingGrid(
            callback.register(ContentIds.WIRELESS_CRAFTING_GRID, () -> new WirelessCraftingGridItem(
                false,
                refinedStorageApi.getEnergyItemHelper(),
                refinedStorageApi.getNetworkItemHelper()
            ) {
                @Override
                public boolean allowComponentsUpdateAnimation(final Player player,
                                                              final InteractionHand hand,
                                                              final ItemStack oldStack,
                                                              final ItemStack newStack) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
        Items.INSTANCE.setCreativeWirelessCraftingGrid(
            callback.register(ContentIds.CREATIVE_WIRELESS_CRAFTING_GRID, () -> new WirelessCraftingGridItem(
                true,
                refinedStorageApi.getEnergyItemHelper(),
                refinedStorageApi.getNetworkItemHelper()
            ) {
                @Override
                public boolean allowComponentsUpdateAnimation(final Player player,
                                                              final InteractionHand hand,
                                                              final ItemStack oldStack,
                                                              final ItemStack newStack) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
    }

    private void registerCapabilities() {
        registerEnergyItemProviders();
    }

    private void registerEnergyItemProviders() {
        EnergyStorage.ITEM.registerForItems(
            (stack, context) ->
                new EnergyStorageAdapter(Items.INSTANCE.getWirelessCraftingGrid().createEnergyStorage(stack)),
            Items.INSTANCE.getWirelessCraftingGrid()
        );
    }

    private void registerCreativeModeTabListener(final RefinedStorageApi refinedStorageApi) {
        final ResourceKey<CreativeModeTab> creativeModeTab = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            refinedStorageApi.getCreativeModeTabId()
        );
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(
            entries -> CreativeModeTabItems.addItems(entries::accept)
        );
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigImpl.class, Toml4jConfigSerializer::new);
    }
}
