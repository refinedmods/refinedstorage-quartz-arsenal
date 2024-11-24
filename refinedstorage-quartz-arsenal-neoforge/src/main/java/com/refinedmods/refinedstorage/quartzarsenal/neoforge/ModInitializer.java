package com.refinedmods.refinedstorage.quartzarsenal.neoforge;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.content.ExtendedMenuTypeFactory;
import com.refinedmods.refinedstorage.common.content.RegistryCallback;
import com.refinedmods.refinedstorage.neoforge.support.energy.EnergyStorageAdapter;
import com.refinedmods.refinedstorage.quartzarsenal.common.AbstractModInitializer;
import com.refinedmods.refinedstorage.quartzarsenal.common.ContentIds;
import com.refinedmods.refinedstorage.quartzarsenal.common.CreativeModeTabItems;
import com.refinedmods.refinedstorage.quartzarsenal.common.Items;
import com.refinedmods.refinedstorage.quartzarsenal.common.Platform;
import com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil;
import com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridItem;

import java.util.function.Supplier;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(QuartzArsenalIdentifierUtil.QUARTZ_ARSENAL_MOD_ID)
public class ModInitializer extends AbstractModInitializer {
    private final DeferredRegister<Item> itemRegistry =
        DeferredRegister.create(BuiltInRegistries.ITEM, QuartzArsenalIdentifierUtil.QUARTZ_ARSENAL_MOD_ID);
    private final DeferredRegister<MenuType<?>> menuTypeRegistry =
        DeferredRegister.create(BuiltInRegistries.MENU, QuartzArsenalIdentifierUtil.QUARTZ_ARSENAL_MOD_ID);
    private final DeferredRegister<DataComponentType<?>> dataComponentTypeRegistry = DeferredRegister.create(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        QuartzArsenalIdentifierUtil.QUARTZ_ARSENAL_MOD_ID
    );

    public ModInitializer(final IEventBus eventBus, final ModContainer modContainer) {
        final ConfigImpl config = new ConfigImpl();
        modContainer.registerConfig(ModConfig.Type.COMMON, config.getSpec());
        Platform.setConfigProvider(() -> config);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(ClientModInitializer::onClientSetup);
            eventBus.addListener(ClientModInitializer::onRegisterMenuScreens);
            eventBus.addListener(ClientModInitializer::onRegisterKeyMappings);
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
        registerContent(eventBus);
        eventBus.addListener(this::registerCapabilities);
        eventBus.addListener(this::registerCreativeModeTabListener);
    }

    private void registerContent(final IEventBus eventBus) {
        registerItems(eventBus);
        registerMenus(eventBus);
        registerDataComponents(eventBus);
    }

    private void registerItems(final IEventBus eventBus) {
        final RegistryCallback<Item> callback = new ForgeRegistryCallback<>(itemRegistry);
        registerCustomItems(callback);
        itemRegistry.register(eventBus);
    }

    private void registerCustomItems(final RegistryCallback<Item> callback) {
        Items.INSTANCE.setWirelessCraftingGrid(
            callback.register(ContentIds.WIRELESS_CRAFTING_GRID, () -> new WirelessCraftingGridItem(
                false,
                RefinedStorageApi.INSTANCE.getEnergyItemHelper(),
                RefinedStorageApi.INSTANCE.getNetworkItemHelper()
            ) {
                @Override
                public boolean shouldCauseReequipAnimation(final ItemStack oldStack,
                                                           final ItemStack newStack,
                                                           final boolean slotChanged) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
        Items.INSTANCE.setCreativeWirelessCraftingGrid(
            callback.register(ContentIds.CREATIVE_WIRELESS_CRAFTING_GRID, () -> new WirelessCraftingGridItem(
                true,
                RefinedStorageApi.INSTANCE.getEnergyItemHelper(),
                RefinedStorageApi.INSTANCE.getNetworkItemHelper()
            ) {
                @Override
                public boolean shouldCauseReequipAnimation(final ItemStack oldStack,
                                                           final ItemStack newStack,
                                                           final boolean slotChanged) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
    }

    private void registerMenus(final IEventBus eventBus) {
        registerMenus(new ForgeRegistryCallback<>(menuTypeRegistry), new ExtendedMenuTypeFactory() {
            @Override
            public <T extends AbstractContainerMenu, D> MenuType<T> create(final MenuSupplier<T, D> supplier,
                                                                           final StreamCodec<RegistryFriendlyByteBuf, D>
                                                                               streamCodec) {
                return IMenuTypeExtension.create((syncId, inventory, buf) -> {
                    final D data = streamCodec.decode(buf);
                    return supplier.create(syncId, inventory, data);
                });
            }
        });
        menuTypeRegistry.register(eventBus);
    }

    private void registerDataComponents(final IEventBus eventBus) {
        final RegistryCallback<DataComponentType<?>> callback = new ForgeRegistryCallback<>(dataComponentTypeRegistry);
        registerDataComponents(callback);
        dataComponentTypeRegistry.register(eventBus);
    }

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerEnergyItemProviders(event);
    }

    private void registerEnergyItemProviders(final RegisterCapabilitiesEvent event) {
        event.registerItem(
            Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new EnergyStorageAdapter(
                Items.INSTANCE.getWirelessCraftingGrid().createEnergyStorage(stack)
            ),
            Items.INSTANCE.getWirelessCraftingGrid()
        );
    }

    private void registerCreativeModeTabListener(final BuildCreativeModeTabContentsEvent e) {
        final ResourceKey<CreativeModeTab> creativeModeTab = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            RefinedStorageApi.INSTANCE.getCreativeModeTabId()
        );
        if (!e.getTabKey().equals(creativeModeTab)) {
            return;
        }
        CreativeModeTabItems.addItems(e::accept);
    }

    private record ForgeRegistryCallback<T>(DeferredRegister<T> registry) implements RegistryCallback<T> {
        @Override
        public <R extends T> Supplier<R> register(final ResourceLocation id, final Supplier<R> value) {
            return registry.register(id.getPath(), value);
        }
    }
}
