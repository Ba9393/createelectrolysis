package com.ba22.createelectrolysis.block;

import com.ba22.createelectrolysis.blockentity.ModBlockEntities;
import com.ba22.createelectrolysis.blockentity.ElectrolyserBlockEntity;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ElectrolyserBlock extends Block implements EntityBlock, IWrenchable {

    public ElectrolyserBlock(Properties properties) {
        super(properties);
    }

    // Create the BlockEntity for this block
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectrolyserBlockEntity(pos, state);
    }

    // Provide the ticker for the BlockEntity
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.ELECTROLYSER_BLOCK_ENTITY.get()
                ? (lvl, pos, st, be) -> {
            if (be instanceof ElectrolyserBlockEntity myBe) {
                ElectrolyserBlockEntity.tick(lvl, pos, st, myBe);
            }
        }
                : null;
    }
}