package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipe;

public class Engine
{
	public static final ResourceLocation RECIPE_NOT_FOUND_ID = CreateTrainwrecked.asResource("recipe_not_found");
	public static final TrainEngineRecipe NOT_FOUND_RECIPE = new TrainEngineRecipe.Builder().build(RECIPE_NOT_FOUND_ID);

	private final FuelBurner fuelBurner;
	private BlockPos localPos;
	private BlockState blockState;
	private ItemStack item;

	private TrainEngineRecipe recipe;
	private double heat;
	private double speed;

	public Engine(CompoundTag tag)
	{
		this.fuelBurner = new FuelBurner();
		this.readNBT(tag);
		this.recipe = null;
	}

	public Engine(EnginPos enginPos)
	{
		this.fuelBurner = new FuelBurner();
		this.localPos = enginPos.localPos();
		this.blockState = enginPos.blockState();
		this.item = enginPos.item();
		this.recipe = enginPos.recipe();
	}

	public void onFuelBurned(double burned, double allocatedSpeed)
	{
		TrainEngineRecipe recipe = this.getRecipe();

		if (recipe.getFuelPerSpeed() > 0)
		{
			this.setSpeed(burned / recipe.getFuelPerSpeed());
		}
		else if (burned > 0)
		{
			this.setSpeed(allocatedSpeed / 20.0D);
		}
		else
		{
			this.setSpeed(0.0D);
		}

		this.setHeat(this.getHeat() + (burned * recipe.getHeatPerFuel()));
	}

	public void tick(Train train, Level level)
	{
		if (this.recipe == null)
		{
			List<TrainEngineRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRAIN_ENGINE.get());
			this.recipe = recipes.stream().filter(r -> r.getBlock().test(this.item)).findFirst().orElse(null);
		}

		this.setHeat(this.getHeat() - this.getRecipe().getAirCoolingRate() / 20);
		// System.out.println("heat: " + this.getHeat() + ", " + this.getHeat() / this.getRecipe().getHeatCapacity());
	}

	public CompoundTag writeNBT()
	{
		CompoundTag tag = new CompoundTag();
		tag.put("fuelBurner", this.fuelBurner.write());
		tag.put("localPos", NbtUtils.writeBlockPos(this.localPos));
		tag.put("blockState", NbtUtils.writeBlockState(this.blockState));
		tag.put("item", this.item.serializeNBT());
		return tag;
	}

	public void readNBT(CompoundTag tag)
	{
		this.fuelBurner.read(tag.getCompound("fuelBurner"));
		this.localPos = NbtUtils.readBlockPos(tag.getCompound("localPos"));
		this.blockState = NbtUtils.readBlockState(tag.getCompound("blockState"));
		this.item = ItemStack.of(tag.getCompound("item"));
	}

	public FuelBurner getFuelBurner()
	{
		return this.fuelBurner;
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

	public TrainEngineRecipe getRecipe()
	{
		return this.recipe != null ? this.recipe : NOT_FOUND_RECIPE;
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

}
