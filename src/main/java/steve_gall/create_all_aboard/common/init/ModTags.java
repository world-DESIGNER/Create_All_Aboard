package steve_gall.create_all_aboard.common.init;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import steve_gall.create_all_aboard.common.CreateAllAboard;

public class ModTags
{
	public static class TagCreator<T>
	{
		private final Function<ResourceLocation, TagKey<T>> creator;

		public TagCreator(Function<ResourceLocation, TagKey<T>> creator)
		{
			this.creator = creator;
		}

		public TagKey<T> own(String path)
		{
			return this.create(CreateAllAboard.asResource(path));
		}

		public TagKey<T> forge(String path)
		{
			return this.create(new ResourceLocation("forge", path));
		}

		public TagKey<T> copy(TagKey<?> tag)
		{
			return this.create(tag.location());
		}

		public TagKey<T> create(ResourceLocation name)
		{
			return this.creator.apply(name);
		}

	}

	public static class Blocks
	{
		public static final TagCreator<Block> TAGS = new TagCreator<>(BlockTags::create);
	}

	public static class Items
	{
		public static final TagCreator<Item> TAGS = new TagCreator<>(ItemTags::create);
		public static final TagKey<Item> ENGINES = TAGS.own("engines");
		public static final TagKey<Item> ENGINES_STEAM = TAGS.own("engines/steam");
		public static final TagKey<Item> ENGINES_DIESEL = TAGS.own("engines/diesel");
	}

	public static class Fluids
	{
		public static final TagCreator<Fluid> TAGS = new TagCreator<>(FluidTags::create);
		public static final TagKey<Fluid> FUELS = TAGS.own("fuels");
		public static final TagKey<Fluid> FUELS_STEAM = TAGS.own("fuels/steam");
		public static final TagKey<Fluid> FUELS_DIESEL = TAGS.own("fuels/diesel");

		public static final TagKey<Fluid> DIESEL = TAGS.forge("diesel");
		public static final TagKey<Fluid> BIODIESEL = TAGS.forge("biodiesel");
	}

	private ModTags()
	{

	}

}
