package steve_gall.create_trainwrecked.common.util;

import net.minecraft.tags.TagEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemTagEntry extends RegistryTagEntry<Item, Ingredient>
{
	public static final ItemTagEntryType TYPE = new ItemTagEntryType();

	public ItemTagEntry(TagEntry tagEntry)
	{
		super(tagEntry);
	}

	@Override
	public RegistryTagEntryType<Item, Ingredient, ItemTagEntry> getType()
	{
		return TYPE;
	}

}
