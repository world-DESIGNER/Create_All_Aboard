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

public class Engine
{
	public static final ResourceLocation RECIPE_NOT_FOUND_ID = CreateTrainwrecked.asResource("recipe_not_found");
	public static final TrainEngineTypeRecipe NOT_FOUND_RECIPE = new TrainEngineTypeRecipe.Builder<>().build(RECIPE_NOT_FOUND_ID);

	private final FuelBurner fuelBurner;
	private BlockPos localPos;
	private BlockState blockState;
	private ItemStack item;

	private TrainEngineTypeRecipe recipe;
	private double fuelUsedRatio;
	private double heat;
	private double speed;
	private boolean overheated;
	private int overheatTimer;

	public Engine(EnginPos enginPos)
	{
		this.fuelBurner = new FuelBurner();
		this.localPos = enginPos.localPos();
		this.blockState = enginPos.blockState();
		this.item = enginPos.item();

		this.recipe = enginPos.recipe();
	}

	public Engine(CompoundTag tag)
	{
		this.fuelBurner = new FuelBurner(tag.getCompound("fuelBurner"));
		this.localPos = NbtUtils.readBlockPos(tag.getCompound("localPos"));
		this.blockState = NbtUtils.readBlockState(tag.getCompound("blockState"));
		this.item = ItemStack.of(tag.getCompound("item"));

		this.recipe = null;
		this.fuelUsedRatio = tag.getDouble("fuelUsedRatio");
		this.heat = tag.getDouble("heat");
		this.speed = tag.getDouble("speed");
		this.overheated = tag.getBoolean("overheated");
		this.overheatTimer = tag.getInt("overheatTimer");
	}

	public Engine(FriendlyByteBuf buffer)
	{
		this.fuelBurner = new FuelBurner(buffer);
		this.localPos = buffer.readBlockPos();
		this.blockState = NbtUtils.readBlockState(buffer.readNbt());
		this.item = buffer.readItem();

		this.recipe = null;
		this.fuelUsedRatio = buffer.readDouble();
		this.heat = buffer.readDouble();
		this.speed = buffer.readDouble();
		this.overheated = buffer.readBoolean();
		this.overheatTimer = buffer.readInt();
	}

	public void onFuelBurned(double toBurn, double burned, double allocatedSpeed)
	{
		this.fuelUsedRatio = toBurn > 0.0D ? (burned / toBurn) : 1.0D;

		TrainEngineTypeRecipe recipe = this.getRecipe();

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
			List<TrainEngineTypeRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRAIN_ENGINE_TYPE.get());
			this.recipe = recipes.stream().filter(r -> r.getBlock().test(this.item)).findFirst().orElse(null);
		}

		this.fuelUsedRatio = 0.0D;

		TrainEngineTypeRecipe recipe = this.getRecipe();
		int heatCapacity = recipe.getHeatCapacity();

		if (heatCapacity <= 0)
		{
			return;
		}

		this.setHeat(this.getHeat() - recipe.getAirCoolingRate() / 20);

		double heat = this.getHeat();
		double cooled = ((TrainExtension) train).getCoolingSystem().useCoolant(train, heat);
		this.setHeat(heat - cooled);

		if (this.getHeat() > heatCapacity)
		{
			this.setOverheat(recipe.getOverheatDuration());
		}
		else if (this.isOverheated())
		{
			this.setOverheat(this.getOverheatTimer() - 1);
		}

	}

	public static CompoundTag toNbt(Engine engine)
	{
		CompoundTag tag = new CompoundTag();
		tag.put("fuelBurner", engine.fuelBurner.writeNbt());
		tag.put("localPos", NbtUtils.writeBlockPos(engine.localPos));
		tag.put("blockState", NbtUtils.writeBlockState(engine.blockState));
		tag.put("item", engine.item.serializeNBT());

		tag.putDouble("fuelUsedRatio", engine.fuelUsedRatio);
		tag.putDouble("heat", engine.heat);
		tag.putDouble("speed", engine.speed);
		tag.putBoolean("overheated", engine.overheated);
		tag.putInt("overheatTimer", engine.overheatTimer);

		return tag;
	}

	public static void toNetwork(FriendlyByteBuf buffer, Engine engine)
	{
		engine.getFuelBurner().writeNetwork(buffer);
		buffer.writeBlockPos(engine.localPos);
		buffer.writeNbt(NbtUtils.writeBlockState(engine.blockState));
		buffer.writeItem(engine.item);

		buffer.writeDouble(engine.fuelUsedRatio);
		buffer.writeDouble(engine.heat);
		buffer.writeDouble(engine.speed);
		buffer.writeBoolean(engine.overheated);
		buffer.writeInt(engine.overheatTimer);
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

	public void setOverheat(int duration)
	{
		if (duration > 0)
		{
			this.overheated = true;
			this.overheatTimer = duration;
			this.setSpeed(0.0D);
		}
		else
		{
			this.resetOverheat();
		}

	}

	public void resetOverheat()
	{
		this.overheated = false;
		this.overheatTimer = 0;
	}

	public boolean isOverheated()
	{
		return this.overheated;
	}

	public int getOverheatTimer()
	{
		return this.overheatTimer;
	}

}
