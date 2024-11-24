package com.refinedmods.refinedstorage.quartzarsenal.common;

import net.minecraft.network.chat.MutableComponent;

import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.QUARTZ_ARSENAL_MOD_ID;
import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.createQuartzArsenalTranslation;
import static com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil.createQuartzArsenalTranslationKey;

public final class ContentNames {
    public static final String MOD_TRANSLATION_KEY = "mod." + QUARTZ_ARSENAL_MOD_ID;
    public static final String OPEN_WIRELESS_CRAFTING_GRID_TRANSLATION_KEY = createQuartzArsenalTranslationKey(
        "key", "open_wireless_crafting_grid"
    );
    public static final MutableComponent WIRELESS_CRAFTING_GRID = createQuartzArsenalTranslation(
        "item", "wireless_crafting_grid"
    );
    public static final MutableComponent CREATIVE_WIRELESS_CRAFTING_GRID = createQuartzArsenalTranslation(
        "item", "creative_wireless_crafting_grid"
    );

    private ContentNames() {
    }
}
