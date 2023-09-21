package steve_gall.create_trainwrecked.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

@SuppressWarnings("unchecked")
public abstract class SimpleRecipeBuilder<BUILDER extends SimpleRecipeBuilder<? extends BUILDER, RECIPE>, RECIPE extends SerializableRecipe<?>>
{
	private final List<ICondition> conditions;

	public SimpleRecipeBuilder()
	{
		this.conditions = new ArrayList<>();
	}

	public abstract void fromJson(JsonObject pJson);

	public abstract void fromNetwork(FriendlyByteBuf pBuffer);

	public abstract RECIPE build(ResourceLocation id);

	public FinishedRecipe finish(ResourceLocation id)
	{
		RECIPE recipe = this.build(id);
		FinishedRecipe finish = recipe.finish();
		return new ConditionFinishedRecipe(finish, this.conditions);
	}

	public BUILDER condition(ICondition condition)
	{
		this.conditions.add(condition);
		return (BUILDER) this;
	}

	public List<ICondition> conditions()
	{
		return this.conditions;
	}

}
