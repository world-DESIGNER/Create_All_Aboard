package steve_gall.create_trainwrecked.client.init;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry.PonderStoryBoard;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.ModBlocks;

public class ModPonders
{
	public static final PonderTag ASSEMBLING_TRAIN_TAG = new PonderTag(new ResourceLocation(CreateTrainwrecked.MOD_ID, "assembling_train"))//
			.item(AllBlocks.TRACK_STATION.get(), true, false)//
			.defaultLang("Assembling Train - New!", "Didn't you find the original train assembly conditions boring? Here's a new challenge for all of you!")//
			.addToIndex();

	public static final PonderRegistrationHelper PONDERS = new PonderRegistrationHelper(CreateTrainwrecked.MOD_ID);
	public static final PonderStoryBoardEntry ASSEMBLING_TRAIN_STORY = createAssemblingTrainStory(AllBlocks.TRACK_STATION.get(), "steam_train", ModPonders::assemblingTrain);
	public static final PonderStoryBoardEntry STEAM_ENGINE_STORY = createAssemblingTrainStory(AllBlocks.STEAM_ENGINE.get(), "steam_train", ModPonders::steamEngine);
	public static final PonderStoryBoardEntry TRAIN_STEAM_ENGINE_STORY = createAssemblingTrainStory(ModBlocks.TRAIN_STEAM_ENGINE.get(), "steam_train", ModPonders::steamEngine);

	public static final int TEXT_SHOW_TIME = 20;
	public static final int TEXT_FADING_TIME = 15;
	public static final int TEXT_IDLE_TIME = TEXT_SHOW_TIME + TEXT_FADING_TIME;
	public static final int OUT_LINE_FADING_TIME = 10;
	public static final int OUT_LINE_SHOW_TIME = TEXT_IDLE_TIME - OUT_LINE_FADING_TIME;
	public static final int SECTION_FADING_TIME = 15;

	public static PonderStoryBoardEntry createAssemblingTrainStory(ItemLike item, String schematicPath, PonderStoryBoard storyBoard)
	{
		PonderRegistry.TAGS.forTag(ASSEMBLING_TRAIN_TAG).add(item);
		return PONDERS.addStoryBoard(ForgeRegistries.ITEMS.getKey(item.asItem()), schematicPath, storyBoard, ASSEMBLING_TRAIN_TAG);
	}

	public static void init()
	{

	}

	public static void assemblingTrain(SceneBuilder scene, SceneBuildingUtil util)
	{
		scene.title("assembly_train", "Assembling Train - New!");
		scene.configureBasePlate(0, -6, 20);
		scene.scaleSceneView(0.80F);
		scene.setSceneOffsetY(-2.0F);
		scene.showBasePlate();

		for (int i = 17; i >= 0; i--)
		{
			scene.world.showSection(util.select.position(0 + i, 1, 4), Direction.DOWN);
			scene.idle(1);
		}
		scene.idle(SECTION_FADING_TIME - 1);

		scene.world.showSection(util.select.layers(2, 2), Direction.DOWN);
		scene.idle(SECTION_FADING_TIME);
		scene.idle(20);

		BlockPos controller = util.grid.at(8, 3, 4);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Until now, only a train control was needed to assemble a train.").placeNearTarget().pointAt(util.vector.blockSurface(controller, Direction.UP)).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(controller), OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		scene.world.showSection(util.select.layersFrom(4), Direction.DOWN);
		scene.idle(SECTION_FADING_TIME + 5);

		BlockPos engine = util.grid.at(13, 2, 3);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Now, an engine is required for assembly.").placeNearTarget().pointAt(util.vector.blockSurface(engine, Direction.UP)).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(engine), OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		BlockPos fluidInterface = util.grid.at(11, 4, 4);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Not only the engine, but also a portable fluid interface for transferring fuel is required.").placeNearTarget().pointAt(util.vector.blockSurface(fluidInterface, Direction.UP)).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(fluidInterface), OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("Among the carriages with tanks, at least one carriage requires a portable fluid interface.").placeNearTarget();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.fromTo(8, 2, 2, 14, 5, 6), OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		Selection fuelTanks = util.select.fromTo(10, 3, 3, 12, 3, 5);
		Vec3 fuelTankTarget = util.vector.blockSurface(util.grid.at(10, 3, 3), Direction.NORTH);

		scene.overlay.showText(TEXT_SHOW_TIME).text("If the appropriate fuel for the engine is stored in the train's fluid tank...").placeNearTarget().pointAt(fuelTankTarget).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, fuelTanks, OUT_LINE_SHOW_TIME + TEXT_IDLE_TIME);
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("The train automatically consumes the fuel from the tank while operating.").placeNearTarget().pointAt(fuelTankTarget);
		scene.idle(TEXT_IDLE_TIME);

		scene.addKeyframe();

		int anvils = 7;
		BlockPos anvilFrom = util.grid.at(5, 3, 4);
		BlockPos anvilTo = anvilFrom.above(anvils - 1);
		Selection anvilArea = util.select.fromTo(anvilFrom, anvilTo);
		scene.world.hideSection(anvilArea, Direction.DOWN);

		for (int i = 0; i < anvils; i++)
		{
			BlockPos at = anvilFrom.above(i);
			setBlockAndShow(scene, util, at, Blocks.ANVIL.defaultBlockState(), Direction.DOWN);
			scene.idle(2);
		}
		scene.idle(SECTION_FADING_TIME - 2 + 5);

		scene.overlay.showText(TEXT_SHOW_TIME).text("The number of blocks that can be glued to the train is limited by the engine's specifications and quantity.").placeNearTarget().pointAt(anvilArea.getCenter());
		scene.overlay.showOutline(PonderPalette.GREEN, null, anvilArea, OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		scene.world.hideSection(anvilArea, Direction.UP);
		scene.idle(SECTION_FADING_TIME + 5);

		scene.overlay.showText(TEXT_SHOW_TIME).text("As the number of blocks loaded on the train increases, the train's acceleration and turning top speed decrease.").placeNearTarget().attachKeyFrame();
		scene.idle(TEXT_IDLE_TIME);

		scene.markAsFinished();
	}

	public static void steamEngine(SceneBuilder scene, SceneBuildingUtil util)
	{
		scene.title("steam_engine", "Steam Engine");
		scene.configureBasePlate(0, -6, 20);
		scene.scaleSceneView(0.80F);
		scene.setSceneOffsetY(-2.0F);
		scene.showBasePlate();

		for (int i = 17; i >= 0; i--)
		{
			scene.world.showSection(util.select.position(0 + i, 1, 4), Direction.DOWN);
			scene.idle(1);
		}
		scene.idle(SECTION_FADING_TIME - 1);

		scene.world.showSection(util.select.layers(2, 4), Direction.DOWN);
		scene.idle(SECTION_FADING_TIME);
		scene.idle(10);

		BlockPos engine = util.grid.at(13, 2, 3);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Steam engine is the most basic form of engine.").placeNearTarget().pointAt(util.vector.blockSurface(engine, Direction.UP)).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(engine), OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("When installed on a train with a water-filled tank...").placeNearTarget();
		scene.idle(TEXT_IDLE_TIME);

		Selection fuelTanks = util.select.fromTo(10, 3, 3, 12, 3, 5);
		Vec3 fuelTankTarget = util.vector.blockSurface(util.grid.at(10, 3, 3), Direction.NORTH);
		scene.overlay.showText(TEXT_SHOW_TIME).text("The train automatically consumes the water in the tank.").placeNearTarget().pointAt(fuelTankTarget).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, fuelTanks, OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		BlockPos burner = util.grid.at(10, 2, 3);
		scene.overlay.showText(TEXT_SHOW_TIME).text("When assembling the train, a heat source is required to heat the water in the fluid tank.").placeNearTarget().pointAt(util.vector.blockSurface(burner, Direction.DOWN)).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(burner), OUT_LINE_SHOW_TIME + TEXT_IDLE_TIME);
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("The heat source must be placed directly below the fluid tank.").placeNearTarget().pointAt(util.vector.blockSurface(burner, Direction.DOWN));
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("Depending on the number and heat level of the heat sources, water consumption, maximum speed limit, and acceleration vary.").placeNearTarget();
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME + 15).text("The heat level of a heat source varies depending on its type.").placeNearTarget().pointAt(util.vector.blockSurface(burner, Direction.DOWN)).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(burner), (TEXT_IDLE_TIME + 15) * 2 - OUT_LINE_FADING_TIME);
		scene.idle(10);
		scene.world.destroyBlock(burner);
		scene.idle(10);
		setBlockAndShow(scene, util, burner, Blocks.MAGMA_BLOCK.defaultBlockState(), Direction.SOUTH);
		scene.idle(SECTION_FADING_TIME + 15);

		scene.overlay.showText(TEXT_SHOW_TIME + 15).text("Some heat sources consume fuel in the form of items.").placeNearTarget().pointAt(util.vector.blockSurface(burner, Direction.DOWN));
		scene.idle(10);
		scene.world.destroyBlock(burner);
		scene.idle(10);
		setBlockAndShow(scene, util, burner, AllBlocks.BLAZE_BURNER.getDefaultState().setValue(BlazeBurnerBlock.HEAT_LEVEL, HeatLevel.SMOULDERING), Direction.SOUTH);
		scene.idle(SECTION_FADING_TIME + 15);

		BlockPos barrel = util.grid.at(5, 3, 3);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Fuel inside the train's boxes or containers is automatically consumed by the heat source.").placeNearTarget().pointAt(util.vector.blockSurface(barrel, Direction.UP)).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(barrel), OUT_LINE_SHOW_TIME + TEXT_IDLE_TIME);
		scene.idle(TEXT_IDLE_TIME);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Fuel in the item vault is not consumed.").placeNearTarget().pointAt(util.vector.blockSurface(barrel, Direction.UP));
		scene.idle(TEXT_IDLE_TIME);

		scene.markAsFinished();
	}

	public static void setBlockAndShow(SceneBuilder scene, SceneBuildingUtil util, BlockPos at, BlockState state, Direction direction)
	{
		scene.addInstruction(s ->
		{
			PonderWorld world = s.getWorld();
			world.setBlock(at, state, Block.UPDATE_ALL);
		});

		scene.world.showSection(util.select.position(at), direction);
	}

	private ModPonders()
	{

	}

}
