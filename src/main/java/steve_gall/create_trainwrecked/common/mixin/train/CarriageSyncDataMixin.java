package steve_gall.create_trainwrecked.common.mixin.train;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageSyncData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.capability.IFluidHandler;
import steve_gall.create_trainwrecked.common.content.contraption.MountedStorageManagerExtension;
import steve_gall.create_trainwrecked.common.content.train.CarriageContraptionHelper;
import steve_gall.create_trainwrecked.common.content.train.CarriageExtension;
import steve_gall.create_trainwrecked.common.content.train.CarriageSyncDataExtension;
import steve_gall.create_trainwrecked.common.content.train.TrainPart;
import steve_gall.create_trainwrecked.common.fluid.FluidTankData;

@Mixin(value = CarriageSyncData.class, remap = false)
public abstract class CarriageSyncDataMixin implements CarriageSyncDataExtension
{
	@Unique
	private List<FluidTankData> syncedFluids = new ArrayList<>();
	@Unique
	private List<CompoundTag> engineData = new ArrayList<>();
	@Unique
	private List<CompoundTag> heatSourceData = new ArrayList<>();
	@Unique
	private double trainSpeed = 0.0F;

	@Inject(method = "copy", at = @At(value = "TAIL"), cancellable = true)
	private void copy(CallbackInfoReturnable<CarriageSyncData> cir)
	{
		CarriageSyncDataMixin copy = (CarriageSyncDataMixin) (Object) cir.getReturnValue();
		copy.syncedFluids.addAll(this.syncedFluids.stream().map(FluidTankData::copy).toList());
		copy.engineData.addAll(this.engineData.stream().map(CompoundTag::copy).toList());
		copy.heatSourceData.addAll(this.heatSourceData.stream().map(CompoundTag::copy).toList());
		copy.trainSpeed = this.trainSpeed;
	}

	@Inject(method = "write", at = @At(value = "TAIL"), cancellable = true)
	private void write(FriendlyByteBuf buffer, CallbackInfo ci)
	{
		buffer.writeCollection(this.syncedFluids, FluidTankData::toNetwork);
		buffer.writeCollection(this.engineData, FriendlyByteBuf::writeNbt);
		buffer.writeCollection(this.heatSourceData, FriendlyByteBuf::writeNbt);
		buffer.writeDouble(this.trainSpeed);
	}

	@Inject(method = "read", at = @At(value = "TAIL"), cancellable = true)
	private void read(FriendlyByteBuf buffer, CallbackInfo ci)
	{
		this.syncedFluids = buffer.readList(FluidTankData::fromNetwork);
		this.engineData = buffer.readList(FriendlyByteBuf::readNbt);
		this.heatSourceData = buffer.readList(FriendlyByteBuf::readNbt);
		this.trainSpeed = buffer.readDouble();
	}

	@Inject(method = "update", at = @At(value = "TAIL"), cancellable = true)
	private void update(CarriageContraptionEntity entity, Carriage carriage, CallbackInfo ci)
	{
		IFluidHandler fluids = carriage.storage.getFluids();
		int tanks = fluids.getTanks();
		this.syncedFluids.clear();

		for (int tank = 0; tank < tanks; tank++)
		{
			this.syncedFluids.add(new FluidTankData(fluids.getFluidInTank(tank), fluids.getTankCapacity(tank)));
		}

		CarriageExtension extension = (CarriageExtension) carriage;
		this.engineData.clear();
		this.engineData.addAll(extension.getEngines().stream().map(TrainPart::toNbt).toList());
		this.heatSourceData.clear();
		this.heatSourceData.addAll(extension.getHeatSources().stream().map(TrainPart::toNbt).toList());

		this.trainSpeed = carriage.train.speed;
	}

	@Inject(method = "apply", at = @At(value = "HEAD"), cancellable = true)
	private void apply(CarriageContraptionEntity entity, Carriage carriage, CallbackInfo ci)
	{
		((MountedStorageManagerExtension) carriage.storage).setSyncedFluids(this.syncedFluids);

		CarriageExtension extension = (CarriageExtension) carriage;
		CarriageContraptionHelper.copyData(extension.getEngines(), this.engineData);
		CarriageContraptionHelper.copyData(extension.getHeatSources(), this.heatSourceData);
	}

	@Override
	@Unique
	public List<FluidTankData> getSyncedFluids()
	{
		return this.syncedFluids;
	}

	@Override
	@Unique
	public List<CompoundTag> getEngineCaches()
	{
		return this.engineData;
	}

	@Override
	public List<CompoundTag> getHeatSourceCaches()
	{
		return this.heatSourceData;
	}

	@Override
	@Unique
	public double getTrainSpeed()
	{
		return this.trainSpeed;
	}

}
