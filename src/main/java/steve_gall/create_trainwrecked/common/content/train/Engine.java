package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;

public class Engine
{
	public static final ResourceLocation RECIPE_NOT_FOUND_ID = CreateTrainwrecked.asResource("recipe_not_found");
	public static final TrainEngineTypeRecipe NOT_FOUND_RECIPE = new TrainEngineTypeRecipe.Builder<>().build(RECIPE_NOT_FOUND_ID);

	private BlockPos localPos;
	private BlockState blockState;
	private ItemStack item;

	private TrainEngineTypeRecipe recipe;
	private double fuelUsedRatio;
	private double heat;
	private double speed;
	private boolean overheated;

	public Engine(EnginPos enginPos)
	{
		this.localPos = enginPos.localPos();
		this.blockState = enginPos.blockState();
		this.item = enginPos.item();

		this.recipe = enginPos.recipe();
	}

	public Engine(CompoundTag tag)
	{
		this.localPos = NbtUtils.readBlockPos(tag.getCompound("localPos"));
		this.blockState = NbtUtils.readBlockState(tag.getCompound("blockState"));
		this.item = ItemStack.of(tag.getCompound("item"));

		this.recipe = null;
		this.fuelUsedRatio = tag.getDouble("fuelUsedRatio");
		this.heat = tag.getDouble("heat");
		this.speed = tag.getDouble("speed");
		this.overheated = tag.getBoolean("overheated");
	}

	public Engine(FriendlyByteBuf buffer)
	{
		this.localPos = buffer.readBlockPos();
		this.blockState = NbtUtils.readBlockState(buffer.readNbt());
		this.item = buffer.readItem();

		this.recipe = null;
		this.fuelUsedRatio = buffer.readDouble();
		this.heat = buffer.readDouble();
		this.speed = buffer.readDouble();
		this.overheated = buffer.readBoolean();
	}

	public void onFuelBurned(FuelBurning fuel, double allocatedSpeed)
	{
		this.fuelUsedRatio = this.getFuelUsingRatio(fuel);

		TrainEngineTypeRecipe recipe = this.getRecipe();
		double speed = recipe.getPredictSpeed(fuel.toBurn(), fuel.burned(), allocatedSpeed);
		this.setSpeed(speed);

		double heat = this.getHeat();
		this.setHeat(heat + (fuel.burned() * recipe.getHeatPerFuel()));
	}

	public double getFuelUsingRatio(FuelBurning fuel)
	{
		return fuel.toBurn() > 0.0D ? (fuel.burned() / fuel.toBurn()) : 1.0D;
	}

	public void tick(Train train, Level level)
	{
		if (this.recipe == null)
		{
			List<TrainEngineTypeRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRAIN_ENGINE_TYPE.get());
			this.recipe = recipes.stream().filter(r -> ItemTagEntry.TYPE.testIngredient(r.getBlocks(), this.item)).findFirst().orElse(null);
		}

		this.fuelUsedRatio = 0.0D;

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
		this.setHeat(heat - recipe.getAirCoolingRate() / 20);
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
		else if (recipe.overheatedResettingTemp() >= (heat / heatCapacity))
		{
			this.setOverheat(false);
		}

	}

	public static CompoundTag toNbt(Engine engine)
	{
		CompoundTag tag = new CompoundTag();
		tag.put("localPos", NbtUtils.writeBlockPos(engine.localPos));
		tag.put("blockState", NbtUtils.writeBlockState(engine.blockState));
		tag.put("item", engine.item.serializeNBT());

		tag.putDouble("fuelUsedRatio", engine.fuelUsedRatio);
		tag.putDouble("heat", engine.heat);
		tag.putDouble("speed", engine.speed);
		tag.putBoolean("overheated", engine.overheated);

		return tag;
	}

	public static void toNetwork(FriendlyByteBuf buffer, Engine engine)
	{
		buffer.writeBlockPos(engine.localPos);
		buffer.writeNbt(NbtUtils.writeBlockState(engine.blockState));
		buffer.writeItem(engine.item);

		buffer.writeDouble(engine.fuelUsedRatio);
		buffer.writeDouble(engine.heat);
		buffer.writeDouble(engine.speed);
		buffer.writeBoolean(engine.overheated);
	}

	public BlockPos getBlockPos()
	{
		return this.localPos;
	}

	public BlockState getBlockState()
	{
		return this.blockState;
	}

	public ItemStack getItem()
	{
		return this.item.copy();
	}

	public TrainEngineTypeRecipe getRecipe()
	{
		return this.recipe != null ? this.recipe : NOT_FOUND_RECIPE;
	}

	public double getFuelUsedRatio()
	{
		return this.fuelUsedRatio;
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
