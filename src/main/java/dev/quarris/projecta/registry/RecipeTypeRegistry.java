package dev.quarris.projecta.registry;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.content.recipe.AlchemicalBrewingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import quarris.qlib.api.registry.ContentRegistry;

import java.util.ArrayList;
import java.util.List;

public class RecipeTypeRegistry extends ContentRegistry<RecipeSerializer<?>> {

    private static final List<RecipeType<?>> registeredRecipeTypes = new ArrayList<>();

    public final RecipeType<AlchemicalBrewingRecipe> alchemicalBrewingType = registerRecipeType(ModRef.res("alchemical_brewing"));

    public final RegistryObject<AlchemicalBrewingRecipe.Serializer> alchemicalBrewingSerializer = this.register("alchemical_brewing", AlchemicalBrewingRecipe.Serializer::new);

    public RecipeTypeRegistry(String modid, IForgeRegistry<RecipeSerializer<?>> registry) {
        super(modid, registry);
    }

    public static void registerRecipeTypeListener(IEventBus bus) {
        bus.addGenericListener(ForgeRegistries.RECIPE_SERIALIZERS.getRegistrySuperType(), (RegistryEvent.Register<RecipeSerializer<?>> event) -> registeredRecipeTypes.forEach(type -> Registry.register(Registry.RECIPE_TYPE, type.toString(), type)));
    }

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(ResourceLocation id) {
        RecipeType<T> type = new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        };

        registeredRecipeTypes.add(type);
        return type;
    }
}
