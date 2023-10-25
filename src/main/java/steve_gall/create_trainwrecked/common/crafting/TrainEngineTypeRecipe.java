package steve_gall.create_trainwrecked.common.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private final ResourceLocation id;
	private final List<ItemTagEntry> blockTypes;
	private final float maxSpeed;
	private final float acceleration;
	private final double carriageStressMultiplier;
	private final List<FluidTagEntry> fuelTypes;
	private final boolean limitableByHeat;
	private final double fuelPerSpeed;
	private final double fuelPerHeatLevel;
	private final int heatCapacity;
	private final double heatPerFuel;
	private final double airCoolingRate;
	private final double overheatedResettingTemp;
	private final CrashBehavior crashBehavior;

	private final List<Ingredient> blocks;
	private final List<FluidIngredient> fuels;

	private TrainEngineTypeRecipe(ResourceLocation id, Builder<?> builder)
	{
		this.id = id;
		this.blockTypes = Collections.unmodifiableList(builder.blockType());
		this.maxSpeed = builder.maxSpeed();
		this.acceleration = builder.acceleration();
		this.carriageStressMultiplier = builder.carriageStressMultiplier();
		this.fuelTypes = Collections.unmodifiableList(builder.fuelType());
		this.limitableByHeat = builder.limitableByHeat();
		this.fuelPerSpeed = builder.fuelPerSpeed();
		this.fuelPerHeatLevel = builder.fuelPerHeatLevel();
		this.heatCapacity = builder.heatCapacity();
		this.heatPerFuel = builder.heatPerFuel();
		this.airCoolingRate = builder.airCoolingRate();
		this.overheatedResettingTemp = builder.overheatedResettingTemp();
		this.crashBehavior = builder.crashBehavior();

		this.blocks = this.blockTypes.stream().map(ItemTagEntry::toIngredient).toList();
		this.fuels = this.fuelTypes.stream().map(FluidTagEntry::toIngredient).toList();
	}

	public double getPredictSpeed(double toBurn, double burned, double allocatedSpeed, int heatLevel)
	{
		double fuelPerSpeed = this.getFuelPerSpeed();

		if (fuelPerSpeed > 0.0D)
		{
			double fuelPerHeatLevel = this.getFuelPerHeatLevel();
			double burnedBySpeed = burned;

			if (fuelPerHeatLevel > 0.0D)
			{
				if (heatLevel <= 0)
				{
					burnedBySpeed = 0.0D;
				}
				else
				{
					burnedBySpeed = burnedBySpeed / (heatLevel * fuelPerHeatLevel);
				}

			}

			return burnedBySpeed * 20.0D / fuelPerSpeed;
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

	public double getFuelUsage(double speed, int heatLevel)
	{
		return getFuelUsage(this.getFuelPerSpeed(), this.getFuelPerHeatLevel(), speed, heatLevel);
	}

	public static double getFuelUsage(double fuelPerSpeed, double fuelPerHeatLevel, double speed, int heatLevel)
	{
		double fuelUsage = speed * fuelPerSpeed;

		if (fuelPerHeatLevel > 0.0D)
		{
			fuelUsage *= heatLevel * fuelPerHeatLevel;
		}

		return fuelUsage;
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
		double carriageStressMultiplier = this.getCarriageStressMultiplier();

		if (carriageStressMultiplier <= 0.0D)
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
		double carriageStressMultiplier = this.getCarriageStressMultiplier();

		if (carriageStressMultiplier <= 0.0D)
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
		pJson.add("blockType", ItemTagEntry.TYPE.listToJson(this.getBlockTypes()));
		pJson.addProperty("maxSpeed", this.getMaxSpeed());
		pJson.addProperty("acceleration", this.getAcceleration());
		pJson.addProperty("carriageStressMultiplier", this.getCarriageStressMultiplier());
		pJson.add("fuelType", FluidTagEntry.TYPE.listToJson(this.getFuelTypes()));
		pJson.addProperty("limitableByHeat", this.isLimitableByHeat());
		pJson.addProperty("fuelPerSpeed", this.getFuelPerSpeed());
		pJson.addProperty("fuelPerHeatLevel", this.getFuelPerHeatLevel());
		pJson.addProperty("heatCapacity", this.getHeatCapacity());
		pJson.addProperty("heatPerFuel", this.getHeatPerFuel());
		pJson.addProperty("airCoolingRate", this.getAirCoolingRate());
		pJson.addProperty("overheatedResettingTemp", this.getOverheatedResettingTemp());
		CrashBehavior crashBehavior = this.getCrashBehavior();

		if (crashBehavior.getExplosionRadius() > 0.0F)
		{
			pJson.add("crashBehavior", crashBehavior.toJson());
		}

	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer)
	{
		ItemTagEntry.TYPE.listToNetowrk(pBuffer, this.getBlockTypes());
		pBuffer.writeFloat(this.getMaxSpeed());
		pBuffer.writeFloat(this.getAcceleration());
		pBuffer.writeDouble(this.getCarriageStressMultiplier());
		FluidTagEntry.TYPE.listToNetowrk(pBuffer, this.getFuelTypes());
		pBuffer.writeBoolean(this.isLimitableByHeat());
		pBuffer.writeDouble(this.getFuelPerSpeed());
		pBuffer.writeDouble(this.getFuelPerHeatLevel());
		pBuffer.writeInt(this.getHeatCapacity());
		pBuffer.writeDouble(this.getHeatPerFuel());
		pBuffer.writeDouble(this.getAirCoolingRate());
		pBuffer.writeDouble(this.getOverheatedResettingTemp());
		this.getCrashBehavior().toNetwork(pBuffer);
	}

	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}

	public List<ItemTagEntry> getBlockTypes()
	{
		return this.blockTypes;
	}

	public List<Ingredient> getBlocks()
	{
		return this.blocks;
	}

	public float getMaxSpeed()
	{
		return this.maxSpeed;
	}

	public float getAcceleration()
	{
		return this.acceleration;
	}

	public double getCarriageStressMultiplier()
	{
		return this.carriageStressMultiplier;
	}

	public List<FluidTagEntry> getFuelTypes()
	{
		return this.fuelTypes;
	}

	public List<FluidIngredient> getFuels()
	{
		return this.fuels;
	}

	public boolean isLimitableByHeat()
	{
		return this.limitableByHeat;
	}

	public double getFuelPerSpeed()
	{
		return this.fuelPerSpeed;
	}

	public double getFuelPerHeatLevel()
	{
		return this.fuelPerHeatLevel;
	}

	public int getHeatCapacity()
	{
		return this.heatCapacity;
	}

	public boolean hasHeatCapacity()
	{
		return this.getHeatCapacity() > 0;
	}

	public double getHeatPerFuel()
	{
		return this.heatPerFuel;
	}

	public double getAirCoolingRate()
	{
		return this.airCoolingRate;
	}

	public double getOverheatedResettingTemp()
	{
		return this.overheatedResettingTemp;
	}

	public CrashBehavior getCrashBehavior()
	{
		return this.crashBehavior;
	}

	public static class Serializer extends SimpleRecipeSerializer<TrainEngineTypeRecipe, Builder<?>>
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
	public static class Builder<T extends Builder<T>> extends SimpleRecipeBuilder<T, TrainEngineTypeRecipe>
	{
		private final List<ItemTagEntry> blockType = new ArrayList<>();
		private float maxSpeed = 0.0F;
		private float acceleration = 0.0F;
		private double carriageStressMultiplier = 1.0D;
		private List<FluidTagEntry> fuelType = new ArrayList<>();
		private boolean limitableByHeat = false;
		private double fuelPerSpeed = 0.0D;
		private double fuelPerHeatLevel = 0.0D;
		private int heatCapacity = 0;
		private double heatPerFuel = 0.0D;
		private double airCoolingRate = 0.0D;
		private double overheatedResettingTemp = 0.0D;
		private CrashBehavior crashBehavior = new CrashBehavior.Builder().build();

		public Builder()
		{

		}

		public Builder(JsonObject pJson)
		{
			this.blockType().addAll(ItemTagEntry.TYPE.getAsTagEntryList(pJson, "blockType"));
			this.maxSpeed(GsonHelper.getAsFloat(pJson, "maxSpeed"));
			this.acceleration(GsonHelper.getAsFloat(pJson, "acceleration"));
			this.carriageStressMultiplier(GsonHelper.getAsFloat(pJson, "carriageStressMultiplier"));
			this.fuelType().addAll(FluidTagEntry.TYPE.getAsTagEntryList(pJson, "fuelType"));
			this.limitableByHeat(GsonHelper.getAsBoolean(pJson, "limitableByHeat"));
			this.fuelPerSpeed(GsonHelper.getAsDouble(pJson, "fuelPerSpeed"));
			this.fuelPerHeatLevel(GsonHelper.getAsDouble(pJson, "fuelPerHeatLevel"));
			this.heatCapacity(GsonHelper.getAsInt(pJson, "heatCapacity"));
			this.heatPerFuel(GsonHelper.getAsDouble(pJson, "heatPerFuel"));
			this.airCoolingRate(GsonHelper.getAsDouble(pJson, "airCoolingRate"));
			this.overheatedResettingTemp(GsonHelper.getAsDouble(pJson, "overheatedResettingTemp"));

			JsonObject jcrashBehavior = GsonHelper.getAsJsonObject(pJson, "crashBehavior", null);

			if (jcrashBehavior != null)
			{
				this.crashBehavior(new CrashBehavior(jcrashBehavior));
			}

		}

		public Builder(FriendlyByteBuf pBuffer)
		{
			this.blockType().addAll(ItemTagEntry.TYPE.listFromNetwork(pBuffer));
			this.maxSpeed(pBuffer.readFloat());
			this.acceleration(pBuffer.readFloat());
			this.carriageStressMultiplier(pBuffer.readDouble());
			this.fuelType().addAll(FluidTagEntry.TYPE.listFromNetwork(pBuffer));
			this.limitableByHeat(pBuffer.readBoolean());
			this.fuelPerSpeed(pBuffer.readDouble());
			this.fuelPerHeatLevel(pBuffer.readDouble());
			this.heatCapacity(pBuffer.readInt());
			this.heatPerFuel(pBuffer.readDouble());
			this.airCoolingRate(pBuffer.readDouble());
			this.overheatedResettingTemp(pBuffer.readDouble());
			this.crashBehavior(new CrashBehavior(pBuffer));
		}

		@Override
		public TrainEngineTypeRecipe build(ResourceLocation id)
		{
			return new TrainEngineTypeRecipe(id, this);
		}

		public List<ItemTagEntry> blockType()
		{
			return this.blockType;
		}

		public T blockType(ItemTagEntry blockType)
		{
			this.blockType.add(blockType);
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

		public double carriageStressMultiplier()
		{
			return this.carriageStressMultiplier;
		}

		public T carriageStressMultiplier(double carriageStressMultiplier)
		{
			this.carriageStressMultiplier = Math.max(carriageStressMultiplier, 0.0D);
			return (T) this;
		}

		public List<FluidTagEntry> fuelType()
		{
			return this.fuelType;
		}

		public T fuelType(FluidTagEntry fuelType)
		{
			this.fuelType.add(fuelType);
			return (T) this;
		}

		public boolean limitableByHeat()
		{
			return this.limitableByHeat;
		}

		public T limitableByHeat(boolean limitableByHeat)
		{
			this.limitableByHeat = limitableByHeat;
			return (T) this;
		}

		public double fuelPerSpeed()
		{
			return this.fuelPerSpeed;
		}

		public T fuelPerSpeed(double fuelPerSpeed)
		{
			this.fuelPerSpeed = Math.max(fuelPerSpeed, 0.0D);
			return (T) this;
		}

		public double fuelPerHeatLevel()
		{
			return this.fuelPerHeatLevel;
		}

		public T fuelPerHeatLevel(double fuelPerHeatLevel)
		{
			this.fuelPerHeatLevel = Math.max(fuelPerHeatLevel, 0.0D);
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

		public double heatPerFuel()
		{
			return this.heatPerFuel;
		}

		public T heatPerFuel(double heatPerFuel)
		{
			this.heatPerFuel = Math.max(heatPerFuel, 0.0D);
			return (T) this;
		}

		public double airCoolingRate()
		{
			return this.airCoolingRate;
		}

		public T airCoolingRate(double airCoolingRate)
		{
			this.airCoolingRate = Math.max(airCoolingRate, 0.0D);
			return (T) this;
		}

		public double overheatedResettingTemp()
		{
			return this.overheatedResettingTemp;
		}

		public T overheatedResettingTemp(double overheatedResettingTemp)
		{
			this.overheatedResettingTemp = Math.max(overheatedResettingTemp, 0.0D);
			return (T) this;
		}

		public CrashBehavior crashBehavior()
		{
			return this.crashBehavior;
		}

		public T crashBehavior(CrashBehavior crashBehavior)
		{
			this.crashBehavior = crashBehavior;
			return (T) this;
		}

	}

}
