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
	private final float fuelPerSpeed;
	private final float fuelPerBogey;
	private final float fuelPerBogeyPow2;
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
		this.fuelPerSpeed = builder.fuelPerSpeed();
		this.fuelPerBogey = builder.fuelPerBogey();
		this.fuelPerBogeyPow2 = builder.fuelPerBogeyPow2();
		this.heatCapacity = builder.heatCapacity();
		this.heatPerFuel = builder.heatPerFuel();
		this.airCoolingRate = builder.airCoolingRate();
		this.burnOutDuration = builder.burnOutDuration();

		this.block = this.blockType.toIngredient();
		this.fuel = this.fuelType.toIngredient();
	}

	public static int getMaxBogeyCount(double totalSpeed)
	{
		return (int) (totalSpeed / CreateTrainwreckedConfig.COMMON.bogeyStress.get()) + 1;
	}

	public int getMaxBogeyCount()
	{
		return getMaxBogeyCount(this.getMaxSpeed());
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
		pJson.addProperty("fuelPerSpeed", this.getFuelPerSpeed());
		pJson.addProperty("fuelPerBogey", this.getFuelPerBogey());
		pJson.addProperty("fuelPerBogeyPow2", this.getFuelPerBogeyPow2());
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
		pBuffer.writeFloat(this.getFuelPerSpeed());
		pBuffer.writeFloat(this.getFuelPerBogey());
		pBuffer.writeFloat(this.getFuelPerBogeyPow2());
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

	public float getFuelPerSpeed()
	{
		return this.fuelPerSpeed;
	}

	public float getFuelPerBogey()
	{
		return this.fuelPerBogey;
	}

	public float getFuelPerBogeyPow2()
	{
		return this.fuelPerBogeyPow2;
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
		private float fuelPerSpeed = 0.0F;
		private float fuelPerBogey = 0.0F;
		private float fuelPerBogeyPow2 = 0.0F;
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
			this.fuelPerSpeed(GsonHelper.getAsFloat(pJson, "fuelPerSpeed"));
			this.fuelPerBogey(GsonHelper.getAsFloat(pJson, "fuelPerBogey"));
			this.fuelPerBogeyPow2(GsonHelper.getAsFloat(pJson, "fuelPerBogeyPow2"));
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
			this.fuelPerSpeed(pBuffer.readFloat());
			this.fuelPerBogey(pBuffer.readFloat());
			this.fuelPerBogeyPow2(pBuffer.readFloat());
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
			this.maxSpeed = maxSpeed;
			return this;
		}

		public float acceleration()
		{
			return this.acceleration;
		}

		public Builder acceleration(float acceleration)
		{
			this.acceleration = acceleration;
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

		public float fuelPerSpeed()
		{
			return this.fuelPerSpeed;
		}

		public Builder fuelPerSpeed(float fuelPerSpeed)
		{
			this.fuelPerSpeed = fuelPerSpeed;
			return this;
		}

		public float fuelPerBogey()
		{
			return this.fuelPerBogey;
		}

		public Builder fuelPerBogey(float fuelPerBogey)
		{
			this.fuelPerBogey = fuelPerBogey;
			return this;
		}

		public float fuelPerBogeyPow2()
		{
			return this.fuelPerBogeyPow2;
		}

		public Builder fuelPerBogeyPow2(float fuelPerBogeyPow2)
		{
			this.fuelPerBogeyPow2 = fuelPerBogeyPow2;
			return this;
		}

		public int heatCapacity()
		{
			return this.heatCapacity;
		}

		public Builder heatCapacity(int heatCapacity)
		{
			this.heatCapacity = heatCapacity;
			return this;
		}

		public int heatPerFuel()
		{
			return this.heatPerFuel;
		}

		public Builder heatPerFuel(int heatPerFuel)
		{
			this.heatPerFuel = heatPerFuel;
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
			this.burnOutDuration = burnOutDuration;
			return this;
		}

	}

}
