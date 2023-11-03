package steve_gall.create_all_aboard.common.util;

import java.util.Arrays;
import java.util.stream.Stream;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemTagEntryType implements RegistryTagEntryType<Item, ItemStack, Ingredient, ItemTagEntry>
{
	@Override
	public Item getEmptyValue()
	{
		return Items.AIR;
	}

	@Override
	public Ingredient getEmptyIngredient()
	{
		return Ingredient.EMPTY;
	}

	public ItemTagEntry of(ItemLike item)
	{
		return this.of(item.asItem());
	}

	@Override
	public ItemTagEntry of(WrappedTagEntry tagEntry)
	{
		return new ItemTagEntry(tagEntry);
	}

	@Override
	public IForgeRegistry<Item> getRegistry()
	{
		return ForgeRegistries.ITEMS;
	}

	@Override
	public Ingredient toIngredient(TagKey<Item> tagKey)
	{
		return Ingredient.of(tagKey);
	}

	@Override
	public Ingredient toIngredient(Item value)
	{
		return Ingredient.of(value);
	}

	@Override
	public Stream<ItemStack> getIngredientMatchingStacks(Ingredient ingredient)
	{
		return Arrays.stream(ingredient.getItems());
	}

	@Override
	public boolean testIngredient(Ingredient ingredient, ItemStack stack)
	{
		return ingredient.test(stack);
	}

}
