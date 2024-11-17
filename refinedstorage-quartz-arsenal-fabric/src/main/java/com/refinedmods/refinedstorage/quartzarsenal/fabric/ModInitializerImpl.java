package com.refinedmods.refinedstorage.quartzarsenal.fabric;

import com.refinedmods.refinedstorage.quartzarsenal.common.Common;

import net.fabricmc.api.ModInitializer;

public class ModInitializerImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        Common.helloWorld();
    }
}
