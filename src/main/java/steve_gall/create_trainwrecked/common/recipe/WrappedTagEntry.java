package steve_gall.create_trainwrecked.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public record WrappedTagEntry(ResourceLocation id, boolean isTag, boolean isRequired)
{
	public static WrappedTagEntry fromJson(JsonElement json)
	{
		String idString;
		boolean isRequired;

		if (json instanceof JsonObject jsonobject)
		{
			idString = GsonHelper.getAsString(jsonobject, "id");
			isRequired = GsonHelper.getAsBoolean(jsonobject, "required", true);
		}
		else
		{
			idString = GsonHelper.convertToString(json, "id");
			isRequired = true;
		}

		if (idString.startsWith("#"))
		{
			ResourceLocation id = new ResourceLocation(idString.substring(1));
			return new WrappedTagEntry(id, true, isRequired);
		}
		else
		{
			ResourceLocation id = new ResourceLocation(idString);
			return new WrappedTagEntry(id, false, isRequired);
		}

	}

	public static WrappedTagEntry fromNetwork(FriendlyByteBuf buffer)
	{
		ResourceLocation id = buffer.readResourceLocation();
		boolean isTag = buffer.readBoolean();
		boolean isRequired = buffer.readBoolean();
		return new WrappedTagEntry(id, isTag, isRequired);
	}

	public JsonElement toJson()
	{

		if (this.isRequired())
		{
			if (this.isTag())
			{
				return new JsonPrimitive("#" + this.id().toString());
			}
			else
			{
				return new JsonPrimitive(this.id().toString());
			}

		}
		else
		{
			JsonObject json = new JsonObject();
			json.addProperty("id", this.id().toString());
			json.addProperty("isTag", this.isTag());
			json.addProperty("isRequired", this.isRequired());
			return json;
		}

	}

	public void toNetwork(FriendlyByteBuf buffer)
	{
		buffer.writeResourceLocation(this.id());
		buffer.writeBoolean(this.isTag());
		buffer.writeBoolean(this.isRequired());
	}

}
