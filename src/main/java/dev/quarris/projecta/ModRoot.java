package dev.quarris.projecta;

import dev.quarris.projecta.registry.ContentRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModRef.ID)
public class ModRoot {

    public ModRoot() {
        ContentRegistry.init(FMLJavaModLoadingContext.get().getModEventBus());
    }
}