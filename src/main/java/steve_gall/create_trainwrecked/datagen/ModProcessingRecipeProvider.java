package steve_gall.create_trainwrecked.datagen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.ModItems;

public class ModProcessingRecipeProvider
{
	public static void run(DataGenerator gen)
	{
		List<ProcessingRecipeGen> providiers = new ArrayList<>();
		providiers.add(new FillingRecipeGen(gen));

		gen.addProvider(true, new DataProvider()
		{
			@Override
			public String getName()
			{
				return CreateTrainwrecked.MODE_NAME + "'s Processing Recipes";
			}

			@Override
			public void run(CachedOutput dc) throws IOException
			{
				providiers.forEach(g ->
				{
					try
					{
						g.run(dc);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				});
			}
		});
	}

	public static class FillingRecipeGen extends ProcessingRecipeGen
	{
		public FillingRecipeGen(DataGenerator generator)
		{
			super(generator);
			this.create(asPath("train_steam_engine"), b -> b.require(Fluids.WATER, 1000).require(Ingredient.of(AllBlocks.STEAM_ENGINE.get())).output(ModItems.TRAIN_STEAM_ENGINE.get()));
		}

		private ResourceLocation asPath(String name)
		{
			return CreateTrainwrecked.asResource(name);
		}

		@Override
		protected IRecipeTypeInfo getRecipeType()
		{
			return AllRecipeTypes.FILLING;
		}

	}

}
