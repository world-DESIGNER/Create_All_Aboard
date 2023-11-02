package steve_gall.create_all_aboard.common.content.train;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraftforge.items.IItemHandlerModifiable;
import steve_gall.create_all_aboard.common.crafting.HeatStage;
import steve_gall.create_all_aboard.common.crafting.TrainHeatSourceRecipe;
import steve_gall.create_all_aboard.common.init.ModRecipeTypes;

public class HeatSource extends TrainPart<TrainHeatSourceRecipe>
{
	public static final TrainHeatSourceRecipe NOT_FOUND_RECIPE = new TrainHeatSourceRecipe.Builder<>().build(RECIPE_NOT_FOUND_ID);
	public static final HeatStage NOT_FOUND_PASSIVE = new HeatStage.Builder().level(0).passive().build();

	private HeatStage passive;
	private List<HeatStage> ingredients;
	private int maxLevel;

	private int fuelLevel;
	private int fuelTime;

	public HeatSource(Level level, CapturedPos capture)
	{
		super(level, capture);
	}

	public HeatSource(CompoundTag tag)
	{
		super(tag);

		this.fuelLevel = tag.getInt("fuelLevel");
		this.fuelTime = tag.getInt("fuelTime");
	}

	@Override
	public void writeNbt(CompoundTag tag)
	{
		super.writeNbt(tag);

		tag.putInt("fuelLevel", this.fuelLevel);
		tag.putInt("fuelTime", this.fuelTime);
	}

	@Override
	public void readSyncData(FriendlyByteBuf buffer)
	{
		super.readSyncData(buffer);

		this.fuelLevel = buffer.readInt();
		this.fuelTime = buffer.readInt();
	}

	@Override
	public void writeSyncData(FriendlyByteBuf buffer)
	{
		super.writeSyncData(buffer);

		buffer.writeInt(this.fuelLevel);
		buffer.writeInt(this.fuelTime);
	}

	@Override
	public void tickServer(Train train, Level level)
	{
		super.tickServer(train, level);

		int fuelTime = this.getFuelTime();

		if (fuelTime > 0)
		{
			this.setFuelTime(fuelTime - 1);
		}

	}

	public void burnFuel(Train train)
	{
		if (this.getFuelTime() > 0)
		{
			return;
		}

		for (HeatStage stage : this.ingredients)
		{
			for (Carriage carriage : train.carriages)
			{
				IItemHandlerModifiable items = carriage.storage.getFuelItems();
				int slots = items.getSlots();

				for (int slot = 0; slot < slots; slot++)
				{
					ItemStack item = items.extractItem(slot, 1, true);
					int burnTime = stage.getBurnTime(item);

					if (burnTime > 0)
					{
						items.extractItem(slot, 1, false);
						this.setFuel(stage.getLevel(), burnTime);
						return;
					}

				}

			}

		}

	}

	@Override
	protected boolean testCapture()
	{
		if (this.getBlockState().getBlock() == Blocks.CAMPFIRE && !this.getBlockState().getValue(CampfireBlock.LIT))
		{
			return false;
		}

		return super.testCapture();
	}

	@Override
	protected void onRecipeCached(@Nullable TrainHeatSourceRecipe recipe)
	{
		super.onRecipeCached(recipe);

		this.maxLevel = 0;
		this.passive = NOT_FOUND_PASSIVE;
		this.ingredients = new ArrayList<>();

		if (recipe != null)
		{
			for (HeatStage stage : recipe.getStages())
			{
				int level = stage.getLevel();
				this.maxLevel = Math.max(this.maxLevel, level);

				if (stage.getIngredientType() == HeatStage.IngredientType.PASSIVE)
				{
					if (this.passive.getLevel() < level)
					{
						this.passive = stage;
					}

				}
				else
				{
					this.ingredients.add(stage);
				}

			}

			this.ingredients.sort((o1, o2) -> Integer.compare(o2.getLevel(), o1.getLevel()));
		}

	}

	@Override
	public RecipeType<TrainHeatSourceRecipe> getRecipeType()
	{
		return ModRecipeTypes.TRAIN_HEAT_SOURCE.get();
	}

	@Override
	protected TrainHeatSourceRecipe getFallbackRecipe()
	{
		return NOT_FOUND_RECIPE;
	}

	@Override
	protected boolean testRecipe(TrainHeatSourceRecipe recipe)
	{
		return recipe.getBlocks().stream().anyMatch(i -> i.test(this.item));
	}

	public int getLevel()
	{
		int fuelLevel = this.getFuelLevel();
		return fuelLevel > 0 ? fuelLevel : this.passive.getLevel();
	}

	public HeatLevel getLevelAsHeatLevel()
	{
		int level = this.getLevel();

		if (level <= 1)
		{
			return HeatLevel.SMOULDERING;
		}
		else if (level == 2)
		{
			return HeatLevel.KINDLED;
		}
		else
		{
			return HeatLevel.SEETHING;
		}

	}

	public double getSpeedLimit()
	{
		return HeatStage.getSpeedLimit(this.getLevel());
	}

	public void setFuel(int fuellevel, int fuelTime)
	{
		this.fuelLevel = Math.max(fuellevel, 0);
		this.fuelTime = Math.max(fuelTime, 0);
	}

	public void resetFuel()
	{
		this.fuelLevel = 0;
		this.fuelTime = 0;
	}

	public int getFuelLevel()
	{
		return this.fuelLevel;
	}

	public int getFuelTime()
	{
		return this.fuelTime;
	}

	public void setFuelTime(int fuelTime)
	{
		if (fuelTime > 0 && this.getFuelLevel() > 0)
		{
			this.fuelTime = fuelTime;
		}
		else
		{
			this.resetFuel();
		}

	}

	public int getMaxLevel()
	{
		return this.maxLevel;
	}

}
