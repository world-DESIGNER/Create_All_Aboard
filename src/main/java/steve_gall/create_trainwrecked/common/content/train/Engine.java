package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.fluid.FluidHelper;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipe;

public class Engine
{
	public static final ResourceLocation RECIPE_NOT_FOUND_ID = CreateTrainwrecked.asResource("recipe_not_found");
	public static final TrainEngineRecipe NOT_FOUND_RECIPE = new TrainEngineRecipe.Builder().build(RECIPE_NOT_FOUND_ID);

	private BlockPos localPos;
	private BlockState blockState;
	private ItemStack item;

	private TrainEngineRecipe recipe;
	private double heat;
	private double speed;
	private double remainedFuel;

	public Engine(CompoundTag tag)
	{
		this.readNBT(tag);
		this.recipe = null;
	}

	public Engine(EnginPos enginPos)
	{
		this.localPos = enginPos.localPos();
		this.blockState = enginPos.blockState();
		this.item = enginPos.item();
		this.recipe = enginPos.recipe();
	}

	public void burnFuel(Train train, double targetSpeed)
	{
		TrainEngineRecipe recipe = this.getRecipe();
		double toBurn = (targetSpeed * recipe.getFuelPerSpeed()) / 20;
		int extracting = Mth.ceil(Math.max(toBurn - this.remainedFuel, 0.0D));
		int extracted = 0;

		if (extracting > 0)
		{
			for (Carriage carriage : train.carriages)
			{
				IFluidHandler fluidStroage = carriage.storage.getFluids();

				for (FluidStack fluidStack : recipe.getFuel().getMatchingFluidStacks())
				{
					if (extracted >= extracting)
					{
						break;
					}

					FluidStack burning = FluidHelper.deriveAmount(fluidStack, extracting - extracted);
					FluidStack burned = fluidStroage.drain(burning, FluidAction.EXECUTE);

					if (!burned.isEmpty())
					{
						extracted += burned.getAmount();
					}

				}

			}

		}

		double burning = 0.0D;

		if (extracted + this.remainedFuel > toBurn)
		{
			burning = toBurn;
			this.remainedFuel += extracted - toBurn;
		}
		else
		{
			this.remainedFuel = 0.0D;
		}

		this.setSpeed(burning / recipe.getFuelPerSpeed());
		this.setHeat(this.getHeat() + (burning * recipe.getHeatPerFuel()));

//		System.out.println("toBurn:" + toBurn + ", extracting: " + extracting + ", extracted: " + extracted + ", burning: " + burning + ", remainedFuel: " + remainedFuel);
	}

	public void tick(Train train, Level level)
	{
		if (this.recipe == null)
		{
			List<TrainEngineRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRAIN_ENGINE.get());
			this.recipe = recipes.stream().filter(r -> r.getBlock().test(this.item)).findFirst().orElse(null);
		}

		this.setHeat(this.getHeat() - this.getRecipe().getAirCoolingRate() / 20);
//		System.out.println("heat: " + this.getHeat() + ", " + this.getHeat() / this.getRecipe().getHeatCapacity());
	}

	public CompoundTag writeNBT()
	{
		CompoundTag tag = new CompoundTag();
		tag.put("localPos", NbtUtils.writeBlockPos(this.localPos));
		tag.put("blockState", NbtUtils.writeBlockState(this.blockState));
		tag.put("item", this.item.serializeNBT());

		tag.putDouble("remainedFuel", this.remainedFuel);
		return tag;
	}

	public void readNBT(CompoundTag tag)
	{
		this.localPos = NbtUtils.readBlockPos(tag.getCompound("localPos"));
		this.blockState = NbtUtils.readBlockState(tag.getCompound("blockState"));
		this.item = ItemStack.of(tag.getCompound("item"));

		this.remainedFuel = tag.getDouble("remainedFuel");
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
