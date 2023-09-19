package steve_gall.create_trainwrecked.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.ModItems;

public class ModItemModelProvider extends ItemModelProvider
{
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, CreateTrainwrecked.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		handheld(ModItems.JERRYCAN.get());
	}

	public ItemModelBuilder handheld(ItemLike item)
	{
		return this.handheld(item, itemTexture(item));
	}

	public ItemModelBuilder handheld(ItemLike item, ResourceLocation texture)
	{
		return this.withExistingParent(name(item), "item/handheld").texture("layer0", texture);
	}

	public ResourceLocation itemTexture(ItemLike item)
	{
		return this.modLoc("item/" + name(item));
	}

	public String name(ItemLike item)
	{
		return item.asItem().getRegistryName().getPath();
	}

}
