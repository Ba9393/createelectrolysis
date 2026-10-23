package com.ba22.createelectrolysis.fluid;

import com.ba22.createelectrolysis.CreateElectrolysis;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, CreateElectrolysis.MODID);

    /** OXYGEN */
    public static final DeferredHolder<FluidType, FluidType> OXYGEN_TYPE = FLUID_TYPES.register("oxygen",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("Oxygen")
                    .density(-10)       
                    .viscosity(0)
                    .canSwim(false)
                    .canDrown(false)
                    .canPushEntity(false)
                    .supportsBoating(false)
                    .canExtinguish(false)));

    /** HYDROGEN */
    public static final DeferredHolder<FluidType, FluidType> HYDROGEN_TYPE = FLUID_TYPES.register("hydrogen",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("Hydrogen")
                    .density(-10)
                    .viscosity(0)
                    .canSwim(false)
                    .canDrown(false)
                    .canPushEntity(false)
                    .supportsBoating(false)
                    .canExtinguish(false)));
}