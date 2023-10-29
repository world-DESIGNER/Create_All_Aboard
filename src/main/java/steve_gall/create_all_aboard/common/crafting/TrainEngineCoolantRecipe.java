package steve_gall.create_all_aboard.common.crafting;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.create_all_aboard.common.init.ModRecipeSerializers;
import steve_gall.create_all_aboard.common.init.ModRecipeTypes;

public class TrainEngineCoolantRecipe implements SerializableRecipe<Container>, NonContainerRecipe
{
	private final ResourceLocation id;
	private final Ingredient itemIngredient;
	private final FluidIngredient fluidIngredient;
	private final int cooling;

	public TrainEngineCoolantRecipe(ResourceLocation id, Builder<?> builder)
	{
		this.id = id;
		this.itemIngredient = builder.itemIngredient();
		this.fluidIngredient = builder.fluidIngredient();
		this.cooling = builder.cooling();

		if (this.itemIngredient != null && this.fluidIngredient != null)
		{
			throw new IllegalArgumentException("item and fluid both ingredients are not null ");
		}
		else if (this.itemIngredient == null && this.fluidIngredient == null)
		{
			throw new IllegalArgumentException("item and fluid both ingredients are null ");
		}

	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ModRecipeSerializers.TRAIN_ENGINE_COOLANT.get();
	}

	@Override
	public RecipeType<?> getType()
	{
		return ModRecipeTypes.TRAIN_ENGINE_COOLANT.get();
	}

	@Override
	public void toJson(JsonObject pJson)
	{
		Ingredient item = this.getItemIngredient();
		FluidIngredient fluid = this.getFluidIngredient();

		if (item != null)
		{
			pJson.add("item", item.toJson());
		}
		else if (fluid != null)
		{
			pJson.add("fluid", fluid.serialize());
		}

		pJson.addProperty("cooling", this.getCooling());
	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer)
	{
		Ingredient item = this.getItemIngredient();
		FluidIngredient fluid = this.getFluidIngredient();

		if (item != null)
		{
			pBuffer.writeInt(0);
			item.toNetwork(pBuffer);
		}
		else if (fluid != null)
		{
			pBuffer.writeInt(1);
			fluid.write(pBuffer);
		}
		else
		{
			pBuffer.writeInt(2);
		}

		pBuffer.writeInt(this.getCooling());
	}

	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}

	@Nullable
	public Ingredient getItemIngredient()
	{
		return this.itemIngredient;
	}

	@Nullable
	public FluidIngredient getFluidIngredient()
	{
		return this.fluidIngredient;
	}

	public int getCooling()
	{
		return this.cooling;
	}

	public static class Serializer extends SimpleRecipeSerializer<TrainEngineCoolantRecipe, Builder<?>>
	{
		@Override
		protected Builder<?> fromJson(JsonObject pJson)
		{
			return new Builder<>(pJson);
		}

		@Override
		protected Builder<?> fromNetwork(FriendlyByteBuf pBuffer)
		{
			return new Builder<>(pBuffer);
		}

	}

	@SuppressWarnings("unchecked")
	public static class Builder<T extends Builder<T>> extends SimpleRecipeBuilder<T, TrainEngineCoolantRecipe>
	{
		private Ingredient itemIngredient = null;
		private FluidIngredient fluidIngredient = null;
		private int cooling = 0;

		public Builder()
		{

		}

		public Builder(JsonObject pJson)
		{
			JsonElement itemJson = pJson.get("item");
			JsonElement fluidJson = pJson.get("fluid");

			if (itemJson != null && fluidJson != null)
			{
				throw new IllegalArgumentException("item and fluid both ingredients are not null ");
			}
			else if (itemJson == null && fluidJson == null)
			{
				throw new IllegalArgumentException("item and fluid both ingredients are null ");
			}

			if (itemJson != null)
			{
				this.itemIngredient(Ingredient.fromJson(itemJson));
			}
			else if (fluidJson != null)
			{
				this.fluidIngredient(FluidIngredient.deserialize(fluidJson));
			}

			this.cooling(GsonHelper.getAsInt(pJson, "cooling"));
		}

		public Builder(FriendlyByteBuf pBuffer)
		{
			int type = pBuffer.readInt();
			this.itemIngredient(null);
			this.fluidIngredient(null);

			if (type == 0)
			{
				this.itemIngredient(Ingredient.fromNetwork(pBuffer));
			}
			else if (type == 1)
			{
				this.fluidIngredient(FluidIngredient.read(pBuffer));
			}

			this.cooling(pBuffer.readInt());
		}

		@Nullable
		public Ingredient itemIngredient()
		{
			return this.itemIngredient;
		}

		public T itemIngredient(@Nullable Ingredient ingredient)
		{
			this.itemIngredient = ingredient;
			return (T) this;
		}

		@Nullable
		public FluidIngredient fluidIngredient()
		{
			return this.fluidIngredient;
		}

		public T fluidIngredient(@Nullable FluidIngredient ingredient)
		{
			this.fluidIngredient = ingredient;
			return (T) this;
		}

		public int cooling()
		{
			return this.cooling;
		}

		public T cooling(int cooling)
		{
			this.cooling = Math.max(cooling, 0);
			return (T) this;
		}

		@Override
		public TrainEngineCoolantRecipe build(ResourceLocation id)
		{
			return new TrainEngineCoolantRecipe(id, this);
		}

	}

}
