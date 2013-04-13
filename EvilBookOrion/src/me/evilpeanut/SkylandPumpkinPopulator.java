package me.evilpeanut;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class SkylandPumpkinPopulator extends BlockPopulator {
	@Override
	public void populate(World world, Random positionRandomizer, Chunk chunk) {
		if (positionRandomizer.nextInt(25) == 0) {
			int worldChunkX = chunk.getX() * 16;
			int worldChunkZ = chunk.getZ() * 16;
			for (int pumpkinNumber = 0; pumpkinNumber <= positionRandomizer.nextInt(9); ++pumpkinNumber) {
				int pumpkinX = worldChunkX + positionRandomizer.nextInt(8) - positionRandomizer.nextInt(8);
				int pumpkinZ = worldChunkZ + positionRandomizer.nextInt(8) - positionRandomizer.nextInt(8);
				int pumpkinY = world.getHighestBlockYAt(pumpkinX, pumpkinZ);
				if (world.getBlockTypeIdAt(pumpkinX, pumpkinY, pumpkinZ) == 0 && world.getBlockTypeIdAt(pumpkinX, pumpkinY - 1, pumpkinZ) == 2) {
					world.getBlockAt(pumpkinX, pumpkinY, pumpkinZ).setTypeIdAndData(86, (byte) positionRandomizer.nextInt(4), false);
				}
			}
		}
	}
}