package steve_gall.create_all_aboard.common.content.train;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_all_aboard.common.CreateAllAboard;

public abstract class TrainPart<RECIPE extends Recipe<Container>>
{
	public static final ResourceLocation RECIPE_NOT_FOUND_ID = CreateAllAboard.asResource("recipe_not_found");

	protected final BlockPos localPos;
	protected final BlockState blockState;
	protected final ItemStack item;

	private boolean recipeCached = false;
	private RECIPE recipe = null;

	public TrainPart(Level level, CapturedPos capture)
	{
		this.localPos = capture.localPos();
		this.blockState = capture.blockState();
		this.item = capture.item();

		this.cacheRecipe(level);
	}

	public TrainPart(CompoundTag tag)
	{
		this.localPos = NbtUtils.readBlockPos(tag.getCompound("localPos"));
		this.blockState = NbtUtils.readBlockState(tag.getCompound("blockState"));
		this.item = ItemStack.of(tag.getCompound("item"));

		this.readSyncData(tag);
	}

	public void tickClient(Train train, Level level)
	{
		this.cacheRecipe(level);
	}

	public void tickServer(Train train, Level level)
	{
		this.cacheRecipe(level);
	}

	protected void cacheRecipe(Level level)
	{
		if (!this.recipeCached)
		{
			this.recipeCached = true;
			RECIPE foundRecipe = null;

			if (this.testCapture())
			{
				List<RECIPE> recipes = level.getRecipeManager().getAllRecipesFor(this.getRecipeType());
				foundRecipe = recipes.stream().filter(this::testRecipe).findFirst().orElse(null);
			}

			this.recipe = foundRecipe;
			this.onRecipeCached(foundRecipe);
		}

	}

	protected void onRecipeCached(@Nullable RECIPE recipe)
	{

	}

	public abstract RecipeType<RECIPE> getRecipeType();

	protected abstract RECIPE getFallbackRecipe();

	protected boolean testCapture()
	{
		return true;
	}

	protected abstract boolean testRecipe(RECIPE recipe);

	public CompoundTag toNbt()
	{
		CompoundTag tag = new CompoundTag();
		this.writeNbt(tag);
		return tag;
	}

	public void writeNbt(CompoundTag tag)
	{
		tag.put("localPos", NbtUtils.writeBlockPos(this.localPos));
		tag.put("blockState", NbtUtils.writeBlockState(this.blockState));
		tag.put("item", this.item.serializeNBT());
		this.writeSyncData(tag);
	}

	public void readSyncData(CompoundTag tag)
	{

	}

	public void writeSyncData(CompoundTag tag)
	{

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

	public RECIPE getRecipe()
	{
		return this.isRecipeFound() ? this.recipe : this.getFallbackRecipe();
	}

	public boolean isRecipeFound()
	{
		return this.recipe != null;
	}

}
