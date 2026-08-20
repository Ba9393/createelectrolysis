package com.ba22.createelectrolysis;

import com.ba22.createelectrolysis.block.ModBlocks;
import com.ba22.createelectrolysis.blockentity.ModBlockEntities;
import com.ba22.createelectrolysis.datagen.DataGenerators;
import com.ba22.createelectrolysis.event.ModCapabilitiesEvent;
import com.ba22.createelectrolysis.fluid.ModFluidTypes;
import com.ba22.createelectrolysis.fluid.ModFluids;
import com.ba22.createelectrolysis.item.ModCreativeModeTabs;
import com.ba22.createelectrolysis.item.ModItems;
import com.ba22.createelectrolysis.recipe.ModRecipeTypes;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CreateElectrolysis.MODID)
public class CreateElectrolysis {
    public static final String MODID = "createelectrolysis";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateElectrolysis(IEventBus modEventBus, ModContainer modContainer) {

        // Register the deferred registers
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModFluidTypes.FLUID_TYPES.register(modEventBus);
        ModFluids.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModRecipeTypes.register(modEventBus);

        // Register the capabilities
        modEventBus.addListener(ModCapabilitiesEvent::registerCapabilities);
        // Register the data generators
        modEventBus.addListener(DataGenerators::gatherData);

    }
}