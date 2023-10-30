package steve_gall.create_all_aboard.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.create_all_aboard.client.CreateTrainwreckedClient;
import steve_gall.create_all_aboard.common.compat.CompatibilityManager;
import steve_gall.create_all_aboard.common.compat.CompatibilityMod;
import steve_gall.create_all_aboard.common.config.CreateTrainwreckedConfig;
import steve_gall.create_all_aboard.common.init.ModBlockEntityTypes;
import steve_gall.create_all_aboard.common.init.ModBlocks;
import steve_gall.create_all_aboard.common.init.ModCreativeModTabs;
import steve_gall.create_all_aboard.common.init.ModItems;
import steve_gall.create_all_aboard.common.init.ModRecipeSerializers;
import steve_gall.create_all_aboard.common.init.ModRecipeTypes;

@Mod(CreateAllAboard.MOD_ID)
public class CreateAllAboard
{
	public static final String MOD_ID = "create_all_aboard";
	public static final String MODE_NAME = ModList.get().getModContainerById(CreateAllAboard.MOD_ID).orElse(null).getModInfo().getDisplayName();
	public static final Logger LOGGER = LogUtils.getLogger();

	public CreateAllAboard()
	{
		CreateTrainwreckedConfig.registerConfigs(ModLoadingContext.get());
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CreateTrainwreckedClient::init);

		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.BLOCKS.register(fml_bus);
		ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(fml_bus);
		ModItems.ITEMS.register(fml_bus);
		ModRecipeTypes.RECIPE_TYPES.register(fml_bus);
		ModRecipeSerializers.RECIPE_SERIALIZERS.register(fml_bus);
		ModCreativeModTabs.TABS.register(fml_bus);

		IEventBus forge_bus = MinecraftForge.EVENT_BUS;

		CompatibilityManager.MODS.forEach(CompatibilityMod::load);
	}

	public static String translationKey(CharSequence path)
	{
		return translationKey("", MOD_ID, path);
	}

	public static String translationKey(ResourceLocation id)
	{
		return translationKey("", id.getNamespace(), id.getPath());
	}

	public static String translationKey(CharSequence category, CharSequence path)
	{
		return translationKey(category, MOD_ID, path);
	}

	public static String translationKey(CharSequence category, ResourceLocation id)
	{
		return translationKey(category, id.getNamespace(), id.getPath());
	}

	public static String translationKey(CharSequence category, CharSequence namespace, CharSequence path)
	{
		StringBuilder builder = new StringBuilder();

		if (!StringUtils.isEmpty(category))
		{
			builder.append(category).append(".");
		}

		return builder.append(namespace).append(".").append(path).toString();
	}

	public static ResourceLocation asResource(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

}
