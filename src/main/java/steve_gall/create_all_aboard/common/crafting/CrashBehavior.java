package steve_gall.create_all_aboard.common.crafting;

import org.apache.commons.lang3.EnumUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

public class CrashBehavior
{
	private final float explosionRadius;
	private final boolean causesFire;
	private final Level.ExplosionInteraction explosionMode;

	public CrashBehavior(Builder builder)
	{
		this.explosionRadius = builder.explosionRadius();
		this.causesFire = builder.causesFire();
		this.explosionMode = builder.explosionMode();
	}

	public CrashBehavior(JsonElement json)
	{
		this(new Builder(json));
	}

	public CrashBehavior(FriendlyByteBuf buffer)
	{
		this(new Builder(buffer));
	}

	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		json.addProperty("explosionRadius", this.getExplosionRadius());
		json.addProperty("causesFire", this.isCausesFire());
		json.addProperty("explosionMode", this.getExplosionMode().name().toLowerCase());
		return json;
	}

	public void toNetwork(FriendlyByteBuf buffer)
	{
		buffer.writeFloat(this.getExplosionRadius());
		buffer.writeBoolean(this.isCausesFire());
		buffer.writeEnum(this.getExplosionMode());
	}

	public float getExplosionRadius()
	{
		return this.explosionRadius;
	}

	public boolean isCausesFire()
	{
		return this.causesFire;
	}

	public Level.ExplosionInteraction getExplosionMode()
	{
		return this.explosionMode;
	}

	public static class Builder
	{
		private float explosionRadius = 0.0F;
		private boolean causesFire = false;
		private Level.ExplosionInteraction explosionMode = Level.ExplosionInteraction.NONE;

		public Builder()
		{

		}

		public Builder(JsonElement json)
		{
			JsonObject jobject = json.getAsJsonObject();
			this.explosionRadius(GsonHelper.getAsFloat(jobject, "explosionRadius", 0.0F));
			this.causesFire(GsonHelper.getAsBoolean(jobject, "causesFire"));
			this.explosionMode(EnumUtils.getEnumIgnoreCase(Level.ExplosionInteraction.class, GsonHelper.getAsString(jobject, "explosionMode"), Level.ExplosionInteraction.NONE));
		}

		public Builder(FriendlyByteBuf buffer)
		{
			this.explosionRadius(buffer.readFloat());
			this.causesFire(buffer.readBoolean());
			this.explosionMode(buffer.readEnum(Level.ExplosionInteraction.class));
		}

		public CrashBehavior build()
		{
			return new CrashBehavior(this);
		}

		public float explosionRadius()
		{
			return this.explosionRadius;
		}

		public Builder explosionRadius(float explosionRadius)
		{
			this.explosionRadius = explosionRadius;
			return this;
		}

		public boolean causesFire()
		{
			return this.causesFire;
		}

		public Builder causesFire(boolean causesFire)
		{
			this.causesFire = causesFire;
			return this;
		}

		public Level.ExplosionInteraction explosionMode()
		{
			return this.explosionMode;
		}

		public Builder explosionMode(Level.ExplosionInteraction explosionMode)
		{
			this.explosionMode = explosionMode;
			return this;
		}

	}

}
