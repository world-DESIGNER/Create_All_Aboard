package steve_gall.create_trainwrecked.common.util;

import com.google.gson.JsonElement;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class RegistryTagEntry<VALUE, INGREDIENT>
{
	private final TagEntry tagEntry;

	public RegistryTagEntry(TagEntry tagEntry)
	{
		this.tagEntry = tagEntry;
	}

	@Override
	public String toString()
	{
		return this.getTagEntry().toString();
	}

	public INGREDIENT toIngredient()
	{
		RegistryTagEntryType<VALUE, INGREDIENT, ?> type = this.getType();
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
			return type.toIngredient(value);
		}

	}

	public abstract RegistryTagEntryType<VALUE, INGREDIENT, ? extends RegistryTagEntry<VALUE, INGREDIENT>> getType();

	public TagEntry getTagEntry()
	{
		return this.tagEntry;
	}

	@SuppressWarnings("unchecked")
	public JsonElement toJson()
	{
		RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>> type = (RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>>) this.getType();
		return type.toJson(this);
	}

	@SuppressWarnings("unchecked")
	public void toNetwork(FriendlyByteBuf buffer)
	{
		RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>> type = (RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>>) this.getType();
		type.toNetwork(buffer, this);
	}

	@SuppressWarnings("unchecked")
	public Tag toNbt()
	{
		RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>> type = (RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>>) this.getType();
		return type.toNbt(this);
	}

}
