package dev.quarris.projecta.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import quarris.qlib.api.registry.AbstractItemRegistry;

public class ItemRegistry extends AbstractItemRegistry {

    ItemRegistry(String modid, IForgeRegistry<Item> registry) {
        super(modid, registry);

        this.registerBlockItems(Content.BLOCKS);
    }
}
