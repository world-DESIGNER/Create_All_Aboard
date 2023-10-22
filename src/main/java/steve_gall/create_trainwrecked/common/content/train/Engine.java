package steve_gall.create_trainwrecked.common.content.train;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.nbt.CompoundTag;
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

	private float prevAngle;
	private float angle;

	public Engine(Level level, CapturedPos capture)
	{
		super(level, capture);
	}

	public Engine(CompoundTag tag)
	{
		super(tag);
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
	public void tickClient(Train train, Level level)
	{
		super.tickClient(train, level);

		this.prevAngle = this.angle;

		if (train.graph != null)
		{
			this.angle += this.getSpeed() / 20.0D;
		}

	}

	public void coolingAir(Train train)
	{
		TrainEngineTypeRecipe recipe = this.getRecipe();

		if (!recipe.hasHeatCapacity())
		{
			return;
		}

		double heat = this.getHeat();
		this.setHeat(heat - recipe.getAirCoolingRate() / 20.0D);
	}

	public void coolingCoolant(Train train)
	{
		this.coolingCoolant(train, this.getHeat());
	}

	public void coolingCoolant(Train train, double cooling)
	{
		if (!this.getRecipe().hasHeatCapacity())
		{
			return;
		}

		double heat = this.getHeat();
		double cooled = ((TrainExtension) train).getCoolingSystem().useCoolant(train, Math.min(heat, cooling));
		this.setHeat(heat - cooled);
	}

	public void updateOverheat()
	{
		TrainEngineTypeRecipe recipe = this.getRecipe();

		if (!recipe.hasHeatCapacity())
		{
			return;
		}

		int heatCapacity = recipe.getHeatCapacity();
		double heat = this.getHeat();

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
	public void readSyncData(CompoundTag tag)
	{
		super.readSyncData(tag);

		this.heat = tag.getDouble("heat");
		this.speed = tag.getDouble("speed");
		this.overheated = tag.getBoolean("overheated");
	}

	@Override
	public void writeSyncData(CompoundTag tag)
	{
		super.writeSyncData(tag);

		tag.putDouble("heat", this.heat);
		tag.putDouble("speed", this.speed);
		tag.putBoolean("overheated", this.overheated);
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
		if (!this.getRecipe().hasHeatCapacity())
		{
			return 0.0D;
		}

		return this.heat;
	}

	public double getRemainHeatForAlive()
	{
		TrainEngineTypeRecipe recipe = this.getRecipe();
		return Math.max(this.getHeat() - (recipe.getOverheatedResettingTemp() * recipe.getHeatCapacity()), 0.0D);
	}

	public void setHeat(double heat)
	{
		if (!this.getRecipe().hasHeatCapacity())
		{
			return;
		}

		this.heat = Math.max(heat, 0.0D);
		this.updateOverheat();
	}

	public void setOverheat(boolean overheated)
	{
		if (!this.getRecipe().hasHeatCapacity())
		{
			return;
		}

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
		if (!this.getRecipe().hasHeatCapacity())
		{
			return false;
		}

		return this.overheated;
	}

	public float getTargetAngle()
	{
		return this.angle;
	}

	public float getAnimatingAngle()
	{
		return Mth.lerp(AnimationTickHolder.getPartialTicks(), this.prevAngle, this.angle);
	}

}
