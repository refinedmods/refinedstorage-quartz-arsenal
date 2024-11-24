package com.refinedmods.refinedstorage.quartzarsenal.common;

import net.minecraft.resources.ResourceLocation;

import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.createQuartzArsenalIdentifier;

public final class ContentIds {
    public static final ResourceLocation WIRELESS_CRAFTING_GRID =
        createQuartzArsenalIdentifier("wireless_crafting_grid");
    public static final ResourceLocation CREATIVE_WIRELESS_CRAFTING_GRID =
        createQuartzArsenalIdentifier("creative_wireless_crafting_grid");

    private ContentIds() {
    }
}
