package com.refinedmods.refinedstorage.quartzarsenal.common;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public final class QuartzArsenalIdentifierUtil {
    public static final String QUARTZ_ARSENAL_MOD_ID = "refinedstorage_quartz_arsenal";

    private QuartzArsenalIdentifierUtil() {
    }

    public static ResourceLocation createQuartzArsenalIdentifier(final String value) {
        return ResourceLocation.fromNamespaceAndPath(QUARTZ_ARSENAL_MOD_ID, value);
    }

    public static MutableComponent createQuartzArsenalTranslation(final String category, final String value) {
        return Component.translatable(createQuartzArsenalTranslationKey(category, value));
    }

    public static String createQuartzArsenalTranslationKey(final String category, final String value) {
        return String.format("%s.%s.%s", category, QUARTZ_ARSENAL_MOD_ID, value);
    }
}
