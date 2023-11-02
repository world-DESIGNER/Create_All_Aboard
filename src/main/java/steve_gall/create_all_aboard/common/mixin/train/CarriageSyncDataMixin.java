package steve_gall.create_all_aboard.common.mixin.train;

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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.capability.IFluidHandler;
import steve_gall.create_all_aboard.common.content.contraption.MountedStorageManagerExtension;
import steve_gall.create_all_aboard.common.content.train.CarriageExtension;
import steve_gall.create_all_aboard.common.content.train.CarriageSyncDataExtension;
import steve_gall.create_all_aboard.common.content.train.Engine;
import steve_gall.create_all_aboard.common.content.train.HeatSource;
import steve_gall.create_all_aboard.common.content.train.TrainPartsSyncData;
import steve_gall.create_all_aboard.common.fluid.FluidTankData;

@Mixin(value = CarriageSyncData.class, remap = false)
public abstract class CarriageSyncDataMixin implements CarriageSyncDataExtension
{
	@Unique
	private List<FluidTankData> syncedFluids = new ArrayList<>();
	@Unique
	private TrainPartsSyncData<Engine> engineData = new TrainPartsSyncData<>();
	@Unique
	private TrainPartsSyncData<HeatSource> heatSourceData = new TrainPartsSyncData<>();
	@Unique
	private double trainSpeed = 0.0F;

	@Inject(method = "copy", at = @At(value = "TAIL"), cancellable = true)
	private void copy(CallbackInfoReturnable<CarriageSyncData> cir)
	{
		CarriageSyncDataMixin copy = (CarriageSyncDataMixin) (Object) cir.getReturnValue();
		copy.syncedFluids.addAll(this.syncedFluids.stream().map(FluidTankData::copy).toList());
		copy.engineData.copy(this.engineData);
		copy.heatSourceData.copy(this.heatSourceData);
		copy.trainSpeed = this.trainSpeed;
	}

	@Inject(method = "write", at = @At(value = "TAIL"), cancellable = true)
	private void write(FriendlyByteBuf buffer, CallbackInfo ci)
	{
		buffer.writeCollection(this.syncedFluids, FluidTankData::toNetwork);
		this.engineData.write(buffer);
		this.heatSourceData.write(buffer);
		buffer.writeDouble(this.trainSpeed);
	}

	@Inject(method = "read", at = @At(value = "TAIL"), cancellable = true)
	private void read(FriendlyByteBuf buffer, CallbackInfo ci)
	{
		this.syncedFluids = buffer.readList(FluidTankData::fromNetwork);
		this.engineData.read(buffer);
		this.heatSourceData.read(buffer);
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
		this.engineData.update(extension.getEngines());
		this.heatSourceData.update(extension.getHeatSources());

		this.trainSpeed = carriage.train.speed;
	}

	@Inject(method = "apply", at = @At(value = "HEAD"), cancellable = true)
	private void apply(CarriageContraptionEntity entity, Carriage carriage, CallbackInfo ci)
	{
		((MountedStorageManagerExtension) carriage.storage).setSyncedFluids(this.syncedFluids);

		CarriageExtension extension = (CarriageExtension) carriage;
		this.engineData.apply(extension.getEngines());
		this.heatSourceData.apply(extension.getHeatSources());
	}

	@Override
	@Unique
	public List<FluidTankData> getSyncedFluids()
	{
		return this.syncedFluids;
	}

	@Override
	@Unique
	public TrainPartsSyncData<Engine> getEngineData()
	{
		return this.engineData;
	}

	@Override
	public TrainPartsSyncData<HeatSource> getHeatSourceData()
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
