package steve_gall.create_trainwrecked.common.content.train;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

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
import steve_gall.create_trainwrecked.common.crafting.HeatStage;
import steve_gall.create_trainwrecked.common.crafting.TrainHeatSourceRecipe;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;

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

	public HeatSource(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.fuelLevel = buffer.readInt();
		this.fuelTime = buffer.readInt();
	}

	@Override
	public void serializeNbt(CompoundTag tag)
	{
		super.serializeNbt(tag);

		tag.putInt("fuelLevel", this.fuelLevel);
		tag.putInt("fuelTime", this.fuelTime);
	}

	@Override
	public void serializeNetwork(FriendlyByteBuf buffer)
	{
		super.serializeNetwork(buffer);

		buffer.writeInt(this.fuelLevel);
		buffer.writeInt(this.fuelTime);
	}

	@Override
	public void tick(Train train, Level level)
	{
		super.tick(train, level);

		int fuelTime = this.getFuelTime();

		if (fuelTime > 0)
		{
			this.setFuel(this.getFuelLevel(), fuelTime - 1);
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

	public static CompoundTag toNbt(HeatSource heatSource)
	{
		CompoundTag tag = new CompoundTag();
		heatSource.serializeNbt(tag);
		return tag;
	}

	public static void toNetwork(FriendlyByteBuf buffer, HeatSource HeatSource)
	{
		HeatSource.serializeNetwork(buffer);
	}

	public int getLevel()
	{
		return this.getFuelTime() > 0 ? this.fuelLevel : this.passive.getLevel();
	}

	public double getSpeedLimit()
	{
		return HeatStage.getSpeedLimit(this.getLevel());
	}

	public int getFuelLevel()
	{
		return this.fuelLevel;
	}

	public int getFuelTime()
	{
		return this.fuelTime;
	}

	public void setFuel(int fuellevel, int fuelTime)
	{
		this.fuelLevel = Math.max(fuellevel, 0);
		this.fuelTime = Math.max(fuelTime, 0);
	}

	public int getMaxLevel()
	{
		return this.maxLevel;
	}

}
