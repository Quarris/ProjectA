package dev.quarris.projecta.client.events;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.client.particles.BubblingParticle;
import dev.quarris.projecta.client.renderer.tile.AlchemistCauldronBlockEntityRenderer;
import dev.quarris.projecta.registry.ContentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegisterEvents {

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ContentRegistry.BlockEntityTypes.ALCHEMIST_CAULDRON.get(), AlchemistCauldronBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        engine.register(ContentRegistry.ParticleTypes.BUBBLING.get(), BubblingParticle.Factory::new);
    }
}
