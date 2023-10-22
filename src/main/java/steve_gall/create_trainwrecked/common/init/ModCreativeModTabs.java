package steve_gall.create_trainwrecked.common.init;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;

public class ModCreativeModTabs
{
	private static final MutableComponent DISPLAY_NAME = Component.literal(CreateTrainwrecked.MODE_NAME);

	public static final String TAB_ID = CreateTrainwrecked.MOD_ID + "_tab";

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
