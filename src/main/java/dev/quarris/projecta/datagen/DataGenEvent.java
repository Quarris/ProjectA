package dev.quarris.projecta.datagen;

import dev.quarris.projecta.ModRef;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEvent {

    @SubscribeEvent
    public static void onDataGather(GatherDataEvent event) {
        event.getGenerator().addProvider(new BlockStateDataHandler(event.getGenerator(), ModRef.ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new ItemModelDataHandler(event.getGenerator(), ModRef.ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new BlockTagsDataHandler(event.getGenerator(), ModRef.ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new FluidTagsDataHandler(event.getGenerator(), ModRef.ID, event.getExistingFileHelper()));
    }
}
