package com.ba22.createelectrolysis.datagen;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.fluid.ModFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * This class is responsible for generating fluid tags for the mod.
 * It extends FluidTagsProvider and defines the tags for fluids in the mod.
 */

public class ModFluidTagProvider extends FluidTagsProvider {
    public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, CreateElectrolysis.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider p_256366_) {
        tag(FluidTags.create(ResourceLocation.fromNamespaceAndPath(CreateElectrolysis.MODID, "gas")))
                .add(ModFluids.OXYGEN_SOURCE.get(), ModFluids.OXYGEN_FLOWING.get())
                .add(ModFluids.HYDROGEN_SOURCE.get(), ModFluids.HYDROGEN_FLOWING.get());
    }
}
