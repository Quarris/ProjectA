package dev.quarris.projecta.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import dev.quarris.projecta.content.tiles.AlchemicalCauldronBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;
import quarris.qlib.api.client.render.FluidRenderer;

public class AlchemicalCauldronBlockEntityRenderer implements BlockEntityRenderer<AlchemicalCauldronBlockEntity> {

    public AlchemicalCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(AlchemicalCauldronBlockEntity cauldron, float pPartialTick, PoseStack matrix, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        FluidStack fluid = cauldron.getFluid();
        if (!fluid.isEmpty()) {
            matrix.pushPose();
            BlockPos pos = cauldron.getBlockPos();
            float fill = cauldron.getFill();
            FluidRenderer.renderFluidFace(cauldron.getLevel(), pos, matrix, fluid, pBufferSource.getBuffer(RenderType.text(InventoryMenu.BLOCK_ATLAS)), Direction.UP, new Vector3d(0.5, 2.01 / 16 + (10.0 / 16) * fill, 0.5), 7 / 16f, pPackedLight);
            matrix.popPose();
        }
    }
}
