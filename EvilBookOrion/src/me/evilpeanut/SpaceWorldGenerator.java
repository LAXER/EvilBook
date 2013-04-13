package me.evilpeanut;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class SpaceWorldGenerator extends ChunkGenerator {
	private Map<Point, List<Planetoid>> cache;
	private long seed;

	public SpaceWorldGenerator(EvilBook plugin) {
		seed = (long) plugin.getServer().getWorlds().get(0).getSeed();
		cache = new HashMap<Point, List<Planetoid>>();
	}

	@SuppressWarnings("unchecked")
	public byte[] generate(World world, Random random, int x, int z) {
		byte[] retVal = new byte[32768];
		Arrays.fill(retVal, (byte) 0);
		int sysX;
		if (x >= 0) {
			sysX = x / 50;
		} else {
			sysX = (int) Math.ceil((-x) / (51));
			sysX = -sysX;
		}
		int sysZ;
		if (z >= 0) {
			sysZ = z / 50;
		} else {
			sysZ = (int) Math.ceil((-z) / (51));
			sysZ = -sysZ;
		}
		List<Planetoid> curSystem = cache.get(new Point(sysX, sysZ));
		if (curSystem == null) {
			File systemFolder = new File("plugins/EvilBook/SpaceLand Systems/");
			File systemFile = new File(systemFolder, "system_" + sysX + "." + sysZ + ".dat");
			if (systemFile.exists()) {
				try {
					FileInputStream fis = new FileInputStream(systemFile);
					ObjectInputStream ois = new ObjectInputStream(fis);
					curSystem = (List<Planetoid>) ois.readObject();
					cache.put(new Point(sysX, sysZ), curSystem);
					ois.close();
					fis.close();
				} catch (Exception ex) {

				}
			} else {
				curSystem = generatePlanets(sysX, sysZ);
				try {
					systemFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(systemFile);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(curSystem);
					oos.flush();
					oos.close();
					fos.flush();
					fos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				cache.put(new Point(sysX, sysZ), curSystem);
			}
		}
		int chunkXPos;
		if (x >= 0) {
			chunkXPos = (x % 50) * 16;
		} else {
			chunkXPos = ((-x) % 50) * 16;
			if (chunkXPos == 0) {
				chunkXPos = 800;
			}
			chunkXPos = 800 - chunkXPos;
		}
		int chunkZPos;
		if (z >= 0) {
			chunkZPos = (z % 50) * 16;
		} else {
			chunkZPos = ((-z) % 50) * 16;
			if (chunkZPos == 0) {
				chunkZPos = 800;
			}
			chunkZPos = 800 - chunkZPos;
		}
		try {
			for (Planetoid curPl : curSystem) {
				int relCenterX = curPl.xPos - chunkXPos;
				int relCenterZ = curPl.zPos - chunkZPos;
				for (int curX = -curPl.radius; curX <= curPl.radius; curX++) {
					int blkX = curX + relCenterX;
					if (blkX >= 0 && blkX < 16) {
						int distFromCenter = Math.abs(curX);
						int radius = (int) Math.ceil(Math.sqrt((curPl.radius * curPl.radius) - (distFromCenter * distFromCenter)));
						for (int curZ = -radius; curZ <= radius; curZ++) {
							int blkZ = curZ + relCenterZ;
							if (blkZ >= 0 && blkZ < 16) {
								int zDistFromCenter = Math.abs(curZ);
								int zRadius = (int) Math.ceil(Math.sqrt((radius * radius) - (zDistFromCenter * zDistFromCenter)));
								for (int curY = -zRadius; curY <= zRadius; curY++) {
									int blkY = curPl.yPos + curY;
									retVal[(blkX * 16 + blkZ) * 128 + blkY] = (byte) curPl.shellBlk.getId();
								}
							}
						}
					}
				}
				int coreRadius = curPl.radius - curPl.shellThickness;
				if (coreRadius > 0) {
					for (int curX = -coreRadius; curX <= coreRadius; curX++) {
						int blkX = curX + relCenterX;
						if (blkX >= 0 && blkX < 16) {
							int distFromCenter = Math.abs(curX);
							int radius = (int) Math.ceil(Math.sqrt((coreRadius * coreRadius) - (distFromCenter * distFromCenter)));
							for (int curZ = -radius; curZ <= radius; curZ++) {
								int blkZ = curZ + relCenterZ;
								if (blkZ >= 0 && blkZ < 16) {
									int zDistFromCenter = Math.abs(curZ);
									int zRadius = (int) Math.ceil(Math.sqrt((radius * radius) - (zDistFromCenter * zDistFromCenter)));
									for (int curY = -zRadius; curY <= zRadius; curY++) {
										int blkY = curPl.yPos + curY;
										retVal[(blkX * 16 + blkZ) * 128 + blkY] = (byte) curPl.coreBlk.getId();
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {}
		return retVal;
	}

	public boolean canSpawn(World world, int x, int z) {
		return true;
	}

	private List<Planetoid> generatePlanets(int x, int z) {
		Random generator = new Random();
		List<Planetoid> planetoids = new ArrayList<Planetoid>();
		if (x == 0 && z == 0) {
			Planetoid spawnPl = new Planetoid();
			spawnPl.xPos = 7;
			spawnPl.yPos = 70;
			spawnPl.zPos = 7;
			spawnPl.coreBlk = Material.PUMPKIN;
			spawnPl.shellBlk = Material.DIRT;
			spawnPl.shellThickness = 2;
			spawnPl.radius = 6;
			planetoids.add(spawnPl);
		}
		if (x < 0) {
			seed = seed << 1;
		}
		if (z < 0) {
			seed = -seed;
		}
		Random rand = new Random(seed);
		for (int i = 0; i < Math.abs(x) + Math.abs(z); i++) {
			rand.nextDouble();
		}
		for (int i = 0; i < 750; i++) {
			int planettype = generator.nextInt(10) + 1;
			Planetoid curPl = new Planetoid();
			curPl.shellBlk = Material.STONE;
			curPl.coreBlk = Material.COBBLESTONE;
			if (planettype == 1) {
				curPl.shellBlk = Material.ICE;
				curPl.coreBlk = Material.STATIONARY_WATER;
			}
			if (planettype == 2) {
				curPl.shellBlk = Material.STATIONARY_LAVA;
				curPl.coreBlk = Material.DIAMOND_ORE;
			}
			if (planettype == 3) {
				curPl.shellBlk = Material.DIRT;
				curPl.coreBlk = Material.PUMPKIN;
			}
			if (planettype == 4) {
				curPl.shellBlk = Material.NETHERRACK;
				curPl.coreBlk = Material.SOUL_SAND;
			}
			curPl.shellThickness = (byte) (rand.nextInt(2) + 3);
			curPl.radius = (byte) (rand.nextInt(35) + 5);
			curPl.xPos = -1;
			while (curPl.xPos == -1) {
				int curTry = rand.nextInt(800);
				if (curTry + curPl.radius < 800 && curTry - curPl.radius >= 0) {
					curPl.xPos = curTry;
				}
			}
			curPl.yPos = rand.nextInt(128 - curPl.radius * 2) + curPl.radius;
			curPl.zPos = -1;
			while (curPl.zPos == -1) {
				int curTry = rand.nextInt(800);
				if (curTry + curPl.radius < 800 && curTry - curPl.radius >= 0) {
					curPl.zPos = curTry;
				}
			}
			boolean discard = false;
			for (Planetoid pl : planetoids) {
				int distMin = pl.radius + curPl.radius + 10;
				if (distanceSquared(pl, curPl) < distMin * distMin) {
					discard = true;
					break;
				}
			}
			if (!discard) {
				planetoids.add(curPl);
			}
		}
		return planetoids;
	}

	private int distanceSquared(Planetoid pl1, Planetoid pl2) {
		int xDist = pl2.xPos - pl1.xPos;
		int yDist = pl2.yPos - pl1.yPos;
		int zDist = pl2.zPos - pl1.zPos;
		return xDist * xDist + yDist * yDist + zDist * zDist;
	}
}