package com.ba22.createelectrolysis.datagen;

import com.ba22.createelectrolysis.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * This class is responsible for generating block states and models for the mod's blocks. 
 * It extends the BlockStateProvider class provided by NeoForge.
 */

public class ModBlockStateProvider extends BlockStateProvider  {
    
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, com.ba22.createelectrolysis.CreateElectrolysis.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ELECTROLYSER.get(),
                models().getExistingFile(modLoc("block/electrolyser")));
        
        fluidBlock(ModBlocks.OXYGEN, modLoc("block/oxygen_still"));
        fluidBlock(ModBlocks.HYDROGEN, modLoc("block/hydrogen_still"));
    }
    
    private void fluidBlock(DeferredBlock<?> deferredBlock, net.minecraft.resources.ResourceLocation particleTexture) {
        simpleBlock(deferredBlock.get(), models().getBuilder(deferredBlock.getId().getPath())
                .texture("particle", particleTexture)
                .renderType("minecraft:translucent"));
    }
    
}
