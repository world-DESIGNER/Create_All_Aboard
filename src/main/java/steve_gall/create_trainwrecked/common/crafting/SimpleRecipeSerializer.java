package steve_gall.create_trainwrecked.common.crafting;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SimpleRecipeSerializer<RECIPE extends SerializableRecipe<?>, BUILDER extends SimpleRecipeBuilder<? extends BUILDER, RECIPE>> implements RecipeSerializer<RECIPE>
{
	private final Supplier<? extends BUILDER> builderSupplier;

	public SimpleRecipeSerializer(Supplier<? extends BUILDER> builderSupplier)
	{
		this.builderSupplier = builderSupplier;
	}

	@Override
	public RECIPE fromJson(ResourceLocation pRecipeId, JsonObject pJson)
	{
		BUILDER builder = this.builderSupplier.get();
		builder.fromJson(pJson);
		return builder.build(pRecipeId);
	}

	@Override
	public @Nullable RECIPE fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer)
	{
		BUILDER builder = this.builderSupplier.get();
		builder.fromNetwork(pBuffer);
		return builder.build(pRecipeId);
	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer, RECIPE pRecipe)
	{
		pRecipe.toNetwork(pBuffer);
	}

}
