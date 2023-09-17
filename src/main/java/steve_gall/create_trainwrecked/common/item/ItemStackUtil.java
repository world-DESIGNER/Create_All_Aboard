package steve_gall.create_trainwrecked.common.item;

import net.minecraft.world.item.ItemStack;

public class ItemStackUtil
{
	public static ItemStack deriveCount(ItemStack itemStack, int newCount)
	{
		itemStack = itemStack.copy();
		itemStack.setCount(newCount);
		return itemStack;
	}

	private ItemStackUtil()
	{

	}

}
