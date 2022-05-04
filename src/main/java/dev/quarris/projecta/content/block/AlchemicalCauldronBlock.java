package dev.quarris.projecta.content.block;

import dev.quarris.projecta.ModRef;
import dev.quarris.projecta.content.tile.AlchemicalCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;
import quarris.qlib.api.content.block.IBlockEntityProvider;
import quarris.qlib.api.registry.components.block.IOverrideItemProperties;

public class AlchemicalCauldronBlock extends Block implements IBlockEntityProvider<AlchemicalCauldronBlockEntity>, IOverrideItemProperties {

    private static final VoxelShape SHAPE = makeShape();
    private static final VoxelShape OUTER_SHAPE = Shapes.or(SHAPE, Shapes.box(1 / 16d, 1 / 16d, 1 / 16d, 15 / 16d, 16 / 16d, 15 / 16d));

    public AlchemicalCauldronBlock() {
        super(Properties.copy(Blocks.CAULDRON));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, this.getTile(pLevel, pPos).getFluidStorage())) {
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }

        if (!pLevel.isClientSide() && pPlayer.getItemInHand(pHand).getItem() == Items.STICK) {
            this.getTile(pLevel, pPos).stir();
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(Level pLevel, T pBlockEntity) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (level, pos, state, tile) -> {
            if (tile instanceof AlchemicalCauldronBlockEntity) {
                ((AlchemicalCauldronBlockEntity) tile).tick();
            }
        };
    }

    @Override
    public void override(Item.Properties properties) {
        properties.tab(ModRef.TAB);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AlchemicalCauldronBlockEntity(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return OUTER_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.1875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.0625, 0, 0.0625, 0.9375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.9375, 0.0625, 0, 1, 0.9375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0, 0.9375, 0.9375, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0.9375, 0.9375, 0.9375, 1), BooleanOp.OR);
        //shape = Shapes.join(shape, Shapes.box(0.0625, 0.875, 0.0625, 0.9375, 1, 0.1875), BooleanOp.OR);
        //shape = Shapes.join(shape, Shapes.box(0.0625, 0.875, 0.8125, 0.9375, 1, 0.9375), BooleanOp.OR);
        //shape = Shapes.join(shape, Shapes.box(0.0625, 0.875, 0.1875, 0.1875, 1, 0.8125), BooleanOp.OR);
        //shape = Shapes.join(shape, Shapes.box(0.8125, 0.875, 0.1875, 0.9375, 1, 0.8125), BooleanOp.OR);

        return shape;
    }
}
