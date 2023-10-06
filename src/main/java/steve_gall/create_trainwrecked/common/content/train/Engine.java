package steve_gall.create_trainwrecked.common.content.train;

import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;

public class Engine extends TrainPart<TrainEngineTypeRecipe>
{
	public static final TrainEngineTypeRecipe NOT_FOUND_RECIPE = new TrainEngineTypeRecipe.Builder<>().build(RECIPE_NOT_FOUND_ID);

	private double heat;
	private double speed;
	private boolean overheated;

	public Engine(Level level, CapturedPos capture)
	{
		super(level, capture);
	}

	public Engine(CompoundTag tag)
	{
		super(tag);

		this.heat = tag.getDouble("heat");
		this.speed = tag.getDouble("speed");
		this.overheated = tag.getBoolean("overheated");
	}

	public Engine(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.heat = buffer.readDouble();
		this.speed = buffer.readDouble();
		this.overheated = buffer.readBoolean();
	}

	@Override
	public RecipeType<TrainEngineTypeRecipe> getRecipeType()
	{
		return ModRecipeTypes.TRAIN_ENGINE_TYPE.get();
	}

	@Override
	protected TrainEngineTypeRecipe getFallbackRecipe()
	{
		return NOT_FOUND_RECIPE;
	}

	@Override
	protected boolean testRecipe(TrainEngineTypeRecipe recipe)
	{
		return ItemTagEntry.TYPE.testIngredient(recipe.getBlocks(), this.item);
	}

	public void onFuelBurned(FuelBurning fuel, double allocatedSpeed, int heatLevel)
	{
		TrainEngineTypeRecipe recipe = this.getRecipe();
		double speed = recipe.getPredictSpeed(fuel.toBurn(), fuel.burned(), allocatedSpeed, heatLevel);
		this.setSpeed(speed);

		double heat = this.getHeat();
		this.setHeat(heat + (fuel.burned() * recipe.getHeatPerFuel()));
	}

	@Override
	public void tick(Train train, Level level)
	{
		super.tick(train, level);

		TrainEngineTypeRecipe recipe = this.getRecipe();

		if (recipe.getHeatCapacity() <= 0)
		{
			return;
		}

		this.coolingAir(train);
		this.coolingCoolant(train);
		this.updateOverheat(train);
	}

	public void coolingAir(Train train)
	{
		TrainEngineTypeRecipe recipe = this.getRecipe();
		double heat = this.getHeat();
		this.setHeat(heat - recipe.getAirCoolingRate() / 20.0D);
	}

	public void coolingCoolant(Train train)
	{
		double heat = this.getHeat();
		double cooled = ((TrainExtension) train).getCoolingSystem().useCoolant(train, heat);
		this.setHeat(heat - cooled);
	}

	public void updateOverheat(Train train)
	{
		TrainEngineTypeRecipe recipe = this.getRecipe();
		double heat = this.getHeat();
		int heatCapacity = recipe.getHeatCapacity();

		if (heat > heatCapacity)
		{
			this.setOverheat(true);
		}
		else if (recipe.getOverheatedResettingTemp() >= (heat / heatCapacity))
		{
			this.setOverheat(false);
		}

	}

	@Override
	public void serializeNbt(CompoundTag tag)
	{
		super.serializeNbt(tag);

		tag.putDouble("heat", this.heat);
		tag.putDouble("speed", this.speed);
		tag.putBoolean("overheated", this.overheated);
	}

	@Override
	public void serializeNetwork(FriendlyByteBuf buffer)
	{
		super.serializeNetwork(buffer);

		buffer.writeDouble(this.heat);
		buffer.writeDouble(this.speed);
		buffer.writeBoolean(this.overheated);
	}

	public static CompoundTag toNbt(Engine engine)
	{
		CompoundTag tag = new CompoundTag();
		engine.serializeNbt(tag);
		return tag;
	}

	public static void toNetwork(FriendlyByteBuf buffer, Engine engine)
	{
		engine.serializeNetwork(buffer);
	}

	public void setSpeed(double speed)
	{
		this.speed = Mth.clamp(speed, 0.0D, this.getRecipe().getMaxSpeed());
	}

	public double getSpeed()
	{
		return this.speed;
	}

	public double getHeat()
	{
		return this.heat;
	}

	public void setHeat(double heat)
	{
		this.heat = Math.max(heat, 0.0D);
	}

	public void setOverheat(boolean overheated)
	{
		if (overheated)
		{
			this.overheated = true;
			this.setSpeed(0.0D);
		}
		else
		{
			this.overheated = false;
		}

	}

	public boolean isOverheated()
	{
		return this.overheated;
	}

}
