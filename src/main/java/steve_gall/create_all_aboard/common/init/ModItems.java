package steve_gall.create_all_aboard.common.init;

import java.util.function.Function;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper.Palette;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.item.JerrycanItem;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Keys.ITEMS, CreateAllAboard.MOD_ID);
	public static final Function<Item, TooltipModifier> STANDARD_TOOLTIP_MODIFIER = item -> new ItemDescription.Modifier(item, Palette.STANDARD_CREATE);
	public static final RegistryObject<BlockItem> TRAIN_STEAM_ENGINE = ITEMS.register("train_steam_engine", () -> new BlockItem(ModBlocks.TRAIN_STEAM_ENGINE.get(), defaultProperties()));
	public static final RegistryObject<JerrycanItem> JERRYCAN = ITEMS.register("jerrycan", () -> new JerrycanItem(defaultProperties()));

	public static Properties defaultProperties()
	{
		return new Properties();
	}

	static
	{
		for (RegistryObject<Item> entry : ITEMS.getEntries())
		{
			TooltipModifier.REGISTRY.registerDeferred(entry.getId(), STANDARD_TOOLTIP_MODIFIER);
		}

	}

	private ModItems()
	{

	}

}
