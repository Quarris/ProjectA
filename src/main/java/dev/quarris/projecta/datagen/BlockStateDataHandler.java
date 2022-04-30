package dev.quarris.projecta.datagen;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.registry.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateDataHandler extends BlockStateProvider {

    public BlockStateDataHandler(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(Registry.Blocks.ALCHEMIST_CAULDRON.get(), this.models().getExistingFile(ModRef.res("block/alchemist_cauldron")));
    }
}
