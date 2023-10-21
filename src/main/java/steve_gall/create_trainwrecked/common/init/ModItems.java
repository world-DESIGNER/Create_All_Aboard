package steve_gall.create_trainwrecked.common.init;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper.Palette;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.item.JerrycanItem;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Keys.ITEMS, CreateTrainwrecked.MOD_ID);
	public static final RegistryObject<BlockItem> TRAIN_STEAM_ENGINE = ITEMS.register("train_steam_engine", () -> new BlockItem(ModBlocks.TRAIN_STEAM_ENGINE.get(), defaultProperties()));
	public static final RegistryObject<JerrycanItem> JERRYCAN = ITEMS.register("jerrycan", () -> new JerrycanItem(defaultProperties()));

	public static Properties defaultProperties()
	{
		return new Properties().tab(ModCreativeModTabs.TAB);
	}

	static
	{
		TooltipModifier.REGISTRY.registerDeferred(JERRYCAN.getId(), item -> new ItemDescription.Modifier(item, Palette.STANDARD_CREATE));
	}

	private ModItems()
	{

	}

}
