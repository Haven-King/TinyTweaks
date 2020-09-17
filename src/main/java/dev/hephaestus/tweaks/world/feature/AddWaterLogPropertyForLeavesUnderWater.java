package dev.hephaestus.tweaks.world.feature;

import com.mojang.serialization.Codec;
import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class AddWaterLogPropertyForLeavesUnderWater extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> WATER_LOG_LEAVES = new AddWaterLogPropertyForLeavesUnderWater(DefaultFeatureConfig.CODEC);


    public AddWaterLogPropertyForLeavesUnderWater(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    //Find leaves and replace them waterlogged property if they're between the seafloor and sea level.
    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int seaLevel = world.getSeaLevel();
        for (int x = -8; x <= 24; x++) {
            for (int z = -8; z <= 24; z++) {
                int totalX = pos.getX() + x;
                int totalZ = pos.getZ() + z;

                mutable.set(totalX, seaLevel, totalZ);
                int seaFloor = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                for (int y = seaLevel; y >= seaFloor; y--) {
                    BlockState leaveState = world.getBlockState(mutable);
                    if (leaveState.getBlock() instanceof LeavesBlock) {
                        world.setBlockState(mutable, leaveState.with(Properties.WATERLOGGED, true), 2);
                    }
                    mutable.move(Direction.DOWN);
                }
            }
        }
        return true;
    }


    public static void replaceLeaves() { ;
        Registry.register(Registry.FEATURE, new Identifier(Tweaks.MOD_ID, "water_log_leaves"), WATER_LOG_LEAVES);

        for (Biome biome : Registry.BIOME) {
            //Make sure a biome is not end or nether and that its depth is under 0.0(Should aim for Swamps & Ocean Biomes 90% of the time.
            if (!biome.getCategory().equals(Biome.Category.NETHER) && !biome.getCategory().equals(Biome.Category.THEEND) && biome.getDepth() <= 0.0) {
                biome.addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, WATER_LOG_LEAVES.configure(DefaultFeatureConfig.DEFAULT).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
            }
        }
    }
}