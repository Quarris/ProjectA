package dev.quarris.projecta.registry;

import dev.quarris.projecta.ModRef;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import quarris.qlib.api.registry.AbstractTagStorage;

public class TagStorage {

    public final BlockTags blocks = new BlockTags();
    public final ItemTags items = new ItemTags();
    public final FluidTags fluids = new FluidTags();

    TagStorage() {}

    public static class BlockTags extends AbstractTagStorage<Block> {
        public final TagKey<Block> heatSource = this.create("heat_source");

        BlockTags() {
            super(ModRef.ID, Registry.BLOCK_REGISTRY);
        }
    }

    public static class ItemTags extends AbstractTagStorage<Item> {

        ItemTags() {
            super(ModRef.ID, Registry.ITEM_REGISTRY);
        }
    }

    public static class FluidTags extends AbstractTagStorage<Fluid> {

        public final TagKey<Fluid> heatSource = this.create("heat_source");

        FluidTags() {
            super(ModRef.ID, Registry.FLUID_REGISTRY);
        }
    }
}
