package steve_gall.create_trainwrecked.common.crafting;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.create_trainwrecked.common.config.CreateTrainwreckedConfig;
import steve_gall.create_trainwrecked.common.init.ModRecipeSerializers;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.FluidTagEntry;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;

public class TrainEngineTypeRecipe implements SerializableRecipe<Container>, NonContainerRecipe
{
	protected final ResourceLocation id;

	private final ItemTagEntry blockType;
	private final float maxSpeed;
	private final float acceleration;
	private final float carriageStressMultiplier;
	private final FluidTagEntry fuelType;
	private final boolean fuelShare;
	private final float fuelMinimum;
	private final float fuelPerSpeed;
	private final float fuelPerRecipe;
	private final float fuelPerRecipePow;
	private final int heatCapacity;
	private final int heatPerFuel;
	private final float airCoolingRate;
	private final float overheatedResettingTemp;

	private final Ingredient block;
	private final FluidIngredient fuel;

	private TrainEngineTypeRecipe(ResourceLocation id, Builder<?> builder)
	{
		this.id = id;
		this.blockType = builder.blockType();
		this.maxSpeed = builder.maxSpeed();
		this.acceleration = builder.acceleration();
		this.carriageStressMultiplier = builder.carriageStressMultiplier();
		this.fuelType = builder.fuelType();
		this.fuelShare = builder.fuelShare();
		this.fuelMinimum = builder.fuelMinimum();
		this.fuelPerSpeed = builder.fuelPerSpeed();
		this.fuelPerRecipe = builder.fuelPerRecipe();
		this.fuelPerRecipePow = builder.fuelPerRecipePow();
		this.heatCapacity = builder.heatCapacity();
		this.heatPerFuel = builder.heatPerFuel();
		this.airCoolingRate = builder.airCoolingRate();
		this.overheatedResettingTemp = builder.overheatedResettingTemp();

		this.block = this.blockType.toIngredient();
		this.fuel = this.fuelType.toIngredient();
	}

	public double getPredictSpeed(double toBurn, double burned, double allocatedSpeed)
	{
		double fuelPerSpeed = this.getFuelPerSpeed();

		if (fuelPerSpeed > 0)
		{
			return burned * 20.0D / fuelPerSpeed;
		}
		else if (burned > 0)
		{
			return allocatedSpeed;
		}
		else
		{
			return 0.0D;
		}

	}

	public double getFuelUsage(int sameRecipeCount, double speed)
	{
		float minimum = this.getFuelMinimum();
		float perSpeed = this.getFuelPerSpeed();
		float perRecipe = this.getFuelPerRecipe();
		float perRecipePow = this.getFuelPerRecipePow();
		return minimum + (speed * perSpeed) + (perRecipe * sameRecipeCount) + (perRecipePow > 0.0F ? Math.pow(perRecipePow, sameRecipeCount) : 0.0F);
	}

	public static float getCarriageStress(int carriageCount)
	{
		if (carriageCount <= 1)
		{
			return 0.0F;
		}
		else
		{
			return (carriageCount - 1) * CreateTrainwreckedConfig.COMMON.carriageSpeedStress.get();
		}

	}

	public static double getMaxCarriageCount(double carriageSpeedStressHeap)
	{
		return (carriageSpeedStressHeap / CreateTrainwreckedConfig.COMMON.carriageSpeedStress.get()) + 1;
	}

	public static int getMaxContraptionsBlockCount(double carriageBlocksHeap)
	{
		return (int) (carriageBlocksHeap / CreateTrainwreckedConfig.COMMON.carriageBlocksLimit.get());
	}

	public double getMaxCarriageCount()
	{
		return getMaxCarriageCount(this.getCarriageSpeedStressHeap());
	}

	public double getCarriageSpeedStressHeap()
	{
		float carriageStressMultiplier = this.getCarriageStressMultiplier();

		if (carriageStressMultiplier <= 0)
		{
			return Double.POSITIVE_INFINITY;
		}
		else
		{
			return this.getMaxSpeed() / carriageStressMultiplier;
		}

	}

	public double getMaxBlockCountPerCarriage()
	{
		float carriageStressMultiplier = this.getCarriageStressMultiplier();

		if (carriageStressMultiplier <= 0)
		{
			return Double.POSITIVE_INFINITY;
		}
		else
		{
			return CreateTrainwreckedConfig.COMMON.carriageBlocksLimit.get() / carriageStressMultiplier;
		}

	}

	public double getHeatDuration(double fuelUsage)
	{
		return this.getHeatCapacity() / ((this.getHeatPerFuel() * fuelUsage) - this.getAirCoolingRate());
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ModRecipeSerializers.TRAIN_ENGINE_TYPE.get();
	}

	@Override
	public RecipeType<?> getType()
	{
		return ModRecipeTypes.TRAIN_ENGINE_TYPE.get();
	}

	@Override
	public void toJson(JsonObject pJson)
	{
		pJson.add("blockType", this.getBlockType().toJson());
		pJson.addProperty("maxSpeed", this.getMaxSpeed());
		pJson.addProperty("acceleration", this.getAcceleration());
		pJson.addProperty("carriageStressMultiplier", this.getCarriageStressMultiplier());
		pJson.add("fuelType", this.getFuelType().toJson());
		pJson.addProperty("fuelShare", this.isFuelShare());
		pJson.addProperty("fuelMinimum", this.getFuelMinimum());
		pJson.addProperty("fuelPerSpeed", this.getFuelPerSpeed());
		pJson.addProperty("fuelPerRecipe", this.getFuelPerRecipe());
		pJson.addProperty("fuelPerRecipePow", this.getFuelPerRecipePow());
		pJson.addProperty("heatCapacity", this.getHeatCapacity());
		pJson.addProperty("heatPerFuel", this.getHeatPerFuel());
		pJson.addProperty("airCoolingRate", this.getAirCoolingRate());
		pJson.addProperty("overheatedResettingTemp", this.overheatedResettingTemp());
	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer)
	{
		this.getBlockType().toNetwork(pBuffer);
		pBuffer.writeFloat(this.getMaxSpeed());
		pBuffer.writeFloat(this.getAcceleration());
		pBuffer.writeFloat(this.getCarriageStressMultiplier());
		this.getFuelType().toNetwork(pBuffer);
		pBuffer.writeBoolean(this.isFuelShare());
		pBuffer.writeFloat(this.getFuelMinimum());
		pBuffer.writeFloat(this.getFuelPerSpeed());
		pBuffer.writeFloat(this.getFuelPerRecipe());
		pBuffer.writeFloat(this.getFuelPerRecipePow());
		pBuffer.writeInt(this.getHeatCapacity());
		pBuffer.writeInt(this.getHeatPerFuel());
		pBuffer.writeFloat(this.getAirCoolingRate());
		pBuffer.writeFloat(this.overheatedResettingTemp());
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

	public float getCarriageStressMultiplier()
	{
		return this.carriageStressMultiplier;
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

	public float getAirCoolingRate()
	{
		return this.airCoolingRate;
	}

	public float overheatedResettingTemp()
	{
		return this.overheatedResettingTemp;
	}

	@SuppressWarnings("unchecked")
	public static class Builder<T extends Builder<T>> extends SimpleRecipeBuilder<T, TrainEngineTypeRecipe>
	{
		private ItemTagEntry blockType = ItemTagEntry.TYPE.empty();
		private float maxSpeed = 0.0F;
		private float acceleration = 0.0F;
		private float carriageStressMultiplier = 1.0F;
		private FluidTagEntry fuelType = FluidTagEntry.TYPE.empty();
		private boolean fuelShare = false;
		private float fuelMinimum = 0.0F;
		private float fuelPerSpeed = 0.0F;
		private float fuelPerRecipe = 0.0F;
		private float fuelPerRecipePow = 0.0F;
		private int heatCapacity = 0;
		private int heatPerFuel = 0;
		private float airCoolingRate = 0.0F;
		private float overheatedResettingTemp = 0.0F;

		public Builder()
		{

		}

		@Override
		public void fromJson(JsonObject pJson)
		{
			this.blockType(ItemTagEntry.TYPE.getAsTagEntry(pJson, "blockType"));
			this.maxSpeed(GsonHelper.getAsFloat(pJson, "maxSpeed"));
			this.acceleration(GsonHelper.getAsFloat(pJson, "acceleration"));
			this.carriageStressMultiplier(GsonHelper.getAsFloat(pJson, "carriageStressMultiplier"));
			this.fuelType(FluidTagEntry.TYPE.getAsTagEntry(pJson, "fuelType"));
			this.fuelShare(GsonHelper.getAsBoolean(pJson, "fuelShare"));
			this.fuelMinimum(GsonHelper.getAsFloat(pJson, "fuelMinimum"));
			this.fuelPerSpeed(GsonHelper.getAsFloat(pJson, "fuelPerSpeed"));
			this.fuelPerRecipe(GsonHelper.getAsFloat(pJson, "fuelPerRecipe"));
			this.fuelPerRecipePow(GsonHelper.getAsFloat(pJson, "fuelPerRecipePow"));
			this.heatCapacity(GsonHelper.getAsInt(pJson, "heatCapacity"));
			this.heatPerFuel(GsonHelper.getAsInt(pJson, "heatPerFuel"));
			this.airCoolingRate(GsonHelper.getAsFloat(pJson, "airCoolingRate"));
			this.overheatedResettingTemp(GsonHelper.getAsFloat(pJson, "overheatedResettingTemp"));
		}

		@Override
		public void fromNetwork(FriendlyByteBuf pBuffer)
		{
			this.blockType(ItemTagEntry.TYPE.fromNetwork(pBuffer));
			this.maxSpeed(pBuffer.readFloat());
			this.acceleration(pBuffer.readFloat());
			this.carriageStressMultiplier(pBuffer.readFloat());
			this.fuelType(FluidTagEntry.TYPE.fromNetwork(pBuffer));
			this.fuelShare(pBuffer.readBoolean());
			this.fuelMinimum(pBuffer.readFloat());
			this.fuelPerSpeed(pBuffer.readFloat());
			this.fuelPerRecipe(pBuffer.readFloat());
			this.fuelPerRecipePow(pBuffer.readFloat());
			this.heatCapacity(pBuffer.readInt());
			this.heatPerFuel(pBuffer.readInt());
			this.airCoolingRate(pBuffer.readFloat());
			this.overheatedResettingTemp(pBuffer.readFloat());
		}

		@Override
		public TrainEngineTypeRecipe build(ResourceLocation id)
		{
			return new TrainEngineTypeRecipe(id, this);
		}

		public ItemTagEntry blockType()
		{
			return this.blockType;
		}

		public T blockType(ItemTagEntry blockType)
		{
			this.blockType = blockType;
			return (T) this;
		}

		public float maxSpeed()
		{
			return this.maxSpeed;
		}

		public T maxSpeed(float maxSpeed)
		{
			this.maxSpeed = Math.max(maxSpeed, 0.0F);
			return (T) this;
		}

		public float acceleration()
		{
			return this.acceleration;
		}

		public T acceleration(float acceleration)
		{
			this.acceleration = Math.max(acceleration, 0.0F);
			return (T) this;
		}

		public float carriageStressMultiplier()
		{
			return this.carriageStressMultiplier;
		}

		public T carriageStressMultiplier(float carriageStressMultiplier)
		{
			this.carriageStressMultiplier = Math.max(carriageStressMultiplier, 0.0F);
			return (T) this;
		}

		public FluidTagEntry fuelType()
		{
			return this.fuelType;
		}

		public T fuelType(FluidTagEntry fuelType)
		{
			this.fuelType = fuelType;
			return (T) this;
		}

		public boolean fuelShare()
		{
			return this.fuelShare;
		}

		public T fuelShare(boolean fuelShare)
		{
			this.fuelShare = fuelShare;
			return (T) this;
		}

		public float fuelMinimum()
		{
			return this.fuelMinimum;
		}

		public T fuelMinimum(float fuelMinimum)
		{
			this.fuelMinimum = Math.max(fuelMinimum, 0.0F);
			return (T) this;
		}

		public float fuelPerSpeed()
		{
			return this.fuelPerSpeed;
		}

		public T fuelPerSpeed(float fuelPerSpeed)
		{
			this.fuelPerSpeed = Math.max(fuelPerSpeed, 0.0F);
			return (T) this;
		}

		public float fuelPerRecipe()
		{
			return this.fuelPerRecipe;
		}

		public T fuelPerRecipe(float fuelPerRecipe)
		{
			this.fuelPerRecipe = Math.max(fuelPerRecipe, 0.0F);
			return (T) this;
		}

		public float fuelPerRecipePow()
		{
			return this.fuelPerRecipePow;
		}

		public T fuelPerRecipePow(float fuelPerRecipePow)
		{
			this.fuelPerRecipePow = Math.max(fuelPerRecipePow, 0.0F);
			return (T) this;
		}

		public int heatCapacity()
		{
			return this.heatCapacity;
		}

		public T heatCapacity(int heatCapacity)
		{
			this.heatCapacity = Math.max(heatCapacity, 0);
			return (T) this;
		}

		public int heatPerFuel()
		{
			return this.heatPerFuel;
		}

		public T heatPerFuel(int heatPerFuel)
		{
			this.heatPerFuel = Math.max(heatPerFuel, 0);
			return (T) this;
		}

		public float airCoolingRate()
		{
			return this.airCoolingRate;
		}

		public T airCoolingRate(float airCoolingRate)
		{
			this.airCoolingRate = airCoolingRate;
			return (T) this;
		}

		public float overheatedResettingTemp()
		{
			return this.overheatedResettingTemp;
		}

		public T overheatedResettingTemp(float overheatedResettingTemp)
		{
			this.overheatedResettingTemp = Math.max(overheatedResettingTemp, 0.0F);
			return (T) this;
		}

	}

}
