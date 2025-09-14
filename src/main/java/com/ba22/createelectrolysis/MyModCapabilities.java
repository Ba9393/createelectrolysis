package com.ba22.createelectrolysis;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class MyModCapabilities {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.MY_BLOCK_ENTITY.get(),
                (blockEntity, side) -> {
                    if (blockEntity instanceof MyBlockEntity) {
                        return ((MyBlockEntity) blockEntity).getEnergyStorage();
                    }
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.MY_BLOCK_ENTITY.get(),
                (blockEntity, side) -> {
                    if (blockEntity instanceof MyBlockEntity be) {
                        return be.getCombinedHandler();
                    }
                    return null;
                }
        );


        // other tanks similarly
    }
}
