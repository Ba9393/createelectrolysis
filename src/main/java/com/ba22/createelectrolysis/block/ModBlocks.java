package com.ba22.createelectrolysis.block;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.fluid.ModFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateElectrolysis.MODID);
    
    /** FLUID BLOCKS */
    public static final DeferredBlock<LiquidBlock> HYDROGEN = BLOCKS.register("hydrogen_block",
            () -> new LiquidBlock(ModFluids.HYDROGEN_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static final DeferredBlock<LiquidBlock> OXYGEN = BLOCKS.register("oxygen_block",
            () -> new LiquidBlock(ModFluids.OXYGEN_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    /** MACHINES */
    public static final DeferredBlock<Block> ELECTROLYSER = BLOCKS.register("electrolyser",
            () -> new ElectrolyserBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .strength(3f)
                    .explosionResistance(1200f)
                    .requiresCorrectToolForDrops()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}