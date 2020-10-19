package corgiaoc.byg.common.world.feature.end.islands;

import com.mojang.serialization.Codec;
import corgiaoc.byg.common.world.feature.config.FloatingIslandConfig;
import corgiaoc.byg.util.noise.fastnoise.lite.FastNoiseLite;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class FloatingIslands2 extends Feature<FloatingIslandConfig> {

    FastNoiseLite perlin = null;

    public FloatingIslands2(Codec<FloatingIslandConfig> codec) {
        super(codec);
    }

    @Override
    public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, FloatingIslandConfig config) {
        setSeed(world.getSeed());

        double radius = 15; /*rand.nextInt(config.getMaxPossibleRadius()) + config.getMinRadius() - 5*/
        double halfRadius = radius / 2;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        if (world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX(), pos.getZ()) > 4)
            return false;

        //top bit on the island (doesn't work at the moment!)
        for (double x = -halfRadius - 8; x <= halfRadius + 8; x++) {
            for (double y = -radius - 2; y <= radius; y++) {
                for (double z = -halfRadius - 8; z <= halfRadius + 8; z++) {
                    double noise = FastNoiseLite.getSpongePerlinValue(perlin.GetNoise(x, y, z)) * 12;
                    double distanceSqt1 = x * x + y * y + z * z + noise * noise;
                    if (distanceSqt1 <= halfRadius * radius) {
                        mutable.setPos(pos).move((int) x, (int) y, (int) z);
                        if (world.getBlockState(mutable).getMaterial() == Material.AIR) {
                            if (y >= 1)
                                world.setBlockState(mutable, config.getBlockProvider().getBlockState(rand, mutable), 2);
                            if (y >= 2)
                                world.setBlockState(mutable, config.getTopBlockProvider().getBlockState(rand, mutable), 2);
                        }
                    }
                }
            }
        }

        //the actual freaking island!!! (actually works now!!!)
        for (double x = -radius - 8; x <= radius + 8; x++) {
            for (double y = -radius - 2; y <= radius + 11; y++) {
                for (double z = -radius - 8; z <= radius + 8; z++) {
                    double noise = FastNoiseLite.getSpongePerlinValue(perlin.GetNoise(x, y, z)) * 12;
                    double scaledNoise = (noise / 11) * ((y * 3) / ((x * x) + (z * z)));
                    if (scaledNoise >= 0.5) {
                        if (y >= 1) {
                            world.setBlockState(pos.add(x, y - 22, z), config.getBlockProvider().getBlockState(rand, mutable), 2);
                            if (y >= 21) {
                                world.setBlockState(pos.add(x, y - 22, z), config.getBlockProvider().getBlockState(rand, mutable), 2);
                                if (y >= 22) {
                                    world.setBlockState(pos.add(x, y - 22, z), config.getTopBlockProvider().getBlockState(rand, mutable), 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }


    public void setSeed(long seed) {
        if (perlin == null) {
            perlin = FastNoiseLite.createSpongePerlin((int) seed);
            perlin.SetFrequency(0.2F);
        }
    }
}
