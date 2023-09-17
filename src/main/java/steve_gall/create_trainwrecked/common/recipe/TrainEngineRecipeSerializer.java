package steve_gall.create_trainwrecked.common.recipe;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class TrainEngineRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TrainEngineRecipe>
{
	@Override
	public TrainEngineRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson)
	{
		return new TrainEngineRecipe.Builder(pJson).build(pRecipeId);
	}

	@Override
	public @Nullable TrainEngineRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer)
	{
		return new TrainEngineRecipe.Builder(pBuffer).build(pRecipeId);
	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer, TrainEngineRecipe pRecipe)
	{
		pRecipe.toNetwork(pBuffer);
	}

}
