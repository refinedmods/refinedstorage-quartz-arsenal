package com.refinedmods.refinedstorage.quartzarsenal.neoforge;

import com.refinedmods.refinedstorage.quartzarsenal.common.Common;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Common.MOD_ID)
public class ModInitializer {
    public ModInitializer(final IEventBus eventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(ClientModInitializer::onClientSetup);
        }
        eventBus.addListener(this::onRegister);
    }

    @SubscribeEvent
    public void onRegister(final RegisterEvent e) {
        Common.helloWorld();
    }
}
