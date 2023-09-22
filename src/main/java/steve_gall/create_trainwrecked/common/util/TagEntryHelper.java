package steve_gall.create_trainwrecked.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.util.GsonHelper;

public class TagEntryHelper
{
	public static <T> TagEntry decode(DynamicOps<T> ops, T from)
	{
		return TagEntry.CODEC.decode(ops, from).result().get().getFirst();
	}

	public static <T> T encode(DynamicOps<T> ops, TagEntry tagEntry)
	{
		return TagEntry.CODEC.encodeStart(ops, tagEntry).result().get();
	}

	public static TagEntry fromJson(JsonElement json)
	{
		return decode(JsonOps.INSTANCE, json);
	}

	public static JsonElement toJson(TagEntry tagEntry)
	{
		return encode(JsonOps.INSTANCE, tagEntry);
	}

	public static TagEntry fromNBT(Tag tag)
	{
		return decode(NbtOps.INSTANCE, tag);
	}

	public static Tag toNBT(TagEntry tagEntry)
	{
		return encode(NbtOps.INSTANCE, tagEntry);
	}

	public static TagEntry convertToTagEntry(JsonElement pJson, String pMemberName)
	{
		try
		{
			return fromJson(pJson);
		}
		catch (Exception e)
		{
			throw new JsonSyntaxException("Expected " + pMemberName + " to be a TagEntry, was " + GsonHelper.getType(pJson), e);
		}

	}

	public static TagEntry getAsTagEntry(JsonObject pJson, String pMemberName)
	{
		if (pJson.has(pMemberName))
		{
			return convertToTagEntry(pJson.get(pMemberName), pMemberName);
		}
		else
		{
			throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a TagEntry");
		}

	}

	public static TagEntry fromNetwork(FriendlyByteBuf buffer)
	{
		ResourceLocation id = buffer.readResourceLocation();
		boolean isTag = buffer.readBoolean();
		boolean isRequired = buffer.readBoolean();

		if (isTag)
		{
			return isRequired ? TagEntry.tag(id) : TagEntry.optionalTag(id);
		}
		else
		{
			return isRequired ? TagEntry.element(id) : TagEntry.optionalElement(id);
		}

	}

	public static void toNetwork(FriendlyByteBuf buffer, TagEntry tagEntry)
	{
		buffer.writeResourceLocation(tagEntry.getId());
		buffer.writeBoolean(tagEntry.isTag());
		buffer.writeBoolean(tagEntry.isRequired());
	}

	private TagEntryHelper()
	{

	}

}
