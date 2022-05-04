package dev.quarris.projecta.content.tile;

import com.mojang.math.Vector3d;
import dev.quarris.projecta.content.recipe.AlchemicalBrewingRecipe;
import dev.quarris.projecta.registry.Content;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import quarris.qlib.api.content.block.tile.BasicBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlchemicalCauldronBlockEntity extends BasicBlockEntity {


    private List<AlchemicalBrewingRecipe> matchingRecipes;
    private List<StirStep> stirs = new ArrayList<>();
    private boolean isInvalidRecipe;

    private List<ItemStack> contents = new ArrayList<>();
    private CauldronFluidHandler fluidStorage = new CauldronFluidHandler();
    private int externalHeat;

    private final AABB brewArea;

    public AlchemicalCauldronBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Content.BLOCK_ENTITY_TYPES.alchemicalCauldron.get(), pWorldPosition, pBlockState);
        this.brewArea = new AABB(2.0 / 16, 2.0 / 16, 2.0 / 16, 14.0 / 16, 12.0 / 16, 14.0 / 16).move(pWorldPosition);
    }

    public void tick() {
        BlockPos below = this.getBlockPos().below();
        this.doHeatTick(below);
        if (this.level.getGameTime() % 2 == 0 && this.isHeated() && !this.getFluid().isEmpty()) {
            this.doBubbleEffect();
            this.checkForItems();
        }
    }

    private void checkForItems() {
        this.level.getEntities(EntityType.ITEM, this.brewArea, e -> true)
                .forEach(itemEntity -> {
                    this.contents.add(itemEntity.getItem());
                    itemEntity.discard();
                });
    }

    public void stir() {
        if (this.getFluid().isEmpty()) {
            return;
        }

        StirStep step = new StirStep(new ArrayList<>(this.contents));
        int stepNumber = this.stirs.size();

        if (this.stirs.isEmpty()) {
            this.matchingRecipes = this.getValidRecipes();
            this.isInvalidRecipe = false;
        }

        this.stirs.add(step);
        this.contents.clear();

        if (this.isInvalidRecipe) {
            return;
        }

        this.matchingRecipes.removeIf(recipe -> !recipe.matchesAtStep(step.items, stepNumber));
        boolean completed = this.matchingRecipes.stream()
                .filter(recipe -> recipe.isFinalStep(stepNumber))
                .findFirst()
                .map(recipe -> {
                    this.finalizeRecipe(recipe.getFluidOutput(), recipe.getOutputs());
                    return true;
                }).orElse(false);

        if (completed) {
            return;
        }

        if (this.matchingRecipes.isEmpty()) {
            this.isInvalidRecipe = true;
        }
    }

    private void finalizeRecipe(FluidStack fluid, List<ItemStack> outputs) {
        if (this.level.isClientSide()) {
            return;
        }

        this.fluidStorage.setFluid(fluid.copy());
        BlockPos pos = this.getBlockPos();
        for (ItemStack output : outputs) {
            this.level.addFreshEntity(new ItemEntity(this.level, pos.getX(), pos.getY() + 1, pos.getZ(), output.copy()));
        }

        this.stirs.clear();
        this.sendToClients();
    }

    private List<AlchemicalBrewingRecipe> getValidRecipes() {
        return this.level.getRecipeManager().getAllRecipesFor(Content.RECIPE_TYPES.alchemicalBrewingType).stream()
                .filter(recipe -> recipe.isValid(this.isSuperHeated(), this.getFluid()))
                .collect(Collectors.toList());
    }

    private void doHeatTick(BlockPos below) {
        if (this.level.isFluidAtPosition(below, state -> !state.isEmpty())) {
            Fluid fluid = this.level.getFluidState(below).getType();
            int fluidTemp = fluid.getAttributes().getTemperature();
            this.externalHeat = (int) Mth.approach(this.externalHeat, fluidTemp, 3);
        } else if (this.level.getBlockState(below).is(Content.TAGS.blocks.heatSource)) {
            this.externalHeat = (int) Mth.approach(this.externalHeat, 1000, 3);
        }
    }

    private void doBubbleEffect() {
        if (!this.level.isClientSide()) {
            ServerLevel level = (ServerLevel) this.level;
            BlockPos pos = this.getBlockPos();
            Vector3d center = new Vector3d(pos.getX() + 0.5, pos.getY() + 10.5 / 16, pos.getZ() + 0.5);
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getFluid().getFluid().defaultFluidState().createLegacyBlock()), center.x, center.y, center.z, 1, 0.125, 0.01, 0.125, 0);
        }
    }

    public int getHeatLevel() {
        return Math.max(this.externalHeat, this.getFluidTemperature());
    }

    private void averageOutHeatLevel() {
        this.externalHeat = (int) Mth.lerp(0.5, this.getFluidTemperature(), this.externalHeat);
    }

    public boolean isHeated() {
        return this.getHeatLevel() >= 1000;
    }

    public boolean isSuperHeated() {
        return this.getHeatLevel() >= 2000;
    }

    public FluidStack getFluid() {
        return this.fluidStorage.getFluid();
    }

    public IFluidHandler getFluidStorage() {
        return this.fluidStorage;
    }

    private int getFluidTemperature() {
        return this.getFluid().getFluid().getAttributes().getTemperature(this.getFluid());
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Fluid", this.fluidStorage.serializeNBT());
        pTag.putInt("Heat", this.externalHeat);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fluidStorage.deserializeNBT(pTag.getCompound("Fluid"));
        this.externalHeat = pTag.getInt("Heat");
    }

    public record StirStep(List<ItemStack> items) {
    }

    public class CauldronFluidHandler implements IFluidHandler, INBTSerializable<CompoundTag> {

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
            if (resource.isEmpty() || this.fluid.isEmpty() || !this.fluid.isFluidEqual(resource)) {
                return FluidStack.EMPTY;
            }

            return this.drain(resource.getAmount(), action);
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
            AlchemicalCauldronBlockEntity.this.sendToClients();
            AlchemicalCauldronBlockEntity.this.averageOutHeatLevel();
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

        public void setFluid(FluidStack fluid) {
            this.fluid = fluid;
        }
    }
}
