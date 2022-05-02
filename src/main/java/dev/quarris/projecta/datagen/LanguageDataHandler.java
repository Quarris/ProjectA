package dev.quarris.projecta.datagen;

import dev.quarris.projecta.registry.Content;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageDataHandler extends LanguageProvider {

    public LanguageDataHandler(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        this.add(Content.BLOCKS.alchemicalCauldron.get(), "Alchemical Cauldron");
    }
}
