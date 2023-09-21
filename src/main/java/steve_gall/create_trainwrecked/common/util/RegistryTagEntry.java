package steve_gall.create_trainwrecked.common.util;

import com.google.gson.JsonElement;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class RegistryTagEntry<VALUE extends IForgeRegistryEntry<VALUE>, INGREDIENT>
{
	private final WrappedTagEntry tagEntry;

	public RegistryTagEntry(WrappedTagEntry tagEntry)
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
		WrappedTagEntry tagEntry = this.getTagEntry();

		if (tagEntry.isTag())
		{
			TagKey<VALUE> tagKey = registry.tags().createTagKey(tagEntry.id());
			return type.toIngredient(tagKey);
		}
		else
		{
			VALUE value = registry.getValue(tagEntry.id());
			return type.toIngredient(value);
		}

	}

	public abstract RegistryTagEntryType<VALUE, INGREDIENT, ? extends RegistryTagEntry<VALUE, INGREDIENT>> getType();

	public WrappedTagEntry getTagEntry()
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
	public CompoundTag toNBT()
	{
		RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>> type = (RegistryTagEntryType<VALUE, INGREDIENT, RegistryTagEntry<VALUE, INGREDIENT>>) this.getType();
		return type.toNBT(this);
	}

}
