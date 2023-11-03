package steve_gall.create_all_aboard.common.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class RecipeTypeDeferredRegister
{
	private final String modId;
	private final List<RecipeRegistryObject<RecipeType<?>>> objects;

	public RecipeTypeDeferredRegister(String modId)
	{
		this.modId = modId;
		this.objects = new ArrayList<>();
	}

	public void register(IEventBus eventBus)
	{
		eventBus.addListener(this::onFMLCommonSetup);
	}

	private void onFMLCommonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() ->
		{
			for (RecipeRegistryObject<RecipeType<?>> object : this.objects)
			{
				object.register();
			}
		});

	}

	@SuppressWarnings("unchecked")
	public <TYPE extends RecipeType<?>> RecipeRegistryObject<TYPE> register(String name, Supplier<TYPE> supplier)
	{
		RecipeRegistryObject<TYPE> object = new RecipeRegistryObject<>(new ResourceLocation(this.getModId(), name), supplier);
		this.objects.add((RecipeRegistryObject<RecipeType<?>>) object);
		return object;
	}

	public String getModId()
	{
		return this.modId;
	}

	public class RecipeRegistryObject<TYPE extends RecipeType<?>>
	{
		private final ResourceLocation id;
		private final Supplier<TYPE> supplier;

		private TYPE value;

		public RecipeRegistryObject(ResourceLocation id, Supplier<TYPE> supplier)
		{
			this.id = id;
			this.supplier = supplier;
		}

		private void register()
		{
			if (this.value == null)
			{
				TYPE value = this.supplier.get();
				this.value = value;

				Registry.register(Registry.RECIPE_TYPE, this.getId(), value);
			}

		}

		public ResourceLocation getId()
		{
			return this.id;
		}

		public TYPE get()
		{
			TYPE value = this.value;
			Objects.requireNonNull(value, () -> "Registry Object not present: " + this.getId());
			return value;
		}

	}

}
