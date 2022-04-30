package dev.quarris.projecta.content.tiles;

import dev.quarris.projecta.registry.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import quarris.qlib.api.block.tile.BasicBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AlchemistCauldronBlockEntity extends BasicBlockEntity {

    private CauldronFluidHandler fluidStorage = new CauldronFluidHandler();

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> this.fluidStorage);

    public AlchemistCauldronBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Registry.BlockEntityTypes.ALCHEMIST_CAULDRON.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Fluid", this.fluidStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Fluid")) {
            this.fluidStorage.deserializeNBT(pTag.getCompound("Fluid"));
        }
    }

    public IFluidHandler getFluidStorage() {
        return this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(this.fluidStorage);
    }

    public void tick() {

    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    private class CauldronFluidHandler implements IFluidHandler, INBTSerializable<CompoundTag> {

        private FluidStack fluid = FluidStack.EMPTY;

        public FluidStack getFluid() {
            return this.fluid.copy();
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @NotNull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return this.fluid;
        }

        @Override
        public int getTankCapacity(int tank) {
            return FluidAttributes.BUCKET_VOLUME;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!this.fluid.isEmpty() || resource.getAmount() < FluidAttributes.BUCKET_VOLUME) {
                return 0;
            }

            if (action.execute()) {
                this.fluid = resource.copy();
                this.fluid.setAmount(FluidAttributes.BUCKET_VOLUME);
                this.onContentsChanged();
            }

            return FluidAttributes.BUCKET_VOLUME;
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (this.fluid.isEmpty() || resource.getAmount() < FluidAttributes.BUCKET_VOLUME) {
                return FluidStack.EMPTY;
            }

            if (!resource.isFluidEqual(this.fluid)) {
                return FluidStack.EMPTY;
            }

            return this.drain(FluidAttributes.BUCKET_VOLUME, action);
        }

        @NotNull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (this.fluid.isEmpty() || maxDrain < FluidAttributes.BUCKET_VOLUME) {
                return FluidStack.EMPTY;
            }

            FluidStack toDrain = this.fluid.copy();
            if (action.execute()) {
                this.fluid = FluidStack.EMPTY;
                this.onContentsChanged();
            }

            return toDrain;
        }

        private void onContentsChanged() {
            AlchemistCauldronBlockEntity.this.sendToClients();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            this.fluid.writeToNBT(tag);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.fluid = FluidStack.loadFluidStackFromNBT(nbt);
        }
    }
}
