package steve_gall.create_trainwrecked.common.crafting;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface SerializableRecipe<CONTAINER extends Container> extends Recipe<CONTAINER>
{
	void toJson(JsonObject pJson);

	void toNetwork(FriendlyByteBuf pBuffer);

	public default SimpleFinishedRecipe finish()
	{
		return new SimpleFinishedRecipe(this);
	}

}
