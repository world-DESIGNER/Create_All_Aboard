package steve_gall.create_trainwrecked.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;

public interface RegistryTagEntryType<VALUE, INGREDIENT, ENTRY extends RegistryTagEntry<? extends VALUE, ? extends INGREDIENT>>
{
	VALUE getEmptyValue();

	ENTRY of(TagEntry tagEntry);

	IForgeRegistry<VALUE> getRegistry();

	INGREDIENT toIngredient(TagKey<VALUE> tagKey);

	INGREDIENT toIngredient(VALUE value);

	public default ENTRY empty()
	{
		return this.of(this.getEmptyValue());
	}

	public default ENTRY fromJson(JsonElement json)
	{
		return this.of(TagEntryHelper.fromJson(json));
	}

	public default JsonElement toJson(ENTRY entry)
	{
		return TagEntryHelper.toJson(entry.getTagEntry());
	}

	public default ENTRY getAsTagEntry(JsonObject json, String memberName)
	{
		return this.of(TagEntryHelper.getAsTagEntry(json, memberName));
	}

	public default ENTRY fromNetwork(FriendlyByteBuf buffer)
	{
		return this.of(TagEntryHelper.fromNetwork(buffer));
	}

	public default void toNetwork(FriendlyByteBuf buffer, ENTRY entry)
	{
		TagEntryHelper.toNetwork(buffer, entry.getTagEntry());
	}

	public default ENTRY fromNBT(Tag tag)
	{
		return this.of(TagEntryHelper.fromNBT(tag));
	}

	public default Tag toNBT(ENTRY entry)
	{
		return TagEntryHelper.toNBT(entry.getTagEntry());
	}

	public default ENTRY of(VALUE value)
	{
		return this.of(value, true);
	}

	public default ENTRY of(VALUE value, boolean isRequired)
	{
		ResourceLocation id = this.getRegistry().getKey(value);
		return this.of(isRequired ? TagEntry.element(id) : TagEntry.optionalElement(id));
	}

	public default ENTRY of(TagKey<VALUE> tagKey)
	{
		return this.of(tagKey, true);
	}

	public default ENTRY of(TagKey<VALUE> tagKey, boolean isRequired)
	{
		ResourceLocation location = tagKey.location();
		return this.of(isRequired ? TagEntry.tag(location) : TagEntry.optionalTag(location));
	}

}
