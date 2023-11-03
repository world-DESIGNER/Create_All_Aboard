package steve_gall.create_all_aboard.datagen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModItems;

public class ModProcessingRecipeProvider
{
	public static void run(DataGenerator gen)
	{
		List<ProcessingRecipeGen> providiers = new ArrayList<>();
		providiers.add(new FillingRecipeGen(gen));

		gen.addProvider(new DataProvider()
		{
			@Override
			public String getName()
			{
				return CreateAllAboard.MODE_NAME + "'s Processing Recipes";
			}

			@Override
			public void run(HashCache pCache) throws IOException
			{
				providiers.forEach(g ->
				{
					try
					{
						g.run(pCache);
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
			return CreateAllAboard.asResource(name);
		}

		@Override
		protected IRecipeTypeInfo getRecipeType()
		{
			return AllRecipeTypes.FILLING;
		}

	}

}
