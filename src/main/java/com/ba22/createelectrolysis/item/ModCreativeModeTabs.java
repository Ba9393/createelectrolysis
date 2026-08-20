package com.ba22.createelectrolysis.item;

import com.ba22.createelectrolysis.CreateElectrolysis;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateElectrolysis.MODID);

    public static final Supplier<CreativeModeTab> CREATE_ELCTROLYSIS_TAB = CREATIVE_MODE_TABS.register("create_electrolysis",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.OXYGEN_BUCKET.get()))
                    .title(Component.translatable("Create Electrolysis"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.OXYGEN_BUCKET.get());
                        output.accept(ModItems.HYDROGEN_BUCKET.get());
                        output.accept(ModItems.RODS_ITEM.get());
                        output.accept(ModItems.ELECTROLYSER.get());
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
