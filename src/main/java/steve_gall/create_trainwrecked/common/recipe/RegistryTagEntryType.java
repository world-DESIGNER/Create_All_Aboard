package steve_gall.create_trainwrecked.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface RegistryTagEntryType<VALUE extends IForgeRegistryEntry<VALUE>, INGREDIENT, ENTRY extends RegistryTagEntry<? extends VALUE, ? extends INGREDIENT>>
{
	VALUE getEmptyValue();

	ENTRY of(WrappedTagEntry tagEntry);

	IForgeRegistry<VALUE> getRegistry();

	INGREDIENT toIngredient(TagKey<VALUE> tagKey);

	INGREDIENT toIngredient(VALUE value);

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

	public default ENTRY fromNBT(CompoundTag tag)
	{
		return this.of(WrappedTagEntry.fromNBT(tag));
	}

	public default CompoundTag toNBT(ENTRY entry)
	{
		return entry.getTagEntry().toNBT();
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

}
