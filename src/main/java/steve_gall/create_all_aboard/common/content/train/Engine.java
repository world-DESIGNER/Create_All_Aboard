package steve_gall.create_all_aboard.common.content.train;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import steve_gall.create_all_aboard.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_all_aboard.common.init.ModRecipeTypes;
import steve_gall.create_all_aboard.common.util.ItemTagEntry;

public class Engine extends TrainPart<TrainEngineTypeRecipe>
{
	public static final TrainEngineTypeRecipe NOT_FOUND_RECIPE = new TrainEngineTypeRecipe.Builder<>().build(RECIPE_NOT_FOUND_ID);

	private double heat;
	private double speed;
	private boolean overheated;
	private boolean reverse;

	private float prevAngle;
	private float angle;

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
		this.reverse = tag.getBoolean("reverse");
	}

	@Override
	public void writeNbt(CompoundTag tag)
	{
		super.writeNbt(tag);

		tag.putDouble("heat", this.heat);
		tag.putDouble("speed", this.speed);
		tag.putBoolean("overheated", this.overheated);
		tag.putBoolean("reverse", this.reverse);
	}

	@Override
	public void readSyncData(FriendlyByteBuf buffer)
	{
		super.readSyncData(buffer);

		this.heat = buffer.readDouble();
		this.speed = buffer.readDouble();

		byte flags = buffer.readByte();
		this.overheated = (flags & 0x01) != 0;
		this.reverse = (flags & 0x02) != 0;
	}

	@Override
	public void writeSyncData(FriendlyByteBuf buffer)
	{
		super.writeSyncData(buffer);

		buffer.writeDouble(this.heat);
		buffer.writeDouble(this.speed);

		int flags = 0;
		flags |= this.overheated ? 0x01 : 0x00;
		flags |= this.reverse ? 0x02 : 0x00;
		buffer.writeByte((byte) flags);
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

	public void onFuelBurned(FuelBurning fuel, double allocatedSpeed, boolean reverse, int heatLevel)
	{
		TrainEngineTypeRecipe recipe = this.getRecipe();
		double speed = recipe.getPredictSpeed(fuel.toBurn(), fuel.burned(), allocatedSpeed, heatLevel);
		this.setSpeed(speed);
		this.setReverse(reverse);

		double heat = this.getHeat();
		this.setHeat(heat + (fuel.burned() * recipe.getHeatPerFuel()));
	}

	@Override
	public void tickClient(Train train, Level level)
	{
		super.tickClient(train, level);

		float angle = this.getTargetAngle();
		this.prevAngle = angle;

		if (train.graph != null)
		{
			float delta = (float) this.getSpeed() / 20.0F;
			this.setTargetAngle(angle + (this.isReverse() ? -delta : delta));
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

	public boolean isReverse()
	{
		return this.reverse;
	}

	public void setReverse(boolean reverse)
	{
		this.reverse = reverse;
	}

	public float getTargetAngle()
	{
		return this.angle;
	}

	public void setTargetAngle(float angle)
	{
		this.angle = angle;
	}

	public float getAnimatingAngle()
	{
		return Mth.lerp(AnimationTickHolder.getPartialTicks(), this.prevAngle, this.angle);
	}

}
