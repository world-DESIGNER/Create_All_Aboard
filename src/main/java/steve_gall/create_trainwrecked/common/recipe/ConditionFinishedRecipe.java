package steve_gall.create_trainwrecked.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class ConditionFinishedRecipe extends FinishedRecipeWrapper
{
	private final ICondition[] conditions;

	public ConditionFinishedRecipe(FinishedRecipe wrapped, ICondition... conditions)
	{
		super(wrapped);
		this.conditions = conditions.clone();
	}

	@Override
	public void serializeRecipeData(JsonObject pJson)
	{
		super.serializeRecipeData(pJson);

		if (this.conditions.length > 0)
		{
			JsonArray conditions = new JsonArray();

			for (ICondition condition : this.conditions)
			{
				conditions.add(CraftingHelper.serialize(condition));
			}

			pJson.add("conditions", conditions);
		}

	}

}
