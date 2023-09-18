package steve_gall.create_trainwrecked.common.recipe;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import steve_gall.create_trainwrecked.common.config.CreateTrainwreckedConfig;
import steve_gall.create_trainwrecked.common.init.ModRecipeSerializers;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;

public class TrainEngineRecipe implements SerializableRecipe<Container>
{
	protected final ResourceLocation id;

	private final ItemTagEntry blockType;
	private final float maxSpeed;
	private final float acceleration;
	private final FluidTagEntry fuelType;
	private final boolean fuelShare;
	private final float fuelMinimum;
	private final float fuelPerSpeed;
	private final float fuelPerRecipe;
	private final float fuelPerRecipePow;
	private final int heatCapacity;
	private final int heatPerFuel;
	private final int airCoolingRate;
	private final int burnOutDuration;

	private final Ingredient block;
	private final FluidIngredient fuel;

	private TrainEngineRecipe(ResourceLocation id, Builder builder)
	{
		this.id = id;
		this.blockType = builder.blockType();
		this.maxSpeed = builder.maxSpeed();
		this.acceleration = builder.acceleration();
		this.fuelType = builder.fuelType();
		this.fuelShare = builder.fuelShare();
		this.fuelMinimum = builder.fuelMinimum();
		this.fuelPerSpeed = builder.fuelPerSpeed();
		this.fuelPerRecipe = builder.fuelPerRecipe();
		this.fuelPerRecipePow = builder.fuelPerRecipePow();
		this.heatCapacity = builder.heatCapacity();
		this.heatPerFuel = builder.heatPerFuel();
		this.airCoolingRate = builder.airCoolingRate();
		this.burnOutDuration = builder.burnOutDuration();

		this.block = this.blockType.toIngredient();
		this.fuel = this.fuelType.toIngredient();
	}

	public double getFuelUsage(int sameRecipeCount, double speed)
	{
		float minimum = this.getFuelMinimum();
		float perSpeed = this.getFuelPerSpeed();
		float perRecipe = this.getFuelPerRecipe();
		float perRecipePow = this.getFuelPerRecipePow();
		return minimum + (speed * perSpeed) + (perRecipe * sameRecipeCount) + (perRecipePow != 0.0F ? Math.pow(perRecipePow, sameRecipeCount) : 0.0F);
	}

	public static int getMaxBogeyCount(double totalSpeed)
	{
		return (int) (totalSpeed / CreateTrainwreckedConfig.COMMON.bogeyStress.get()) + 1;
	}

	public int getMaxBogeyCount()
	{
		return getMaxBogeyCount(this.getMaxSpeed());
	}

	public double getHeatDuration(double maxFuelUsage)
	{
		return this.getHeatCapacity() / ((this.getHeatPerFuel() * maxFuelUsage) - this.getAirCoolingRate());
	}

	@Override
	public boolean matches(Container pContainer, Level pLevel)
	{
		return true;
	}

	@Override
	public ItemStack assemble(Container pContainer)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return true;
	}

	@Override
	public ItemStack getResultItem()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ModRecipeSerializers.TRAIN_ENGINE.get();
	}

	@Override
	public RecipeType<?> getType()
	{
		return ModRecipeTypes.TRAIN_ENGINE.get();
	}

	@Override
	public void toJson(JsonObject pJson)
	{
		pJson.add("blockType", this.getBlockType().toJson());
		pJson.addProperty("maxSpeed", this.getMaxSpeed());
		pJson.addProperty("acceleration", this.getAcceleration());
		pJson.add("fuelType", this.getFuelType().toJson());
		pJson.addProperty("fuelShare", this.isFuelShare());
		pJson.addProperty("fuelMinimum", this.getFuelMinimum());
		pJson.addProperty("fuelPerSpeed", this.getFuelPerSpeed());
		pJson.addProperty("fuelPerRecipe", this.getFuelPerRecipe());
		pJson.addProperty("fuelPerRecipePow", this.getFuelPerRecipePow());
		pJson.addProperty("heatCapacity", this.getHeatCapacity());
		pJson.addProperty("heatPerFuel", this.getHeatPerFuel());
		pJson.addProperty("airCoolingRate", this.getAirCoolingRate());
		pJson.addProperty("burnOutDuration", this.getBurnOutDuration());
	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer)
	{
		this.getBlockType().toNetwork(pBuffer);
		pBuffer.writeFloat(this.getMaxSpeed());
		pBuffer.writeFloat(this.getAcceleration());
		this.getFuelType().toNetwork(pBuffer);
		pBuffer.writeBoolean(this.isFuelShare());
		pBuffer.writeFloat(this.getFuelMinimum());
		pBuffer.writeFloat(this.getFuelPerSpeed());
		pBuffer.writeFloat(this.getFuelPerRecipe());
		pBuffer.writeFloat(this.getFuelPerRecipePow());
		pBuffer.writeInt(this.getHeatCapacity());
		pBuffer.writeInt(this.getHeatPerFuel());
		pBuffer.writeInt(this.getAirCoolingRate());
		pBuffer.writeInt(this.getBurnOutDuration());
	}

	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}

	public ItemTagEntry getBlockType()
	{
		return this.blockType;
	}

	public Ingredient getBlock()
	{
		return this.block;
	}

	public float getMaxSpeed()
	{
		return this.maxSpeed;
	}

	public float getAcceleration()
	{
		return this.acceleration;
	}

	public FluidTagEntry getFuelType()
	{
		return this.fuelType;
	}

	public FluidIngredient getFuel()
	{
		return this.fuel;
	}

	public boolean isFuelShare()
	{
		return this.fuelShare;
	}

	public float getFuelMinimum()
	{
		return this.fuelMinimum;
	}

	public float getFuelPerSpeed()
	{
		return this.fuelPerSpeed;
	}

	public float getFuelPerRecipe()
	{
		return this.fuelPerRecipe;
	}

	public float getFuelPerRecipePow()
	{
		return this.fuelPerRecipePow;
	}

	public int getHeatCapacity()
	{
		return this.heatCapacity;
	}

	public int getHeatPerFuel()
	{
		return this.heatPerFuel;
	}

	public int getAirCoolingRate()
	{
		return this.airCoolingRate;
	}

	public int getBurnOutDuration()
	{
		return this.burnOutDuration;
	}

	public static class Builder
	{
		private ItemTagEntry blockType = ItemTagEntry.TYPE.empty();
		private float maxSpeed = 0.0F;
		private float acceleration = 0.0F;
		private FluidTagEntry fuelType = FluidTagEntry.TYPE.empty();
		private boolean fuelShare = false;
		private float fuelMinimum = 0.0F;
		private float fuelPerSpeed = 0.0F;
		private float fuelPerRecipe = 0.0F;
		private float fuelPerRecipePow = 0.0F;
		private int heatCapacity = 0;
		private int heatPerFuel = 0;
		private int airCoolingRate = 0;
		private int burnOutDuration = 0;

		public Builder()
		{

		}

		public Builder(JsonObject pJson)
		{
			this.blockType(ItemTagEntry.TYPE.getAsTagEntry(pJson, "blockType"));
			this.maxSpeed(GsonHelper.getAsFloat(pJson, "maxSpeed"));
			this.acceleration(GsonHelper.getAsFloat(pJson, "acceleration"));
			this.fuelType(FluidTagEntry.TYPE.getAsTagEntry(pJson, "fuelType"));
			this.fuelShare(GsonHelper.getAsBoolean(pJson, "fuelShare"));
			this.fuelMinimum(GsonHelper.getAsFloat(pJson, "fuelMinimum"));
			this.fuelPerSpeed(GsonHelper.getAsFloat(pJson, "fuelPerSpeed"));
			this.fuelPerRecipe(GsonHelper.getAsFloat(pJson, "fuelPerRecipe"));
			this.fuelPerRecipePow(GsonHelper.getAsFloat(pJson, "fuelPerRecipePow"));
			this.heatCapacity(GsonHelper.getAsInt(pJson, "heatCapacity"));
			this.heatPerFuel(GsonHelper.getAsInt(pJson, "heatPerFuel"));
			this.airCoolingRate(GsonHelper.getAsInt(pJson, "airCoolingRate"));
			this.burnOutDuration(GsonHelper.getAsInt(pJson, "burnOutDuration"));
		}

		public Builder(FriendlyByteBuf pBuffer)
		{
			this.blockType(ItemTagEntry.TYPE.fromNetwork(pBuffer));
			this.maxSpeed(pBuffer.readFloat());
			this.acceleration(pBuffer.readFloat());
			this.fuelType(FluidTagEntry.TYPE.fromNetwork(pBuffer));
			this.fuelShare(pBuffer.readBoolean());
			this.fuelMinimum(pBuffer.readFloat());
			this.fuelPerSpeed(pBuffer.readFloat());
			this.fuelPerRecipe(pBuffer.readFloat());
			this.fuelPerRecipePow(pBuffer.readFloat());
			this.heatCapacity(pBuffer.readInt());
			this.heatPerFuel(pBuffer.readInt());
			this.airCoolingRate(pBuffer.readInt());
			this.burnOutDuration(pBuffer.readInt());
		}

		public TrainEngineRecipe build(ResourceLocation id)
		{
			return new TrainEngineRecipe(id, this);
		}

		public ItemTagEntry blockType()
		{
			return this.blockType;
		}

		public Builder blockType(ItemTagEntry blockType)
		{
			this.blockType = blockType;
			return this;
		}

		public float maxSpeed()
		{
			return this.maxSpeed;
		}

		public Builder maxSpeed(float maxSpeed)
		{
			this.maxSpeed = Math.max(maxSpeed, 0.0F);
			return this;
		}

		public float acceleration()
		{
			return this.acceleration;
		}

		public Builder acceleration(float acceleration)
		{
			this.acceleration = Math.max(acceleration, 0.0F);
			return this;
		}

		public FluidTagEntry fuelType()
		{
			return this.fuelType;
		}

		public Builder fuelType(FluidTagEntry fuelType)
		{
			this.fuelType = fuelType;
			return this;
		}

		public boolean fuelShare()
		{
			return this.fuelShare;
		}

		public Builder fuelShare(boolean fuelShare)
		{
			this.fuelShare = fuelShare;
			return this;
		}

		public float fuelMinimum()
		{
			return this.fuelMinimum;
		}

		public Builder fuelMinimum(float fuelMinimum)
		{
			this.fuelMinimum = Math.max(fuelMinimum, 0.0F);
			return this;
		}

		public float fuelPerSpeed()
		{
			return this.fuelPerSpeed;
		}

		public Builder fuelPerSpeed(float fuelPerSpeed)
		{
			this.fuelPerSpeed = Math.max(fuelPerSpeed, 0.0F);
			return this;
		}

		public float fuelPerRecipe()
		{
			return this.fuelPerRecipe;
		}

		public Builder fuelPerRecipe(float fuelPerRecipe)
		{
			this.fuelPerRecipe = Math.max(fuelPerRecipe, 0.0F);
			return this;
		}

		public float fuelPerRecipePow()
		{
			return this.fuelPerRecipePow;
		}

		public Builder fuelPerRecipePow(float fuelPerRecipePow)
		{
			this.fuelPerRecipePow = Math.max(fuelPerRecipePow, 0.0F);
			return this;
		}

		public int heatCapacity()
		{
			return this.heatCapacity;
		}

		public Builder heatCapacity(int heatCapacity)
		{
			this.heatCapacity = Math.max(heatCapacity, 0);
			return this;
		}

		public int heatPerFuel()
		{
			return this.heatPerFuel;
		}

		public Builder heatPerFuel(int heatPerFuel)
		{
			this.heatPerFuel = Math.max(heatPerFuel, 0);
			return this;
		}

		public int airCoolingRate()
		{
			return this.airCoolingRate;
		}

		public Builder airCoolingRate(int airCoolingRate)
		{
			this.airCoolingRate = airCoolingRate;
			return this;
		}

		public int burnOutDuration()
		{
			return this.burnOutDuration;
		}

		public Builder burnOutDuration(int burnOutDuration)
		{
			this.burnOutDuration = Math.max(burnOutDuration, 0);
			return this;
		}

	}

}
