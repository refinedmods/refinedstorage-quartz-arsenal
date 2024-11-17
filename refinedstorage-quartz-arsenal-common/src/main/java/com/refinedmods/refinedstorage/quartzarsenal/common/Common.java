package com.refinedmods.refinedstorage.quartzarsenal.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

public class Common {
    public static final String MOD_ID = "refinedstorage_quartz_arsenal";

    private Common() {
    }

    public static void helloWorld() {
        System.out.println("Hello World! " + BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
    }
}
