package dev.quarris.projecta.client.events;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.client.particles.BubblingParticle;
import dev.quarris.projecta.client.renderer.tile.AlchemicalCauldronBlockEntityRenderer;
import dev.quarris.projecta.registry.Content;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegisterEvents {

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Content.BLOCK_ENTITY_TYPES.alchemicalCauldron.get(), AlchemicalCauldronBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        engine.register(Content.PARTICLE_TYPES.bubbling.get(), BubblingParticle.Factory::new);
    }
}
