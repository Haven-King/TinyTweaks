package dev.hephaestus.tweaks;

import dev.hephaestus.tweaks.client.BarrelBlockEntityRenderer;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tweaks implements ModInitializer, ClientModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "tinytweaks";
    public static final String MOD_NAME = "TinyTweaks";

    public static final double LILY_PAD_MOD = 0.2109375D /* 27 / 128 */;

    public static TweaksConfig CONFIG = new TweaksConfig();

    @Override
    public void onInitialize() {
        AutoConfig.register(TweaksConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TweaksConfig.class).getConfig();
    }

    public static void log(Level level, String message, Object... args){
        LOGGER.log(level, "["+MOD_NAME+"] " + String.format(message, args));
    }

    public static void log(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityType.BARREL, BarrelBlockEntityRenderer::new);
    }
}