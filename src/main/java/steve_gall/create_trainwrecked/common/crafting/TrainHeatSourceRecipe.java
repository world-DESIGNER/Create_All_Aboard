package steve_gall.create_trainwrecked.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.create_trainwrecked.common.init.ModRecipeSerializers;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.GsonHelper2;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;

public class TrainHeatSourceRecipe implements SerializableRecipe<Container>, NonContainerRecipe
{
	private final ResourceLocation id;
	private final List<ItemTagEntry> blockType;
	private final List<HeatStage> stages;

	private final List<Ingredient> blocks;

	private TrainHeatSourceRecipe(ResourceLocation id, Builder<?> builder)
	{
		this.id = id;
		this.blockType = new ArrayList<>(builder.blockType());
		this.stages = new ArrayList<>(builder.stage());

		this.blocks = this.blockType.stream().map(ItemTagEntry::toIngredient).toList();
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ModRecipeSerializers.TRAIN_HEAT_SOURCE.get();
	}

	@Override
	public RecipeType<?> getType()
	{
		return ModRecipeTypes.TRAIN_HEAT_SOURCE.get();
	}

	@Override
	public void toJson(JsonObject pJson)
	{
		pJson.add("blockType", ItemTagEntry.TYPE.listToJson(this.getBlockType()));
		pJson.add("stage", GsonHelper2.toJsonAarryOrElement(this.getStages(), HeatStage::toJson));
	}

	@Override
	public void toNetwork(FriendlyByteBuf pBuffer)
	{
		ItemTagEntry.TYPE.listToNetowrk(pBuffer, this.getBlockType());
		pBuffer.writeCollection(this.getStages(), (b, e) -> e.toNetwork(b));
	}

	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}

	public List<ItemTagEntry> getBlockType()
	{
		return this.blockType;
	}

	public List<Ingredient> getBlocks()
	{
		return this.blocks;
	}

	public List<HeatStage> getStages()
	{
		return this.stages;
	}

	public static class Serializer extends SimpleRecipeSerializer<TrainHeatSourceRecipe, Builder<?>>
	{
		@Override
		protected Builder<?> fromJson(JsonObject pJson)
		{
			return new Builder<>(pJson);
		}

		@Override
		protected Builder<?> fromNetwork(FriendlyByteBuf pBuffer)
		{
			return new Builder<>(pBuffer);
		}

	}

	@SuppressWarnings("unchecked")
	public static class Builder<T extends Builder<T>> extends SimpleRecipeBuilder<T, TrainHeatSourceRecipe>
	{
		private final List<ItemTagEntry> blockType = new ArrayList<>();
		private final List<HeatStage> stage = new ArrayList<>();

		public Builder()
		{

		}

		public Builder(JsonObject pJson)
		{
			this.blockType().addAll(ItemTagEntry.TYPE.getAsTagEntryList(pJson, "blockType"));
			this.stage().addAll(GsonHelper2.parseAsJsonArrayOrElement(pJson, "stage", HeatStage::new));
		}

		public Builder(FriendlyByteBuf pBuffer)
		{
			this.blockType().addAll(ItemTagEntry.TYPE.listFromNetwork(pBuffer));
			this.stage().addAll(pBuffer.readList(HeatStage::new));
		}

		@Override
		public TrainHeatSourceRecipe build(ResourceLocation id)
		{
			return new TrainHeatSourceRecipe(id, this);
		}

		public List<ItemTagEntry> blockType()
		{
			return this.blockType;
		}

		public T blockType(ItemTagEntry blockType)
		{
			this.blockType.add(blockType);
			return (T) this;
		}

		public List<HeatStage> stage()
		{
			return this.stage;
		}

		public T stage(HeatStage stage)
		{
			this.stage.add(stage);
			return (T) this;
		}

	}

}
