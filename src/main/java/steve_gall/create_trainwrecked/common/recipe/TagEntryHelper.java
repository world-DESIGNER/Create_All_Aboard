package steve_gall.create_trainwrecked.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.util.GsonHelper;

public class TagEntryHelper
{
	public static WrappedTagEntry convertToTagEntry(JsonElement pJson, String pMemberName)
	{
		try
		{
			return WrappedTagEntry.fromJson(pJson);
		}
		catch (Exception e)
		{
			throw new JsonSyntaxException("Expected " + pMemberName + " to be a TagEntry, was " + GsonHelper.getType(pJson), e);
		}

	}

	public static WrappedTagEntry getAsTagEntry(JsonObject pJson, String pMemberName)
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

	private TagEntryHelper()
	{

	}

}
