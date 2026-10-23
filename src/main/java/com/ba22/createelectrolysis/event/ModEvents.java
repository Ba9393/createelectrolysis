package com.ba22.createelectrolysis.event;

import com.ba22.createelectrolysis.CreateElectrolysis;
import com.ba22.createelectrolysis.block.ModBlocks;
import com.ba22.createelectrolysis.fluid.ModFluidTypes;
import com.ba22.createelectrolysis.fluid.ModFluids;
import com.ba22.createelectrolysis.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = CreateElectrolysis.MODID, value = Dist.CLIENT)
public class ModEvents {

    
    // Handle right-clicking on a Create Basin with the Rods item to replace it with the Electrolyser block
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide) return;

        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldItem = event.getItemStack();
        BlockState state = level.getBlockState(pos);
        
        if (heldItem.getItem() == ModItems.RODS_ITEM.get()) {
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());

            if (blockId.equals(ResourceLocation.fromNamespaceAndPath("create", "basin"))) {
                level.setBlock(pos, ModBlocks.ELECTROLYSER.get().defaultBlockState(), 3);

                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
    
    // Register the custom fluid textures for Oxygen and Hydrogen
    @SubscribeEvent
    public static void onClientExtensions(RegisterClientExtensionsEvent event) {

        // Assign the textures to Oxygen
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return ResourceLocation.fromNamespaceAndPath(CreateElectrolysis.MODID, "block/oxygen_still");
            }
            @Override
            public ResourceLocation getFlowingTexture() {
                return ResourceLocation.fromNamespaceAndPath(CreateElectrolysis.MODID, "block/oxygen_flowing");
            }
        }, ModFluidTypes.OXYGEN_TYPE.get());

        // Assign the textures to Hydrogen
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return ResourceLocation.fromNamespaceAndPath(CreateElectrolysis.MODID, "block/hydrogen_still");
            }
            @Override
            public ResourceLocation getFlowingTexture() {
                return ResourceLocation.fromNamespaceAndPath(CreateElectrolysis.MODID, "block/hydrogen_flowing");
            }
        }, ModFluidTypes.HYDROGEN_TYPE.get());
    }

    // Register the custom fluid texture renders making them translucent
    // This ensures that placing the fluids in the world will render them correctly with transparency
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.HYDROGEN_SOURCE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.HYDROGEN_FLOWING.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.OXYGEN_SOURCE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.OXYGEN_FLOWING.get(), RenderType.translucent());
        });
    }
}