package com.ba22.createelectrolysis;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Properties;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluidHydrogen {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, CreateElectrolysis.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, CreateElectrolysis.MODID);
    public static final DeferredRegister<Item> BUCKETS = DeferredRegister.createItems(CreateElectrolysis.MODID);
    public static final DeferredRegister<Block> SOURCEBLOCKS = DeferredRegister.createBlocks(CreateElectrolysis.MODID);

    public static final DeferredHolder<FluidType, FluidType> LIQUID_TYPE = FLUID_TYPES.register("hydrogen", () -> new FluidType(FluidType.Properties.create().descriptionId("fluid.createelectrolysis.hydrogen")));

    public static final DeferredHolder<Fluid, FlowingFluid> LIQUID_SOURCE = FLUIDS.register("hydrogen_source", () -> new BaseFlowingFluid.Source(liquidProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> LIQUID_FLOWING = FLUIDS.register("hydrogen_flowing", () -> new BaseFlowingFluid.Flowing(liquidProperties()));

    public static final DeferredHolder<Item, BucketItem> LIQUID_BUCKET = BUCKETS.register("hydrogen_bucket", () -> new BucketItem(LIQUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredHolder<Block, LiquidBlock> LIQUID_BLOCK = SOURCEBLOCKS.register("hydrogen_block", () -> new LiquidBlock(LIQUID_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static void register(IEventBus modbus) {
        FLUID_TYPES.register(modbus);
        FLUIDS.register(modbus);
        BUCKETS.register(modbus);
        SOURCEBLOCKS.register(modbus);
        modbus.addListener(ModFluidHydrogen::clientExt);
    }

    public static DeferredHolder<Item, BucketItem> getBucket() {
        return LIQUID_BUCKET;
    }

    private static final IClientFluidTypeExtensions liquidExt = new IClientFluidTypeExtensions() {
        @Override
        public ResourceLocation getStillTexture() {
            return CreateElectrolysis.rl("block/hydrogen_still");
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return CreateElectrolysis.rl("block/hydrogen_flowing");
        }
    };

    private static void clientExt(RegisterClientExtensionsEvent event) {
        event.registerFluidType(liquidExt, LIQUID_TYPE.get());
    }

    private static Properties liquidProperties() {
        return new Properties(LIQUID_TYPE, LIQUID_SOURCE, LIQUID_FLOWING).bucket(LIQUID_BUCKET);
    }
}
