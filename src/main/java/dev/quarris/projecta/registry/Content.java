package dev.quarris.projecta.registry;

import dev.quarris.projecta.ModRef;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

public class Content {

    public static final BlockRegistry BLOCKS = new BlockRegistry(ModRef.ID, ForgeRegistries.BLOCKS);
    public static final ItemRegistry ITEMS = new ItemRegistry(ModRef.ID, ForgeRegistries.ITEMS);
    public static final BlockEntityTypeRegistry BLOCK_ENTITY_TYPES = new BlockEntityTypeRegistry(ModRef.ID, ForgeRegistries.BLOCK_ENTITIES);
    public static final ParticleTypeRegistry PARTICLE_TYPES = new ParticleTypeRegistry(ModRef.ID, ForgeRegistries.PARTICLE_TYPES);
    public static final TagStorage TAGS = new TagStorage();
    public static final RecipeTypeRegistry RECIPE_TYPES = new RecipeTypeRegistry(ModRef.ID, ForgeRegistries.RECIPE_SERIALIZERS);

    public static void init(IEventBus bus) {
        BLOCKS.init(bus);
        ITEMS.init(bus);
        BLOCK_ENTITY_TYPES.init(bus);
        PARTICLE_TYPES.init(bus);
        RECIPE_TYPES.init(bus);
    }
}
