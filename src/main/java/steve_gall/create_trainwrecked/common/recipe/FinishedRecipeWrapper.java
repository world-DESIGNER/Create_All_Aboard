package steve_gall.create_trainwrecked.common.recipe;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FinishedRecipeWrapper implements FinishedRecipe
{
	private final FinishedRecipe wrapped;

	public FinishedRecipeWrapper(FinishedRecipe wrapped)
	{
		this.wrapped = wrapped;
	}

	@Override
	public void serializeRecipeData(JsonObject pJson)
	{
		this.wrapped.serializeRecipeData(pJson);
	}

	@Override
	public ResourceLocation getId()
	{
		return this.wrapped.getId();
	}

	@Override
	public RecipeSerializer<?> getType()
	{
		return this.wrapped.getType();
	}

	@Override
	public JsonObject serializeAdvancement()
	{
		return this.wrapped.serializeAdvancement();
	}

	@Override
	public ResourceLocation getAdvancementId()
	{
		return this.wrapped.getAdvancementId();
	}

}
