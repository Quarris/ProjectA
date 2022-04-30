package dev.quarris.projecta;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModRef {

    public static final String ID = "projecta";

    public static final CreativeModeTab TAB = new CreativeModeTab(ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.PAPER);
        }
    };

    public static ResourceLocation res(String name) {
        return new ResourceLocation(ID, name);
    }
}
