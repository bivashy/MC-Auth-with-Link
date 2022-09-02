package me.mastercapexd.auth.velocity.hooks.limbo.config;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import net.elytrium.limboapi.api.Limbo;
import net.elytrium.limboapi.api.LimboFactory;
import net.elytrium.limboapi.api.chunk.Dimension;

public class LimboConfig implements ConfigurationHolder {
    @ConfigField
    private Dimension dimension = Dimension.OVERWORLD;
    @ConfigField
    private long time;
    @ConfigField
    private double x, y, z;
    @ConfigField
    private float yaw, pitch;

    private final String name;

    public LimboConfig(ConfigurationSectionHolder sectionHolder) {
        LimboAPIConfig.CONFIGURATION_PROCESSOR.resolve(sectionHolder, this);
        this.name = sectionHolder.key();
    }

    public Limbo createLimbo(LimboFactory factory) {
        return factory.createLimbo(factory.createVirtualWorld(dimension, x, y, z, yaw, pitch)).setWorldTime(time).setName(name);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public long getTime() {
        return time;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getName() {
        return name;
    }
}
