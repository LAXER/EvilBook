package me.evilpeanut;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

/**
 * @author Reece Aaron Lecrivain
 */
public class EvilWorldChunkGenerator extends ChunkGenerator {

	int HILL_HEIGHT = 64;
	int SCALE = 64;
	int MIN_HEIGHT = 64;

	/**
	 * Return the x, y and z as position in the chunk byte array
	 * @param x The X position
	 * @param y The Y position
	 * @param z The Z position
	 * @return The position in the chunk byte array
	 */
    private int xyzToByte(int x, int y, int z) {
    	return (x * 16 + z) * 256 + y;
    }

	/**
	 * Generate and store the chunk into an array of bytes
	 */
	@Override
	public byte[] generate(World world, Random random, int ChunkX, int ChunkZ) {

		byte[] chunk = new byte[16 * 16 * 256];

		Random rand = new Random(world.getSeed());
		SimplexOctaveGenerator gen = new SimplexOctaveGenerator(rand, 8);

		gen.setScale(1 / (double)SCALE);

		Biome biome = world.getBiome(ChunkX * 16, ChunkZ * 16);
		
		for (int x = 0; x < 16; x++) {

			for (int z = 0; z < 16; z++) {

				double noise = gen.noise(x + ChunkX * 16, z + ChunkZ * 16, 0.5 + rand.nextDouble(), 0.5) * HILL_HEIGHT;
				double topHeight = MIN_HEIGHT + noise;

				for (int y = 0; y <= topHeight; y++) {

					// Bedrock Layer
					if (y == 0) {
						if (biome == Biome.ICE_MOUNTAINS || biome == Biome.ICE_PLAINS || biome == Biome.TAIGA || biome == Biome.TAIGA_HILLS) {
							chunk[xyzToByte(x, y, z)] = (byte)79;
						} else if (biome == Biome.JUNGLE || biome == Biome.JUNGLE_HILLS) {
							chunk[xyzToByte(x, y, z)] = (byte)17;
						} else if (biome == Biome.DESERT || biome == Biome.DESERT_HILLS) {
							chunk[xyzToByte(x, y, z)] = (byte)24;
						} else {
							chunk[xyzToByte(x, y, z)] = (byte)7;
						}
						continue;
					}

					// Stone Layer
					if (y >= 1 && y <= topHeight - 4) {
						if (biome == Biome.ICE_MOUNTAINS || biome == Biome.ICE_PLAINS || biome == Biome.TAIGA || biome == Biome.TAIGA_HILLS) {
							chunk[xyzToByte(x, y, z)] = (byte)79;
						} else if (biome == Biome.JUNGLE || biome == Biome.JUNGLE_HILLS) {
							chunk[xyzToByte(x, y, z)] = (byte)17;
						} else if (biome == Biome.DESERT || biome == Biome.DESERT_HILLS) {
							chunk[xyzToByte(x, y, z)] = (byte)12;
						} else {
							chunk[xyzToByte(x, y, z)] = (byte)1;
						}
						continue;
					}

					// Dirt and Grass
					if (y <= 256) {
						if (y > topHeight - 1 && y <= topHeight) {
							if (biome == Biome.ICE_MOUNTAINS || biome == Biome.ICE_PLAINS || biome == Biome.TAIGA || biome == Biome.TAIGA_HILLS) {
								chunk[xyzToByte(x, y, z)] = (byte)78;
							} else if (biome == Biome.JUNGLE || biome == Biome.JUNGLE_HILLS) {
								chunk[xyzToByte(x, y, z)] = (byte)18;
							} else if (biome == Biome.DESERT || biome == Biome.DESERT_HILLS) {
								chunk[xyzToByte(x, y, z)] = (byte)81;
							} else {
								chunk[xyzToByte(x, y, z)] = (byte)2;
							}
						} else {
							if (biome == Biome.ICE_MOUNTAINS || biome == Biome.ICE_PLAINS || biome == Biome.TAIGA || biome == Biome.TAIGA_HILLS) {
								chunk[xyzToByte(x, y, z)] = (byte)80;
							} else if (biome == Biome.JUNGLE || biome == Biome.JUNGLE_HILLS) {
								chunk[xyzToByte(x, y, z)] = (byte)18;
							} else if (biome == Biome.DESERT || biome == Biome.DESERT_HILLS) {
								chunk[xyzToByte(x, y, z)] = (byte)81;
							} else {
								chunk[xyzToByte(x, y, z)] = (byte)3;
							}
						}
						continue;
					}

				}
			}
		}
		return chunk.clone();
	}

	/**
	 * Return the default populators
	 */
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		ArrayList<BlockPopulator> pop = new ArrayList<BlockPopulator>();
		//pop.add(new com.EvilWorld.EvilWorldBlockPopulators.CobblestoneTowers());
		return pop;
	}	
}