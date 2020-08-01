package dev.hephaestus.tweaks;

import dev.hephaestus.tweaks.block.Moistener;
import dev.hephaestus.tweaks.client.render.block.entity.BarrelBlockLabelRenderer;
import dev.hephaestus.tweaks.world.feature.AddWaterLogPropertyForLeavesUnderWater;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
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

        Moistener.canMoisten(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE);
        Moistener.canMoisten(Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB);
        Moistener.canMoisten(Blocks.COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS);
        Moistener.canMoisten(Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE_WALL);

        Moistener.canMoisten(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS);
        Moistener.canMoisten(Blocks.STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB);
        Moistener.canMoisten(Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS);
        Moistener.canMoisten(Blocks.STONE_BRICK_WALL, Blocks.MOSSY_STONE_BRICK_WALL);

        AddWaterLogPropertyForLeavesUnderWater.replaceLeaves();
    }

    public static void log(Level level, String message, Object... args){
        LOGGER.log(level, "["+MOD_NAME+"] " + String.format(message, args));
    }

    public static void log(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityType.BARREL, BarrelBlockLabelRenderer::new);
    }
}