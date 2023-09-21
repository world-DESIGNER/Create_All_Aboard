package steve_gall.create_trainwrecked.common.crafting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class ConditionFinishedRecipe extends FinishedRecipeWrapper
{
	private final List<ICondition> conditions;

	public ConditionFinishedRecipe(FinishedRecipe wrapped, Collection<ICondition> conditions)
	{
		super(wrapped);
		this.conditions = new ArrayList<>(conditions);
	}

	@Override
	public void serializeRecipeData(JsonObject pJson)
	{
		super.serializeRecipeData(pJson);

		if (this.conditions.size() > 0)
		{
			JsonElement _conditionsRaw = pJson.get("conditions");
			JsonArray conditions;

			if (_conditionsRaw instanceof JsonArray _conditions)
			{
				conditions = _conditions;
			}
			else
			{
				conditions = new JsonArray();
				pJson.add("conditions", conditions);
			}

			for (ICondition condition : this.conditions)
			{
				conditions.add(CraftingHelper.serialize(condition));
			}

		}

	}

}
