package steve_gall.create_trainwrecked.common.util;

import net.minecraft.tags.TagEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemTagEntry extends RegistryTagEntry<Item, ItemStack, Ingredient>
{
	public static final ItemTagEntryType TYPE = new ItemTagEntryType();

	public ItemTagEntry(TagEntry tagEntry)
	{
		super(tagEntry);
	}

	@Override
	public RegistryTagEntryType<Item, ItemStack, Ingredient, ItemTagEntry> getType()
	{
		return TYPE;
	}

}
