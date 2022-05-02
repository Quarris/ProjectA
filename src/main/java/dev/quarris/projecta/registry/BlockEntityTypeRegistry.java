package dev.quarris.projecta.registry;

import dev.quarris.projecta.content.tiles.AlchemicalCauldronBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import quarris.qlib.api.registry.AbstractBlockEntityTypeRegistry;

public class BlockEntityTypeRegistry extends AbstractBlockEntityTypeRegistry {

    public final RegistryObject<BlockEntityType<AlchemicalCauldronBlockEntity>> alchemicalCauldron = this.register("alchemical_cauldron",AlchemicalCauldronBlockEntity::new, Content.BLOCKS.alchemicalCauldron);

    public BlockEntityTypeRegistry(String modid, IForgeRegistry<BlockEntityType<?>> registry) {
        super(modid, registry);
    }
}
