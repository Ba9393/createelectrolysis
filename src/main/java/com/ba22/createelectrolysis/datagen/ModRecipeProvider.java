package com.ba22.createelectrolysis.datagen;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.fluid.ModFluids;
import com.ba22.createelectrolysis.item.ModItems;
import com.ba22.createelectrolysis.recipe.ElectrolysisRecipe;
import com.simibubi.create.AllItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * This class is responsible for generating recipes for the mod.
 * It extends RecipeProvider and defines the recipes for items and blocks in the mod.
 */

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        FluidStack inputWater = new FluidStack(Fluids.WATER, 30);
        FluidStack outputHydrogen = new FluidStack(ModFluids.HYDROGEN_SOURCE.get(), 20);
        FluidStack outputOxygen = new FluidStack(ModFluids.OXYGEN_SOURCE.get(), 10);
        
        // Define the recipe for water electrolysis
        ElectrolysisRecipe waterElectrolysis = new ElectrolysisRecipe(inputWater, 10, outputHydrogen, outputOxygen);
        recipeOutput.accept(ResourceLocation.fromNamespaceAndPath(CreateElectrolysis.MODID, "water_electrolysis"), 
                waterElectrolysis, null);
        
        // Define the recipe for the RODS_ITEM
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RODS_ITEM.get(), 4)
                .pattern("###")
                .pattern("I I")
                .pattern("I I")
                .define('#', AllItems.ANDESITE_ALLOY.get())
                .define('I', Items.IRON_NUGGET)
                .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.get()))
                .save(recipeOutput);
    }
}
