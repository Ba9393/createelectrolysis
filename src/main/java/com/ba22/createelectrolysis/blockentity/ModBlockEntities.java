package com.ba22.createelectrolysis.blockentity;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateElectrolysis.MODID);

    public static final Supplier<BlockEntityType<ElectrolyserBlockEntity>> ELECTROLYSER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "electrolyser_block_entity",
            () -> BlockEntityType.Builder.of(
                            ElectrolyserBlockEntity::new,
                            ModBlocks.ELECTROLYSER.get()
                    )
                    .build(null)
    );
}