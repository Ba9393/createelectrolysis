package com.ba22.createelectrolysis.event;

import com.ba22.createelectrolysis.blockentity.ModBlockEntities;
import com.ba22.createelectrolysis.blockentity.ElectrolyserBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;


public class ModCapabilitiesEvent {
    
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.ELECTROLYSER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> {
                    if (blockEntity instanceof ElectrolyserBlockEntity) {
                        return (blockEntity).getEnergyStorage();
                    }
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.ELECTROLYSER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> {
                    if (blockEntity instanceof ElectrolyserBlockEntity be) {
                        return be.getCombinedHandler();
                    }
                    return null;
                }
        );


        // other tanks similarly
    }
}
