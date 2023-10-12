package steve_gall.create_trainwrecked.common.init;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.item.TooltipHelper.Palette;

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
	public static final RegistryObject<JerrycanItem> JERRYCAN = ITEMS.register("jerrycan", () -> new JerrycanItem(new Properties().tab(AllCreativeModeTabs.BASE_CREATIVE_TAB)));

	static
	{
		TooltipModifier.REGISTRY.registerDeferred(JERRYCAN.getId(), item -> new ItemDescription.Modifier(item, Palette.STANDARD_CREATE));
	}

	private ModItems()
	{

	}

}
