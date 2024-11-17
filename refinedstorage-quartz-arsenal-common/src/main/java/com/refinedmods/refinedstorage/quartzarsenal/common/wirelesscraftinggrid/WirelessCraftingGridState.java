package com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid;

import java.util.ArrayList;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public record WirelessCraftingGridState(CraftingInput.Positioned input) {
    private static final Codec<CraftingInput> INPUT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("width").forGetter(CraftingInput::width),
        Codec.INT.fieldOf("height").forGetter(CraftingInput::height),
        Codec.list(ItemStack.OPTIONAL_CODEC).fieldOf("items").forGetter(CraftingInput::items)
    ).apply(instance, CraftingInput::of));

    private static final Codec<CraftingInput.Positioned> POSITIONED_INPUT_CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            INPUT_CODEC.fieldOf("input").forGetter(CraftingInput.Positioned::input),
            Codec.INT.fieldOf("left").forGetter(CraftingInput.Positioned::left),
            Codec.INT.fieldOf("top").forGetter(CraftingInput.Positioned::top)
        ).apply(instance, CraftingInput.Positioned::new));

    public static final Codec<WirelessCraftingGridState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        POSITIONED_INPUT_CODEC.fieldOf("input").forGetter(WirelessCraftingGridState::input)
    ).apply(instance, WirelessCraftingGridState::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, CraftingInput> INPUT_STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, CraftingInput::width,
        ByteBufCodecs.INT, CraftingInput::height,
        ByteBufCodecs.collection(ArrayList::new, ItemStack.OPTIONAL_STREAM_CODEC), CraftingInput::items,
        CraftingInput::of
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, CraftingInput.Positioned> POSITIONED_INPUT_STREAM_CODEC =
        StreamCodec.composite(
            INPUT_STREAM_CODEC, CraftingInput.Positioned::input,
            ByteBufCodecs.INT, CraftingInput.Positioned::left,
            ByteBufCodecs.INT, CraftingInput.Positioned::top,
            CraftingInput.Positioned::new
        );

    public static final StreamCodec<RegistryFriendlyByteBuf, WirelessCraftingGridState> STREAM_CODEC =
        StreamCodec.composite(
            POSITIONED_INPUT_STREAM_CODEC, WirelessCraftingGridState::input,
            WirelessCraftingGridState::new
        );
}
