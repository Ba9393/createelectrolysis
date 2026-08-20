package com.ba22.createelectrolysis.datagen;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * This class is responsible for generating item models for the mod.
 * It extends ItemModelProvider.
 */

public class ModItemModelProvider extends ItemModelProvider {
    
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CreateElectrolysis.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.RODS_ITEM.get());
        basicItem(ModItems.OXYGEN_BUCKET.get());
        basicItem(ModItems.HYDROGEN_BUCKET.get());
        withExistingParent("electrolyser", modLoc("block/electrolyser"));
    }
    
}
