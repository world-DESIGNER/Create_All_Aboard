package steve_gall.create_all_aboard.common.compat;

import static steve_gall.create_all_aboard.client.init.ModPonders.OUT_LINE_SHOW_TIME;
import static steve_gall.create_all_aboard.client.init.ModPonders.SECTION_FADING_TIME;
import static steve_gall.create_all_aboard.client.init.ModPonders.TEXT_IDLE_TIME;
import static steve_gall.create_all_aboard.client.init.ModPonders.TEXT_SHOW_TIME;
import static steve_gall.create_all_aboard.client.init.ModPonders.createAssemblingTrainStory;

import com.drmangotea.createindustry.blocks.CIBlocks;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pointing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import steve_gall.create_all_aboard.client.ponder.instruction.InfinityTrackInstruction;
import steve_gall.create_all_aboard.client.ponder.instruction.PonderBogeyManagerInstruction;

public class TFMGCompatPonders
{
	public static final PonderStoryBoardEntry DIESEL_TRAINE_STORY = createAssemblingTrainStory(CIBlocks.DIESEL_ENGINE.get(), "diesel_train", TFMGCompatPonders::dieselTrain);
	public static final PonderStoryBoardEntry OVERHEAT_STORY = createAssemblingTrainStory(CIBlocks.DIESEL_ENGINE.get(), "diesel_train", TFMGCompatPonders::overheat);

	public static void init()
	{

	}

	public static void dieselTrain(SceneBuilder scene, SceneBuildingUtil util)
	{
		scene.title("diesel_engine", "Diesel Engine");
		scene.configureBasePlate(0, -3, 18);
		scene.scaleSceneView(0.80F);
		scene.setSceneOffsetY(-3.0F);
		scene.showBasePlate();

		Selection tracksWithPlatform = util.select.fromTo(0, 0, 0, 17, 1, 17);

		for (int i = 17; i >= 0; i--)
		{
			scene.world.showSection(util.select.position(i, 1, 7), Direction.DOWN);
			scene.idle(1);
		}
		scene.idle(14);

		Selection train = util.select.layers(2, 4);
		scene.world.showIndependentSection(train, Direction.DOWN);
		scene.idle(25);

		Selection engines = util.select.fromTo(8, 3, 7, 9, 3, 7);
		Selection shafts = util.select.fromTo(8, 5, 7, 9, 5, 7);
		Vec3 engineTarget = engines.getCenter().add(0.0D, 0.0D, -0.5D);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Diesel engine is an advanced form of engine.").placeNearTarget().pointAt(engineTarget).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, engines, OUT_LINE_SHOW_TIME);
		scene.idle(TEXT_IDLE_TIME);

		Selection fuelTanks = util.select.fromTo(7, 3, 7, 10, 3, 7);
		Vec3 fuelTankTarget = util.vector.blockSurface(util.grid.at(7, 3, 7), Direction.NORTH);
		scene.overlay.showText(TEXT_SHOW_TIME).text("When installed on a train with a diesel-filled tank...").placeNearTarget().pointAt(fuelTankTarget).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, fuelTanks, OUT_LINE_SHOW_TIME + TEXT_IDLE_TIME);
		scene.idle(TEXT_IDLE_TIME);
		scene.overlay.showText(TEXT_SHOW_TIME).text("The train automatically consumes the diesel in the tank.").placeNearTarget().pointAt(fuelTankTarget);
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("The train automatically consumes the diesel in the tank.").placeNearTarget().pointAt(fuelTankTarget).attachKeyFrame();
		scene.overlay.showOutline(PonderPalette.GREEN, null, fuelTanks, OUT_LINE_SHOW_TIME);
		BlockPos bogey1 = util.grid.at(5, 2, 7);
		BlockPos bogey2 = util.grid.at(12, 2, 7);
		PonderBogeyManagerInstruction bogeyManager = new PonderBogeyManagerInstruction();
		Selection bogeys = util.select.position(bogey1).add(util.select.position(bogey2));
		scene.world.setKineticSpeed(shafts, 64.0F);
		scene.addInstruction(bogeyManager);
		bogeyManager.setSpeed(scene, bogeys, -2.0F);
		scene.addInstruction(new InfinityTrackInstruction(scene, util, tracksWithPlatform));
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("Diesel engines do not require a heat source!").placeNearTarget().attachKeyFrame();
		scene.idle(TEXT_IDLE_TIME);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Diesel engines perform better than steam engines in terms of efficiency.").placeNearTarget();
		scene.idle(TEXT_IDLE_TIME);

		scene.markAsFinished();
	}

	public static void overheat(SceneBuilder scene, SceneBuildingUtil util)
	{
		scene.title("overheat", "Overheat");
		scene.configureBasePlate(0, -3, 18);
		scene.scaleSceneView(0.80F);
		scene.setSceneOffsetY(-3.0F);
		scene.showBasePlate();

		scene.world.showSection(util.select.everywhere(), null);
		scene.idle(SECTION_FADING_TIME);

		Selection tracksWithPlatform = util.select.fromTo(0, 0, 0, 17, 1, 17);
		Selection engines = util.select.fromTo(8, 3, 7, 9, 3, 7);
		Selection shafts = util.select.fromTo(8, 5, 7, 9, 5, 7);
		BlockPos bogey1 = util.grid.at(5, 2, 7);
		BlockPos bogey2 = util.grid.at(12, 2, 7);
		Selection bogeys = util.select.position(bogey1).add(util.select.position(bogey2));
		PonderBogeyManagerInstruction bogeyManager = new PonderBogeyManagerInstruction();
		InfinityTrackInstruction infinityTrack = new InfinityTrackInstruction(scene, util, tracksWithPlatform);
		scene.addInstruction(bogeyManager);
		scene.addInstruction(infinityTrack);
		infinityTrack.setSuspened(scene, false);
		scene.world.setKineticSpeed(shafts, 64.0F);
		bogeyManager.setSpeed(scene, bogeys, -2.0F);
		scene.idle(40);

		scene.overlay.showText(TEXT_SHOW_TIME).text("If the engine is left running for a long time...").placeNearTarget().attachKeyFrame();
		scene.idle(TEXT_IDLE_TIME + 20);
		infinityTrack.setSuspened(scene, true);
		scene.world.setKineticSpeed(shafts, 0.0F);
		bogeyManager.clear(scene);

		addTrainStopParticle(scene, util, bogey1, 1.0F, 10);
		addTrainStopParticle(scene, util, bogey2, 1.0F, 10);
		scene.idle(25);
		scene.overlay.showText(TEXT_SHOW_TIME).text("The engine overheats and stops working.").placeNearTarget();
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("To prevent this, ice can be used.").placeNearTarget().attachKeyFrame();
		scene.idle(TEXT_IDLE_TIME);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Not only ice, but all ice-related items can be used.").placeNearTarget();
		scene.idle(TEXT_IDLE_TIME);
		scene.overlay.showText(TEXT_SHOW_TIME).text("The degree to which the engine cools varies depending on the coldness of the item.").placeNearTarget();
		scene.idle(TEXT_IDLE_TIME);

		BlockPos barrel = util.grid.at(11, 3, 7);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Ice inside the train's chest or barrel is automatically consumed by the train.").placeNearTarget().pointAt(util.vector.blockSurface(barrel, Direction.NORTH)).attachKeyFrame();
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(barrel, Direction.NORTH), Pointing.RIGHT).withItem(new ItemStack(Items.ICE)), TEXT_SHOW_TIME);
		scene.overlay.showOutline(PonderPalette.GREEN, null, util.select.position(barrel), OUT_LINE_SHOW_TIME + TEXT_IDLE_TIME);
		scene.idle(TEXT_IDLE_TIME);
		scene.overlay.showText(TEXT_SHOW_TIME).text("Ice in the item vault is not consumed.").placeNearTarget().pointAt(util.vector.blockSurface(barrel, Direction.NORTH));
		scene.idle(TEXT_IDLE_TIME);

		scene.overlay.showText(TEXT_SHOW_TIME).text("If the engine is left alone for a sufficient amount of time while overheated...").placeNearTarget().attachKeyFrame();
		scene.idle(TEXT_IDLE_TIME);

		infinityTrack.setSuspened(scene, false);
		scene.world.setKineticSpeed(shafts, 64.0F);
		bogeyManager.setSpeed(scene, bogeys, -2.0F);

		scene.overlay.showText(TEXT_SHOW_TIME).text("The engine cools down, allowing it to operate again.").placeNearTarget();
		scene.idle(TEXT_IDLE_TIME);

		scene.markAsFinished();
	}

	private static void addTrainStopParticle(SceneBuilder scene, SceneBuildingUtil util, BlockPos pos, float amountPerCycle, int cycles)
	{
		scene.effects.emitParticles(util.vector.centerOf(pos), (PonderWorld world, double x, double y, double z) ->
		{
			for (int j : Iterate.positiveAndNegative)
			{
				for (int i : Iterate.positiveAndNegative)
				{
					Vec3 v = Vec3.ZERO.add(j * 1.15D, +0.32D, i);
					Vec3 m = Vec3.ZERO.add(j * 0.25D, -0.29D, 0);
					world.addParticle(ParticleTypes.POOF, x + v.x, y + v.y, z + v.z, m.x, m.y, m.z);
				}
			}
		}, amountPerCycle, cycles);
	}

	private TFMGCompatPonders()
	{

	}

}
