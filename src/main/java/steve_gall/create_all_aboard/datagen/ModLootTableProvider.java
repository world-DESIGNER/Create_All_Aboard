package steve_gall.create_all_aboard.datagen;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ModLootTableProvider extends LootTableProvider
{
	public ModLootTableProvider(PackOutput pOutput)
	{
		super(pOutput, Set.of(), VanillaLootTableProvider.create(pOutput).getTables());
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker)
	{

	}

	@Override
	public List<LootTableProvider.SubProviderEntry> getTables()
	{
		ImmutableList.Builder<LootTableProvider.SubProviderEntry> builder = ImmutableList.builder();
		builder.add(new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK));
		return builder.build();
	}

}
