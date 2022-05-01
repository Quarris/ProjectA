package dev.quarris.projecta.registry;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.content.blocks.AlchemistCauldronBlock;
import dev.quarris.projecta.content.tiles.AlchemistCauldronBlockEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Supplier;

public class ContentRegistry {

    public static void init(IEventBus bus) {
        Blocks.init(bus);
        Items.init(bus);
        BlockEntityTypes.init(bus);
    }

    public static class Blocks {
        private static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ModRef.ID);

        public static final RegistryObject<Block> ALCHEMIST_CAULDRON = register("alchemist_cauldron", AlchemistCauldronBlock::new);

        private static void init(IEventBus bus) {
            REGISTRY.register(bus);
        }

        private static RegistryObject<Block> register(String name, Supplier<Block> supp) {
            return REGISTRY.register(name, supp);
        }
    }

    public static class Items {
        private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModRef.ID);

        public static final RegistryObject<Item> ALCHEMIST_CAULDRON = register("alchemist_cauldron", () -> new BlockItem(Blocks.ALCHEMIST_CAULDRON.get(), new Item.Properties().tab(ModRef.TAB)));

        private static void init(IEventBus bus) {
            REGISTRY.register(bus);
        }

        private static RegistryObject<Item> register(String name, Supplier<Item> supp) {
            return REGISTRY.register(name, supp);
        }
    }

    public static class BlockEntityTypes {
        private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModRef.ID);

        public static final RegistryObject<BlockEntityType<AlchemistCauldronBlockEntity>> ALCHEMIST_CAULDRON =
                register("alchemist_cauldron", AlchemistCauldronBlockEntity::new, Blocks.ALCHEMIST_CAULDRON);

        private static void init(IEventBus bus) {
            REGISTRY.register(bus);
        }

        private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supp, RegistryObject<Block>... validBlocks) {
            return REGISTRY.register(name, () -> BlockEntityType.Builder.of(supp, Arrays.stream(validBlocks).map(RegistryObject::get).toList().toArray(new Block[0])).build(null));
        }
    }

    public static class Tags {

    }

}
