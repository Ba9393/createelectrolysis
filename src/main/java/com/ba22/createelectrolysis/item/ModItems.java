package com.ba22.createelectrolysis.item;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.block.ModBlocks;
import com.ba22.createelectrolysis.fluid.ModFluids;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Registry holder for mod items.
 * This class creates and registers item entries used by the mod.
 */
public class ModItems {

    /**
     * Deferred register used to register items under the mod ID.
     */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateElectrolysis.MODID);
    

    /** OXYGEN */
    public static final DeferredItem<Item> OXYGEN_BUCKET = ITEMS.register("oxygen_bucket",
            () -> new BucketItem(ModFluids.OXYGEN_SOURCE.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    
    /** HYDROGEN */
    public static final DeferredItem<Item> HYDROGEN_BUCKET = ITEMS.register("hydrogen_bucket",
            () -> new BucketItem(ModFluids.HYDROGEN_SOURCE.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    
    /** RODS */
    public static final DeferredItem<Item> RODS_ITEM = ITEMS.registerSimpleItem("rods", new Item.Properties());
    
    /** ELCTROLYSER */
    public static final DeferredItem<Item> ELECTROLYSER = ITEMS.register("electrolyser",
            () -> new BlockItem(ModBlocks.ELECTROLYSER.get(), new Item.Properties()));
    

    /**
     * Registers the deferred item registry on the provided event bus so entries are created at the correct time.
     * @param eventBus Event bus used by the mod loading system
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}