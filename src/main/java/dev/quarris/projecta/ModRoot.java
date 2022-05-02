package dev.quarris.projecta;

import dev.quarris.projecta.registry.Content;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModRef.ID)
public class ModRoot {

    public ModRoot() {
        Content.init(FMLJavaModLoadingContext.get().getModEventBus());
    }
}