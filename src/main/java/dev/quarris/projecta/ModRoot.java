package dev.quarris.projecta;

import dev.quarris.projecta.registry.Content;
import dev.quarris.projecta.registry.RecipeTypeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModRef.ID)
public class ModRoot {

    public ModRoot() {
        RecipeTypeRegistry.registerRecipeTypeListener(FMLJavaModLoadingContext.get().getModEventBus());
        Content.init(FMLJavaModLoadingContext.get().getModEventBus());
    }
}