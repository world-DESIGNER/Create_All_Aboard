package steve_gall.create_trainwrecked.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class GsonHelper2
{
	public static <T> JsonElement getAsJsonElement(JsonObject pJson, String pMemberName)
	{
		if (pJson.has(pMemberName))
		{
			return pJson.get(pMemberName);
		}
		else
		{
			throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a JsonElement");
		}

	}

	public static <T> JsonElement getAsJsonElement(JsonObject pJson, String pMemberName, JsonElement pFallback)
	{
		return pJson.has(pMemberName) ? pJson.get(pMemberName) : pFallback;
	}

	public static <T> JsonElement toJsonAarryOrElement(List<T> list, Function<T, JsonElement> func)
	{
		if (list.size() == 1)
		{
			return func.apply(list.get(0));
		}
		else
		{
			JsonArray jarray = new JsonArray();

			for (T entry : list)
			{
				jarray.add(func.apply(entry));
			}

			return jarray;
		}

	}

	public static <T> List<T> parseAsJsonArrayOrElement(JsonObject pJson, String pMemberName, Function<JsonElement, T> func)
	{
		if (pJson.has(pMemberName))
		{
			JsonElement element = pJson.get(pMemberName);

			if (element instanceof JsonArray jarray)
			{
				List<T> list = new ArrayList<>();

				for (JsonElement child : jarray)
				{
					list.add(func.apply(child));
				}

				return list;
			}
			else
			{
				return Collections.singletonList(func.apply(element));
			}

		}
		else
		{
			throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a JsonElement");
		}

	}

	private GsonHelper2()
	{

	}

}
