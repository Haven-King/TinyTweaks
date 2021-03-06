package dev.hephaestus.tweaks;

import dev.hephaestus.climbable.api.ClimbingSpeedRegistry;
import dev.hephaestus.tweaks.block.Moistener;
import dev.hephaestus.tweaks.client.render.block.entity.BarrelBlockLabelRenderer;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tweaks implements ModInitializer, ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "tinytweaks";
    public static final String MOD_NAME = "TinyTweaks";

    public static final double LILY_PAD_MOD = 0.2109375D;

    public static final TweaksConfig CONFIG;

    static {
        AutoConfig.register(TweaksConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TweaksConfig.class).getConfig();
    }

    public static Tag<Item> SHOWS_GRASS_HITBOXES = TagRegistry.item(new Identifier(MOD_ID, "shows_grass_hitboxes"));

    @Override
    public void onInitialize() {
        Moistener.canMoisten(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE);
        Moistener.canMoisten(Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB);
        Moistener.canMoisten(Blocks.COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS);
        Moistener.canMoisten(Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE_WALL);

        Moistener.canMoisten(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS);
        Moistener.canMoisten(Blocks.STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB);
        Moistener.canMoisten(Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS);
        Moistener.canMoisten(Blocks.STONE_BRICK_WALL, Blocks.MOSSY_STONE_BRICK_WALL);

        ClimbingSpeedRegistry.registerClimbableTag(BlockTags.LOGS, e -> CONFIG.leaves.treeClimbingSpeed, e -> {
            if (CONFIG.leaves.climb) {
                World world = e.world;
                BlockPos feet = e.getBlockPos();

                for (BlockPos pos : BlockPos.iterate(feet, feet.up((int) e.getHeight()))) {
                    if (world.getBlockState(pos).isIn(BlockTags.LEAVES) && (
                            world.getBlockState(pos.north()).getBlock().isIn(BlockTags.LOGS) ||
                            world.getBlockState(pos.east()).getBlock().isIn(BlockTags.LOGS) ||
                            world.getBlockState(pos.south()).getBlock().isIn(BlockTags.LOGS) ||
                            world.getBlockState(pos.west()).getBlock().isIn(BlockTags.LOGS))) {
                        return true;
                    }
                }
            }

            return false;
        });

        UseEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (playerEntity.isSneaking() && CONFIG.armorStandSwap && entity instanceof ArmorStandEntity) {
                ArmorStandEntity armorStand = (ArmorStandEntity) entity;
                boolean fired = false;

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                        ItemStack newStack = armorStand.getEquippedStack(slot);
                        ItemStack oldStack = playerEntity.getEquippedStack(slot);

                        if (!newStack.isEmpty() || !oldStack.isEmpty()) {
                            playerEntity.equipStack(slot, newStack);
                            armorStand.equipStack(slot, oldStack);
                            fired = true;
                        }
                    }
                }

                return fired ? ActionResult.SUCCESS : ActionResult.PASS;
            }

            return ActionResult.PASS;
        });
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