package dev.quarris.projecta.datagen;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.registry.ContentRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class FluidTagsDataHandler extends TagsProvider<Fluid> {

    public FluidTagsDataHandler(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Registry.FLUID, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ContentRegistry.Tags.Fluids.HEAT_SOURCE).addTags(FluidTags.LAVA);
    }

    @Override
    public String getName() {
        return ModRef.ID + ":FluidTags";
    }
}
