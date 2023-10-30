package steve_gall.create_all_aboard.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_all_aboard.common.CreateAllAboard;

public class ModCreativeModTabs
{
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateAllAboard.MOD_ID);
	private static final MutableComponent DISPLAY_NAME = Component.literal(CreateAllAboard.MODE_NAME);

	public static final String TAB_ID = CreateAllAboard.MOD_ID + "_tab";

	public static final RegistryObject<CreativeModeTab> TAB = TABS.register(TAB_ID, //
			() -> CreativeModeTab.builder().//
					icon(() -> new ItemStack(ModItems.TRAIN_STEAM_ENGINE.get())).//
					title(DISPLAY_NAME).//
					displayItems((CreativeModeTab.ItemDisplayParameters pParameters, CreativeModeTab.Output pOutput) ->
					{
						ModItems.ITEMS.getEntries().forEach(i -> pOutput.accept(i.get()));
					}).build());

}
