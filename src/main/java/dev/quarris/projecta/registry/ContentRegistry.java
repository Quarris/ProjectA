package dev.quarris.projecta.registry;

import com.mojang.serialization.Codec;
import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.content.blocks.AlchemistCauldronBlock;
import dev.quarris.projecta.content.particles.BubblingParticleOptions;
import dev.quarris.projecta.content.tiles.AlchemistCauldronBlockEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.BlockTags;
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
import quarris.qlib.api.registry.registry.BlockRegistry;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class ContentRegistry {

    public static void init(IEventBus bus) {
        Blocks.init(bus);
        Items.init(bus);
        BlockEntityTypes.init(bus);
        Tags.init();
        ParticleTypes.init(bus);
    }

    @BlockRegistry(ModRef.ID)
    public static class Blocks {
        private static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ModRef.ID);

        public static final RegistryObject<Block> ALCHEMIST_CAULDRON = register("alchemist_cauldron", AlchemistCauldronBlock::new);

        static void init(IEventBus bus) {
            REGISTRY.register(bus);
        }

        private static RegistryObject<Block> register(String name, Supplier<Block> supp) {
            return REGISTRY.register(name, supp);
        }
    }

    public static class Items {
        private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModRef.ID);

        public static final RegistryObject<Item> ALCHEMIST_CAULDRON = register("alchemist_cauldron", () -> new BlockItem(Blocks.ALCHEMIST_CAULDRON.get(), new Item.Properties().tab(ModRef.TAB)));

        static void init(IEventBus bus) {
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

        static void init(IEventBus bus) {
            REGISTRY.register(bus);
        }

        private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supp, RegistryObject<Block>... validBlocks) {
            return REGISTRY.register(name, () -> BlockEntityType.Builder.of(supp, Arrays.stream(validBlocks).map(RegistryObject::get).toList().toArray(new Block[0])).build(null));
        }
    }

    public static class Tags {

        static void init() {
            Blocks.init();
            Fluids.init();
        }

        public static class Blocks {
            static void init() {}

            public static final TagKey<Block> HEAT_SOURCE = create("heat_source");

            private static TagKey<Block> create(String name) {
                return BlockTags.create(ModRef.res(name));
            }
        }

        public static class Fluids {
            static void init() {}

            public static final TagKey<Fluid> HEAT_SOURCE = create("heat_source");

            private static TagKey<Fluid> create(String name) {
                return FluidTags.create(ModRef.res(name));
            }
        }
    }

    public static class ParticleTypes {
        private static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ModRef.ID);

        public static final RegistryObject<ParticleType<BubblingParticleOptions>> BUBBLING = register("bubbling", false, BubblingParticleOptions.DESERIALIZER, BubblingParticleOptions::codec);

        static void init(IEventBus bus) {
            REGISTRY.register(bus);
        }

        private static RegistryObject<? extends SimpleParticleType> register(String name, boolean overrideLimiter) {
            return REGISTRY.register(name, () -> new SimpleParticleType(overrideLimiter));
        }

        private static <O extends ParticleOptions> RegistryObject<ParticleType<O>> register(String name, boolean overrideLimiter, ParticleOptions.Deserializer<O> deserializer, Function<ParticleType<O>, Codec<O>> codec) {
            return REGISTRY.register(name, () -> new ParticleType<>(overrideLimiter, deserializer) {
                @Override
                public Codec<O> codec() {
                    return codec.apply(this);
                }
            });
        }
    }

}
