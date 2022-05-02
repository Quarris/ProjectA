package dev.quarris.projecta.datagen;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.registry.Content;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagsDataHandler extends TagsProvider<Block> {

    protected BlockTagsDataHandler(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Registry.BLOCK, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(Content.TAGS.blocks.heatSource).addTags(BlockTags.FIRE, BlockTags.CAMPFIRES);
    }

    @Override
    public String getName() {
        return ModRef.ID + ":BlockTags";
    }
}
