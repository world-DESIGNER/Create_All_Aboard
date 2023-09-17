package steve_gall.create_trainwrecked.common.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipeSerializer;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Keys.RECIPE_SERIALIZERS, CreateTrainwrecked.MOD_ID);
	public static final RegistryObject<TrainEngineRecipeSerializer> TRAIN_ENGINE = RECIPE_SERIALIZERS.register("train_engine", TrainEngineRecipeSerializer::new);

	private ModRecipeSerializers()
	{

	}

}
