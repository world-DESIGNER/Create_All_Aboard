package steve_gall.create_trainwrecked.common.util;

import java.util.stream.Stream;

import com.google.gson.JsonElement;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class RegistryTagEntry<VALUE, STACK, INGREDIENT>
{
	private final TagEntry tagEntry;

	public RegistryTagEntry(TagEntry tagEntry)
	{
		this.tagEntry = tagEntry;
	}

	public INGREDIENT toIngredient()
	{
		RegistryTagEntryType<VALUE, STACK, INGREDIENT, ?> type = this.getType();
		IForgeRegistry<VALUE> registry = type.getRegistry();
		TagEntry tagEntry = this.getTagEntry();

		if (tagEntry.isTag())
		{
			TagKey<VALUE> tagKey = registry.tags().createTagKey(tagEntry.getId());
			return type.toIngredient(tagKey);
		}
		else
		{
			VALUE value = registry.getValue(tagEntry.getId());

			if (value != null)
			{
				return type.toIngredient(value);
			}
			else
			{
				return type.getEmptyIngredient();
			}

		}

	}

	public abstract RegistryTagEntryType<VALUE, STACK, INGREDIENT, ? extends RegistryTagEntry<VALUE, STACK, INGREDIENT>> getType();

	public Stream<STACK> getMatchingStacks()
	{
		return this.getType().getIngredientMatchingStacks(this.toIngredient());
	}

	public boolean test(STACK stack)
	{
		return this.getType().testIngredient(this.toIngredient(), stack);
	}

	public TagEntry getTagEntry()
	{
		return this.tagEntry;
	}

	@Override
	public String toString()
	{
		return this.getTagEntry().toString();
	}

	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		else
		{
			return obj instanceof RegistryTagEntry<?, ?, ?> other && this.toString().equals(other.toString());
		}

	}

	@SuppressWarnings("unchecked")
	public JsonElement toJson()
	{
		RegistryTagEntryType<VALUE, STACK, INGREDIENT, RegistryTagEntry<VALUE, STACK, INGREDIENT>> type = (RegistryTagEntryType<VALUE, STACK, INGREDIENT, RegistryTagEntry<VALUE, STACK, INGREDIENT>>) this.getType();
		return type.toJson(this);
	}

	@SuppressWarnings("unchecked")
	public void toNetwork(FriendlyByteBuf buffer)
	{
		RegistryTagEntryType<VALUE, STACK, INGREDIENT, RegistryTagEntry<VALUE, STACK, INGREDIENT>> type = (RegistryTagEntryType<VALUE, STACK, INGREDIENT, RegistryTagEntry<VALUE, STACK, INGREDIENT>>) this.getType();
		type.toNetwork(buffer, this);
	}

	@SuppressWarnings("unchecked")
	public Tag toNbt()
	{
		RegistryTagEntryType<VALUE, STACK, INGREDIENT, RegistryTagEntry<VALUE, STACK, INGREDIENT>> type = (RegistryTagEntryType<VALUE, STACK, INGREDIENT, RegistryTagEntry<VALUE, STACK, INGREDIENT>>) this.getType();
		return type.toNbt(this);
	}

}
