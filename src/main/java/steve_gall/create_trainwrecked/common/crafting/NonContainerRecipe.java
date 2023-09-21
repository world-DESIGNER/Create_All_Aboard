package steve_gall.create_trainwrecked.common.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public interface NonContainerRecipe extends Recipe<Container>
{
	@Override
	public default boolean matches(Container pContainer, Level pLevel)
	{
		return true;
	}

	@Override
	public default ItemStack assemble(Container pContainer)
	{
		return this.getResultItem();
	}

	@Override
	public default boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return true;
	}

	@Override
	public default ItemStack getResultItem()
	{
		return ItemStack.EMPTY;
	}

}
