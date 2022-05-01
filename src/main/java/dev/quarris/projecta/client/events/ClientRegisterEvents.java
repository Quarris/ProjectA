package dev.quarris.projecta.client.events;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.client.renderer.tile.AlchemistCauldronBlockEntityRenderer;
import dev.quarris.projecta.registry.ContentRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegisterEvents {

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ContentRegistry.BlockEntityTypes.ALCHEMIST_CAULDRON.get(), AlchemistCauldronBlockEntityRenderer::new);
    }
}
