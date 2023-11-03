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
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface RegistryTagEntryType<VALUE extends IForgeRegistryEntry<VALUE>, STACK, INGREDIENT, ENTRY extends RegistryTagEntry<VALUE, STACK, INGREDIENT>>
{
	VALUE getEmptyValue();

	INGREDIENT getEmptyIngredient();

	ENTRY of(WrappedTagEntry tagEntry);

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
		return this.of(WrappedTagEntry.fromJson(json));
	}

	public default JsonElement toJson(ENTRY entry)
	{
		return entry.getTagEntry().toJson();
	}

	public default ENTRY getAsTagEntry(JsonObject json, String memberName)
	{
		return this.of(TagEntryHelper.getAsTagEntry(json, memberName));
	}

	public default ENTRY fromNetwork(FriendlyByteBuf buffer)
	{
		return this.of(WrappedTagEntry.fromNetwork(buffer));
	}

	public default void toNetwork(FriendlyByteBuf buffer, ENTRY entry)
	{
		entry.getTagEntry().toNetwork(buffer);
	}

	public default ENTRY fromNbt(Tag tag)
	{
		return this.of(WrappedTagEntry.fromNbt(tag));
	}

	public default Tag toNbt(ENTRY entry)
	{
		return entry.getTagEntry().toNbt();
	}

	public default ENTRY of(VALUE value)
	{
		return this.of(value, true);
	}

	public default ENTRY of(VALUE value, boolean isRequired)
	{
		ResourceLocation id = this.getRegistry().getKey(value);
		return this.of(new WrappedTagEntry(id, false, isRequired));
	}

	public default ENTRY of(TagKey<VALUE> tagKey)
	{
		return this.of(tagKey, true);
	}

	public default ENTRY of(TagKey<VALUE> tagKey, boolean isRequired)
	{
		return this.of(new WrappedTagEntry(tagKey.location(), true, isRequired));
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
