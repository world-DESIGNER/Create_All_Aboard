package steve_gall.create_trainwrecked.common.crafting;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags.AllItemTags;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeHooks;
import steve_gall.create_trainwrecked.common.config.CreateTrainwreckedConfig;

public class HeatStage
{
	private final IngredientType ingredientType;
	private final Ingredient specifiedIngredient;
	private final int ingredientBurnTime;
	private final int level;

	public HeatStage(Builder builder)
	{
		this.ingredientType = builder.ingredientType();
		this.specifiedIngredient = builder.specifiedIngredient();
		this.ingredientBurnTime = builder.ingredientBurnTime();
		this.level = builder.level();
	}

	public HeatStage(JsonElement json)
	{
		this(new Builder(json));
	}

	public HeatStage(FriendlyByteBuf buffer)
	{
		this(new Builder(buffer));
	}

	public static int getBlazeBurnerFuelBurnTime(ItemStack item)
	{
		if (item.getItem() == AllItems.CREATIVE_BLAZE_CAKE.get())
		{
			return Integer.MAX_VALUE;
		}
		else if (AllItemTags.BLAZE_BURNER_FUEL_SPECIAL.matches(item))
		{
			return CreateTrainwreckedConfig.COMMON.heatSourceBlazeFuelSpecialBurnTime.get();
		}
		else if (AllItemTags.BLAZE_BURNER_FUEL_REGULAR.matches(item))
		{
			return CreateTrainwreckedConfig.COMMON.heatSourceBlazeFuelRegularBurnTime.get();
		}
		else
		{
			return 0;
		}

	}

	public int getBurnTime(ItemStack item)
	{
		if (item.isEmpty())
		{
			return 0;
		}
		else if (this.getIngredientType() == IngredientType.BURN_TIME)
		{
			if (getBlazeBurnerFuelBurnTime(item) == 0)
			{
				return ForgeHooks.getBurnTime(item, null);
			}

		}
		else if (this.getIngredientType() == IngredientType.BLAZE_BURNER_FUEL)
		{
			return getBlazeBurnerFuelBurnTime(item);
		}
		else if (this.getIngredientType() == IngredientType.SPECIFY)
		{
			if (this.getSpecifiedIngredient().test(item))
			{
				int burnTime = this.getIngredientBurnTime();

				if (burnTime > 0)
				{
					return burnTime;
				}

				return ForgeHooks.getBurnTime(item, null);
			}

		}

		return 0;
	}

	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		json.addProperty("ingredientType", this.getIngredientType().name().toLowerCase());

		if (this.getIngredientType() == IngredientType.SPECIFY)
		{
			JsonObject jspecified = new JsonObject();
			json.add("specify", jspecified);
			jspecified.add("ingredient", this.getSpecifiedIngredient().toJson());
			int burnTime = this.getIngredientBurnTime();

			if (burnTime > 0)
			{
				jspecified.addProperty("burnTime", burnTime);
			}

		}

		json.addProperty("level", this.getLevel());
		return json;
	}

	public void toNetwork(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(this.getIngredientType());
		this.getSpecifiedIngredient().toNetwork(buffer);
		buffer.writeInt(this.getIngredientBurnTime());
		buffer.writeInt(this.getLevel());
	}

	public IngredientType getIngredientType()
	{
		return this.ingredientType;
	}

	public Ingredient getSpecifiedIngredient()
	{
		return this.specifiedIngredient;
	}

	public int getIngredientBurnTime()
	{
		return this.ingredientBurnTime;
	}

	public int getLevel()
	{
		return this.level;
	}

	public static float getSpeedLimit(int level)
	{
		if (level > 0)
		{
			List<? extends Number> limit = CreateTrainwreckedConfig.COMMON.heatSourceSpeedLimits.get();
			int index = level - 1;

			if (index < limit.size())
			{
				return limit.get(index).floatValue();
			}
			else
			{
				return limit.get(limit.size() - 1).floatValue();
			}

		}
		else
		{
			return 0.0F;
		}

	}

	public float getSpeedLimit()
	{
		return getSpeedLimit(this.getLevel());
	}

	public enum IngredientType
	{
		PASSIVE,
		BURN_TIME,
		BLAZE_BURNER_FUEL,
		SPECIFY,
	}

	public static class Builder
	{
		private IngredientType ingredientType = IngredientType.PASSIVE;
		private Ingredient specifiedIngredient = Ingredient.EMPTY;
		private int ingredientBurnTime = 0;
		private int level = 0;

		public Builder()
		{

		}

		public Builder(JsonElement json)
		{
			JsonObject jobject = json.getAsJsonObject();
			this.ingredientType(EnumUtils.getEnumIgnoreCase(IngredientType.class, GsonHelper.getAsString(jobject, "ingredientType")));

			JsonObject jspecified = GsonHelper.getAsJsonObject(jobject, "specify", null);

			if (jspecified != null)
			{
				this.specifiedIngredient(Ingredient.fromJson(jspecified.get("ingredient")));
				this.ingredientBurnTime(GsonHelper.getAsInt(jobject, "ingredient", 0));
			}

			this.level(GsonHelper.getAsInt(jobject, "level"));
		}

		public Builder(FriendlyByteBuf buffer)
		{
			this.ingredientType(buffer.readEnum(IngredientType.class));
			this.specifiedIngredient(Ingredient.fromNetwork(buffer));
			this.ingredientBurnTime(buffer.readInt());
			this.level(buffer.readInt());
		}

		public HeatStage build()
		{
			return new HeatStage(this);
		}

		public IngredientType ingredientType()
		{
			return this.ingredientType;
		}

		public Builder ingredientType(IngredientType ingredientType)
		{
			this.ingredientType = ingredientType;
			return this;
		}

		public Builder passive()
		{
			return this.ingredientType(IngredientType.PASSIVE);
		}

		public Builder burnTime()
		{
			return this.ingredientType(IngredientType.BURN_TIME);
		}

		public Builder blazeBurnerFuel()
		{
			return this.ingredientType(IngredientType.BLAZE_BURNER_FUEL);
		}

		public Builder specify(Ingredient ingredient)
		{
			return this.specify(ingredient, 0);
		}

		public Builder specify(Ingredient ingredient, int burnTime)
		{
			this.ingredientType(IngredientType.SPECIFY);
			this.specifiedIngredient(ingredient, burnTime);
			return this;
		}

		public Ingredient specifiedIngredient()
		{
			return this.specifiedIngredient;
		}

		public Builder specifiedIngredient(Ingredient ingredient)
		{
			this.specifiedIngredient = ingredient;
			return this;
		}

		public Builder specifiedIngredient(Ingredient ingredient, int burnTime)
		{
			this.specifiedIngredient(ingredient);
			this.ingredientBurnTime(burnTime);
			return this;
		}

		public int level()
		{
			return this.level;
		}

		public Builder level(int level)
		{
			this.level = Math.max(level, 0);
			return this;
		}

		public int ingredientBurnTime()
		{
			return this.ingredientBurnTime;
		}

		public Builder ingredientBurnTime(int burnTime)
		{
			this.ingredientBurnTime = Math.max(burnTime, 0);
			return this;
		}

	}

}
