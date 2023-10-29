package steve_gall.create_all_aboard.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;

public interface RegistryTagEntryType<VALUE, STACK, INGREDIENT, ENTRY extends RegistryTagEntry<VALUE, STACK, INGREDIENT>>
{
	VALUE getEmptyValue();

	INGREDIENT getEmptyIngredient();

	ENTRY of(TagEntry tagEntry);

	IForgeRegistry<VALUE> getRegistry();

	INGREDIENT toIngredient(TagKey<VALUE> tagKey);

	INGREDIENT toIngredient(VALUE value);

	Stream<STACK> getIngredientMatchingStacks(INGREDIENT ingredient);

	boolean testIngredient(INGREDIENT ingredient, STACK stack);

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

	public default ENTRY fromNbt(Tag tag)
	{
		return this.of(TagEntryHelper.fromNbt(tag));
	}

	public default Tag toNbt(ENTRY entry)
	{
		return TagEntryHelper.toNbt(entry.getTagEntry());
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

	public default Stream<STACK> getIngredientMatchingStacks(Collection<INGREDIENT> collection)
	{
		return collection.stream().flatMap(this::getIngredientMatchingStacks);
	}

	public default boolean testIngredient(Collection<INGREDIENT> collection, STACK stack)
	{
		return collection.stream().anyMatch(i -> this.testIngredient(i, stack));
	}

	public default List<ENTRY> getAsTagEntryList(JsonObject json, String memberName)
	{
		return GsonHelper2.parseAsJsonArrayOrElement(json, memberName, this::fromJson);
	}

	public default JsonElement listToJson(List<ENTRY> list)
	{
		return GsonHelper2.toJsonAarryOrElement(list, ENTRY::toJson);
	}

	public default List<ENTRY> listFromNetwork(FriendlyByteBuf buffer)
	{
		return buffer.readCollection(ArrayList::new, this::fromNetwork);
	}

	public default void listToNetowrk(FriendlyByteBuf buffer, List<ENTRY> list)
	{
		buffer.writeCollection(list, (buf, entry) -> entry.toNetwork(buf));
	}

}
