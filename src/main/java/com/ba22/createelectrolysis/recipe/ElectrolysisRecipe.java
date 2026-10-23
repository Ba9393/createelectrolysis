package com.ba22.createelectrolysis.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public record ElectrolysisRecipe(FluidStack inputFluid, int energyRequired, FluidStack outputFluid1, FluidStack outputFluid2) implements Recipe<RecipeInput> {

    // Codec per leggere e scrivere il file JSON
    public static final MapCodec<ElectrolysisRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            FluidStack.CODEC.fieldOf("input").forGetter(ElectrolysisRecipe::inputFluid),
            Codec.INT.fieldOf("energy").forGetter(ElectrolysisRecipe::energyRequired),
            FluidStack.CODEC.fieldOf("output1").forGetter(ElectrolysisRecipe::outputFluid1),
            FluidStack.CODEC.fieldOf("output2").forGetter(ElectrolysisRecipe::outputFluid2)
    ).apply(inst, ElectrolysisRecipe::new));

    // Codec per la sincronizzazione di rete tra Server e Client
    public static final StreamCodec<RegistryFriendlyByteBuf, ElectrolysisRecipe> STREAM_CODEC = StreamCodec.composite(
            FluidStack.STREAM_CODEC, ElectrolysisRecipe::inputFluid,
            ByteBufCodecs.INT, ElectrolysisRecipe::energyRequired,
            FluidStack.STREAM_CODEC, ElectrolysisRecipe::outputFluid1,
            FluidStack.STREAM_CODEC, ElectrolysisRecipe::outputFluid2,
            ElectrolysisRecipe::new
    );

    @Override
    public boolean matches(RecipeInput input, Level level) {
        // La validazione verrà gestita manualmente nel BlockEntity per via dei fluidi
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.ELECTROLYSIS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.ELECTROLYSIS_TYPE.get();
    }
}