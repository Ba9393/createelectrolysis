package com.ba22.createelectrolysis;

import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.security.DrbgParameters;

public class MyBlockEntity extends BlockEntity {
    // --- Forge Energy ---
    private final EnergyStorage energyStorage = new EnergyStorage(100000);

    // --- Fluids ---
    private final FluidTank hydrogenTank = new FluidTank(4000, stack ->
            stack.getFluid().isSame(ModFluidHydrogen.LIQUID_SOURCE.get()));
    private final FluidTank oxygenTank   = new FluidTank(2000, stack ->
            stack.getFluid().isSame(ModFluidOxygen.LIQUID_SOURCE.get()));
    private final FluidTank waterTank    = new FluidTank(6000, stack ->
            stack.getFluid().isSame(Fluids.WATER));

    private final IFluidHandler CombinedHandler = new CombinedTankWrapper(waterTank, hydrogenTank, oxygenTank);

    public final IFluidHandler routerHandler = new IFluidHandler() {
        @Override
        public int getTanks() { return 3; }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return switch (tank) {
                case 0 -> waterTank.getFluid();
                case 1 -> hydrogenTank.getFluid();
                case 2 -> oxygenTank.getFluid();
                default -> FluidStack.EMPTY;
            };
        }

        @Override
        public int getTankCapacity(int tank) {
            return switch (tank) {
                case 0 -> waterTank.getCapacity();
                case 1 -> hydrogenTank.getCapacity();
                case 2 -> oxygenTank.getCapacity();
                default -> 0;
            };
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return switch (tank) {
                case 0 -> waterTank.isFluidValid(stack);
                case 1 -> hydrogenTank.isFluidValid(stack);
                case 2 -> oxygenTank.isFluidValid(stack);
                default -> false;
            };
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (waterTank.isFluidValid(resource)) return waterTank.fill(resource, action);
            if (hydrogenTank.isFluidValid(resource)) return hydrogenTank.fill(resource, action);
            if (oxygenTank.isFluidValid(resource)) return oxygenTank.fill(resource, action);
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (waterTank.isFluidValid(resource)) return waterTank.drain(resource, action);
            if (hydrogenTank.isFluidValid(resource)) return hydrogenTank.drain(resource, action);
            if (oxygenTank.isFluidValid(resource)) return oxygenTank.drain(resource, action);
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            // Optional: choose drain priority
            if (!waterTank.isEmpty()) return waterTank.drain(maxDrain, action);
            if (!hydrogenTank.isEmpty()) return hydrogenTank.drain(maxDrain, action);
            if (!oxygenTank.isEmpty()) return oxygenTank.drain(maxDrain, action);
            return FluidStack.EMPTY;
        }
    };


    public MyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MY_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MyBlockEntity be) {
        if (level.isClientSide) return; // only run server-side

        int waterNeeded = 30;
        int energyNeeded = 10;

        FluidStack hydrogenOut = new FluidStack(ModFluidHydrogen.LIQUID_SOURCE.get(), 20);
        FluidStack oxygenOut   = new FluidStack(ModFluidOxygen.LIQUID_SOURCE.get(), 10);

        boolean hasWater = be.getWaterTank().drain(waterNeeded, IFluidHandler.FluidAction.SIMULATE).getAmount() == waterNeeded;
        boolean hasEnergy = be.getEnergyStorage().extractEnergy(energyNeeded, true) == energyNeeded;

        if (hasWater && hasEnergy) {
            boolean canHydrogen = be.getHydrogenTank().fill(hydrogenOut, IFluidHandler.FluidAction.SIMULATE) == hydrogenOut.getAmount();
            boolean canOxygen   = be.getOxygenTank().fill(oxygenOut, IFluidHandler.FluidAction.SIMULATE) == oxygenOut.getAmount();

            if (canHydrogen && canOxygen) {
                // consume
                be.getWaterTank().drain(waterNeeded, IFluidHandler.FluidAction.EXECUTE);
                be.getEnergyStorage().extractEnergy(energyNeeded, false);

                // produce
                be.getHydrogenTank().fill(hydrogenOut, IFluidHandler.FluidAction.EXECUTE);
                be.getOxygenTank().fill(oxygenOut, IFluidHandler.FluidAction.EXECUTE);

                // mark dirty so it saves & syncs
                setChanged(level, pos, state);
            }
        }
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public FluidTank getHydrogenTank() {
        return hydrogenTank;
    }

    public FluidTank getOxygenTank() {
        return oxygenTank;
    }

    public FluidTank getWaterTank() {
        return waterTank;
    }

    public  IFluidHandler getCombinedHandler() {
        return CombinedHandler;
    }
}
