package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import com.refinedmods.refinedstorage.api.autocrafting.preview.Preview;
import com.refinedmods.refinedstorage.api.autocrafting.task.TaskId;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.grid.operations.GridOperations;
import com.refinedmods.refinedstorage.api.grid.operations.NoopGridOperations;
import com.refinedmods.refinedstorage.api.grid.watcher.GridWatcher;
import com.refinedmods.refinedstorage.api.grid.watcher.GridWatcherManager;
import com.refinedmods.refinedstorage.api.grid.watcher.GridWatcherManagerImpl;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.autocrafting.AutocraftingNetworkComponent;
import com.refinedmods.refinedstorage.api.network.energy.EnergyNetworkComponent;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.NoopStorage;
import com.refinedmods.refinedstorage.api.storage.Storage;
import com.refinedmods.refinedstorage.api.storage.TrackedResourceAmount;
import com.refinedmods.refinedstorage.common.api.security.PlatformSecurityNetworkComponent;
import com.refinedmods.refinedstorage.common.api.storage.PlayerActor;
import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemContext;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;
import com.refinedmods.refinedstorage.common.grid.DirectCommitExtractTransaction;
import com.refinedmods.refinedstorage.common.grid.ExtractTransaction;
import com.refinedmods.refinedstorage.common.grid.SecuredGridOperations;
import com.refinedmods.refinedstorage.common.grid.SnapshotExtractTransaction;
import com.refinedmods.refinedstorage.common.support.RecipeMatrix;
import com.refinedmods.refinedstorage.common.support.RecipeMatrixContainer;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage.quartzarsenal.common.DataComponents;
import com.refinedmods.refinedstorage.quartzarsenal.common.Platform;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;

import static java.util.Objects.requireNonNull;

class WirelessCraftingGrid implements CraftingGrid {
    private final NetworkItemContext context;
    private final Level level;
    private final GridWatcherManager watchers = new GridWatcherManagerImpl();
    @Nullable
    private final RecipeMatrix<CraftingRecipe, CraftingInput> craftingRecipe;
    private final SlotReference slotReference;

    WirelessCraftingGrid(final Player player, final NetworkItemContext context, final SlotReference slotReference) {
        this.level = player.level();
        this.context = context;
        this.slotReference = slotReference;
        this.craftingRecipe = createMatrix(player, () -> updateCraftingRecipe(player), slotReference);
    }

    private static RecipeMatrix<CraftingRecipe, CraftingInput> createMatrix(final Player player,
                                                                            final Runnable listener,
                                                                            final SlotReference slotReference) {
        final RecipeMatrix<CraftingRecipe, CraftingInput> recipe = RecipeMatrix.crafting(listener, player::level);
        slotReference.resolve(player).ifPresent(stack -> setMatrixSlots(stack, recipe));
        return recipe;
    }

    private static void setMatrixSlots(final ItemStack stack,
                                       final RecipeMatrix<CraftingRecipe, CraftingInput> recipe) {
        final WirelessCraftingGridState state = stack.get(DataComponents.INSTANCE.getWirelessCraftingGridState());
        if (state == null) {
            return;
        }
        final CraftingInput.Positioned positioned = state.input();
        final int left = positioned.left();
        final int top = positioned.top();
        final CraftingInput input = positioned.input();
        for (int y = 0; y < input.height(); ++y) {
            for (int x = 0; x < input.width(); ++x) {
                final int matrixIndex = x + left + (y + top) * recipe.getMatrix().getWidth();
                recipe.getMatrix().setItem(matrixIndex, input.getItem(x, y).copy());
            }
        }
    }

    private void updateCraftingRecipe(final Player player) {
        if (craftingRecipe == null) {
            return;
        }
        final CraftingInput.Positioned input = craftingRecipe.getMatrix().asPositionedCraftInput();
        slotReference.resolve(player).ifPresent(stack -> stack.set(
            DataComponents.INSTANCE.getWirelessCraftingGridState(),
            new WirelessCraftingGridState(input)
        ));
    }

    private Optional<StorageNetworkComponent> getStorage() {
        return context.resolveNetwork().map(network -> network.getComponent(StorageNetworkComponent.class));
    }

    private Optional<PlatformSecurityNetworkComponent> getSecurity() {
        return context.resolveNetwork().map(network -> network.getComponent(PlatformSecurityNetworkComponent.class));
    }

    private Optional<AutocraftingNetworkComponent> getAutocrafting() {
        return context.resolveNetwork().map(network -> network.getComponent(AutocraftingNetworkComponent.class));
    }

    private Optional<Network> getNetworkForActiveGrid() {
        if (!isGridActive()) {
            return Optional.empty();
        }
        return context.resolveNetwork();
    }

    @Override
    public RecipeMatrixContainer getCraftingMatrix() {
        return requireNonNull(craftingRecipe).getMatrix();
    }

    @Override
    public ResultContainer getCraftingResult() {
        return requireNonNull(craftingRecipe).getResult();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final Player player, final CraftingInput craftingInput) {
        return requireNonNull(craftingRecipe).getRemainingItems(level, player, craftingInput);
    }

    @Override
    public ExtractTransaction startExtractTransaction(final Player player, final boolean directCommit) {
        return getNetworkForActiveGrid()
            .map(network -> network.getComponent(StorageNetworkComponent.class))
            .map(storage -> (ExtractTransaction) new WirelessCraftingGridExtractTransaction(
                context,
                createExtractTransaction(player, directCommit, storage)
            ))
            .orElse(ExtractTransaction.NOOP);
    }

    private ExtractTransaction createExtractTransaction(final Player player,
                                                        final boolean directCommit,
                                                        final StorageNetworkComponent storage) {
        return directCommit
            ? new DirectCommitExtractTransaction(storage)
            : new SnapshotExtractTransaction(player, storage, getCraftingMatrix());
    }

    @Override
    public boolean clearMatrix(final Player player, final boolean toPlayerInventory) {
        final boolean success = toPlayerInventory
            ? getCraftingMatrix().clearToPlayerInventory(player)
            : clearMatrixIntoStorage(player);
        if (success) {
            context.drainEnergy(Platform.getConfig().getWirelessCraftingGrid().getClearMatrixEnergyUsage());
        }
        return success;
    }

    private boolean clearMatrixIntoStorage(final Player player) {
        return getNetworkForActiveGrid()
            .map(network -> network.getComponent(StorageNetworkComponent.class))
            .map(storage -> getCraftingMatrix().clearIntoStorage(storage, player))
            .orElse(false);
    }

    @Override
    public void transferRecipe(final Player player, final List<List<ItemResource>> recipe) {
        context.drainEnergy(Platform.getConfig().getWirelessCraftingGrid().getRecipeTransferEnergyUsage());
        getCraftingMatrix().transferRecipe(
            player,
            getNetworkForActiveGrid().map(network -> network.getComponent(StorageNetworkComponent.class))
                .orElse(null),
            recipe
        );
    }

    @Override
    public void acceptQuickCraft(final Player player, final ItemStack craftedStack) {
        if (player.getInventory().add(craftedStack)) {
            return;
        }
        final long inserted = getNetworkForActiveGrid()
            .map(network -> network.getComponent(StorageNetworkComponent.class))
            .map(rootStorage -> rootStorage.insert(
                ItemResource.ofItemStack(craftedStack),
                craftedStack.getCount(),
                Action.EXECUTE,
                new PlayerActor(player)
            ))
            .orElse(0L);
        if (inserted != craftedStack.getCount()) {
            final long remainder = craftedStack.getCount() - inserted;
            final ItemStack remainderStack = craftedStack.copyWithCount((int) remainder);
            player.drop(remainderStack, false);
        }
    }

    @Override
    public void addWatcher(final GridWatcher watcher, final Class<? extends Actor> actorType) {
        context.drainEnergy(Platform.getConfig().getWirelessCraftingGrid().getOpenEnergyUsage());
        context.resolveNetwork().ifPresent(network -> watchers.addWatcher(
            watcher,
            actorType,
            network.getComponent(StorageNetworkComponent.class)
        ));
    }

    @Override
    public void removeWatcher(final GridWatcher watcher) {
        context.resolveNetwork().ifPresent(network -> watchers.removeWatcher(
            watcher,
            network.getComponent(StorageNetworkComponent.class)
        ));
    }

    @Override
    public Storage getItemStorage() {
        return getStorage().map(Storage.class::cast).orElseGet(NoopStorage::new);
    }

    @Override
    public boolean isGridActive() {
        final boolean networkActive = context.resolveNetwork()
            .map(network -> network.getComponent(EnergyNetworkComponent.class).getStored() > 0)
            .orElse(false);
        return networkActive && context.isActive();
    }

    @Override
    public List<TrackedResourceAmount> getResources(final Class<? extends Actor> actorType) {
        return getStorage().map(storage -> storage.getResources(actorType)).orElse(Collections.emptyList());
    }

    @Override
    public Set<PlatformResourceKey> getAutocraftableResources() {
        return getAutocrafting()
            .map(AutocraftingNetworkComponent::getOutputs)
            .map(outputs -> outputs.stream()
                .filter(PlatformResourceKey.class::isInstance)
                .map(PlatformResourceKey.class::cast)
                .collect(Collectors.toSet()))
            .orElse(Collections.emptySet());
    }

    @Override
    public GridOperations createOperations(final ResourceType resourceType, final ServerPlayer player) {
        return getStorage()
            .flatMap(rootStorage -> getSecurity()
                .map(security -> createGridOperations(resourceType, player, rootStorage, security)))
            .map(operations -> (GridOperations) new WirelessCraftingGridOperations(operations, context, watchers))
            .orElseGet(NoopGridOperations::new);
    }

    private GridOperations createGridOperations(final ResourceType resourceType,
                                                final ServerPlayer player,
                                                final StorageNetworkComponent rootStorage,
                                                final PlatformSecurityNetworkComponent securityNetworkComponent) {
        final PlayerActor playerActor = new PlayerActor(player);
        final GridOperations operations = resourceType.createGridOperations(rootStorage, playerActor);
        return new SecuredGridOperations(player, securityNetworkComponent, operations);
    }

    @Override
    public CompletableFuture<Optional<Preview>> getPreview(final ResourceKey resource, final long amount) {
        return getAutocrafting()
            .map(component -> component.getPreview(resource, amount))
            .orElseGet(() -> CompletableFuture.completedFuture(Optional.empty()));
    }

    @Override
    public CompletableFuture<Long> getMaxAmount(final ResourceKey resourceKey) {
        return getAutocrafting()
            .map(component -> component.getMaxAmount(resourceKey))
            .orElseGet(() -> CompletableFuture.completedFuture(0L));
    }

    @Override
    public CompletableFuture<Optional<TaskId>> startTask(final ResourceKey resourceKey,
                                                         final long amount,
                                                         final Actor actor,
                                                         final boolean notify) {
        return getAutocrafting()
            .map(component -> component.startTask(resourceKey, amount, actor, notify))
            .map(taskId -> {
                context.drainEnergy(Platform.getConfig().getWirelessCraftingGrid().getAutocraftingEnergyUsage());
                return taskId;
            })
            .orElseGet(() -> CompletableFuture.completedFuture(Optional.empty()));
    }
}
