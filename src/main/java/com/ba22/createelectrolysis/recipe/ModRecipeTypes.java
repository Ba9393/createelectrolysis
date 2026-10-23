package com.ba22.createelectrolysis.recipe;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CreateElectrolysis.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateElectrolysis.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectrolysisRecipe>> ELECTROLYSIS_TYPE = RECIPE_TYPES.register("electrolysis", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "electrolysis";
        }
    });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ElectrolysisRecipe>> ELECTROLYSIS_SERIALIZER = SERIALIZERS.register("electrolysis", () -> new RecipeSerializer<>() {
        @Override
        public MapCodec<ElectrolysisRecipe> codec() {
            return ElectrolysisRecipe.CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ElectrolysisRecipe> streamCodec() {
            return ElectrolysisRecipe.STREAM_CODEC;
        }
    });

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}