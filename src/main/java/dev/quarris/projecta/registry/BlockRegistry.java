package dev.quarris.projecta.registry;

import dev.quarris.projecta.content.blocks.AlchemicalCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import quarris.qlib.api.registry.ContentRegistry;

public class BlockRegistry extends ContentRegistry<Block> {

    public final RegistryObject<Block> alchemicalCauldron = this.register("alchemical_cauldron", AlchemicalCauldronBlock::new);

    BlockRegistry(String modid, IForgeRegistry<Block> registry) {
        super(modid, registry);
    }

}
