package steve_gall.create_all_aboard.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModItems;

public class ModItemModelProvider extends ItemModelProvider
{
	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, CreateAllAboard.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		this.getBuilder(name(ModItems.TRAIN_STEAM_ENGINE.get())).parent(getExistingFile(CreateAllAboard.asResource("block/train_steam_engine/item")));
	}

	public ItemModelBuilder handheld(ItemLike item)
	{
		return this.handheld(item, itemTexture(item));
	}

	public ItemModelBuilder handheld(ItemLike item, ResourceLocation texture)
	{
		return this.withExistingParent(name(item), "item/generated").texture("layer0", texture);
	}

	public ResourceLocation itemTexture(ItemLike item)
	{
		return this.modLoc("item/" + name(item));
	}

	public String name(ItemLike item)
	{
		return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
	}

}
