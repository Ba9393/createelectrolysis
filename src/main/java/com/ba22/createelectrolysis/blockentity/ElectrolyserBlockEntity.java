package com.ba22.createelectrolysis.blockentity;

import com.ba22.createelectrolysis.fluid.ModFluids;
import com.ba22.createelectrolysis.recipe.ElectrolysisRecipe;
import com.ba22.createelectrolysis.recipe.ModRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class ElectrolyserBlockEntity extends BlockEntity implements IHaveGoggleInformation {
    
    public class CustomEnergyStorage extends EnergyStorage {
        public CustomEnergyStorage(int capacity, int maxReceive) {
            super(capacity, maxReceive, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) setChanged();
            return received;
        }
        
        public boolean consumeInternal(int amount, boolean simulate) {
            if (this.energy >= amount) {
                if (!simulate) {
                    this.energy -= amount;
                    setChanged();
                }
                return true;
            }
            return false;
        }
    }

    // --- Forge Energy (FE) ---
    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(100000, 10000);

    // --- Fluids ---
    private final FluidTank hydrogenTank = new FluidTank(4000, stack -> stack.getFluid().isSame(ModFluids.HYDROGEN_SOURCE.get())) {
        @Override protected void onContentsChanged() { setChanged(); }
    };
    private final FluidTank oxygenTank = new FluidTank(2000, stack -> stack.getFluid().isSame(ModFluids.OXYGEN_SOURCE.get())) {
        @Override protected void onContentsChanged() { setChanged(); }
    };
    private final FluidTank waterTank = new FluidTank(6000, stack -> stack.getFluid().isSame(Fluids.WATER)) {
        @Override protected void onContentsChanged() { setChanged(); }
    };

    private final IFluidHandler CombinedHandler = new CombinedTankWrapper(waterTank, hydrogenTank, oxygenTank);

    public ElectrolyserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTROLYSER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ElectrolyserBlockEntity be) {
        if (level.isClientSide) return;

        boolean recipeProcessed = false;
        var recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ELECTROLYSIS_TYPE.get());

        for (RecipeHolder<ElectrolysisRecipe> holder : recipes) {
            ElectrolysisRecipe recipe = holder.value();

            int waterNeeded = recipe.inputFluid().getAmount();
            int energyNeeded = recipe.energyRequired();
            FluidStack recipeInput = recipe.inputFluid();
            FluidStack hydrogenOut = recipe.outputFluid1();
            FluidStack oxygenOut = recipe.outputFluid2();

            if (be.getWaterTank().getFluid().is(recipeInput.getFluid()) && be.getWaterTank().getFluidAmount() >= waterNeeded) {
                boolean hasEnergy = be.energyStorage.consumeInternal(energyNeeded, true);

                if (hasEnergy) {
                    boolean canHydrogen = be.getHydrogenTank().fill(hydrogenOut, IFluidHandler.FluidAction.SIMULATE) == hydrogenOut.getAmount();
                    boolean canOxygen = be.getOxygenTank().fill(oxygenOut, IFluidHandler.FluidAction.SIMULATE) == oxygenOut.getAmount();

                    if (canHydrogen && canOxygen) {
                        // Consuma
                        be.getWaterTank().drain(waterNeeded, IFluidHandler.FluidAction.EXECUTE);
                        be.energyStorage.consumeInternal(energyNeeded, false);

                        // Produce
                        be.getHydrogenTank().fill(hydrogenOut, IFluidHandler.FluidAction.EXECUTE);
                        be.getOxygenTank().fill(oxygenOut, IFluidHandler.FluidAction.EXECUTE);

                        recipeProcessed = true;
                        break;
                    }
                }
            }
        }
        
        if (recipeProcessed || level.getGameTime() % 20 == 0) {
            be.forceSync();
        }
    }

    public void forceSync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Energy", energyStorage.serializeNBT(registries));
        tag.put("WaterTank", waterTank.writeToNBT(registries, new CompoundTag()));
        tag.put("HydrogenTank", hydrogenTank.writeToNBT(registries, new CompoundTag()));
        tag.put("OxygenTank", oxygenTank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(registries, tag.get("Energy"));
        }
        if (tag.contains("WaterTank")) {
            waterTank.readFromNBT(registries, tag.getCompound("WaterTank"));
        }
        if (tag.contains("HydrogenTank")) {
            hydrogenTank.readFromNBT(registries, tag.getCompound("HydrogenTank"));
        }
        if (tag.contains("OxygenTank")) {
            oxygenTank.readFromNBT(registries, tag.getCompound("OxygenTank"));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public EnergyStorage getEnergyStorage() { return energyStorage; }
    public FluidTank getHydrogenTank() { return hydrogenTank; }
    public FluidTank getOxygenTank() { return oxygenTank; }
    public FluidTank getWaterTank() { return waterTank; }
    public IFluidHandler getCombinedHandler() { return CombinedHandler; }

    // This method let the player see the information when looking at the electrolyser with goggles on.
    // Shows the current FE and the fluid levels (water, hydrogen and oxygen).
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Component.literal("    ").append(Component.literal("Energy Stored:").withStyle(ChatFormatting.YELLOW)));
        tooltip.add(Component.literal(" " + energyStorage.getEnergyStored() + " / " + energyStorage.getMaxEnergyStored() + " FE")
                .withStyle(ChatFormatting.AQUA));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("Fluid Levels:").withStyle(ChatFormatting.GRAY));
        formatTankTooltip(tooltip, waterTank, "Water");
        formatTankTooltip(tooltip, hydrogenTank, "Hydrogen");
        formatTankTooltip(tooltip, oxygenTank, "Oxygen");

        return true;
    }

    private void formatTankTooltip(List<Component> tooltip, FluidTank tank, String label) {
        if (tank.isEmpty()) {
            tooltip.add(Component.literal(" • " + label + ": ").withStyle(ChatFormatting.DARK_GRAY)
                    .append(Component.literal("Empty").withStyle(ChatFormatting.RED)));
        } else {
            tooltip.add(Component.literal(" • " + label + ": ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(tank.getFluidAmount() + " / " + tank.getCapacity() + " mB")
                            .withStyle(ChatFormatting.GREEN)));
        }
    }
}