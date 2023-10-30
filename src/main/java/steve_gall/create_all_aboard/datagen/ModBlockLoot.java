package steve_gall.create_all_aboard.datagen;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import steve_gall.create_all_aboard.common.init.ModBlocks;

public class ModBlockLoot extends VanillaBlockLoot
{
	@Override
	protected void generate()
	{
		this.dropSelf(ModBlocks.TRAIN_STEAM_ENGINE.get());
	}

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return ModBlocks.BLOCKS.getEntries().stream().map(b -> b.get()).toList();
	}

}
