package me.evilpeanut;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

/**
 * @author Reece Aaron Lecrivain
 */
public class EvilTheEndGenerator extends ChunkGenerator {
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

		gen.setScale(1);

		for (int x = 0; x < 16; x++) {

			for (int z = 0; z < 16; z++) {

				double noise = gen.noise(x + ChunkX * 16, z + ChunkZ * 16, 0.5 + rand.nextDouble(), 0.5) * 4;
				double topHeight = 5 + noise;

				for (int y = 0; y <= topHeight; y++) {
					chunk[xyzToByte(x, y, z)] = (byte)Material.STONE.getId();
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
