package steve_gall.create_all_aboard.common.init;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import steve_gall.create_all_aboard.common.CreateAllAboard;

public class ModCreativeModTabs
{
	private static final MutableComponent DISPLAY_NAME = new TextComponent(CreateAllAboard.MODE_NAME);

	public static final String TAB_ID = CreateAllAboard.MOD_ID + "_tab";

	public static final CreativeModeTab TAB = new CreativeModeTab(TAB_ID)
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ModItems.TRAIN_STEAM_ENGINE.get());
		}

		@Override
		public Component getDisplayName()
		{
			return DISPLAY_NAME;
		}

	};

}
