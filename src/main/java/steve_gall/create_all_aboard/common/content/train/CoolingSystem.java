package steve_gall.create_all_aboard.common.content.train;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandlerModifiable;
import steve_gall.create_all_aboard.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_all_aboard.common.fluid.FluidHelper;
import steve_gall.create_all_aboard.common.init.ModRecipeTypes;

public class CoolingSystem
{
	private int cooling;
	private List<TrainEngineCoolantRecipe> recipes;
	private List<TrainEngineCoolantRecipe> itemRecipes;
	private List<TrainEngineCoolantRecipe> fluidRecipes;

	public CoolingSystem()
	{

	}

	public void tick(Train train, Level level)
	{
		if (this.recipes == null)
		{
			this.recipes = new ArrayList<>(level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRAIN_ENGINE_COOLANT.get()));
			this.itemRecipes = this.recipes.stream().filter(r -> r.getItemIngredient() != null).toList();
			this.fluidRecipes = this.recipes.stream().filter(r -> r.getFluidIngredient() != null).toList();
		}

	}

	public int getCooling(ItemStack item)
	{
		if (item.isEmpty() || this.itemRecipes == null)
		{
			return 0;
		}
		else
		{
			for (TrainEngineCoolantRecipe recipe : this.itemRecipes)
			{
				if (recipe.getItemIngredient().test(item))
				{
					return recipe.getCooling();
				}

			}

		}

		return 0;
	}

	public TrainEngineCoolantRecipe getCoolingRecipe(FluidStack fluid)
	{
		if (fluid.isEmpty() || this.fluidRecipes == null)
		{
			return null;
		}
		else
		{
			for (TrainEngineCoolantRecipe recipe : this.fluidRecipes)
			{
				if (recipe.getFluidIngredient().test(fluid))
				{
					return recipe;
				}

			}

			return null;
		}

	}

	public double useCoolant(Train train, double heat)
	{
		double cooled = 0.0D;

		while (true)
		{
			if (heat <= 0.0D)
			{
				break;
			}
			else if (this.cooling >= heat)
			{
				cooled += heat;
				this.cooling -= heat;
				heat = 0;
				break;
			}
			else
			{
				cooled += this.cooling;
				heat -= this.cooling;
				this.cooling = 0;
			}

			List<TrainCargoManager> storages = train.carriages.stream().map(s -> s.storage).toList();

			if (this.useItems(storages))
			{
				continue;
			}
			else if (this.useFluids(storages))
			{
				continue;
			}
			else
			{
				break;
			}

		}

		return cooled;
	}

	private boolean useItems(List<TrainCargoManager> storages)
	{
		for (TrainCargoManager storage : storages)
		{
			IItemHandlerModifiable items = storage.getFuelItems();
			int slots = items.getSlots();

			for (int slot = 0; slot < slots; slot++)
			{
				ItemStack item = items.extractItem(slot, 1, true);
				int cooling = this.getCooling(item);

				if (cooling > 0)
				{
					items.extractItem(slot, 1, false);
					this.cooling += cooling;
					return true;
				}

			}

		}

		return false;
	}

	private boolean useFluids(List<TrainCargoManager> storages)
	{
		for (TrainCargoManager storage : storages)
		{
			IFluidHandler fluids = storage.getFluids();
			int tanks = fluids.getTanks();

			for (int tank = 0; tank < tanks; tank++)
			{
				FluidStack fluid = fluids.getFluidInTank(tank);
				TrainEngineCoolantRecipe recipe = this.getCoolingRecipe(fluid);

				if (recipe != null)
				{
					int requiredAmount = recipe.getFluidIngredient().getRequiredAmount();
					FluidStack drain = fluids.drain(FluidHelper.deriveAmount(fluid, requiredAmount), FluidAction.SIMULATE);

					if (!drain.isEmpty() && drain.getAmount() >= requiredAmount)
					{
						fluids.drain(FluidHelper.deriveAmount(fluid, requiredAmount), FluidAction.EXECUTE);
						this.cooling += recipe.getCooling();
						return true;
					}

				}

			}

		}

		return false;
	}

	public void read(CompoundTag tag)
	{
		this.cooling = tag.getInt("cooling");
	}

	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("cooling", this.cooling);

		return tag;
	}

}
