package steve_gall.create_trainwrecked.common.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity.CreativeSmartFluidTank;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.config.CreateTrainwreckedConfig;
import steve_gall.create_trainwrecked.common.fluid.FluidHelper;
import steve_gall.create_trainwrecked.common.mixin.contraption.PortableStorageInterfaceBlockEntityAccessor;
import steve_gall.create_trainwrecked.common.util.NumberHelper;

public class JerrycanItem extends Item
{
	public static String TOOLTIP_FILLED = CreateTrainwrecked.translationKey("tooltip", "filled");
	public static String TOOLTIP_DRAINED = CreateTrainwrecked.translationKey("tooltip", "drained");
	public static String TOOLTIP_ITEM_EMPTY = CreateTrainwrecked.translationKey("tooltip", "item_empty");
	public static String TOOLTIP_ITEM_FULL = CreateTrainwrecked.translationKey("tooltip", "item_full");
	public static String TOOLTIP_STORAGE_EMPTY = CreateTrainwrecked.translationKey("tooltip", "storage_empty");
	public static String TOOLTIP_STORAGE_FULL = CreateTrainwrecked.translationKey("tooltip", "storage_full");

	public JerrycanItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pIsAdvanced)
	{
		super.appendHoverText(pStack, pLevel, pTooltip, pIsAdvanced);

		IFluidHandlerItem jerrycan = pStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseGet(null);

		if (jerrycan != null)
		{
			pTooltip.add(Component.empty());

			for (int i = 0; i < jerrycan.getTanks(); i++)
			{
				pTooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip.capacity", NumberHelper.format(jerrycan.getTankCapacity(i)) + " mB"));

				FluidStack fluid = jerrycan.getFluidInTank(i);

				if (fluid.isEmpty())
				{
					pTooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip.empty"));
				}
				else
				{
					pTooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip.amount", fluid.getDisplayName(), NumberHelper.format(fluid.getAmount()) + " mB"));
				}

			}

		}

	}

	@Override
	public boolean isBarVisible(ItemStack pStack)
	{
		return true;
	}

	@Override
	public int getBarColor(ItemStack pStack)
	{
		IFluidHandlerItem jerrycan = pStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseGet(null);

		if (jerrycan != null)
		{
			for (int i = 0; i < jerrycan.getTanks(); i++)
			{
				FluidStack fluid = jerrycan.getFluidInTank(i);

				if (!fluid.isEmpty())
				{
					if (fluid.getFluid() == Fluids.LAVA)
					{
						return 0xFFDB6B19;
					}
					else if (FMLEnvironment.dist == Dist.CLIENT)
					{
						return IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
					}

				}

			}

		}

		return 0xFF000000;
	}

	@Override
	public int getBarWidth(ItemStack pStack)
	{
		double ratio = 0.0D;
		IFluidHandlerItem jerrycan = pStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseGet(null);

		if (jerrycan != null)
		{
			for (int i = 0; i < jerrycan.getTanks(); i++)
			{
				FluidStack fluid = jerrycan.getFluidInTank(i);

				if (!fluid.isEmpty())
				{
					ratio = fluid.getAmount() / (double) jerrycan.getTankCapacity(i);
				}

			}

		}

		return Mth.ceil(13.0F * ratio);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt)
	{
		return new FluidHandlerItemStack(stack, CreateTrainwreckedConfig.COMMON.oilGunCapacity.get());
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext)
	{
		Level level = pContext.getLevel();

		if (!level.isClientSide() && level.getBlockEntity(pContext.getClickedPos()) instanceof PortableFluidInterfaceBlockEntity pfi)
		{
			if (((PortableStorageInterfaceBlockEntityAccessor) pfi).getConnectedEntity() instanceof CarriageContraptionEntity)
			{
				IFluidHandlerItem jerrycan = pContext.getItemInHand().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
				IFluidHandler to = pfi.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);

				if (to instanceof CreativeSmartFluidTank inventory)
				{
					FluidStack drain = jerrycan.drain(1, FluidAction.SIMULATE);
					inventory.setContainedFluid(drain);
				}
				else if (jerrycan != null && to != null)
				{
					this.move(pContext.getPlayer(), jerrycan, to);
				}

			}

		}

		return InteractionResult.SUCCESS;
	}

	public boolean onUse(Player pPlayer, InteractionHand pHand, Contraption contraption, BlockPos localPos)
	{
		StructureBlockInfo sbi = contraption.getBlocks().get(localPos);

		if (contraption.entity instanceof CarriageContraptionEntity cce && sbi != null && sbi.state.getBlock() == AllBlocks.PORTABLE_FLUID_INTERFACE.get())
		{
			if (!pPlayer.getLevel().isClientSide())
			{
				ItemStack item = pPlayer.getItemInHand(pHand);
				IFluidHandlerItem jerrycan = item.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseGet(null);
				this.move(pPlayer, jerrycan, cce.getCarriage().storage.getFluids());
				return true;
			}

			return true;
		}

		return false;
	}

	private FluidStack move(Player pPlayer, IFluidHandlerItem jerrycan, IFluidHandler fluidStorage)
	{
		int transfer = CreateTrainwreckedConfig.COMMON.oilGunTransfer.get();
		boolean shiftKeyDown = pPlayer.isShiftKeyDown();
		IFluidHandler from = shiftKeyDown ? jerrycan : fluidStorage;
		IFluidHandler to = shiftKeyDown ? fluidStorage : jerrycan;

		FluidStack toDraining = this.drainExistingTest(transfer, from, to);
		FluidStack draining = toDraining != null ? toDraining : from.drain(transfer, FluidAction.SIMULATE);

		if (draining.isEmpty())
		{
			pPlayer.displayClientMessage(Component.translatable(shiftKeyDown ? TOOLTIP_ITEM_EMPTY : TOOLTIP_STORAGE_EMPTY), true);
			return FluidStack.EMPTY;
		}

		int filling = to.fill(draining, FluidAction.SIMULATE);

		if (filling <= 0)
		{
			pPlayer.displayClientMessage(Component.translatable(shiftKeyDown ? TOOLTIP_STORAGE_FULL : TOOLTIP_ITEM_FULL), true);
			return FluidStack.EMPTY;
		}

		FluidStack drained = from.drain(FluidHelper.deriveAmount(draining, filling), FluidAction.EXECUTE);
		int filled = to.fill(drained, FluidAction.EXECUTE);
		FluidStack moved = FluidHelper.deriveAmount(draining, filled);

		pPlayer.displayClientMessage(Component.translatable(shiftKeyDown ? TOOLTIP_FILLED : TOOLTIP_DRAINED, moved.getDisplayName(), NumberHelper.format(moved.getAmount()) + " mB"), true);
		return moved;
	}

	@Nullable
	public FluidStack drainExistingTest(int transfer, IFluidHandler from, IFluidHandler to)
	{
		int toTanks = to.getTanks();
		FluidStack toDraining = null;

		for (int toTank = 0; toTank < toTanks; toTank++)
		{
			FluidStack testDraining = FluidHelper.deriveAmount(to.getFluidInTank(toTank), transfer);
			FluidStack testDrained = from.drain(testDraining, FluidAction.SIMULATE);

			if (!testDrained.isEmpty())
			{
				toDraining = testDrained;
			}

		}

		return toDraining;
	}

}
