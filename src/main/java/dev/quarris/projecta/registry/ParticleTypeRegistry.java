package dev.quarris.projecta.registry;

import dev.quarris.projecta.content.particle.BubblingParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import quarris.qlib.api.registry.AbstractParticleTypeRegistry;

public class ParticleTypeRegistry extends AbstractParticleTypeRegistry {

    public final RegistryObject<ParticleType<BubblingParticleOptions>> bubbling = this.register("bubbling", BubblingParticleOptions.DESERIALIZER, BubblingParticleOptions::codec);

    public ParticleTypeRegistry(String modid, IForgeRegistry<ParticleType<?>> registry) {
        super(modid, registry);
    }
}
