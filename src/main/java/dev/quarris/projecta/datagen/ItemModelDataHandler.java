package dev.quarris.projecta.datagen;

import dev.quarris.projecta.ModRef;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelDataHandler extends ItemModelProvider {

    public ItemModelDataHandler(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("alchemical_cauldron", ModRef.res("block/alchemical_cauldron"));
    }
}
