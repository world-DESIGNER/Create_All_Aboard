package steve_gall.create_all_aboard.common.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemTagEntry extends RegistryTagEntry<Item, ItemStack, Ingredient>
{
	public static final ItemTagEntryType TYPE = new ItemTagEntryType();

	public ItemTagEntry(WrappedTagEntry tagEntry)
	{
		super(tagEntry);
	}

	@Override
	public RegistryTagEntryType<Item, ItemStack, Ingredient, ItemTagEntry> getType()
	{
		return TYPE;
	}

}
