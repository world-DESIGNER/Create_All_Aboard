package steve_gall.create_trainwrecked.common.crafting;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SimpleFinishedRecipe implements FinishedRecipe
{
	private final SerializableRecipe<?> recipe;

	public SimpleFinishedRecipe(SerializableRecipe<?> recipe)
	{
		this.recipe = recipe;
	}

	@Override
	public void serializeRecipeData(JsonObject pJson)
	{
		this.getRecipe().toJson(pJson);
	}

	public SerializableRecipe<?> getRecipe()
	{
		return this.recipe;
	}

	@Override
	public ResourceLocation getId()
	{
		return this.getRecipe().getId();
	}

	@Override
	public RecipeSerializer<?> getType()
	{
		return this.getRecipe().getSerializer();
	}

	@Override
	public JsonObject serializeAdvancement()
	{
		return null;
	}

	@Override
	public ResourceLocation getAdvancementId()
	{
		return null;
	}

}
