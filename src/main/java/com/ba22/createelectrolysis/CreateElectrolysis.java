package com.ba22.createelectrolysis;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.slf4j.Logger;
// Add these imports
import net.neoforged.neoforge.registries.DeferredHolder;

import com.mojang.logging.LogUtils;


import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreateElectrolysis.MODID)
public class CreateElectrolysis {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "createelectrolysis";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "createelectrolysis" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "createelectrolysis" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "createelectrolysis" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "createelectrolysis:electrolyser", combining the namespace and path
    public static final DeferredBlock<MyEntityBlock> ELECTROLYSER = BLOCKS.register("electrolyser", () -> new MyEntityBlock(BlockBehaviour.Properties.of().noOcclusion()));
    // Creates a new BlockItem with the id "createelectrolysis:electrolyser", combining the namespace and path
    public static final DeferredItem<BlockItem> ELECTROLYSER_ITEM = ITEMS.registerSimpleBlockItem("electrolyser", ELECTROLYSER);

    // Creates a new food item with the id "createelectrolysis:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> RODS_ITEM = ITEMS.registerSimpleItem("rods", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    public static final DeferredHolder<Item, BucketItem> HYDROGEN_BUCKET = ModFluidHydrogen.getBucket();
    public static final DeferredHolder<Item, BucketItem> OXYGEN_BUCKET = ModFluidOxygen.getBucket();

    // Creates a creative tab with the id "createelectrolysis:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.createelectrolysis")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ELECTROLYSER_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(RODS_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(ELECTROLYSER_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(HYDROGEN_BUCKET.get());
                output.accept(OXYGEN_BUCKET.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CreateElectrolysis(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(MyModCapabilities::registerCapabilities);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        ModFluidHydrogen.register(modEventBus);
        ModFluidOxygen.register(modEventBus);

        // ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);

        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (CreateElectrolysis) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        // modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        // LOGGER.info("HELLO FROM COMMON SETUP");

        // if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
        //    LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        // }

        // LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        // Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
    //        event.accept(ELECTROLYSER_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // ===== NEW: Right-click Basin with Rods -> replace with Electrolyser =====
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide) return; // server-side only

        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldItem = event.getItemStack();

        var state = level.getBlockState(pos);

        // Get the block registry and ask for the registry name of this block instance
        var blockRegistry = level.registryAccess().registryOrThrow(Registries.BLOCK);
        ResourceLocation id = blockRegistry.getKey(state.getBlock()); // may be null for unregistered blocks

        // Use ResourceLocation factory instead of private constructor
        if (id != null && id.equals(ResourceLocation.fromNamespaceAndPath("create", "basin"))) {
            if (heldItem.getItem() == RODS_ITEM.get()) {
                level.setBlock(pos, ELECTROLYSER.get().defaultBlockState(), 3);

                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
