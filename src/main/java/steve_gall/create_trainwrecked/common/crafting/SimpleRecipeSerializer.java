package steve_gall.create_trainwrecked.common.crafting;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class SimpleRecipeSerializer<RECIPE extends SerializableRecipe<?>, BUILDER extends SimpleRecipeBuilder<? extends BUILDER, RECIPE>> implements RecipeSerializer<RECIPE>
{
	protected abstract BUILDER fromJson(JsonObject pJson);

	protected abstract BUILDER fromNetwork(FriendlyByteBuf pBuffer);

	@Override
	public RECIPE fromJson(ResourceLocation pRecipeId, JsonObject pJson)
	{
		BUILDER builder = fromJson(pJson);
		return builder.build(pRecipeId);
	}

	@Override
	public @Nullable RECIPE fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer)
	{
		BUILDER builder = fromNetwork(pBuffer);
		return builder.build(pRecipeId);
	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer, RECIPE pRecipe)
	{
		pRecipe.toNetwork(pBuffer);
	}

}
