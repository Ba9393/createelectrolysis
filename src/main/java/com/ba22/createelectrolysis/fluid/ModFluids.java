package com.ba22.createelectrolysis.fluid;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.block.ModBlocks;
import com.ba22.createelectrolysis.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, CreateElectrolysis.MODID);

    /** OXYGEN */
    public static final BaseFlowingFluid.Properties OXYGEN_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.OXYGEN_TYPE,
            () -> ModFluids.OXYGEN_SOURCE.get(),
            () -> ModFluids.OXYGEN_FLOWING.get()
    ).block(() -> ModBlocks.OXYGEN.get()).bucket(() -> ModItems.OXYGEN_BUCKET.get());

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> OXYGEN_SOURCE =
            FLUIDS.register("oxygen", () -> new BaseFlowingFluid.Source(OXYGEN_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> OXYGEN_FLOWING =
            FLUIDS.register("oxygen_flowing", () -> new BaseFlowingFluid.Flowing(OXYGEN_PROPERTIES));

    /** HYDROGEN */
    public static final BaseFlowingFluid.Properties HYDROGEN_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.HYDROGEN_TYPE,
            () -> ModFluids.HYDROGEN_SOURCE.get(),
            () -> ModFluids.HYDROGEN_FLOWING.get()
    ).block(() -> ModBlocks.HYDROGEN.get()).bucket(() -> ModItems.HYDROGEN_BUCKET.get());

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> HYDROGEN_SOURCE =
            FLUIDS.register("hydrogen", () -> new BaseFlowingFluid.Source(HYDROGEN_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> HYDROGEN_FLOWING =
            FLUIDS.register("hydrogen_flowing", () -> new BaseFlowingFluid.Flowing(HYDROGEN_PROPERTIES));

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}