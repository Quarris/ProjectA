package dev.quarris.projecta.content.recipe;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import dev.quarris.projecta.registry.Content;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import quarris.qlib.api.content.recipe.SpecialRecipe;
import quarris.qlib.api.data.json.FluidStackJsonSerializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlchemicalBrewingRecipe extends SpecialRecipe {

    // Inputs
    private final boolean superHeated;
    private final FluidStack input;
    private final List<Step> steps;

    // Outputs
    private final FluidStack fluidOutput;
    private final List<ItemStack> outputs;

    public AlchemicalBrewingRecipe(ResourceLocation id, boolean superHeated, FluidStack input, List<Step> steps, FluidStack fluidOutput, List<ItemStack> outputs) {
        super(id);
        this.input = input;
        this.steps = steps;
        this.fluidOutput = fluidOutput;
        this.outputs = outputs;
        this.superHeated = superHeated;
    }

    public boolean isFinalStep(int step) {
        return step == this.steps.size() - 1;
    }

    public boolean isValid(boolean superHeated, FluidStack fluid) {
        return superHeated == this.superHeated && this.input.isFluidEqual(fluid);
    }

    public boolean matchesAtStep(List<ItemStack> inputs, int stepNumber) {
        if (stepNumber >= this.steps.size()) {
            return false;
        }

        return this.steps.get(stepNumber).matchesStep(inputs);
    }

    public FluidStack getFluidOutput() {
        return this.fluidOutput.copy();
    }

    public List<ItemStack> getOutputs() {
        return this.outputs.stream().map(ItemStack::copy).toList();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Content.RECIPE_TYPES.alchemicalBrewingSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Content.RECIPE_TYPES.alchemicalBrewingType;
    }

    public record Step(List<Ingredient> ingredients) {

        public boolean matchesStep(List<ItemStack> inputs) {
            if (inputs.size() != this.ingredients.size()) {
                return false;
            }

            List<ItemStack> testingInputs = new ArrayList<>(inputs);
            for (Ingredient ingredient : this.ingredients) {
                for (Iterator<ItemStack> ite = testingInputs.iterator(); ite.hasNext();) {
                    ItemStack input = ite.next();
                    if (ingredient.test(input)) {
                        ite.remove();
                        break;
                    }
                }
            }

            return testingInputs.isEmpty();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlchemicalBrewingRecipe> {

        // TODO add fluid tag input
        @Override
        public AlchemicalBrewingRecipe fromJson(ResourceLocation id, JsonObject json) {
            boolean superHeated = JsonUtils.getBooleanOr("super_heated", json, false);
            FluidStack input = FluidStackJsonSerializer.deserialize(json.get("fluid"));
            List<Step> steps = new ArrayList<>();
            json.getAsJsonArray("steps").forEach(stepJElement -> {
                JsonObject stepJObject = stepJElement.getAsJsonObject();
                List<Ingredient> ingredients = new ArrayList<>();
                stepJObject.getAsJsonArray("ingredients").forEach(ingredientJElement -> {
                    ingredients.add(Ingredient.fromJson(ingredientJElement));
                });
                steps.add(new Step(ingredients));
            });

            FluidStack outputFluid = json.has("fluidOutput") ? FluidStackJsonSerializer.deserialize(json.get("fluidOutput")) : FluidStack.EMPTY;
            List<ItemStack> outputs = new ArrayList<>();
            json.getAsJsonArray("outputs")
                    .forEach(jElement -> outputs.add(CraftingHelper.getItemStack(jElement.getAsJsonObject(), true, true)));

            return new AlchemicalBrewingRecipe(id, superHeated, input, steps, outputFluid, outputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AlchemicalBrewingRecipe recipe) {
            buf.writeBoolean(recipe.superHeated);                   // Is Super Heated
            buf.writeFluidStack(recipe.input);                      // Fluid Input
            buf.writeVarInt(recipe.steps.size());                   // Steps
            for (Step step : recipe.steps) {
                buf.writeVarInt(step.ingredients.size());               // Step Ingredients
                for (Ingredient ingredient : step.ingredients) {
                    ingredient.toNetwork(buf);
                }
            }
            buf.writeFluidStack(recipe.fluidOutput);                // Fluid Output
            buf.writeVarInt(recipe.outputs.size());                 // Item Outputs
            for (ItemStack output : recipe.outputs) {
                buf.writeItem(output);
            }
        }

        @Nullable
        @Override
        public AlchemicalBrewingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            boolean superHeated = buf.readBoolean();                                // Is Super Heated
            FluidStack input = buf.readFluidStack();                                // Fluid Input
            int stepsSize = buf.readVarInt();                                       // Steps
            List<Step> steps = new ArrayList<>(stepsSize);
            for (int i = 0; i < stepsSize; i++) {
                int ingredientsSize = buf.readVarInt();                                 // Step Ingredients
                List<Ingredient> ingredients = new ArrayList<>(ingredientsSize);
                for (int j = 0; j < ingredientsSize; j++) {
                    ingredients.add(Ingredient.fromNetwork(buf));
                }
                steps.add(new Step(ingredients));
            }

            FluidStack fluidOutput = buf.readFluidStack();                          // Fluid Output
            int outputsSize = buf.readVarInt();                                     // Item Outputs
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < outputsSize; i++) {
                outputs.add(buf.readItem());
            }

            return new AlchemicalBrewingRecipe(id, superHeated, input, steps ,fluidOutput, outputs);
        }
    }
}
