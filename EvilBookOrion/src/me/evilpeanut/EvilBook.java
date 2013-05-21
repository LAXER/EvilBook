package me.evilpeanut;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Weather;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

/**
 * @author Reece Aaron Lecrivain
 */
public class EvilBook extends JavaPlugin {
	public Map<String, PlayerProfile> playerProfiles = new HashMap<String, PlayerProfile>();
	public Map<Short, List<String>> blockList = new HashMap<Short, List<String>>();
	public Map<Byte, String> hangingEntityList = new HashMap<Byte, String>();
	public List<DynamicSign> dynamicSignList = new ArrayList<DynamicSign>();
	public Map<String, Location> warpList = new HashMap<String, Location>();
	public Map<String, Byte> commandBlacklist = new HashMap<String, Byte>();
	public List<EvilEditBlock> EvilEdit = new ArrayList<EvilEditBlock>();
	public Map<String, Byte> fishList = new HashMap<String, Byte>();
	public List<Region> regionList = new ArrayList<Region>();
	public ScoreboardManager scoreboardManager;
	
	//
	// Survival Scoreboard
	//
	public Scoreboard survivalStatsScoreboard;
	public Team survivalTeam;
	
	//
	// EvilEdit Scoreboard
	//
	//public Scoreboard evilEditStatsScoreboard;
	//public Team evilEditTeam;
	
	/**
	 * Called when the plugin is enabled
	 */
	public void onEnable() {
		//
		// Register The Event Listener
		//
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new EventListener(this), this);
		//
		// Check the plugin folders and files exist
		//
		File check = new File("plugins/EvilBook");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook'");
		check = new File("plugins/EvilBook/Players");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/Players'");
		check = new File("plugins/EvilBook/Regions");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/Regions'");
		check = new File("plugins/EvilBook/Protection");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/Protection'");
		check = new File("plugins/EvilBook/Dynamic Signs");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/Dynamic Signs'");
		check = new File("plugins/EvilBook/SpaceLand Systems");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/SpaceLand Systems'");
		check = new File("plugins/EvilBook/Inventories");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/Inventories'");
		check = new File("plugins/EvilBook/Inventories/Creative");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/Inventories/Creative'");
		check = new File("plugins/EvilBook/Inventories/Survival");
		if (check.exists() == false && check.mkdir() == false) logSevere("Failed to create directory 'plugins/EvilBook/Inventories/Survival'");
		check = new File("plugins/EvilBook/Warps.db");
		if (check.exists() == false) try {check.createNewFile();} catch (Exception e) {logSevere("Failed to create file 'plugins/EvilBook/Warps.db'");}
		check = new File("plugins/EvilBook/ContainerProtection.db");
		if (check.exists() == false) try {check.createNewFile();} catch (Exception e) {logSevere("Failed to create file 'plugins/EvilBook/ContainerProtection.db'");}
		//
		// Scoreboard
		//
		scoreboardManager = Bukkit.getScoreboardManager();
		//
		// Survival Scoreboard
		//
		survivalStatsScoreboard = scoreboardManager.getNewScoreboard();
		survivalTeam = survivalStatsScoreboard.registerNewTeam("Survival");
		survivalTeam.setAllowFriendlyFire(true);
		Objective objective = survivalStatsScoreboard.registerNewObjective("Levels", "Skills");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("Skills");
		Score strengthScore = objective.getScore(Bukkit.getOfflinePlayer("§aStrength"));
		strengthScore.setScore(1);
		Score miningScore = objective.getScore(Bukkit.getOfflinePlayer("§aMining"));
		miningScore.setScore(1);
		Score fishingScore = objective.getScore(Bukkit.getOfflinePlayer("§aFishing"));
		fishingScore.setScore(1);
		//
		// Evil Edit Scoreboard
		//
		//evilEditStatsScoreboard = scoreboardManager.getNewScoreboard();
		//evilEditTeam = survivalStatsScoreboard.registerNewTeam("EvilEdit");
		//Objective evilEditObjective = evilEditStatsScoreboard.registerNewObjective("EvilEditStats", "Evil Edit");
		//evilEditObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		//
		// Load the command blacklist
		//
		Properties prop = new Properties();
		check = new File("plugins/EvilBook/CommandBlackList.db");
		if (check.exists())
		{
			try {
				FileInputStream inputStream = new FileInputStream(check);
				prop.load(inputStream);
				inputStream.close();
				for (String propName : prop.stringPropertyNames()) commandBlacklist.put(propName, Byte.valueOf(prop.getProperty(propName)));
			} catch (Exception exception) {
				logSevere("Failed to load command black list");
				exception.printStackTrace();
			}
		} else {
			try {
				check.createNewFile();
				FileInputStream inputStream = new FileInputStream(check);
				prop.load(inputStream);
				inputStream.close();
				prop.setProperty("/restart", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/setall", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/cleandatabase", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/rollback", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/spam", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/toggleredstone", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/stop", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/op", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/deop", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/reload", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/debug", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/xp", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/charge", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/reward", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/rewardall", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/makeadmin", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/demote", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/kickall", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/setspawn", Byte.toString(Rank.ServerOwner.getID()));
				prop.setProperty("/setrank", Byte.toString(Rank.Custom.getID()));
				prop.setProperty("/pardon", Byte.toString(Rank.RainbowOwner.getID()));
				prop.setProperty("/promote", Byte.toString(Rank.RainbowOwner.getID()));
				prop.setProperty("/broadcast", Byte.toString(Rank.Owner.getID()));
				prop.setProperty("/say", Byte.toString(Rank.Owner.getID()));
				prop.setProperty("/vanish", Byte.toString(Rank.CoOwner.getID()));
				prop.setProperty("/hide", Byte.toString(Rank.CoOwner.getID()));
				prop.setProperty("/unvanish", Byte.toString(Rank.CoOwner.getID()));
				prop.setProperty("/show", Byte.toString(Rank.CoOwner.getID()));
				prop.setProperty("/regen", Byte.toString(Rank.Elite.getID()));
				prop.setProperty("/copy", Byte.toString(Rank.Councillor.getID()));
				prop.setProperty("/paste", Byte.toString(Rank.Councillor.getID()));
				prop.setProperty("/sphere", Byte.toString(Rank.SpecialAdmin.getID()));
				prop.setProperty("/cspawn", Byte.toString(Rank.SpecialAdmin.getID()));
				prop.setProperty("/spawncreature", Byte.toString(Rank.SpecialAdmin.getID()));
				prop.setProperty("/drain", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/overlay", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/walls", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/outline", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/hollow", Byte.toString(Rank.Admin.getID()));	
				prop.setProperty("/undo", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/fill", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/replace", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/memstat", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/clean", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/del", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/delete", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/storm", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/rain", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/sun", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/time", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/butcher", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/tree", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/dawn", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/day", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/dusk", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/night", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/rename", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/tphere", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/teleporthere", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/region", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/log", Byte.toString(Rank.Admin.getID()));
				prop.setProperty("/kick", Byte.toString(Rank.Moderator.getID()));
				prop.setProperty("/sky", Byte.toString(Rank.Moderator.getID()));
				prop.setProperty("/skyland", Byte.toString(Rank.Moderator.getID()));
				prop.setProperty("/spaceland", Byte.toString(Rank.Architect.getID()));
				prop.setProperty("/space", Byte.toString(Rank.Architect.getID()));
				prop.setProperty("/flatland", Byte.toString(Rank.AdvancedBuilder.getID()));
				FileOutputStream outputStream = new FileOutputStream(check);
				prop.store(outputStream, "Format:\nCommand = Minimum rank required\nRank values:\nVisitor = " + Rank.Visitor.getID() + 
						"\nBuilder = " + Rank.Builder.getID() + "\nAdvancedBuilder = " + Rank.AdvancedBuilder + "\nArchitect = " + Rank.Architect.getID() + "\nModerator = " + Rank.Moderator.getID() + 
						"\nPolice = " + Rank.Police.getID() + "\nAdmin = " + Rank.Admin.getID() + "\nSpecialAdmin = " + Rank.SpecialAdmin.getID() + 
						"\nCounciller = " + Rank.Councillor.getID() + "\nElite = " + Rank.Elite.getID() + "\nCoOwner = " + Rank.CoOwner.getID() + 
						"\nOwner = " + Rank.Owner.getID() + "\nRainbowOwner = " + Rank.RainbowOwner.getID() + "\nCustom = " + Rank.Custom.getID() +
						"\nServerOwner = " + Rank.ServerOwner.getID() + "\nDate created:");
				outputStream.close();
				for (String propName : prop.stringPropertyNames()) commandBlacklist.put(propName, Byte.valueOf(prop.getProperty(propName)));
			} catch (Exception exception) {
				logSevere("Failed to create command black list");
				exception.printStackTrace();
			}
		}
		//
		// The End Generator
		//
		// TODO: Custom the end
		//getServer().getWorlds().get(2).
		//WorldCreator evilEndLand = new WorldCreator(getServer().getWorlds().get(2).getName());
		//evilEndLand.generator(new EvilTheEndGenerator());
		//evilEndLand.environment(Environment.THE_END);
		//getServer().createWorld(evilEndLand);
		//
		// EvilLand Generator
		//
		WorldCreator evilLand = new WorldCreator("EvilLand");
		evilLand.generator(new EvilWorldChunkGenerator());
		getServer().createWorld(evilLand);
		//
		// FlatLand Generator
		//
		WorldCreator flatLand = new WorldCreator("FlatLand");
		flatLand.type(WorldType.FLAT);
		getServer().createWorld(flatLand);
		//
		// SpaceLand Generator
		//
		WorldCreator spaceLand = new WorldCreator("SpaceLand");
		spaceLand.generator(new SpaceWorldGenerator(this));
		getServer().createWorld(spaceLand);
		//
		// FreeSurvival Generator
		//
		getServer().createWorld(new WorldCreator("FreeSurvivalLand"));
		//
		// Survival Generator
		//
		WorldCreator survivalLand = new WorldCreator("SurvivalLand");
		getServer().createWorld(survivalLand);
		//
		// Survival Nether Generator
		//
		WorldCreator survivalLandNether = new WorldCreator("SurvivalLandNether");
		survivalLandNether.environment(Environment.NETHER);
		getServer().createWorld(survivalLandNether);
		//
		// Skyland Generator
		//
		WorldCreator skyLand = new WorldCreator("SkyLand");
		skyLand.generator(new SkylandGenerator());
		getServer().createWorld(skyLand);
		//
		// Adventure Generator
		//
		getServer().createWorld(new WorldCreator("AdventureLand"));
		//
		// Nazi Zombies - Nacht Der Untoten Generator
		//
		getServer().createWorld(new WorldCreator("NachtDerUntoten"));
		//
		// Load regions
		//
		String[] regionFiles = new File("plugins/EvilBook/Regions").list();
		Properties regionProp;
		for (int regionNo = 0; regionNo < regionFiles.length; regionNo++) {
			try {
				FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Regions/" + regionFiles[regionNo]);
				regionProp = new Properties();
				regionProp.load(inputStream);
				inputStream.close();
				if (getServer().getWorld(regionProp.getProperty("worldName")) == null) continue;
				Region region = new Region(regionProp.getProperty("regionName"),
						new Location(getServer().getWorld(regionProp.getProperty("worldName")), Integer.valueOf(regionProp.getProperty("locationA").split(",")[0]), Integer.valueOf(regionProp.getProperty("locationA").split(",")[1]), Integer.valueOf(regionProp.getProperty("locationA").split(",")[2])),
						new Location(getServer().getWorld(regionProp.getProperty("worldName")), Integer.valueOf(regionProp.getProperty("locationB").split(",")[0]), Integer.valueOf(regionProp.getProperty("locationB").split(",")[1]), Integer.valueOf(regionProp.getProperty("locationB").split(",")[2])),
						Boolean.valueOf(regionProp.getProperty("isProtected")),
						regionProp.getProperty("ownerName"),
						regionProp.getProperty("welcomeMessage"),
						regionProp.getProperty("leaveMessage"),
						regionProp.getProperty("allowedPlayers")
						);
				regionList.add(region);
			} catch (Exception e) {
				logSevere("Failed to load region " + regionFiles[regionNo]);
				e.printStackTrace();
			}
		}
		//
		// Load Warps
		//
		Properties warpFile = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Warps.db");
			warpFile.load(inputStream);
			inputStream.close();
		} catch (Exception e) {
			logSevere("Failed to load warp file");
			e.printStackTrace();
		}
		int removedCorruptWarps = 0;
		for (Object key : warpFile.keySet().toArray()) {
			try {
				if (!getServer().getWorlds().contains(getServer().getWorld(warpFile.getProperty((String) key).split(":")[0]))) {
					logInfo("Warp " + (String)key + " removed: World " + warpFile.getProperty((String) key).split(":")[0] + " is unavailable");
					warpFile.remove(key);
					removedCorruptWarps++;
				} else {
					warpList.put((String)key, new Location(getServer().getWorld(warpFile.getProperty((String) key).split(":")[0]), 
							Double.valueOf(warpFile.getProperty((String) key).split(":")[1]),
							Double.valueOf(warpFile.getProperty((String) key).split(":")[2]), 
							Double.valueOf(warpFile.getProperty((String) key).split(":")[3]),
							Float.valueOf(warpFile.getProperty((String) key).split(":")[4]),
							Float.valueOf(warpFile.getProperty((String) key).split(":")[5])));
				}
			} catch (Exception e) {
				warpFile.remove(key);
				removedCorruptWarps++;
			}
		}
		if (removedCorruptWarps != 0) {
			try {
				FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Warps.db");
				warpFile.store(outputStream, null);
				outputStream.close();
				logInfo("Removed " + removedCorruptWarps + " corrupt warps");
			} catch (Exception e) {
				logSevere("Failed to remove " + removedCorruptWarps + " corrupt warps");
				e.printStackTrace();
			}
		}
		//
		// Convert player profiles and preform checks
		//
		String[] playerFiles = new File("plugins/EvilBook/Players").list();
		Properties playerProp;
		for (int playerNo = 0; playerNo < playerFiles.length; playerNo++) {
			try {
				FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Players/" + playerFiles[playerNo]);
				playerProp = new Properties();
				playerProp.load(inputStream);
				inputStream.close();
				if (playerProp.getProperty("Rank") == null) {
					playerProp.setProperty("Rank", "Visitor");
					FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Players/" + playerFiles[playerNo]);
					playerProp.store(outputStream, null);
					outputStream.close();
					logInfo("Repaired player profile: " + playerFiles[playerNo]);
					continue;
				}
			} catch (Exception e) {
				logSevere("Failed to load player " + playerFiles[playerNo]);
				e.printStackTrace();
			}
		}
		//
		// Load time signs
		//
		try {
			Properties propTimeSign = new Properties();
			String[] timeSignFiles = new File("plugins/EvilBook/Dynamic Signs/").list();
			for (int timeSignNo = 0; timeSignNo < timeSignFiles.length; timeSignNo++) {
				FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Dynamic Signs/" + timeSignFiles[timeSignNo]);
				propTimeSign.load(inputStream);
				inputStream.close();
				String location = propTimeSign.getProperty("Location");
				String[] text = new String[4];
				text[0] = propTimeSign.getProperty("Text1");
				text[1] = propTimeSign.getProperty("Text2");
				text[2] = propTimeSign.getProperty("Text3");
				text[3] = propTimeSign.getProperty("Text4");
				dynamicSignList.add(new DynamicSign(new Location(getServer().getWorld(location.split(",")[0]), Double.valueOf(location.split(",")[1]), Double.valueOf(location.split(",")[2]), Double.valueOf(location.split(",")[3])), text));
			}
		} catch (Exception e1) {
			logSevere("Failed to load time sign database");
			e1.printStackTrace();
		}
		//
		// Load Fish Name List
		//
		fishList.put("Jade Lungfish", (byte)10);
		fishList.put("Krasarang Paddlefish", (byte)12);
		fishList.put("Reef Octopus", (byte)14);
		fishList.put("Golden Carp", (byte)15);
		fishList.put("Emperor Salmon", (byte)16);
		fishList.put("Giant Mantis Shrimp", (byte)18);
		//
		// Load Block List
		//
		blockList.put((short)0, Arrays.asList("Air", "Void", "Nothing", "Space"));
		blockList.put((short)1, Arrays.asList("Stone", "SmoothStone", "Rock"));
		blockList.put((short)2, Arrays.asList("Grass", "GrassDirt"));
		blockList.put((short)3, Arrays.asList("Dirt", "Mud", "Ground"));
		blockList.put((short)4, Arrays.asList("CobbleStone", "Cobble"));
		blockList.put((short)5, Arrays.asList("Planks", "Plank", "WoodPlanks", "WoodPlank", "WoodenPlanks", "WoodenPlank"));
		blockList.put((short)6, Arrays.asList("Sapling", "Tree", "TreeSapling", "TreeSeed"));
		blockList.put((short)7, Arrays.asList("BedRock", "BedStone", "Adminium"));
		blockList.put((short)8, Arrays.asList("Water"));
		blockList.put((short)9, Arrays.asList("StationaryWater", "StaticWater", "StillWater"));
		blockList.put((short)10, Arrays.asList("Lava"));
		blockList.put((short)11, Arrays.asList("StationaryLava", "StaticLava", "StillLava"));
		blockList.put((short)12, Arrays.asList("Sand"));
		blockList.put((short)13, Arrays.asList("Gravel"));
		blockList.put((short)14, Arrays.asList("Gold", "GoldOre"));
		blockList.put((short)15, Arrays.asList("Iron", "IronOre"));
		blockList.put((short)16, Arrays.asList("Coal", "CoalOre"));
		blockList.put((short)17, Arrays.asList("Wood", "Trunk"));
		blockList.put((short)18, Arrays.asList("Leave", "Leaves"));
		blockList.put((short)19, Arrays.asList("Sponge"));
		blockList.put((short)20, Arrays.asList("Glass", "GlassBlock"));
		blockList.put((short)21, Arrays.asList("LapisOre", "LapisLazuliOre"));
		blockList.put((short)22, Arrays.asList("LapisBlock", "LapisLazuliBlock"));
		blockList.put((short)23, Arrays.asList("Dispenser", "ItemDispenser"));
		blockList.put((short)24, Arrays.asList("Sandstone"));
		blockList.put((short)25, Arrays.asList("NoteBlock", "Note"));
		blockList.put((short)26, Arrays.asList("Bed"));
		blockList.put((short)27, Arrays.asList("PoweredRail", "PowerRail"));
		blockList.put((short)28, Arrays.asList("DetectorRail", "PressureRail", "PressurePlateRail"));
		blockList.put((short)29, Arrays.asList("StickyPiston"));
		blockList.put((short)30, Arrays.asList("Cobweb", "Cobwebs", "Web"));
		blockList.put((short)31, Arrays.asList("TallGrass"));
		blockList.put((short)32, Arrays.asList("DeadBush", "DeadSapling"));
		blockList.put((short)33, Arrays.asList("Piston"));
		blockList.put((short)34, Arrays.asList("PistonExtension"));
		blockList.put((short)35, Arrays.asList("Wool"));
		blockList.put((short)36, Arrays.asList("BlockMovedByPiston"));
		blockList.put((short)37, Arrays.asList("Dandelion"));
		blockList.put((short)38, Arrays.asList("Rose"));
		blockList.put((short)39, Arrays.asList("BrownMushroom", "Mushroom"));
		blockList.put((short)40, Arrays.asList("RedMushroom"));
		blockList.put((short)41, Arrays.asList("GoldBlock", "BlockOfGold"));
		blockList.put((short)42, Arrays.asList("IronBlock", "BlockOfIron"));
		blockList.put((short)43, Arrays.asList("DoubleSlab"));
		blockList.put((short)44, Arrays.asList("Slab"));
		blockList.put((short)45, Arrays.asList("Brick", "Bricks"));
		blockList.put((short)46, Arrays.asList("TNT", "Dynamite", "Explosive"));
		blockList.put((short)47, Arrays.asList("Bookshelf"));
		blockList.put((short)48, Arrays.asList("MossStone", "MossyStone", "MossCobble", "MossyCobble", "MossCobbleStone", "MossyCobbleStone"));
		blockList.put((short)49, Arrays.asList("Obsidian"));
		blockList.put((short)50, Arrays.asList("Torch"));
		blockList.put((short)51, Arrays.asList("Fire"));
		blockList.put((short)52, Arrays.asList("Spawner", "MonsterSpawner", "MOBSpawner"));
		blockList.put((short)53, Arrays.asList("OakWoodStairs", "OakWoodStair", "WoodStairs", "WoodStair", "WoodenStairs", "WoodenStair"));
		blockList.put((short)54, Arrays.asList("Chest"));
		blockList.put((short)55, Arrays.asList("RedstoneWire", "Wire"));
		blockList.put((short)56, Arrays.asList("Diamond", "DiamondOre"));
		blockList.put((short)57, Arrays.asList("DiamondBlock", "BlockOfDiamond"));
		blockList.put((short)58, Arrays.asList("Crafting table", "CraftingTable", "CraftingBench"));
		blockList.put((short)59, Arrays.asList("WheatSeed", "WheatSeeds"));
		blockList.put((short)60, Arrays.asList("Farmland", "PlowedLand"));
		blockList.put((short)61, Arrays.asList("Furnace", "Oven"));
		blockList.put((short)62, Arrays.asList("BurningFurnace", "BurningOven"));
		blockList.put((short)63, Arrays.asList("Sign", "SignPost"));
		blockList.put((short)64, Arrays.asList("Door", "WoodenDoor", "WoodDoor"));
		blockList.put((short)65, Arrays.asList("Ladder"));
		blockList.put((short)66, Arrays.asList("Rail", "Track"));
		blockList.put((short)67, Arrays.asList("CobbleStoneStairs", "CobbleStoneStair", "CobbleStairs", "CobbleStair"));
		blockList.put((short)68, Arrays.asList("WallSign", "WallSignPost"));
		blockList.put((short)69, Arrays.asList("Lever", "Switch"));
		blockList.put((short)70, Arrays.asList("StonePressurePlate", "PressurePlate"));
		blockList.put((short)71, Arrays.asList("IronDoor"));
		blockList.put((short)72, Arrays.asList("WoodenPressurePlate", "WoodPressurePlate"));
		blockList.put((short)73, Arrays.asList("Redstone", "RedstoneOre"));
		blockList.put((short)74, Arrays.asList("GlowingRedstone", "GlowingRedstoneOre"));
		blockList.put((short)75, Arrays.asList("OffRedstoneTorch"));
		blockList.put((short)76, Arrays.asList("RedstoneTorch", "OnRedstoneTorch"));
		blockList.put((short)77, Arrays.asList("StoneButton"));
		blockList.put((short)78, Arrays.asList("SnowPatch", "FlatSnow"));
		blockList.put((short)79, Arrays.asList("Ice", "IceBlock"));
		blockList.put((short)80, Arrays.asList("Snow", "SnowBlock"));
		blockList.put((short)81, Arrays.asList("Cactus"));
		blockList.put((short)82, Arrays.asList("Clay", "ClayBlock"));
		blockList.put((short)83, Arrays.asList("SugarCane", "Sugar", "Reed", "Cane"));
		blockList.put((short)84, Arrays.asList("Jukebox"));
		blockList.put((short)85, Arrays.asList("Fence"));
		blockList.put((short)86, Arrays.asList("Pumpkin"));
		blockList.put((short)87, Arrays.asList("Netherrack", "NetherStone"));
		blockList.put((short)88, Arrays.asList("SoulSand", "SinkingSand"));
		blockList.put((short)89, Arrays.asList("Glowstone"));
		blockList.put((short)90, Arrays.asList("Portal", "NetherPortal"));
		blockList.put((short)91, Arrays.asList("JackOLantern", "Lantern", "PumpkinLantern"));
		blockList.put((short)92, Arrays.asList("Cake", "CakeBlock"));
		blockList.put((short)93, Arrays.asList("RedstoneRepeater", "OffRedstoneRepeater"));
		blockList.put((short)94, Arrays.asList("OnRedstoneRepeater"));
		blockList.put((short)95, Arrays.asList("LockedChest"));
		blockList.put((short)96, Arrays.asList("TrapDoor"));
		blockList.put((short)97, Arrays.asList("MonsterEgg", "SilverfishSpawner"));
		blockList.put((short)98, Arrays.asList("StoneBricks", "StoneBrick"));
		blockList.put((short)99, Arrays.asList("HugeBrownMushroom", "HugeMushroom"));
		blockList.put((short)100, Arrays.asList("HugeRedMushroom"));
		blockList.put((short)101, Arrays.asList("IronBars", "Bars"));
		blockList.put((short)102, Arrays.asList("GlassPane", "ThinGlass"));
		blockList.put((short)103, Arrays.asList("Melon"));
		blockList.put((short)104, Arrays.asList("PumpkinStem"));
		blockList.put((short)105, Arrays.asList("MelonStem"));
		blockList.put((short)106, Arrays.asList("Vines", "Vine"));
		blockList.put((short)107, Arrays.asList("FenceGate", "Gate"));
		blockList.put((short)108, Arrays.asList("BrickStairs", "BrickStair"));
		blockList.put((short)109, Arrays.asList("StoneBrickStairs", "StoneBrickStair"));
		blockList.put((short)110, Arrays.asList("Mycelium"));
		blockList.put((short)111, Arrays.asList("LilyPad"));
		blockList.put((short)112, Arrays.asList("NetherBricks", "NetherBrick"));
		blockList.put((short)113, Arrays.asList("NetherBrickFence"));
		blockList.put((short)114, Arrays.asList("NetherBrickStairs", "NetherBrickStair"));
		blockList.put((short)115, Arrays.asList("NetherWart"));
		blockList.put((short)116, Arrays.asList("EnchantingTable", "EnchantmentTable"));
		blockList.put((short)117, Arrays.asList("Brewing stand", "BrewingStand"));
		blockList.put((short)118, Arrays.asList("Cauldron"));
		blockList.put((short)119, Arrays.asList("EndPortal"));
		blockList.put((short)120, Arrays.asList("EndPortalFrame"));
		blockList.put((short)121, Arrays.asList("EndStone"));
		blockList.put((short)122, Arrays.asList("DragonEgg"));
		blockList.put((short)123, Arrays.asList("RedstoneLamp", "OffRedstoneLamp"));
		blockList.put((short)124, Arrays.asList("OnRedstoneLamp"));
		blockList.put((short)125, Arrays.asList("WoodenDoubleSlab"));
		blockList.put((short)126, Arrays.asList("WoodenSlab"));
		blockList.put((short)127, Arrays.asList("CocoaPlant"));
		blockList.put((short)128, Arrays.asList("SandstoneStairs", "SandstoneStair"));
		blockList.put((short)129, Arrays.asList("Emerald", "EmeraldOre"));
		blockList.put((short)130, Arrays.asList("EnderChest"));
		blockList.put((short)131, Arrays.asList("TripwireHook"));
		blockList.put((short)132, Arrays.asList("Tripwire"));
		blockList.put((short)133, Arrays.asList("EmeraldBlock", "BlockOfEmerald"));
		blockList.put((short)134, Arrays.asList("SpruceWoodStairs", "SpruceWoodStair"));
		blockList.put((short)135, Arrays.asList("BirchWoodStairs", "BirchWoodStair"));
		blockList.put((short)136, Arrays.asList("JungleWoodStairs", "JungleWoodStair"));
		blockList.put((short)137, Arrays.asList("CommandBlock", "CommandBeacon"));
		blockList.put((short)138, Arrays.asList("Beacon"));
		blockList.put((short)139, Arrays.asList("CobblestoneWall", "CobbleWall"));
		blockList.put((short)140, Arrays.asList("FlowerPot", "PlantPot", "Pot"));
		blockList.put((short)141, Arrays.asList("Carrots"));
		blockList.put((short)142, Arrays.asList("Potatoes", "Potatos"));
		blockList.put((short)143, Arrays.asList("WoodButton", "WoodenButton"));
		blockList.put((short)144, Arrays.asList("Head", "MobHead"));
		blockList.put((short)145, Arrays.asList("Anvil"));
		blockList.put((short)146, Arrays.asList("Trapped chest", "TrappedChest", "TrapChest", "ChestTrap", "RedstoneChest"));
		blockList.put((short)147, Arrays.asList("LightWeightedPressurePlate", "LightPressurePlate"));
		blockList.put((short)148, Arrays.asList("HeavyWeightedPressurePlate", "HeavyPressurePlate"));
		blockList.put((short)149, Arrays.asList("InactiveRedstoneComparator", "RedstoneComparator", "Comparator"));
		blockList.put((short)150, Arrays.asList("ActiveRedstoneComparator", "ActiveComparator"));
		blockList.put((short)151, Arrays.asList("DaylightSensor", "LightSensor", "SolarPanel"));
		blockList.put((short)152, Arrays.asList("BlockOfRedstone", "RedstoneBlock"));
		blockList.put((short)153, Arrays.asList("NetherQuartzOre", "QuartzOre", "NetherOre"));
		blockList.put((short)154, Arrays.asList("Hopper"));
		blockList.put((short)155, Arrays.asList("BlockofQuartz", "QuartzBlock"));
		blockList.put((short)156, Arrays.asList("QuartzStairs", "QuartzStair"));
		blockList.put((short)157, Arrays.asList("ActivatorRail"));
		blockList.put((short)158, Arrays.asList("Dropper"));
		//
		// Load Hanging Entity List
		//
		hangingEntityList.put((byte)9, "Painting");
		hangingEntityList.put((byte)18, "Item Frame");
		//
		// Scheduler
		//
		Scheduler scheduler = new Scheduler(this);
		scheduler.TipsAutosave();
		scheduler.UpdateDynamicSigns();
	}

	/**
	 * Called when a command is executed
	 */
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		// TODO: Make certain commands available to effect other players eg. Spawn creature at x players position
		// TODO: Add some honeypot stuff
		// TODO: Add protection for vehicle spam
		// TODO: Add mob disguise
		// TODO: Add skylands survival world
		// TODO: Add message to player showing unlocks on rankup
		// TODO: Improve help system
		// TODO: Add mail system
		// TODO: Add temporary bans
		// TODO: Add ban system with ban log and reason (Old ban system)
		// TODO: Add customization for block blacklist
		// TODO: Add questing system, mobs that talk and give quests, sendBlockChange to show route of quest by a line of wool, mabey an adventure questing world?
		// TODO: Improve book given on join
		// TODO: Add more sign commands
		// TODO: Add projectile protection
		// TODO: Improve speed of region checking (Per world region hashmaps?)	
		// TODO: Fix kick bug when the stair a player is sitting on is broken]
		//
		// Clean Database Command
		//
		if (command.getName().equalsIgnoreCase("cleandatabase")) {
			sender.sendMessage("§7Cleaning database");
			String[] logFiles = new File("plugins/EvilBook/Protection").list();
			long nowTime = new Date().getTime();
			for (int logNo = 0; logNo < logFiles.length; logNo++) {
				if (nowTime - new File("plugins/EvilBook/Protection/" + logFiles[logNo]).lastModified() > 1296000000L) new File("plugins/EvilBook/Protection/" + logFiles[logNo]).delete();
			}
			sender.sendMessage("§7Database cleaned");
		}
		//
		// Set All Command
		//
		if (command.getName().equalsIgnoreCase("setall")) {
			if (args.length != 2 || args[0] == null || args[1] == null) return true;
			for (Player p : getServer().getOnlinePlayers()) p.kickPlayer("§cUpdating player profiles, come back instantly");
			String[] playerFiles = new File("plugins/EvilBook/Players").list();
			Properties playerProp;
			for (int playerNo = 0; playerNo < playerFiles.length; playerNo++) {
				try {
					FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Players/" + playerFiles[playerNo]);
					playerProp = new Properties();
					playerProp.load(inputStream);
					inputStream.close();
					playerProp.setProperty(args[0], args[1]);
					FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Players/" + playerFiles[playerNo]);
					playerProp.store(outputStream, null);
					outputStream.close();
				} catch (Exception e) {
					logSevere("Failed to load player " + playerFiles[playerNo]);
					e.printStackTrace();
				}
			}
		}
		//
		// Slap Command
		//
		if (command.getName().equalsIgnoreCase("slap")) {
			if (args.length == 1) {
				if (getPlayer(args[0]) != null) {
					if (isInSurvival(getPlayer(args[0])) == false) {
						getPlayer(args[0]).setVelocity(new Vector(new Random().nextDouble() * 2.0D - 1.0D, new Random().nextDouble() * 1.0D, new Random().nextDouble() * 2.0D - 1.0D));
						getPlayer(args[0]).sendMessage((sender instanceof Player ? ((Player) sender).getDisplayName() : "§7The server") + " slapped you");
						sender.sendMessage("§7You slapped " + getPlayer(args[0]).getDisplayName());
					} else {
						sender.sendMessage("§7You can't slap players in survival");
					}
				} else {
					sender.sendMessage("§7You can't slap an offline player");
				}
			} else if (args.length == 0 && sender instanceof Player) {
				((Player)sender).setVelocity(new Vector(new Random().nextDouble() * 2.0D - 1.0D, new Random().nextDouble() * 1.0D, new Random().nextDouble() * 2.0D - 1.0D));
				sender.sendMessage("§7You slapped yourself");
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/slap <player>");
			}
			return true;
		}
		//
		// Shock Command
		//
		if (command.getName().equalsIgnoreCase("shock")) {
			if (args.length == 1) {
				if (getPlayer(args[0]) != null) {
					if (isInSurvival(getPlayer(args[0])) == false) {
						getPlayer(args[0]).getWorld().strikeLightning(getPlayer(args[0]).getLocation());
						getPlayer(args[0]).sendMessage((sender instanceof Player ? ((Player) sender).getDisplayName() : "§7The server") + " shocked you");
						sender.sendMessage("§7You shocked " + getPlayer(args[0]).getDisplayName());
					} else {
						sender.sendMessage("§7You can't shock players in survival");
					}
				} else {
					sender.sendMessage("§7You can't shock an offline player");
				}
			} else if (args.length == 0 && sender instanceof Player) {
				((Player)sender).getWorld().strikeLightning(((Player)sender).getLocation());
				sender.sendMessage("§7You shocked yourself");
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/shock <player>");
			}
			return true;
		}
		//
		// Rocket Command
		//
		if (command.getName().equalsIgnoreCase("rocket")) {
			if (args.length == 1) {
				if (getPlayer(args[0]) != null) {
					if (isInSurvival(getPlayer(args[0])) == false) {
						getPlayer(args[0]).setVelocity(new Vector(0, 20, 0));
						getPlayer(args[0]).sendMessage((sender instanceof Player ? ((Player) sender).getDisplayName() : "§7The server") + " rocketed you");
						sender.sendMessage("§7You rocketed " + getPlayer(args[0]).getDisplayName());
					} else {
						sender.sendMessage("§7You can't rocket players in survival");
					}
				} else {
					sender.sendMessage("§7You can't rocket an offline player");
				}
			} else if (args.length == 0 && sender instanceof Player) {
				((Player)sender).setVelocity(new Vector(0, 20, 0));
				sender.sendMessage("§7You rocketed yourself");
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/rocket <player>");
			}
			return true;
		}
		//
		// View Money Balance Command
		//
		if (command.getName().equalsIgnoreCase("money") || command.getName().equalsIgnoreCase("balance") || command.getName().equalsIgnoreCase("bal")) {
			if (args.length == 0 && sender instanceof Player) {
				sender.sendMessage("§5Your account balance is §d$" + getProfile(sender).money);
			} else if (args.length == 1) {
				if (isProfileExistant(args[0])) {
					if (getPlayer(args[0]) != null) {
						sender.sendMessage("§5" + getPlayer(args[0]).getDisplayName() + "'s account balance is §d$" + getProfile(args[0]).money);
					} else {
						sender.sendMessage("§5" + getServer().getOfflinePlayer(args[0]).getName() + "'s account balance is §d$" + getOfflineProperty(args[0], "Money"));
					}
				} else {
					sender.sendMessage("§7You can't view a player's balance who doesn't exist");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/bal <player>");
				sender.sendMessage("§d/money <player>");
				sender.sendMessage("§d/balance <player>");
			}
			return true;
		}
		//
		// Charge Player Money Command
		//
		if (command.getName().equalsIgnoreCase("charge")) {
			if (args.length == 2) {
				if (isInteger(args[1])) {
					if (isProfileExistant(args[0])) {
						if (getPlayer(args[0]) != null) {
							getPlayer(args[0]).sendMessage("§7You have been charged $" + args[1]);
							getProfile(args[0]).money -= Integer.valueOf(args[1]);
						} else {
							String money = getOfflineProperty(args[0], "Money");
							money = Integer.toString(Integer.valueOf(money) - Integer.valueOf(args[1]));
							setOfflineProperty(args[0], "Money", money);
						}
						sender.sendMessage("§7Money charged from " + getServer().getOfflinePlayer(args[0]).getName());
					} else {
						sender.sendMessage("§7You can't charge a player who doesn't exist");
					}
				} else {
					sender.sendMessage("§7Please enter a valid amount of money to charge");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/charge [player] [amount]");
			}
			return true;
		}
		//
		// Reward Player Money Command
		//
		if (command.getName().equalsIgnoreCase("reward")) {
			if (args.length == 2) {
				if (isInteger(args[1])) {
					if (isProfileExistant(args[0])) {
						if (getPlayer(args[0]) != null) {
							getPlayer(args[0]).sendMessage("§7You have been rewarded $" + args[1]);
							getProfile(args[0]).money += Integer.valueOf(args[1]);
						} else {
							String money = getOfflineProperty(args[0], "Money");
							money = Integer.toString(Integer.valueOf(money) + Integer.valueOf(args[1]));
							setOfflineProperty(args[0], "Money", money);
						}
						sender.sendMessage("§7Money rewarded to " + getServer().getOfflinePlayer(args[0]).getName());
					} else {
						sender.sendMessage("§7You can't reward a player who doesn't exist");
					}
				} else {
					sender.sendMessage("§7Please enter a valid amount of money to reward");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/reward [player] [amount]");
			}
			return true;
		}
		//
		// Reward Online Players Money Command
		//
		if (command.getName().equalsIgnoreCase("rewardall")) {
			if (args.length == 1) {
				if (isInteger(args[0])) {
					for (Player player : getServer().getOnlinePlayers()) {
						getProfile(player).money += Integer.valueOf(args[0]);
						player.sendMessage("§7You have been rewarded $" + args[0]);
					}
					sender.sendMessage("§7Money rewarded to all online players");
				} else {
					sender.sendMessage("§7Please enter a valid amount of money to reward");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/rewardall [amount]");
			}
			return true;
		}
		//
		// Kill Player Command
		//
		if (command.getName().equalsIgnoreCase("kill")) {
			if (args.length == 0) {
				((Player) sender).setHealth(0);
			} else {
				if (sender instanceof Player == false || getProfile(sender).rank.getID() >= Rank.Custom.getID()) {
					if (getPlayer(args[0]) != null) {
						getPlayer(args[0]).setHealth(0);
					} else {
						sender.sendMessage("§7You can't kill an offline player");
					}
				} else {
					sender.sendMessage("§7This command is blocked for security reasons");
				}
			}
			return true;
		}
		//
		// Make Admin Command
		//
		if (command.getName().equalsIgnoreCase("makeadmin")) {
			if (args.length == 1) {
				if (isProfileExistant(args[0])) {
					if (getPlayer(args[0]) != null) {
						getProfile(args[0]).rank = Rank.Admin;
						getPlayer(args[0]).setPlayerListName("§" + getProfile(args[0]).rank.getColor(getProfile(args[0])) + ((getProfile(args[0]).nameAlias == null || getProfile(args[0]).nameAlias.equals("null")) ? (getPlayer(args[0]).getName().length() > 14 ? getPlayer(args[0]).getName().substring(0, 14) : getPlayer(args[0]).getName()) : (getProfile(args[0]).nameAlias.length() > 14 ? getProfile(args[0]).nameAlias.substring(0, 14) : getProfile(args[0]).nameAlias)));
						getPlayer(args[0]).sendMessage("§7You have been promoted to admin");
					} else {
						setOfflineProperty(args[0], "Rank", "Admin");
					}
					sender.sendMessage("§7" + args[0] + " has been promoted to admin rank");
				} else {
					sender.sendMessage("§7You can't promote a player who doesn't exist");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/makeadmin [player]");
			}
			return true;
		}
		//
		// Stop Command
		//
		if (command.getName().equalsIgnoreCase("stop")) {
			String message = "§a";
			for (String msg : args) message += " " + msg;
			for (Player p : getServer().getOnlinePlayers()) {
				getProfile(p).saveProfile();
				p.kickPlayer(message.trim());
			}
			getServer().shutdown();
			return true;
		}
		//
		// Donate Command
		//
		if (command.getName().equalsIgnoreCase("donate") || command.getName().equalsIgnoreCase("admin")) {
			sender.sendMessage("§5How to purchase a rank or donate to the server");
			sender.sendMessage("§d1 - Goto our website at §6http://amentrix.no-ip.org");
			sender.sendMessage("§d2 - Click the §6Purchase A Rank §dbanner on the site");
			sender.sendMessage("§d3 - Select the Rank you wish to purchase");
			sender.sendMessage("§d4 - Transaction complete, enjoy the rank privileges rewarded!");
			sender.sendMessage("§7All ranks and their privileges are shown on the Amentrix website");
			sender.sendMessage("§7All purchased ranks allow the use of blocked items and admin content");
			return true;
		}
		//
		// Help Command
		//
		if (command.getName().equalsIgnoreCase("help")) {
			sender.sendMessage("§5For a list of all the commands and tutorials goto our website");
			sender.sendMessage("§dhttp://amentrix.no-ip.org");
			sender.sendMessage("§7You can request admin assistance using the §6/req §7command");
			return true;
		}
		//
		// List Command
		//
		if (command.getName().equalsIgnoreCase("list")) {
			sender.sendMessage("§6Online players §9[§6" + getServer().getOnlinePlayers().length + "/" + getServer().getMaxPlayers() + "§9]");
			for (Player p : getServer().getOnlinePlayers()) sender.sendMessage((getProfile(p.getName()).nameAlias == null || !getProfile(p.getName()).nameAlias.equals("null")) ? p.getDisplayName() + " §7(" + p.getName() + ")" : p.getDisplayName());
			return true;
		}
		//
		// Memstat Command
		//
		if (command.getName().equalsIgnoreCase("memstat")) {
			sender.sendMessage("§5Total Memory: §d" + Runtime.getRuntime().maxMemory() / 1048576 + "MB");
			int freePercentage = (int) Math.round((double)(Runtime.getRuntime().freeMemory()) / (double)(Runtime.getRuntime().maxMemory()) * 100);
			sender.sendMessage("§5Used Memory: §d" + ((Runtime.getRuntime().maxMemory() / 1048576) - (Runtime.getRuntime().freeMemory() / 1048576)) + "MB §e(" + (100 - freePercentage) + "%)");
			sender.sendMessage("§5Free Memory: §d" + Runtime.getRuntime().freeMemory() / 1048576 + "MB §e(" + freePercentage + "%)");
			alertOwner(sender.getName() + " executed the memstat command");
			return true;
		}
		//
		// Demote Command
		//
		if (command.getName().equalsIgnoreCase("demote")) {
			if (args.length == 1) {
				if (isProfileExistant(args[0])) {
					if (getPlayer(args[0]) != null) {
						getProfile(args[0]).rank = Rank.getPreviousRank(getProfile(args[0]).rank);
						getPlayer(args[0]).setPlayerListName("§" + getProfile(args[0]).rank.getColor(getProfile(args[0])) + ((getProfile(args[0]).nameAlias == null || getProfile(args[0]).nameAlias.equals("null")) ? (getPlayer(args[0]).getName().length() > 14 ? getPlayer(args[0]).getName().substring(0, 14) : getPlayer(args[0]).getName()) : (getProfile(args[0]).nameAlias.length() > 14 ? getProfile(args[0]).nameAlias.substring(0, 14) : getProfile(args[0]).nameAlias)));
						getPlayer(args[0]).sendMessage("§7You have been demoted to " + getProfile(args[0]).rank);
						sender.sendMessage("§7" + getPlayer(args[0]).getDisplayName() + " has been demoted to " + getProfile(args[0]).rank);
					} else {
						setOfflineProperty(args[0], "Rank", Rank.getPreviousRank(Rank.valueOf(getOfflineProperty(args[0], "Rank"))).toString());
						sender.sendMessage("§7" + getServer().getOfflinePlayer(args[0]).getName() + " has been demoted to " + Rank.valueOf(getOfflineProperty(args[0], "Rank")).getName());
					}
				} else {
					sender.sendMessage("§7You can't demote a player who doesn't exist");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/demote [player]");
			}
			return true;
		}
		//
		// Promote Command
		//
		if (command.getName().equalsIgnoreCase("promote")) {
			if (args.length == 1) {
				if (isProfileExistant(args[0])) {
					if (getPlayer(args[0]) != null) {
						if (Rank.getNextRank(getProfile(args[0]).rank).getID() > Rank.Moderator.getID() && sender instanceof Player && getProfile(sender).rank.getID() == Rank.Custom.getID()) {
							sender.sendMessage("§7You can't promote a player to above moderator");
							return true;
						}
						if (Rank.getNextRank(getProfile(args[0]).rank).getID() > Rank.Architect.getID() && sender instanceof Player && getProfile(sender).rank.getID() < Rank.Custom.getID()) {
							sender.sendMessage("§7You can't promote a player to above architect");
							return true;
						}
						getProfile(args[0]).rank = Rank.getNextRank(getProfile(args[0]).rank);
						getPlayer(args[0]).setPlayerListName("§" + getProfile(args[0]).rank.getColor(getProfile(args[0])) + ((getProfile(args[0]).nameAlias == null || getProfile(args[0]).nameAlias.equals("null")) ? (getPlayer(args[0]).getName().length() > 14 ? getPlayer(args[0]).getName().substring(0, 14) : getPlayer(args[0]).getName()) : (getProfile(args[0]).nameAlias.length() > 14 ? getProfile(args[0]).nameAlias.substring(0, 14) : getProfile(args[0]).nameAlias)));
						getPlayer(args[0]).sendMessage("§7You have been promoted to " + getProfile(args[0]).rank.getName());
						sender.sendMessage("§7" + getPlayer(args[0]).getDisplayName() + " has been promoted to " + getProfile(args[0]).rank.getName());
					} else {
						if (sender instanceof Player && getProfile(sender).rank != Rank.ServerOwner) {
							sender.sendMessage("§7You can't promote offline players");
							return true;
						}
						setOfflineProperty(args[0], "Rank", Rank.getNextRank(Rank.valueOf(getOfflineProperty(args[0], "Rank"))).toString());
						sender.sendMessage("§7" + getServer().getOfflinePlayer(args[0]).getName() + " has been promoted to " + Rank.valueOf(getOfflineProperty(args[0], "Rank")).getName());
					}
				} else {
					sender.sendMessage("§7You can't promoted a player who doesn't exist");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/promote [player]");
			}
			return true;
		}
		//
		// Message Command
		//
		if (command.getName().equalsIgnoreCase("msg")) {
			if (args.length > 1) {
				if (getPlayer(args[0]) != null) {
					String message = "";
					for (String msg : args) if (!(msg == args[0])) message += " " + msg;
					sender.sendMessage("§7To " + getPlayer(args[0]).getDisplayName() + "§7:§f" + message);
					if (sender instanceof Player) {
						getPlayer(args[0]).sendMessage("§7From " + ((Player) sender).getDisplayName() + ":§f" + message);
					} else {
						getPlayer(args[0]).sendMessage("§7From Server:§f" + message);
					}
				} else {
					sender.sendMessage("§7You can't message an offline player");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/msg [player] [message]");
			}
			return true;
		}
		//
		// Broadcast Command
		//
		if (command.getName().equalsIgnoreCase("broadcast") || command.getName().equalsIgnoreCase("say")) {
			if (args.length > 0) {
				String broadcast = "";
				for (String msg : args) broadcast += " " + msg;
				getServer().broadcastMessage("§d[§5" + (sender.getName().equals("CONSOLE") ? "Server" : sender.getName()) + "§d]" + toFormattedString(broadcast));
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/broadcast [message]");
				sender.sendMessage("§d/say [message]");
			}
			return true;
		}
		//
		// Rules Command
		//
		if (command.getName().equalsIgnoreCase("rules")) {
			sender.sendMessage("§a[§b1§a] §6Dont Greif! §7We have a logging system and rollback");
			sender.sendMessage("§a[§b2§a] §6Dont Advertise! §7Whats the point, use youtube ect.");
			sender.sendMessage("§a[§b3§a] §6Dont Spam! §7It's just annoying");
			sender.sendMessage("§7Breaking the rules may result in a ban");
			return true;
		}
		//
		// Ranks Command
		//
		if (command.getName().equalsIgnoreCase("ranks")) {
			if (args.length == 0 || args[0].equals("1")) {
				sender.sendMessage("§cRanks §f- §cPage 1 of 2 §f- §7/ranks <page>");
				sender.sendMessage("§0[§7Visitor§0] §7The default rank of a new player");
				sender.sendMessage("§0[§EBuilder§0] §7Show your creations to an Admin");
				sender.sendMessage("§0[§5Adv.Builder§0] §7Show more creations to an Admin");
				sender.sendMessage("§0[§DArchitect§0] §7Carry out tasks for an Admin");
				sender.sendMessage("§0[§9Moderator§0] §7Selected by the Owner's");
				sender.sendMessage("§0[§3Police§0] §7Selected by the Owner's");
				sender.sendMessage("§0[§4Admin§0] §7Purchased from the Amentrix website");
				sender.sendMessage("§7To purchase a rank please see /admin");
			} else if (args[0].equals("2")) {
				sender.sendMessage("§cRanks §f- §cPage 2 of 2 §f- §7/ranks <page>");
				sender.sendMessage("§0[§4SpecAdmin§0] §7Purchased from the Amentrix website");
				sender.sendMessage("§0[§ACounciller§0] §7Purchased from the Amentrix website");
				sender.sendMessage("§0[§CElite§0] §7Purchased from the Amentrix website");
				sender.sendMessage("§0[§6Co-Owner§0] §7Purchased from the Amentrix website");
				sender.sendMessage("§0[§BOwner§0] §7Purchased from the Amentrix website");
				sender.sendMessage("§0[§AO§Bw§Cn§De§Er§0] §7Purchased from the Amentrix website");
				sender.sendMessage("§0[§BServerHost§0] §7EvilPeanut");
				sender.sendMessage("§7To purchase a rank please see /admin");
			}
			return true;
		}
		//
		// Clean Command
		//
		if (command.getName().equalsIgnoreCase("clean")) {
			int entitiesRemoved = 0;
			for (World w : getServer().getWorlds()) {
				entitiesRemoved += w.getEntities().size();
				for (Entity entity : w.getEntities()) {
					if (!(entity instanceof Player) && !(entity instanceof Painting) && !(entity instanceof FallingBlock)
							&& (!(entity instanceof Item) || ((Item)entity).getItemStack().getTypeId() != 387) && !(entity instanceof EnderSignal) && (!(entity instanceof ExperienceOrb) || isInSurvival(entity) == false)
							&& !(entity instanceof Fish) && !(entity instanceof ItemFrame) && !(entity instanceof Snowball)
							&& !(entity instanceof Explosive) && !(entity instanceof Weather) && (!(entity instanceof Tameable) 
							|| ((Tameable)entity).isTamed() == false) && !(entity instanceof EnderPearl) && !(entity instanceof Egg)
							&& !(entity instanceof ThrownExpBottle) && !(entity instanceof ThrownPotion)) {
						entity.remove();
					} else {
						entitiesRemoved--;
					}
				}
			}
			sender.sendMessage("§dServer cleaned of §5" + entitiesRemoved + "§d entities");
			alertOwner(sender.getName() + " executed the clean command");
			return true;
		}
		//
		// View Colors Command
		//
		if (command.getName().equalsIgnoreCase("colors") || command.getName().equalsIgnoreCase("colours")) {
			if (args.length == 0 || args[0].equals("1")) {
				sender.sendMessage("§cColors §f- §cPage 1 of 2 §f- §7/colors <page>");
				sender.sendMessage("  §0#0 Black");
				sender.sendMessage("  §1#1 Dark Blue");
				sender.sendMessage("  §2#2 Dark Green");
				sender.sendMessage("  §3#3 Dark Aqua");
				sender.sendMessage("  §4#4 Dark Red");
				sender.sendMessage("  §5#5 Dark Purple");
				sender.sendMessage("  §6#6 Gold");
				sender.sendMessage("  §7#7 Gray");
				sender.sendMessage("  §8#8 Dark Grey");
			} else if (args[0].equals("2")) {
				sender.sendMessage("§cColors §f- §cPage 2 of 2 §f- §7/colors <page>");
				sender.sendMessage("  §9#9 Blue");
				sender.sendMessage("  §a#a Green");
				sender.sendMessage("  §b#b Aqua");
				sender.sendMessage("  §c#c Red");
				sender.sendMessage("  §d#d Light Purple");
				sender.sendMessage("  §e#e Yellow");
				sender.sendMessage("  §f#f White");
			}
			return true;
		}
		//
		// Kick Command
		//
		if (command.getName().equalsIgnoreCase("kick")) {
			if (args.length == 0) {
				sender.sendMessage("§7Please provide a player and optionally a reason for the kick");
				sender.sendMessage("§7/kick [player] <reason>");
			} else {
				if (getPlayer(args[0]) != null) {
					if (args.length == 1) {
						getPlayer(args[0]).kickPlayer("§cYou have been kicked from the server");
						alertOwner(sender.getName() + " kicked " + args[0]);
					} else {
						String message = "§c";
						for (String msg : args) if (msg != args[0]) message += msg + " ";
						getPlayer(args[0]).kickPlayer(message.trim());
						alertOwner(sender.getName() + " kicked " + args[0] + " with the message '" + message + "§7'");
					}
				} else {
					sender.sendMessage("§7You can't kick an offline player");
				}
			}
			return true;
		}
		//
		// Kick All Command
		//
		if (command.getName().equalsIgnoreCase("kickall")) {
			if (args.length >= 1) {
				String message = "§c";
				for (String msg : args) message += msg + " ";
				for (Player p : getServer().getOnlinePlayers()) p.kickPlayer(message.trim());
			} else {
				for (Player p : getServer().getOnlinePlayers()) p.kickPlayer("§cYou have been kicked from the server");
			}
			return true;
		}
//-------------------------------------------------------------------------------------------------------------------------------
// COMMANDS BELOW THIS POINT ARE NOT AVAILABLE VIA CONSOLE
//-------------------------------------------------------------------------------------------------------------------------------
		//
		// Check to see if the sender is a player
		//
		if (sender instanceof Player == false) {
			logInfo("This command is not supported by the console");
			return true;
		}
		//
		// Zombies Command
		//
		if (command.getName().equalsIgnoreCase("zombies")) {
			if (args.length == 1 && args[0].equalsIgnoreCase("NachtDerUntoten")) {
				((Player) sender).teleport(getServer().getWorld("NachtDerUntoten").getSpawnLocation());
				((Player) sender).setTexturePack("https://dl.dropboxusercontent.com/s/zspplijmzhdxkg0/Textures.zip");
				sender.sendMessage(ChatColor.RED + "Welcome to Nazi Zombies - Nacht Der Untoten");
			} else {
				sender.sendMessage("");
			}
			return true;
		}
		//
		// Set Rank Command
		//
		if (command.getName().equalsIgnoreCase("setrank")) {
			if (args.length == 1) {
				if (args[0].toLowerCase().contains("serverhost") || args[0].toLowerCase().contains("&k")) {
					sender.sendMessage("§7This rank name is blocked");
					return true;
				}
				try {
					FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Players/" + sender.getName() + ".properties");
					Properties playerProp = new Properties();
					playerProp.load(inputStream);
					inputStream.close();
					String prefix = args[0].startsWith("&") ? args[0] : "&6" + args[0];
					playerProp.setProperty("CustomRankPrefix", prefix);
					FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Players/" + sender.getName() + ".properties");
					playerProp.store(outputStream, null);
					outputStream.close();
					getProfile(sender).customRankColor = prefix.substring(1, 2);
					getProfile(sender).customRankPrefix = "§0[" + prefix.replaceAll("&", "§") + "§0]";
				} catch (Exception e) {
					logSevere("Failed to set rank in player profile 'plugins/EvilBook/Players/" + sender.getName() + ".properties'");
					e.printStackTrace();
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/setrank [rank]");
			}
			return true;
		}
		//
		// Adventure Command
		//
		if (command.getName().equalsIgnoreCase("adventure")) {
			((Player) sender).teleport(getServer().getWorld("AdventureLand").getSpawnLocation());
			sender.sendMessage("§7Welcome to the adventure land");
			return true;
		}
		//
		// Creative Command
		//
		if (command.getName().equalsIgnoreCase("creative")) {
			((Player)sender).teleport(getProfile(sender).creativeLocation == null ? getServer().getWorlds().get(0).getSpawnLocation() : getProfile(sender).creativeLocation);
			return true;
		}
		//
		// Survival Command
		//
		if (command.getName().equalsIgnoreCase("survival")) {
			((Player)sender).teleport(getProfile(sender).survivalLocation == null ? getServer().getWorld("SurvivalLand").getSpawnLocation() : getProfile(sender).survivalLocation);
			return true;
		}
		//
		// Set Jump Height Command
		//
		if (command.getName().equalsIgnoreCase("jump")) {
			if (args.length == 1) {
				if (isDouble(args[0]) && Double.valueOf(args[0]) <= 41) {
					getProfile(sender).jumpAmplifier = Double.valueOf(args[0]) / 4;
				} else {
					sender.sendMessage("§7Please enter a valid jump height");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/jump [height]");
			}
			return true;
		}
		//
		// Set Run Speed Command
		//
		if (command.getName().equalsIgnoreCase("run")) {
			if (args.length == 1) {
				if (isInteger(args[0]) && Integer.valueOf(args[0]) <= 127) {
					getProfile(sender).runAmplifier = Integer.valueOf(args[0]);
				} else {
					sender.sendMessage("§7Please enter a valid run speed");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/run [speed]");
			}
			return true;
		}
		//
		// Disguise Command
		//
		if (command.getName().equalsIgnoreCase("disguise")) {
			// TODO: Finish
		}
		//
		// Name Command
		//
		if (command.getName().equalsIgnoreCase("name")) {
			if (args.length >= 1) {
				if (((Player) sender).getItemInHand().getTypeId() != 0) {
					String name = "";
					for (String arg : args) name += arg + " ";
					if (((Player) sender).getItemInHand().getTypeId() == 397) {
						ItemStack skull;
						if (name.trim().equalsIgnoreCase("Creeper")) {
							skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.CREEPER.ordinal());
						} else if (name.trim().equalsIgnoreCase("Skeleton")) {
							skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
						} else if (name.trim().equalsIgnoreCase("Wither")) {
							skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.WITHER.ordinal());
						} else if (name.trim().equalsIgnoreCase("Zombie")) {
							skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.ZOMBIE.ordinal());
						} else {
							skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
						}
					    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
					    skullMeta.setOwner(name.trim());
					    skullMeta.setDisplayName(name.trim() + "'s Head");
					    skull.setItemMeta(skullMeta);
					    ((Player) sender).setItemInHand(skull);
					} else {
						ItemMeta meta = ((Player) sender).getItemInHand().getItemMeta();
						meta.setDisplayName(name.trim());
						((Player) sender).getItemInHand().setItemMeta(meta);
						sender.sendMessage("§7Item renamed to §d" + name.trim());
					}
				} else {
					sender.sendMessage("§7You must be holding an item to rename it");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/name [name]");
			}
			return true;
		}
		//
		// Advertise Command
		//
		if (command.getName().equalsIgnoreCase("advert") || command.getName().equalsIgnoreCase("advertise")) {
			if (args.length > 0) {
				if (getProfile(sender).money >= 40) {
					String broadcast = "";
					for (String msg : args) broadcast += " " + msg;
					getServer().broadcastMessage("§d[§5Advert§d]" + broadcast);
					alertOwner(sender.getName() + " executed the advertise command");
					getProfile(sender).money -= 40;
					sender.sendMessage("§7Created advert §c-$40");
				} else {
					sender.sendMessage("§5You don't have enough money for this item");
					sender.sendMessage("§dYou need to earn $" + (40 - getProfile(sender).money));
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/advert [message]");
				sender.sendMessage("§d/advertise [message]");
			}
			return true;
		}
		//
		// Rollback Command
		//
		if (command.getName().equalsIgnoreCase("rollback")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("EvilPeanut")) {
					sender.sendMessage("§7This user is protected from rollback");
				} else {
					rollbackEdits(((Player) sender), args[0]);
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/rollback [playerName]");
			}
			return true;
		}
		//
		// Log Command
		//
		if (command.getName().equalsIgnoreCase("log")) {
			if (getProfile(sender).isLogging) {
				getProfile(sender).isLogging = false;
				sender.sendMessage("§7Logging mode disabled");
			} else {
				getProfile(sender).isLogging = true;
				sender.sendMessage("§7Logging mode enabled");
			}
			return true;
		}
		//
		// Helmet command
		//
		if (command.getName().equalsIgnoreCase("helmet")) {
			if (isInSurvival(((Player)sender))) {
				sender.sendMessage("§7Custome helmets can't be used in survival");
				return true;
			}
			((Player) sender).getInventory().setHelmet(((Player) sender).getInventory().getItemInHand());
			sender.sendMessage("§7You have set your helmet to the block you are holding");
			return true;
		}
		//
		// Butter command
		//
		if (command.getName().equalsIgnoreCase("butter")) {
			if (isInSurvival(((Player)sender))) {
				sender.sendMessage("§7Butter is illegal in survival");
				return true;
			}
			ItemStack butter = new ItemStack(Material.GOLD_INGOT);
			ItemMeta yourItemStackMeta = butter.getItemMeta();
			yourItemStackMeta.setDisplayName("Butter");
			yourItemStackMeta.setLore(Arrays.asList("This butter was made by JacobClark"));
			butter.setItemMeta(yourItemStackMeta);
			butter.setAmount(64);
			((Player) sender).getInventory().addItem(butter);
			butter = new ItemStack(Material.GOLD_HELMET);
			yourItemStackMeta = butter.getItemMeta();
			yourItemStackMeta.setDisplayName("Butter Helmet");
			yourItemStackMeta.setLore(Arrays.asList("A helmet made by the butter gods"));
			butter.setItemMeta(yourItemStackMeta);
			((Player) sender).getInventory().setHelmet(butter);
			butter = new ItemStack(Material.GOLD_CHESTPLATE);
			yourItemStackMeta = butter.getItemMeta();
			yourItemStackMeta.setDisplayName("Butter Chestplate");
			yourItemStackMeta.setLore(Arrays.asList("A chestplate made by the butter gods"));
			butter.setItemMeta(yourItemStackMeta);
			((Player) sender).getInventory().setChestplate(butter);
			butter = new ItemStack(Material.GOLD_LEGGINGS);
			yourItemStackMeta = butter.getItemMeta();
			yourItemStackMeta.setDisplayName("Butter Leggings");
			yourItemStackMeta.setLore(Arrays.asList("Leggings made by the butter gods"));
			butter.setItemMeta(yourItemStackMeta);
			((Player) sender).getInventory().setLeggings(butter);
			butter = new ItemStack(Material.GOLD_BOOTS);
			yourItemStackMeta = butter.getItemMeta();
			yourItemStackMeta.setDisplayName("Butter Boots");
			yourItemStackMeta.setLore(Arrays.asList("Boots made by the butter gods"));
			butter.setItemMeta(yourItemStackMeta);
			((Player) sender).getInventory().setBoots(butter);
			sender.sendMessage("§7You have been blessed by the gods of butter");
			return true;
		}
		//
		// Region command
		//
		if (command.getName().equalsIgnoreCase("region")) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("scan")) {
					if (regionList.size() == 0) return true;
					Boolean isInRegion = false;
					for (int regionNo = 0; regionNo < regionList.size(); regionNo++) {
						if (isInRegionXRange(regionList.get(regionNo), ((Player)sender).getLocation())) {
							if (isInRegionYRange(regionList.get(regionNo), ((Player)sender).getLocation())) {
								if (isInRegionZRange(regionList.get(regionNo), ((Player)sender).getLocation())) {
									sender.sendMessage("§7You are in the region " + regionList.get(regionNo).regionName + " owned by " + regionList.get(regionNo).ownerName);
									isInRegion = true;
								} else {
									continue;
								}
							} else {
								continue;
							}
						} else {
							continue;
						}
					}
					if (isInRegion == false) sender.sendMessage("§7You aren't in any regions");
					return true;
				} else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {
					if (args.length == 2) {
						if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
							sender.sendMessage("§7Please select two locations using the golden shovel tool");
						} else {
							if (new File("plugins/EvilBook/Regions/" + args[1] + ".properties").exists()) {
								sender.sendMessage("§7A region with this name already exists");
								return true;
							}
							Region region = new Region(args[1],
									getProfile(sender).actionLocationA,
									getProfile(sender).actionLocationB,
									false,
									sender.getName(),
									null,
									null,
									null);
							regionList.add(region);
							try {
								Properties regionProp = new Properties();
								regionProp.setProperty("isProtected", "false");
								regionProp.setProperty("worldName", region.locationA.getWorld().getName());
								regionProp.setProperty("locationA", region.locationA.getBlockX() + "," + region.locationA.getBlockY() + "," + region.locationA.getBlockZ());
								regionProp.setProperty("locationB", region.locationB.getBlockX() + "," + region.locationB.getBlockY() + "," + region.locationB.getBlockZ());
								regionProp.setProperty("ownerName", region.ownerName);
								regionProp.setProperty("regionName", region.regionName);
								FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								regionProp.store(outputStream, null);
								outputStream.close();
							} catch (Exception e) {
								logSevere("Failed to create region " + args[1] + ".properties");
								e.printStackTrace();
							}
							sender.sendMessage("§7Region " + args[1] + " created");
						}
					} else {
						sender.sendMessage("§5Incorrect command usage");
						sender.sendMessage("§d/region add [regionName]");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("protect")) {
					if (args.length == 2) {
						if (new File("plugins/EvilBook/Regions/" + args[1] + ".properties").exists()) {
							for (Region region : regionList) if (region.regionName.equalsIgnoreCase(args[1])) {
								if (sender.getName() != region.ownerName && getProfile(sender).rank != Rank.ServerOwner) {
									sender.sendMessage("§7You don't own this region");
									return true;
								}
								region.isProtected = true;	
							}
							try {
								FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								Properties regionProp = new Properties();
								regionProp.load(inputStream);
								inputStream.close();
								regionProp.setProperty("isProtected", "true");
								FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								regionProp.store(outputStream, null);
								outputStream.close();
							} catch (Exception e) {
								logSevere("Failed to protect region " + args[1] + ".properties");
								e.printStackTrace();
							}
							sender.sendMessage("§7Region " + args[1] + " protected");
						} else {
							if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
								sender.sendMessage("§7Please select two locations using the golden shovel tool");
							} else {
								Region region = new Region(args[1],
										getProfile(sender).actionLocationA,
										getProfile(sender).actionLocationB,
										true,
										sender.getName(),
										null,
										null,
										null);
								regionList.add(region);
								try {
									Properties regionProp = new Properties();
									regionProp.setProperty("isProtected", "true");
									regionProp.setProperty("worldName", region.locationA.getWorld().getName());
									regionProp.setProperty("locationA", region.locationA.getBlockX() + "," + region.locationA.getBlockY() + "," + region.locationA.getBlockZ());
									regionProp.setProperty("locationB", region.locationB.getBlockX() + "," + region.locationB.getBlockY() + "," + region.locationB.getBlockZ());
									regionProp.setProperty("ownerName", region.ownerName);
									regionProp.setProperty("regionName", region.regionName);
									FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
									regionProp.store(outputStream, null);
									outputStream.close();
								} catch (Exception e) {
									logSevere("Failed to create region " + args[1] + ".properties");
									e.printStackTrace();
								}
								sender.sendMessage("§7Region " + args[1] + " created and protected");
							}
						}
					} else {
						sender.sendMessage("§5Incorrect command usage");
						sender.sendMessage("§d/region protect [regionName]");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("remove")) {
					if (args.length == 2) {
						for (int regionNo = 0; regionNo < regionList.size(); regionNo++) {
							if (regionList.get(regionNo).regionName.equalsIgnoreCase(args[1])) {
								if (sender.getName() != regionList.get(regionNo).ownerName && getProfile(sender).rank.getID() < Rank.Elite.getID()) {
									sender.sendMessage("§7You don't own this region");
									return true;
								}
								if (regionList.get(regionNo).ownerName.equalsIgnoreCase(sender.getName()) || getProfile(sender).rank.getID() >= Rank.RainbowOwner.getID()) {
									new File("plugins/EvilBook/Regions/" + args[1] + ".properties").delete();
									regionList.remove(regionNo);
									sender.sendMessage("§7Region " + args[1] + " removed");
									return true;
								} else {
									sender.sendMessage("§7You must own the region to remove it");
									return true;
								}
							}
						}
						sender.sendMessage("§7No regions exist with this name");
						return true;
					} else {
						sender.sendMessage("§5Incorrect command usage");
						sender.sendMessage("§d/region remove [regionName]");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("setwelcome")) {
					if (args.length > 2) {
						if (new File("plugins/EvilBook/Regions/" + args[1] + ".properties").exists()) {
							String message = "";
							for (String msg : args) if (msg != args[0] && msg != args[1]) message += msg + " ";
							for (Region region : regionList) if (region.regionName.equalsIgnoreCase(args[1])) {
								if (sender.getName() != region.ownerName && getProfile(sender).rank != Rank.ServerOwner) {
									sender.sendMessage("§7You don't own this region");
									return true;
								}
								region.welcomeMessage = message.trim();	
							}
							try {
								FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								Properties regionProp = new Properties();
								regionProp.load(inputStream);
								inputStream.close();
								regionProp.setProperty("welcomeMessage", message);
								FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								regionProp.store(outputStream, null);
								outputStream.close();
							} catch (Exception e) {
								logSevere("Failed to set region welcome " + args[1] + ".properties");
								e.printStackTrace();
							}
							sender.sendMessage("§7Region " + args[1] + " welcome message set");
						} else {
							sender.sendMessage("§7No regions exist with this name");
						}
					} else {
						sender.sendMessage("§5Incorrect command usage");
						sender.sendMessage("§d/region setWelcome [regionName] [welcomeMessage]");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("setleave")) {
					if (args.length > 2) {
						if (new File("plugins/EvilBook/Regions/" + args[1] + ".properties").exists()) {
							String message = "";
							for (String msg : args) if (msg != args[0] && msg != args[1]) message += msg + " ";
							for (Region region : regionList) if (region.regionName.equalsIgnoreCase(args[1])) {
								if (sender.getName() != region.ownerName && getProfile(sender).rank != Rank.ServerOwner) {
									sender.sendMessage("§7You don't own this region");
									return true;
								}
								region.leaveMessage = message.trim();	
							}
							try {
								FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								Properties regionProp = new Properties();
								regionProp.load(inputStream);
								inputStream.close();
								regionProp.setProperty("leaveMessage", message);
								FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								regionProp.store(outputStream, null);
								outputStream.close();
							} catch (Exception e) {
								logSevere("Failed to set region leave " + args[1] + ".properties");
								e.printStackTrace();
							}
							sender.sendMessage("§7Region " + args[1] + " leave message set");
						} else {
							sender.sendMessage("§7No regions exist with this name");
						}
					} else {
						sender.sendMessage("§5Incorrect command usage");
						sender.sendMessage("§d/region setLeave [regionName] [leaveMessage]");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("addplayer")) {
					// TODO: Add
					return true;
				} else if (args[0].equalsIgnoreCase("removeplayer")) {
					// TODO: Add
					return true;
				} else if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
					if (args.length == 2) {
						for (Region region : regionList) if (region.regionName.equalsIgnoreCase(args[1])) ((Player) sender).teleport(region.locationA);
					} else {
						sender.sendMessage("§d/region tp [regionName]");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("setWarp")) {
					if (args.length == 3) {
						if (new File("plugins/EvilBook/Regions/" + args[1] + ".properties").exists()) {
							for (Region region : regionList) if (region.regionName.equalsIgnoreCase(args[1])) {
								if (sender.getName() != region.ownerName && getProfile(sender).rank != Rank.ServerOwner) {
									sender.sendMessage("§7You don't own this region");
									return true;
								}
								region.warpName = args[2].toLowerCase();	
							}
							try {
								FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								Properties regionProp = new Properties();
								regionProp.load(inputStream);
								inputStream.close();
								regionProp.setProperty("warpName", args[2].toLowerCase());
								FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Regions/" + args[1] + ".properties");
								regionProp.store(outputStream, null);
								outputStream.close();
							} catch (Exception e) {
								logSevere("Failed to set region warp " + args[1] + ".properties");
								e.printStackTrace();
							}
							sender.sendMessage("§7Region " + args[1] + " warp set");
						} else {
							sender.sendMessage("§7No regions exist with this name");
						}
					} else {
						sender.sendMessage("§5Incorrect command usage");
						sender.sendMessage("§d/region setWarp [regionName] [warpName]");
					}
					return true;
				} else {
					sender.sendMessage("§5Incorrect command usage");
					sender.sendMessage("§d/region scan");
					sender.sendMessage("§d/region add [regionName]");
					sender.sendMessage("§d/region protect [regionName]");
					sender.sendMessage("§d/region remove [regionName]");
					sender.sendMessage("§d/region setWelcome [regionName] [welcomeMessage]");
					sender.sendMessage("§d/region setLeave [regionName] [leaveMessage]");
					sender.sendMessage("§d/region tp [regionName]");
					sender.sendMessage("§d/region setWarp [regionName] [warpName]");
					return true;
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/region scan");
				sender.sendMessage("§d/region add [regionName]");
				sender.sendMessage("§d/region protect [regionName]");
				sender.sendMessage("§d/region remove [regionName]");
				sender.sendMessage("§d/region setWelcome [regionName] [welcomeMessage]");
				sender.sendMessage("§d/region setLeave [regionName] [leaveMessage]");
				sender.sendMessage("§d/region tp [regionName]");
				sender.sendMessage("§d/region setWarp [regionName] [warpName]");
			}
			return true;
		}
		//
		// Admin assistance request command
		//
		if (command.getName().equalsIgnoreCase("req")) {
			if (args.length >= 1) {
				String message = "§c";
				for (String msg : args) message += msg + " ";
				Boolean adminOnline = false;
				for (Player p : getServer().getOnlinePlayers()) {
					if (getProfile(p).rank.getID() >= Rank.Admin.getID()) {
						p.sendMessage(sender.getName() + " requires admin assistance: " + message.trim());
						adminOnline = true;
					}
				}
				if (adminOnline == false) {
					sender.sendMessage("§7No administrators are online to recieve your request");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/req [message]");
			}
			return true;
		}
		//
		// View The Shop Catalogue
		//
		if (command.getName().equalsIgnoreCase("shop") || command.getName().equalsIgnoreCase("catalogue")) {
			((Player) sender).getInventory().addItem(getBook("Shop Catalogue", "Amentrix", Arrays.asList(
				"§cCatalogue §4- §cContents\n\n§dPage 2 to 5\n§5Name colors\n\n§dPage 6 to 8\n§5Name titles",
				"§cName colors §7$500\n\n§0Black\n§8/buy blackname\n\n§1Dark Blue\n§8/buy darkbluename\n\n§2Dark Green\n§8/buy darkgreenname\n\n§3Dark Aqua\n§8/buy darkaquaname",
				"§cName colors §7$500\n\n§4Dark Red\n§8/buy darkredname\n\n§5Dark Purple\n§8/buy darkpurplename\n\n§6Gold\n§8/buy goldname\n\n§7Grey\n§8/buy greyname",
				"§cName colors §7$500\n\n§8Dark Grey\n§8/buy darkgreyname\n\n§9Blue\n§8/buy bluename\n\n§aGreen\n§8/buy greenname\n\n§bAqua\n§8/buy aquaname",
				"§cName colors §7$500\n\n§cRed\n§8/buy redname\n\n§dPink\n§8/buy pinkname\n\n§eYellow\n§8/buy yellowname\n\n§aR§ba§ci§dn§eb§ao§bw\n§8/buy rainbowname",
				"§cName titles §7$500\n\n§dMr\n§8/buy mrtitle\n\n§dMrs\n§8/buy mrstitle\n\n§dMiss\n§8/buy misstitle\n\n§dLord\n§8/buy lordtitle",
				"§cName titles §7$500\n\n§dDr\n§8/buy drtitle\n\n§dProf\n§8/buy proftitle\n\n§dProf\n§8/buy proftitle\n\n§dMiner\n§8/buy minertitle",
				"§cName titles §7$500\n\n§dCrafter\n§8/buy craftertitle\n\n§dEpic\n§8/buy epictitle\n\n§dElf\n§8/buy elftitle\n\n§dCustom title §7$2000\n§8/buy customtitle [title]")));
			sender.sendMessage("§7A shop catalogue has been put in your inventory");
			return true;
		}
		//
		// Pay Command
		//
		if (command.getName().equalsIgnoreCase("pay") || command.getName().equalsIgnoreCase("givemoney")) {
			if (args.length == 2) {
				if (isProfileExistant(args[0])) {
					if (isInteger(args[1]) && Integer.valueOf(args[1]) > 0) {
						if (getProfile(sender).money >= Integer.valueOf(args[1])) {
							if (getPlayer(args[0]) != null) {
								getProfile(sender).money -= Integer.valueOf(args[1]);
								getProfile(args[0]).money += Integer.valueOf(args[1]);
								getPlayer(args[0]).sendMessage("§7You have recieved §a$" + args[1] + " §7from " + getPlayer(args[0]).getDisplayName());
								sender.sendMessage("§7You have paid " + getPlayer(args[0]).getDisplayName() + " §c$" + args[1]);
							} else {
								String money = getOfflineProperty(args[0], "Money");
								money = Integer.toString(Integer.valueOf(money) + Integer.valueOf(args[1]));
								setOfflineProperty(args[0], "Money", money);
								getProfile(sender).money -= Integer.valueOf(args[1]);
								sender.sendMessage("§7You have paid " + getServer().getOfflinePlayer(args[0]).getName() + " §c$" + args[1]);
							}
						} else {
							sender.sendMessage("§7You don't have enough money to do this");
						}
					} else {
						sender.sendMessage("§7This is an invalid amount of money to pay");
					}
				} else {
					sender.sendMessage("§7You can't pay a player who doesn't exist");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/pay [player] [amount]");
			}
			return true;
		}
		//
		// Gamemode Command
		//
		if (command.getName().equalsIgnoreCase("gamemode")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
					((Player) sender).setGameMode(GameMode.SURVIVAL);
					sender.sendMessage("§7You have changed to survival gamemode");
				} else if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")) {
					if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
						((Player) sender).setGameMode(GameMode.CREATIVE);
						sender.sendMessage("§7You have changed to creative gamemode");
					} else {
						sender.sendMessage("§7Creative gamemode can't be used in survival");
					}
				} else if (args[0].equalsIgnoreCase("adventure") || args[0].equals("2")) {
					((Player) sender).setGameMode(GameMode.ADVENTURE);
					sender.sendMessage("§7You have changed to adventure gamemode");
				} else {
					sender.sendMessage("§7This gamemode doesn't exist");
				}
			} else if (args.length == 2 && getProfile(sender).rank == Rank.ServerOwner) {
				if (getPlayer(args[1]) != null) {
					if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
						getPlayer(args[1]).setGameMode(GameMode.SURVIVAL);
						getPlayer(args[1]).sendMessage(((Player) sender).getDisplayName() + "§7 changed you to survival gamemode");
						sender.sendMessage("§7You have changed " + getPlayer(args[1]).getDisplayName() + "§7 to survival gamemode");
						alertOwner(sender.getName() + " changed " + getPlayer(args[1]).getName() + "'s gamemode to survival");
					} else if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")) {
						if (isInSurvival(getPlayer(args[1])) == false || getProfile(sender).rank == Rank.ServerOwner) {
							getPlayer(args[1]).setGameMode(GameMode.CREATIVE);
							getPlayer(args[1]).sendMessage(((Player) sender).getDisplayName() + "§7 changed you to creative gamemode");
							sender.sendMessage("§7You have changed " + getPlayer(args[1]).getDisplayName() + "§7 to creative gamemode");
							alertOwner(sender.getName() + " changed " + getPlayer(args[1]).getName() + "'s gamemode to creative");
						} else {
							sender.sendMessage("§7Creative gamemode can't be used on a player in survival");
						}
					} else if (args[0].equalsIgnoreCase("adventure") || args[0].equals("2")) {
						getPlayer(args[1]).setGameMode(GameMode.ADVENTURE);
						getPlayer(args[1]).sendMessage(((Player) sender).getDisplayName() + "§7 changed you to adventure gamemode");
						sender.sendMessage("§7You have changed " + getPlayer(args[1]).getDisplayName() + "§7 to adventure gamemode");
						alertOwner(sender.getName() + " changed " + getPlayer(args[1]).getName() + "'s gamemode to adventure");
					} else {
						sender.sendMessage("§7This gamemode doesn't exist");
					}
				} else {
					sender.sendMessage("§7You change the gamemode of an offline player");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/gamemode [mode]");
			}
			return true;
		}
		//
		// Generate Filled Sphere Command
		//
		if (command.getName().equalsIgnoreCase("sphere")) {		
			if (args.length != 2 && args.length != 3 && args.length != 5) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/sphere [blockID] [radius]");
				sender.sendMessage("§d/sphere [blockID] [blockData] [radius]");
				sender.sendMessage("§d/sphere [blockID] [blockData] [radiusX] [radiusY] [radiusZ]");
				return true;
			}	
			
			if (args.length == 2 && (!isInteger(args[0]) || !isInteger(args[1]) || Integer.valueOf(args[0]) >= blockList.size())) {
				sender.sendMessage("§7Please enter valid radius, block ID's and block data values");
				return true;
			} else if (args.length == 3 && (!isInteger(args[0]) || !isByte(args[1]) || !isInteger(args[2]) || Integer.valueOf(args[0]) >= blockList.size())) {
				sender.sendMessage("§7Please enter valid radius, block ID's and block data values");
				return true;
			} else if (args.length == 5 && (!isInteger(args[0]) || !isByte(args[1]) || !isInteger(args[2]) || !isInteger(args[3]) || !isInteger(args[4]) || Integer.valueOf(args[0]) >= blockList.size())) {
				sender.sendMessage("§7Please enter valid radius, block ID's and block data values");
				return true;
			}
			
			double radiusX = args.length == 2 ? Integer.valueOf(args[1]) + 0.5 : args.length == 3 ? Integer.valueOf(args[2]) + 0.5 : Integer.valueOf(args[2]) + 0.5;
			double radiusY = args.length == 2 ? Integer.valueOf(args[1]) + 0.5 : args.length == 3 ? Integer.valueOf(args[2]) + 0.5 : Integer.valueOf(args[3]) + 0.5;
			double radiusZ = args.length == 2 ? Integer.valueOf(args[1]) + 0.5 : args.length == 3 ? Integer.valueOf(args[2]) + 0.5 : Integer.valueOf(args[4]) + 0.5;
			
			if ((radiusX > 25 || radiusY > 25 || radiusZ > 25) && getProfile(sender).rank != Rank.ServerOwner) {
				sender.sendMessage("§7The maximum radius limit is 25");
				return true;
			}
			
	        alertOwner(sender.getName() + " created a sphere of " + args[0]);
	        
	        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
			
			int blockID = Integer.valueOf(args[0]);
			byte blockData = args.length == 2 ? 0 : Byte.valueOf(args[2]);

			final double invRadiusX = 1 / radiusX;
			final double invRadiusY = 1 / radiusY;
			final double invRadiusZ = 1 / radiusZ;

			final int ceilRadiusX = (int) Math.ceil(radiusX);
			final int ceilRadiusY = (int) Math.ceil(radiusY);
			final int ceilRadiusZ = (int) Math.ceil(radiusZ);

			double nextXn = 0;
			forX: for (int x = 0; x <= ceilRadiusX; ++x) {
				final double xn = nextXn;
				nextXn = (x + 1) * invRadiusX;
				double nextYn = 0;
				forY: for (int y = 0; y <= ceilRadiusY; ++y) {
					final double yn = nextYn;
					nextYn = (y + 1) * invRadiusY;
					double nextZn = 0;
					forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
						final double zn = nextZn;
						nextZn = (z + 1) * invRadiusZ;

						double distanceSq = lengthSq(xn, yn, zn);
						if (distanceSq > 1) {
							if (z == 0) {
								if (y == 0) {
									break forX;
								}
								break forY;
							}
							break forZ;
						}

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, y, z)).getData(), ((Player)sender).getLocation().add(x, y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, y, z)).getData(), ((Player)sender).getLocation().add(-x, y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, -y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, -y, z)).getData(), ((Player)sender).getLocation().add(x, -y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, -y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, y, -z)).getData(), ((Player)sender).getLocation().add(x, y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, y, -z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, -y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, -y, z)).getData(), ((Player)sender).getLocation().add(-x, -y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, -y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, -y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, -y, -z)).getData(), ((Player)sender).getLocation().add(x, -y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, -y, -z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, y, -z)).getData(), ((Player)sender).getLocation().add(-x, y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, y, -z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, -y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, -y, -z)).getData(), ((Player)sender).getLocation().add(-x, -y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, -y, -z), sender.getName()));
					}
				}
			}
			return true;
		}
		//
		// Generate Hollow Sphere Command
		//
		if (command.getName().equalsIgnoreCase("hsphere")) {		
			if (args.length != 2 && args.length != 3 && args.length != 5) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/hsphere [blockID] [radius]");
				sender.sendMessage("§d/hsphere [blockID] [blockData] [radius]");
				sender.sendMessage("§d/hsphere [blockID] [blockData] [radiusX] [radiusY] [radiusZ]");
				return true;
			}	
			
			if (args.length == 2 && (!isInteger(args[0]) || !isInteger(args[1]) || Integer.valueOf(args[0]) >= blockList.size())) {
				sender.sendMessage("§7Please enter valid radius, block ID's and block data values");
				return true;
			} else if (args.length == 3 && (!isInteger(args[0]) || !isByte(args[1]) || !isInteger(args[2]) || Integer.valueOf(args[0]) >= blockList.size())) {
				sender.sendMessage("§7Please enter valid radius, block ID's and block data values");
				return true;
			} else if (args.length == 5 && (!isInteger(args[0]) || !isByte(args[1]) || !isInteger(args[2]) || !isInteger(args[3]) || !isInteger(args[4]) || Integer.valueOf(args[0]) >= blockList.size())) {
				sender.sendMessage("§7Please enter valid radius, block ID's and block data values");
				return true;
			}
			
			double radiusX = args.length == 2 ? Integer.valueOf(args[1]) + 0.5 : args.length == 3 ? Integer.valueOf(args[2]) + 0.5 : Integer.valueOf(args[2]) + 0.5;
			double radiusY = args.length == 2 ? Integer.valueOf(args[1]) + 0.5 : args.length == 3 ? Integer.valueOf(args[2]) + 0.5 : Integer.valueOf(args[3]) + 0.5;
			double radiusZ = args.length == 2 ? Integer.valueOf(args[1]) + 0.5 : args.length == 3 ? Integer.valueOf(args[2]) + 0.5 : Integer.valueOf(args[4]) + 0.5;
			
			if ((radiusX > 25 || radiusY > 25 || radiusZ > 25) && getProfile(sender).rank != Rank.ServerOwner) {
				sender.sendMessage("§7The maximum radius limit is 25");
				return true;
			}
			
	        alertOwner(sender.getName() + " created a hollow sphere of " + args[0]);
	        
	        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
			
			int blockID = Integer.valueOf(args[0]);
			byte blockData = args.length == 2 ? 0 : Byte.valueOf(args[2]);

			final double invRadiusX = 1 / radiusX;
			final double invRadiusY = 1 / radiusY;
			final double invRadiusZ = 1 / radiusZ;

			final int ceilRadiusX = (int) Math.ceil(radiusX);
			final int ceilRadiusY = (int) Math.ceil(radiusY);
			final int ceilRadiusZ = (int) Math.ceil(radiusZ);

			double nextXn = 0;
			forX: for (int x = 0; x <= ceilRadiusX; ++x) {
				final double xn = nextXn;
				nextXn = (x + 1) * invRadiusX;
				double nextYn = 0;
				forY: for (int y = 0; y <= ceilRadiusY; ++y) {
					final double yn = nextYn;
					nextYn = (y + 1) * invRadiusY;
					double nextZn = 0;
					forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
						final double zn = nextZn;
						nextZn = (z + 1) * invRadiusZ;

						double distanceSq = lengthSq(xn, yn, zn);
						if (distanceSq > 1) {
							if (z == 0) {
								if (y == 0) {
									break forX;
								}
								break forY;
							}
							break forZ;
						}

						if (lengthSq(nextXn, yn, zn) <= 1 && lengthSq(xn, nextYn, zn) <= 1 && lengthSq(xn, yn, nextZn) <= 1) {
							continue;
						}

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, y, z)).getData(), ((Player)sender).getLocation().add(x, y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, y, z)).getData(), ((Player)sender).getLocation().add(-x, y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, -y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, -y, z)).getData(), ((Player)sender).getLocation().add(x, -y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, -y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, y, -z)).getData(), ((Player)sender).getLocation().add(x, y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, y, -z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, -y, z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, -y, z)).getData(), ((Player)sender).getLocation().add(-x, -y, z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, -y, z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(x, -y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(x, -y, -z)).getData(), ((Player)sender).getLocation().add(x, -y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(x, -y, -z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, y, -z)).getData(), ((Player)sender).getLocation().add(-x, y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, y, -z), sender.getName()));

						getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(((Player)sender).getLocation().add(-x, -y, -z)), ((Player)sender).getWorld().getBlockAt(((Player)sender).getLocation().add(-x, -y, -z)).getData(), ((Player)sender).getLocation().add(-x, -y, -z), sender.getName()));
						EvilEdit.add(new EvilEditBlock(blockID, blockData, ((Player)sender).getLocation().add(-x, -y, -z), sender.getName()));
					}
				}
			}
			return true;
		}
		//
		// Replace Command
		//
		if (command.getName().equalsIgnoreCase("replace")) {
			if (args.length != 2 && args.length != 4) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/replace [blockID] [blockID]");
				sender.sendMessage("§d/replace [blockID] [blockData] [blockID] [blockData]");
				return true;
			}
			if ((args.length == 2 && (!isInteger(args[0]) || !isInteger(args[1]))) || (args.length == 4 && (!isInteger(args[0]) || !isByte(args[1]) || !isInteger(args[2]) || !isByte(args[3])))) {
				sender.sendMessage("§7Please enter valid block ID's and block data values");
				return true;
			}
			if ((args.length == 2 && (Integer.valueOf(args[0]) >= blockList.size() || Integer.valueOf(args[1]) >= blockList.size() || Integer.valueOf(args[0]) < 0 || Integer.valueOf(args[1]) < 0)) || (args.length == 4 && (Integer.valueOf(args[0]) >= blockList.size() || Integer.valueOf(args[2]) >= blockList.size() || Integer.valueOf(args[0]) < 0 || Integer.valueOf(args[2]) < 0))) {
				sender.sendMessage("§7Please enter valid block ID's");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to replace");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only replace up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " replaced an area of " + args[0] + " blocks with " + args[1]);
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        int blockIDReplace = Integer.valueOf(args[0]);
		        int blockID = args.length == 2 ? Integer.valueOf(args[1]) : Integer.valueOf(args[2]);
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                {
		                	if (args.length == 4) {
			                	if ((((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, y, z).getData() != Byte.valueOf(args[1])) || ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != blockIDReplace) continue;
		                		getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                		EvilEdit.add(new EvilEditBlock(blockID, Byte.valueOf(args[3]), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	} else {
			                	if (((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID || ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != blockIDReplace) continue;
		                		getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                		EvilEdit.add(new EvilEditBlock(blockID, new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	}
		                }
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Fill Command
		//
		if (command.getName().equalsIgnoreCase("fill")) {
			if (args.length != 1 && args.length != 2) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/fill [blockID]");
				sender.sendMessage("§d/fill [blockID] [blockData]");
				return true;
			}
			if (!isInteger(args[0])) {
				sender.sendMessage("§7Please enter a valid block ID");
				return true;
			}
			if (args.length == 2 && !isByte(args[1])) {
				sender.sendMessage("§7Please enter a valid block data");
				return true;
			}
			if (Integer.valueOf(args[0]) >= blockList.size() || Integer.valueOf(args[0]) < 0) {
				sender.sendMessage("§7Please enter valid block ID's");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to fill");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only fill up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " filled an area of blocks with " + args[0]);
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        //((Player)sender).setScoreboard(evilEditStatsScoreboard);
		        //getProfile(sender).evilEditProgress = evilEditStatsScoreboard.getObjective("EvilEditStats").getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + sender.getName()));
		        //getProfile(sender).evilEditProgress.setScore(getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ));
		        int blockID = Integer.valueOf(args[0]);
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                {
		                	if ((args.length == 1 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, y, z).getData() == 0) || (args.length == 2 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, y, z).getData() == Byte.valueOf(args[1]))) continue;
		                	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	if (args.length == 2) {
		                		EvilEdit.add(new EvilEditBlock(blockID, Byte.valueOf(args[1]), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	} else {
		                		EvilEdit.add(new EvilEditBlock(blockID, new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	}
		                }
		            }
		        }
		        
		        //sender.sendMessage("Resetting score");
		        //evilEditStatsScoreboard.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + sender.getName()));
		       // for (Map.Entry<ScoreboardObjective, ScoreboardScore> e : savedScores.entrySet()) {
		        	//myBoard.board.getPlayerScoreForObjective(playerName, e.getKey()).setScore(e.getValue().getScore());
		       // }
		        
		       // evilEditStatsScoreboard.getObjective("EvilEditStats").getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + sender.getName())).r
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        	e.printStackTrace();
	        }
			return true;
		}
		//
		// Delete Command
		//
		if (command.getName().equalsIgnoreCase("del") || command.getName().equalsIgnoreCase("delete")) {
			if (args.length > 2) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/del");
				sender.sendMessage("§d/del [blockID]");
				sender.sendMessage("§d/del [blockID] [blockData]");
				return true;
			}
			if (args.length >= 1 && !isInteger(args[0])) {
				sender.sendMessage("§7Please enter a valid block ID to delete");
				return true;
			}
			if (args.length == 2 && !isByte(args[1])) {
				sender.sendMessage("§7Please enter a valid block data to delete");
				return true;
			}
			if (args.length >= 1 && (Integer.valueOf(args[0]) >= blockList.size() || Integer.valueOf(args[0]) < 0)) {
				sender.sendMessage("§7Please enter valid block ID's");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to delete");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only delete up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " deleted an area of blocks");
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                {
		                	if (((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == 0 || (args.length == 1 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != Integer.valueOf(args[0])) || (args.length == 2 && (((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != Integer.valueOf(args[0]) || ((Player)sender).getWorld().getBlockAt(x, y, z).getData() != Byte.valueOf(args[1])))) continue;
		                	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	EvilEdit.add(new EvilEditBlock(0, new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                }
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Copy Command
		//
		if (command.getName().equalsIgnoreCase("copy")) {
			if (args.length != 0) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/copy");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to copy");
				return true;
			}
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only copy up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " copied an area of blocks");
		        getProfile(sender).EvilEditCopy = new ArrayList<EvilEditBlock>();
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                { 
		                	getProfile(sender).EvilEditCopy.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), bottomBlockX - x, bottomBlockY - y, bottomBlockZ - z), sender.getName()));
		                }
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Paste Command
		//
		if (command.getName().equalsIgnoreCase("paste")) {
			if (isInSurvival((Player)sender)) {
				sender.sendMessage("§7Paste can't be used in survival");
				return true;
			}
			if (args.length != 0) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/paste");
				return true;
			}
			if (getProfile(sender).EvilEditCopy.size() == 0) {
				sender.sendMessage("§7Please copy blocks before pasting");
				return true;
			}
	        try {
				int topBlockX = getProfile(sender).EvilEditCopy.get(0).getLocation().getBlockX();
				int bottomBlockX = getProfile(sender).EvilEditCopy.get(0).getLocation().getBlockX();
		        int topBlockY = getProfile(sender).EvilEditCopy.get(0).getLocation().getBlockY();
		        int bottomBlockY = getProfile(sender).EvilEditCopy.get(0).getLocation().getBlockY();
		        int topBlockZ = getProfile(sender).EvilEditCopy.get(0).getLocation().getBlockZ();
		        int bottomBlockZ = getProfile(sender).EvilEditCopy.get(0).getLocation().getBlockZ();
		        for (EvilEditBlock block : getProfile(sender).EvilEditCopy) {
		        	if (block.getLocation().getBlockX() > topBlockX) topBlockX = block.getLocation().getBlockX();
		        	if (block.getLocation().getBlockX() < bottomBlockX) bottomBlockX = block.getLocation().getBlockX();
		        	if (block.getLocation().getBlockY() > topBlockY) topBlockY = block.getLocation().getBlockY();
		        	if (block.getLocation().getBlockY() < bottomBlockY) bottomBlockY = block.getLocation().getBlockY();
		        	if (block.getLocation().getBlockZ() > topBlockZ) topBlockZ = block.getLocation().getBlockZ();
		        	if (block.getLocation().getBlockZ() < bottomBlockZ) bottomBlockZ = block.getLocation().getBlockZ();
		        }
		        alertOwner(sender.getName() + " pasted an area of blocks");
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        for (int i = 0; i != getProfile(sender).EvilEditCopy.size(); i++) {
		        	Location loc = new Location(((Player)sender).getLocation().getWorld(), ((Player)sender).getLocation().getBlockX() - getProfile(sender).EvilEditCopy.get(i).getLocation().getBlockX(), ((Player)sender).getLocation().getBlockY() - getProfile(sender).EvilEditCopy.get(i).getLocation().getBlockY(), ((Player)sender).getLocation().getBlockZ() - getProfile(sender).EvilEditCopy.get(i).getLocation().getBlockZ());
		        	if (((Player)sender).getWorld().getBlockAt(loc).getTypeId() == getProfile(sender).EvilEditCopy.get(i).getTypeID() && ((Player)sender).getWorld().getBlockAt(loc).getData() == getProfile(sender).EvilEditCopy.get(i).getData()) continue;
		        	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(loc), ((Player)sender).getWorld().getBlockAt(loc).getData(), loc, sender.getName()));
                	EvilEdit.add(new EvilEditBlock(getProfile(sender).EvilEditCopy.get(i).getTypeID(), getProfile(sender).EvilEditCopy.get(i).getData(), loc, sender.getName()));
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Overlay Command
		//
		if (command.getName().equalsIgnoreCase("overlay")) {
			if (args.length != 1 && args.length != 2) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/overlay [blockID]");
				sender.sendMessage("§d/overlay [blockID] <blockData>");
				return true;
			}
			if (!isInteger(args[0])) {
				sender.sendMessage("§7Please enter a valid block ID");
				return true;
			}
			if (args.length == 2 && !isByte(args[1])) {
				sender.sendMessage("§7Please enter a valid block data");
				return true;
			}
			if (Integer.valueOf(args[0]) >= blockList.size() || Integer.valueOf(args[0]) < 0) {
				sender.sendMessage("§7Please enter valid block ID's");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to overlay");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only overlay up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " overlayed an area of blocks with " + args[0]);
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        int blockID = Integer.valueOf(args[0]);
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		            	int highestY = ((Player)sender).getWorld().getHighestBlockYAt(x, z);
		            	if ((args.length == 1 && ((Player)sender).getWorld().getBlockTypeIdAt(x, highestY, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, highestY, z).getData() == 0) || (args.length == 2 && ((Player)sender).getWorld().getBlockTypeIdAt(x, highestY, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, highestY, z).getData() == Byte.valueOf(args[1]))) continue;
		            	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, highestY, z), ((Player)sender).getWorld().getBlockAt(x, highestY, z).getData(), new Location(((Player)sender).getWorld(), x, highestY, z), sender.getName()));
		            	if (args.length == 2) {
		            		EvilEdit.add(new EvilEditBlock(blockID, Byte.valueOf(args[1]), new Location(((Player)sender).getWorld(), x, highestY, z), sender.getName()));
		            	} else {
		            		EvilEdit.add(new EvilEditBlock(blockID, new Location(((Player)sender).getWorld(), x, highestY, z), sender.getName()));
		            	}
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Walls Command
		//
		if (command.getName().equalsIgnoreCase("walls")) {
			if (args.length != 1 && args.length != 2) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/walls [blockID]");
				sender.sendMessage("§d/walls [blockID] <blockData>");
				return true;
			}
			if (!isInteger(args[0])) {
				sender.sendMessage("§7Please enter a valid block ID");
				return true;
			}
			if (args.length == 2 && !isByte(args[1])) {
				sender.sendMessage("§7Please enter a valid block data");
				return true;
			}
			if (Integer.valueOf(args[0]) >= blockList.size() || Integer.valueOf(args[0]) < 0) {
				sender.sendMessage("§7Please enter valid block ID's");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to wall");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only wall up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " walled an area of blocks with " + args[0]);
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        int blockID = Integer.valueOf(args[0]);
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                {
		                	if (x == bottomBlockX || x == topBlockX || z == bottomBlockZ || z == topBlockZ) {
			                	if ((args.length == 1 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, y, z).getData() == 0) || (args.length == 2 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, y, z).getData() == Byte.valueOf(args[1]))) continue;
			                	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
			                	if (args.length == 2) {
			                		EvilEdit.add(new EvilEditBlock(blockID, Byte.valueOf(args[1]), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
			                	} else {
			                		EvilEdit.add(new EvilEditBlock(blockID, new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
			                	}
		                	}
		                }
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Outline Command
		//
		if (command.getName().equalsIgnoreCase("outline")) {
			if (args.length != 1 && args.length != 2) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/outline [blockID]");
				sender.sendMessage("§d/outline [blockID] <blockData>");
				return true;
			}
			if (!isInteger(args[0])) {
				sender.sendMessage("§7Please enter a valid block ID");
				return true;
			}
			if (args.length == 2 && !isByte(args[1])) {
				sender.sendMessage("§7Please enter a valid block data");
				return true;
			}
			if (Integer.valueOf(args[0]) >= blockList.size() || Integer.valueOf(args[0]) < 0) {
				sender.sendMessage("§7Please enter valid block ID's");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to outline");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only outline up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " outlined an area of blocks with " + args[0]);
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        int blockID = Integer.valueOf(args[0]);
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                {
		                	if (x == bottomBlockX || x == topBlockX || z == bottomBlockZ || z == topBlockZ || y == bottomBlockY || y == topBlockY) {
			                	if ((args.length == 1 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, y, z).getData() == 0) || (args.length == 2 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == blockID && ((Player)sender).getWorld().getBlockAt(x, y, z).getData() == Byte.valueOf(args[1]))) continue;
			                	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
			                	if (args.length == 2) {
			                		EvilEdit.add(new EvilEditBlock(blockID, Byte.valueOf(args[1]), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
			                	} else {
			                		EvilEdit.add(new EvilEditBlock(blockID, new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
			                	}
		                	}
		                }
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Hollow Command
		//
		if (command.getName().equalsIgnoreCase("hollow")) {
			if (args.length != 0) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/hollow");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to hollow");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only hollow up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " hollowed an area of blocks");
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                {
		                	if (x != bottomBlockX && x != topBlockX && z != bottomBlockZ && z != topBlockZ && y != bottomBlockY && y != topBlockY) {
			                	if (((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) == 0) continue;
			                	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
			                	EvilEdit.add(new EvilEditBlock(0, new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	}
		                }
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Regen Command
		//
		if (command.getName().equalsIgnoreCase("regen")) {
			alertOwner(sender.getName() + " regenerated an area of blocks");
	        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
	        for (int x = 0; x <= 16; x++)
	        {
	            for (int z = 0; z <= 16; z++)
	            {
	                for (int y = 0; y <= 256; y++)
	                {
	                	int realX = ((((Player)sender).getLocation().getBlockX() / 16) * 16) + x;
	                	int realZ = ((((Player)sender).getLocation().getBlockZ() / 16) * 16) + z;
		                getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(realX, y, realZ), ((Player)sender).getWorld().getBlockAt(realX, y, realZ).getData(), new Location(((Player)sender).getWorld(), realX, y, realZ), sender.getName()));
	                }
	            }
	        }
	       if (((Player)sender).getWorld().regenerateChunk(((Player)sender).getLocation().getBlockX() / 16, ((Player)sender).getLocation().getBlockZ() / 16)) {
	    	   sender.sendMessage("§7Regenerated chunks");
	       } else {
	    	   sender.sendMessage("§7Failed to regenerate chunks");
	       }
	       return true;
		}
		//
		// Drain Command
		//
		if (command.getName().equalsIgnoreCase("drain")) {
			if (args.length != 0) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/drain");
				return true;
			}
			if (getProfile(sender).actionLocationA == null || getProfile(sender).actionLocationB == null) {
				sender.sendMessage("§7Please select two locations to drain");
				return true;
			}
	        if (EvilEdit.size() != 0) sender.sendMessage("§7Your edit will begin shortly, another action is in progress");
	        try {
				int topBlockX = (getProfile(sender).actionLocationA.getBlockX() < getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int bottomBlockX = (getProfile(sender).actionLocationA.getBlockX() > getProfile(sender).actionLocationB.getBlockX() ? getProfile(sender).actionLocationB.getBlockX() : getProfile(sender).actionLocationA.getBlockX());
		        int topBlockY = (getProfile(sender).actionLocationA.getBlockY() < getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int bottomBlockY = (getProfile(sender).actionLocationA.getBlockY() > getProfile(sender).actionLocationB.getBlockY() ? getProfile(sender).actionLocationB.getBlockY() : getProfile(sender).actionLocationA.getBlockY());
		        int topBlockZ = (getProfile(sender).actionLocationA.getBlockZ() < getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        int bottomBlockZ = (getProfile(sender).actionLocationA.getBlockZ() > getProfile(sender).actionLocationB.getBlockZ() ? getProfile(sender).actionLocationB.getBlockZ() : getProfile(sender).actionLocationA.getBlockZ());
		        if (getNumberOfBlocksInArea(topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ) > getProfile(sender).rank.getEvilEditAreaLimit() && getProfile(sender).rank != Rank.ServerOwner) {
		        	sender.sendMessage("§7You can only drain up to an area of " + getProfile(sender).rank.getEvilEditAreaLimit() + " blocks");
		        	sender.sendMessage("§7Rank-up to lift this limit");
					return true;
		        }
		        alertOwner(sender.getName() + " drained an area of blocks");
		        getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
		        for (int x = bottomBlockX; x <= topBlockX; x++)
		        {
		            for (int z = bottomBlockZ; z <= topBlockZ; z++)
		            {
		                for (int y = bottomBlockY; y <= topBlockY; y++)
		                {
		                	if (((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != 8 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != 9 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != 10 && ((Player)sender).getWorld().getBlockTypeIdAt(x, y, z) != 11) continue;
		                	getProfile(sender).EvilEditUndo.add(new EvilEditBlock(((Player)sender).getWorld().getBlockTypeIdAt(x, y, z), ((Player)sender).getWorld().getBlockAt(x, y, z).getData(), new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                	EvilEdit.add(new EvilEditBlock(0, new Location(((Player)sender).getWorld(), x, y, z), sender.getName()));
		                }
		            }
		        }
	        } catch (Exception e) {
	        	sender.sendMessage("§7Evil Edit action failed");
	        }
			return true;
		}
		//
		// Undo Command
		//
		if (command.getName().equalsIgnoreCase("undo")) {
			if (getProfile(sender).EvilEditUndo.size() == 0) {
				sender.sendMessage("§7There are no edits to undo");
			} else {
				// TODO: Make this faster :P
				EvilEdit.addAll(getProfile(sender).EvilEditUndo);
				getProfile(sender).EvilEditUndo = new ArrayList<EvilEditBlock>();
			}
			return true;
		}
		//
		// Debug Command
		//
		if (command.getName().equalsIgnoreCase("debug")) {
			//long startTime = System.nanoTime();
			// Code
			//long testA = System.nanoTime() - startTime;
			//long startTime2 = System.nanoTime();
			// Code
			//long testB = System.nanoTime() - startTime2;
			//sender.sendMessage("TestA = " + (testA / 100000));
			//sender.sendMessage("TestB = " + (testB / 100000));
			//EntityHuman v;
			//((Player) sender).getWorld().spawnEntity(((Player) sender).getLocation(), EntityType.fromId(49));
			/*
			WorldServer world = ((CraftWorld)((Player) sender).getWorld()).getHandle();
			EntityPlayer playerEntity = new EntityPlayer(((CraftServer) getServer()).getServer(), world, args[0], new PlayerInteractManager(world));
			playerEntity.spawnIn(world);
			playerEntity.setPosition(((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ());
			world.addEntity(playerEntity);
			*/
			return true;
		}
		//
		// Rename Command
		//
		if (command.getName().equalsIgnoreCase("rename")) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("clear")) {
					getProfile(sender).setNameAlias(null, ((Player) sender));
					sender.sendMessage("§7You have removed your name alias");
					alertOwner(sender.getName() + " removed their name alias");
				} else {
					String name = "";
					for (String arg : args) name += arg + " ";
					if (name.trim().length() <= 14) {
						String[] playerFiles = new File("plugins/EvilBook/Players").list();
						for (int playerNo = 0; playerNo < playerFiles.length; playerNo++) {
							if (name.trim().equalsIgnoreCase(playerFiles[playerNo].substring(0, playerFiles[playerNo].lastIndexOf('.'))) && playerFiles[playerNo].substring(0, playerFiles[playerNo].lastIndexOf('.')).equalsIgnoreCase(sender.getName()) == false) {
								sender.sendMessage("§7The name '§d" + name.trim() + "§7' is in use");
								return true;
							}
						}
						getProfile(sender).setNameAlias(name.trim(), ((Player) sender));
						sender.sendMessage("§7You have renamed yourself to §d" + name.trim());
						alertOwner(sender.getName() + " renamed themselves to " + name.trim());
					} else {
						sender.sendMessage("§7The maximum rename length is 14 characters");
					}
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/rename [name]");
			}
			return true;
		}
		//
		// Mute Command
		//
		if (command.getName().equalsIgnoreCase("mute")) {
			if (args.length == 1) {
				getProfile(sender).mutedPlayers += args[0].toLowerCase();
				sender.sendMessage("§7You have muted " + args[0]);
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/mute [player]");
			}
			return true;
		}
		//
		// Un-mute Command
		//
		if (command.getName().equalsIgnoreCase("unmute")) {
			if (args.length == 1) {
				getProfile(sender).mutedPlayers = getProfile(sender).mutedPlayers.replaceAll(args[0].toLowerCase(), "");
				sender.sendMessage("§7You have unmuted " + args[0]);
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/unmute [player]");
			}
			return true;
		}
		//
		// Vanish Command
		//
		if (command.getName().equalsIgnoreCase("vanish") || command.getName().equalsIgnoreCase("hide")) {
			if (isInSurvival((Player)sender) && getProfile(sender).rank != Rank.ServerOwner) {
				sender.sendMessage("§7Vanish can't be used in survival");
				return true;
			}
			for (Player other : getServer().getOnlinePlayers()) other.hidePlayer((Player) sender);
			sender.sendMessage("§7You are invisible");
			alertOwner(sender.getName() + " made themselves invisible");
			return true;
		}
		//
		// Unvanish Command
		//
		if (command.getName().equalsIgnoreCase("unvanish") || command.getName().equalsIgnoreCase("show")) {
			for (Player other : getServer().getOnlinePlayers()) other.showPlayer((Player) sender);
			sender.sendMessage("§7You are visible");
			return true;
		}
		//
		// Set Home Command
		//
		if (command.getName().equalsIgnoreCase("sethome")) {
			getProfile(sender).homeLocation = ((Player) sender).getLocation();
			sender.sendMessage("§7Home set");
			return true;
		}
		//
		// Teleport To Home Command
		//
		if (command.getName().equalsIgnoreCase("home")) {
			if (getProfile(sender).homeLocation != null) {
				((Player) sender).teleport(getProfile(sender).homeLocation);
				sender.sendMessage("§7Welcome home");
			} else {
				sender.sendMessage("§7No home set");
			}
			return true;
		}
		//
		// Teleport To SpaceLand Command
		//
		if (command.getName().equalsIgnoreCase("spaceland") || command.getName().equalsIgnoreCase("space")) {
			((Player) sender).teleport(getServer().getWorld("Spaceland").getSpawnLocation());
			return true;
		}
		//
		// Teleport To EvilLand Command
		//
		if (command.getName().equalsIgnoreCase("evilland")) {
			((Player) sender).teleport(getServer().getWorld("EvilLand").getSpawnLocation());
			return true;
		}
		//
		// Teleport To FlatLand Command
		//
		if (command.getName().equalsIgnoreCase("flatland")) {
			((Player) sender).teleport(getServer().getWorld("Flatland").getSpawnLocation());
			return true;
		}
		//
		// Teleport To SkyLand Command
		//
		if (command.getName().equalsIgnoreCase("skyland") || command.getName().equalsIgnoreCase("sky")) {
			((Player) sender).teleport(getServer().getWorld("Skyland").getSpawnLocation());
			return true;
		}
		//
		// Spawn Creature Command
		//
		if (command.getName().equalsIgnoreCase("spawncreature") || command.getName().equalsIgnoreCase("cspawn")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				if (args.length >= 1) {
					EntityType entityType = getEntity(args[0]);
					if (entityType != null) {
						if ((entityType != EntityType.ENDER_DRAGON && entityType != EntityType.WITHER && entityType != EntityType.ENDER_CRYSTAL) || getProfile(sender).rank == Rank.ServerOwner) {
							if (args.length == 1) {
								if (((Player) sender).getNearbyEntities(64, 64, 64).size() + 1 >= 400 && getProfile(sender).rank != Rank.ServerOwner) {
									sender.sendMessage("§7Nearby entity limit reached");
								} else {
									((Player) sender).getWorld().spawnEntity(((Player) sender).getLocation(), entityType);
									sender.sendMessage("§7Spawned a " + args[0].toLowerCase());
									alertOwner(sender.getName() + " spawned a " + args[0].toLowerCase());
								}
							} else if (args.length == 2) {
								if (isInteger(args[1])) {
									if (((Player) sender).getNearbyEntities(64, 64, 64).size() + Integer.valueOf(args[1]) >= 400 && getProfile(sender).rank != Rank.ServerOwner) {
										sender.sendMessage("§7Nearby entity limit reached");
									} else {
										int amount = Integer.valueOf(args[1]);
										for (int i = 0; i < amount; i++) ((Player) sender).getWorld().spawnEntity(((Player) sender).getLocation(), entityType);
										sender.sendMessage("§7Spawned " + args[1] + " " + args[0].toLowerCase() + "'s");
										alertOwner(sender.getName() + " spawned " + args[1] + " " + args[0].toLowerCase() + "'s");
									}
								} else {
									sender.sendMessage("§7Please enter a valid number of creatures to spawn");
								}
							}
						} else {
							sender.sendMessage("§7This creature is banned");
						}
					} else {
						sender.sendMessage("§7This creature doesn't exist");
					}
				} else {
					sender.sendMessage("§5Incorrect command usage");
					sender.sendMessage("§d/cspawn [mob] <amount>");
					sender.sendMessage("§d/spawncreature [mob] <amount>");
				}
			} else {
				sender.sendMessage("§7Creatures can't be spawned in survival");
			}
			return true;
		}
		//
		// Warp Command
		//
		if (command.getName().equalsIgnoreCase("warp")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					if (getProfile(sender).warps != null) {
						sender.sendMessage("§5My warps");
						sender.sendMessage("§d" + getProfile(sender).warps);
					} else {
						sender.sendMessage("§7You don't own any warps");
					}
				} else {
					if (warpList.containsKey(args[0].toLowerCase())) {
						((Player) sender).teleport(warpList.get(args[0].toLowerCase()));
						sender.sendMessage("§7Teleported to warp");
					} else {
						sender.sendMessage("§7A warp with that name doesn't exist");
					}
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/warp [warpName]");
			}
			return true;
		}
		//
		// Set Warp Command
		//
		if (command.getName().equalsIgnoreCase("setwarp") || command.getName().equalsIgnoreCase("createwarp")) {
			if (args.length == 1) {
				if (getProfile(sender).money >= 20 || getProfile(sender).rank.getID() >= Rank.Admin.getID()) {
					if (args[0].length() <= 16) {
						try {
							Properties warpFile = new Properties();
							File file = new File("plugins/EvilBook/Warps.db");
							FileInputStream inputStream = new FileInputStream(file);
							warpFile.load(inputStream);
							inputStream.close();
							if (warpFile.getProperty(args[0].toLowerCase()) == null) {
								Location loc = ((Player) sender).getLocation();
								warpFile.setProperty(args[0].toLowerCase(), loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch());
								FileOutputStream outputStream = new FileOutputStream(file);
								warpFile.store(outputStream, null);
								outputStream.close();
								warpList.put(args[0].toLowerCase(), loc);
								if (getProfile(sender).rank.getID() >= Rank.Admin.getID()) {
									sender.sendMessage("§7Created warp §d" + args[0]);
								} else {
									getProfile(sender).money -= 20;
									sender.sendMessage("§7Created warp §d" + args[0] + " §c-$20");
								}
								if (getProfile(sender).warps != null) {
									getProfile(sender).warps += ", " + args[0];
								} else {
									getProfile(sender).warps = args[0];
								}
							} else {
								sender.sendMessage("§7A warp named §d" + args[0] + " §7already exists");
							}
						} catch (Exception e) {
							sender.sendMessage("§7Warp creation failed");
							alertOwner(sender.getName() + " encountered an error whilst trying to set a warp");
							e.printStackTrace();
						}
					} else {
						sender.sendMessage("§7The maximum warp name length is 16 characters");
					}
				} else {
					sender.sendMessage("§5You don't have enough money for this item");
					sender.sendMessage("§dYou need to earn $" + (20 - getProfile(sender).money));
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/setwarp [warpName]");
			}
			return true;
		}
		//
		// Enchant Command
		//
		if (command.getName().equalsIgnoreCase("enchant")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				if (args.length == 2) {
					if (isInteger(args[1])) {
						try {
							((Player) sender).getItemInHand().addEnchantment(getEnchantment(args[0].toUpperCase()), Integer.valueOf(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.ARROW_DAMAGE) sender.sendMessage("§7Item enchanted with the power of damage " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.ARROW_FIRE) sender.sendMessage("§7Item enchanted with flame");
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.ARROW_INFINITE) sender.sendMessage("§7Item enchanted with infinite arrows");
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.ARROW_KNOCKBACK) sender.sendMessage("§7Item enchanted with knockback " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.DAMAGE_ALL) sender.sendMessage("§7Item enchanted with sharpness " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.DAMAGE_ARTHROPODS) sender.sendMessage("§7Item enchanted with bane of arthropods " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.DAMAGE_UNDEAD) sender.sendMessage("§7Item enchanted with smite " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.DIG_SPEED) sender.sendMessage("§7Item enchanted with efficiency " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.DURABILITY) sender.sendMessage("§7Item enchanted with unbreaking " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.FIRE_ASPECT) sender.sendMessage("§7Item enchanted with fire aspect " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.KNOCKBACK) sender.sendMessage("§7Item enchanted with knockback " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.LOOT_BONUS_BLOCKS) sender.sendMessage("§7Item enchanted with fortune " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.LOOT_BONUS_MOBS) sender.sendMessage("§7Item enchanted with looting " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.OXYGEN) sender.sendMessage("§7Item enchanted with respiration " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.PROTECTION_ENVIRONMENTAL) sender.sendMessage("§7Item enchanted with protection " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.PROTECTION_EXPLOSIONS) sender.sendMessage("§7Item enchanted with explosive protection " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.PROTECTION_FALL) sender.sendMessage("§7Item enchanted with fall protection " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.PROTECTION_FIRE) sender.sendMessage("§7Item enchanted with fire protection " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.PROTECTION_PROJECTILE) sender.sendMessage("§7Item enchanted with projectile protection " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.SILK_TOUCH) sender.sendMessage("§7Item enchanted with silk touch");
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.THORNS) sender.sendMessage("§7Item enchanted with thorns " + toRomanNumerals(args[1]));
							if (getEnchantment(args[0].toUpperCase()) == Enchantment.WATER_WORKER) sender.sendMessage("§7Item enchanted with aqua affinity");
						} catch (Exception e) {
							if (getEnchantment(args[0].toUpperCase()) == null) {
								sender.sendMessage("§7This enchantment doesn't exist");
							} else if (Integer.valueOf(args[1]) > getEnchantment(args[0].toUpperCase()).getMaxLevel()) {
								sender.sendMessage("§7The maximum level for this enchantment is " + getEnchantment(args[0].toUpperCase()).getMaxLevel());
							} else {
								sender.sendMessage("§7This item can't have this enchantment");
							}
						}
					} else {
						sender.sendMessage("§7Please enter a valid enchantment level");
					}
				} else {
					sender.sendMessage("§5Incorrect command usage");
					sender.sendMessage("§d/enchant [enchantmentID] [enchantmentLevel]");
				}
			} else {
				sender.sendMessage("§7Items can't be enchanted in survival via command");
			}
			return true;
		}
		//
		// Back Command
		//
		if (command.getName().equalsIgnoreCase("back")) {
			if (getProfile(sender).deathLocation != null) {
				((Player) sender).teleport(getProfile(sender).deathLocation);
				sender.sendMessage("§7Teleported to your death position");
			} else {
				sender.sendMessage("§7You haven't died recently");
			}
			return true;
		}
		//
		// Storm Command
		//
		if (command.getName().equalsIgnoreCase("storm")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				((Player) sender).getWorld().setThundering(true);
				((Player) sender).getWorld().setStorm(true);
				sender.sendMessage("§7World weather changed to stormy");
			} else {
				sender.sendMessage("§7Weather can't be changed in survival");
			}
			return true;
		}
		//
		// Rain Command
		//
		if (command.getName().equalsIgnoreCase("rain")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				((Player) sender).getWorld().setStorm(true);
				sender.sendMessage("§7World weather changed to rainy");
			} else {
				sender.sendMessage("§7Weather can't be changed in survival");
			}
			return true;
		}
		//
		// Sun Command
		//
		if (command.getName().equalsIgnoreCase("sun")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				((Player) sender).getWorld().setThundering(false);
				((Player) sender).getWorld().setStorm(false);
				sender.sendMessage("§7World weather changed to sunny");
			} else {
				sender.sendMessage("§7Weather can't be changed in survival");
			}
			return true;
		}
		//
		// AFK Command
		//
		if (command.getName().equalsIgnoreCase("afk")) {
			getServer().broadcastMessage("§5" + ((Player) sender).getDisplayName() + " §dhas gone AFK");
			return true;
		}
		//
		// Time Command
		//
		if (command.getName().equalsIgnoreCase("time")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				if (args.length == 1) {
					if (isInteger(args[0])) {
						((Player) sender).getWorld().setTime(Long.valueOf(args[0]));
					} else if (args[0].equalsIgnoreCase("dawn")) {
						((Player) sender).getWorld().setTime(0L);
					} else if (args[0].equalsIgnoreCase("day")) {
						((Player) sender).getWorld().setTime(6000L);
					} else if (args[0].equalsIgnoreCase("dusk")) {
						((Player) sender).getWorld().setTime(12000L);
					} else if (args[0].equalsIgnoreCase("night")) {
						((Player) sender).getWorld().setTime(18000L);
					} else {
						sender.sendMessage("§7This is not a valid time");
						return true;
					}
					sender.sendMessage("§7World time changed");
				} else {
					sender.sendMessage("§5Incorrect command usage");
					sender.sendMessage("§d/time [time]");
					sender.sendMessage("§d/time dawn");
					sender.sendMessage("§d/time day");
					sender.sendMessage("§d/time dusk");
					sender.sendMessage("§d/time night");
				}
			} else {
				sender.sendMessage("§7Time can't be changed in survival");
			}
			return true;
		}
		//
		// Set Spawn Command
		//
		if (command.getName().equalsIgnoreCase("setspawn")) {
			Player player = (Player) sender;
			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			sender.sendMessage("§7The world spawn has been moved");
			return true;
		}
		//
		// Spawn Command
		//
		if (command.getName().equalsIgnoreCase("spawn")) {
			((Player) sender).teleport(((Player) sender).getWorld().getSpawnLocation());
			sender.sendMessage("§7Teleported to the spawn");
			return true;
		}
		//
		// Me Command
		//
		if (command.getName().equalsIgnoreCase("me")) {
			String me = "";
			for (String msg : args) me += " " + msg;
			getServer().broadcastMessage("* " + ((Player) sender).getDisplayName() + me);
			return true;
		}
		//
		// Butcher Command
		//
		if (command.getName().equalsIgnoreCase("butcher")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				int entities = ((Player) sender).getWorld().getLivingEntities().size();
				for (LivingEntity entity : ((Player) sender).getWorld().getLivingEntities()) 
					if (!(entity instanceof Player)) entity.remove(); else entities--;
				sender.sendMessage("§5" + entities + "§d Animals butchered");
				alertOwner(sender.getName() + " executed the butcher command");
			} else {
				sender.sendMessage("§7Animals can't be butchered in survival");
			}
			return true;
		}
		//
		// Tree Spawn Command
		//
		if (command.getName().equalsIgnoreCase("tree")) {
			Player player = (Player) sender;
			if (isInSurvival(player) == false || getProfile(sender).rank == Rank.ServerOwner) {
				if (args.length == 0) {
					player.getWorld().generateTree(player.getLocation(), TreeType.TREE);
					alertOwner(sender.getName() + " spawned a tree");
				} else {
					if (args[0].equalsIgnoreCase("Tree") || args[0].equalsIgnoreCase("Redwood") || args[0].equalsIgnoreCase("Birch") || args[0].equalsIgnoreCase("Jungle")) {
						player.getWorld().generateTree(player.getLocation(), TreeType.valueOf(args[0].toUpperCase()));
						alertOwner(sender.getName() + " spawned a tree");
					} else {
						sender.sendMessage("§7Please Specify A Type of Tree From The Following");
						sender.sendMessage("§7Default /tree");
						sender.sendMessage("§7RedWood /tree redwood");
						sender.sendMessage("§7Birch   /tree birch");
						sender.sendMessage("§7Jungle  /tree jungle");
					}
				}
			} else {
				sender.sendMessage("§7Trees can't be spawned in survival");
			}
			return true;
		}
		//
		// Hug Command
		//
		if (command.getName().equalsIgnoreCase("hug")) {
			if (args.length >= 1) {
				getServer().broadcastMessage(((Player) sender).getDisplayName() + " hugs " + args[0]);
			} else {
				getServer().broadcastMessage(((Player) sender).getDisplayName() + " hugs themselves");
			}
			return true;
		}
		//
		// Clear Inventory Command
		//
		if (command.getName().equalsIgnoreCase("clear")) {
			((Player) sender).getInventory().clear();
			sender.sendMessage("§7Your inventory has been cleared");
			return true;
		}
		//
		// Dawn Time Command
		//
		if (command.getName().equalsIgnoreCase("dawn")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				((Player) sender).getWorld().setTime(0L);
				sender.sendMessage("§7World time changed to dawn");
			} else {
				sender.sendMessage("§7Time can't be changed in survival");
			}
			return true;
		}
		//
		// Day Time Command
		//
		if (command.getName().equalsIgnoreCase("day")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				((Player) sender).getWorld().setTime(6000L);
				sender.sendMessage("§7World time changed to day");
			} else {
				sender.sendMessage("§7Time can't be changed in survival");
			}
			return true;
		}
		//
		// Dusk Time Command
		//
		if (command.getName().equalsIgnoreCase("dusk")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				((Player) sender).getWorld().setTime(12000L);
				sender.sendMessage("§7World time changed to dusk");
			} else {
				sender.sendMessage("§7Time can't be changed in survival");
			}
			return true;
		}
		//
		// Night Time Command
		//
		if (command.getName().equalsIgnoreCase("night")) {
			if (isInSurvival(((Player) sender)) == false || getProfile(sender).rank == Rank.ServerOwner) {
				((Player) sender).getWorld().setTime(18000L);
				sender.sendMessage("§7World time changed to night");
			} else {
				sender.sendMessage("§7Time can't be changed in survival");
			}
			return true;
		}
		//
		// Accept Teleport Request Command
		//
		if (command.getName().equalsIgnoreCase("accept")) {
			if (getProfile(sender).teleportantName != null) {
				if (getPlayer(getProfile(sender).teleportantName) != null) {
					getPlayer(getProfile(sender).teleportantName).teleport((Player)sender);
					getPlayer(getProfile(sender).teleportantName).sendMessage("§7Teleport request accepted");
				} else {
					sender.sendMessage("§7The player who sent the request isn't online");
				}
			}
			return true;
		}
		//
		// Teleport To Player Command
		//
		if (command.getName().equalsIgnoreCase("tp") || command.getName().equalsIgnoreCase("teleport")) {
			if (args.length == 1) {
				if (getPlayer(args[0]) != null) {
					if (!isInSurvival(getPlayer(args[0])) || getProfile(sender).rank == Rank.ServerOwner) {
						if (getProfile(getPlayer(args[0])).rank.getID() >= Rank.Admin.getID() && getProfile(sender).rank.getID() < Rank.Custom.getID()) {
							getProfile(getPlayer(args[0])).teleportantName = sender.getName();
							getPlayer(args[0]).sendMessage("§d" + ((Player) sender).getDisplayName() + " §dwishes to teleport to you");
							getPlayer(args[0]).sendMessage("§aTo accept the request type /accept");
							sender.sendMessage("§7A teleport request has been sent");
						} else {
							((Player) sender).teleport(getPlayer(args[0]));
						}
					} else {
						sender.sendMessage("§7You can't teleport to a player in survival");
					}
				} else {
					sender.sendMessage("§7You can't teleport to an offline player");
				}
			} else if (args.length == 2 && getProfile(sender).rank.getID() >= Rank.CoOwner.getID()) {
				if (getPlayer(args[0]) != null && getPlayer(args[1]) != null) {
					if ((!isInSurvival(getPlayer(args[0])) && !isInSurvival(getPlayer(args[1]))) || getProfile(sender).rank == Rank.ServerOwner) {
						getPlayer(args[0]).teleport(getPlayer(args[1]));
					} else {
						sender.sendMessage("§7You can't teleport a player in survival");
					}
				} else {
					sender.sendMessage("§7You can't teleport an offline player");
				}
			} else if (args.length == 3) {
				if (!isInSurvival((Player) sender) || getProfile(sender).rank == Rank.ServerOwner) {
					if (isDouble(args[0]) && isDouble(args[1]) && isDouble(args[2])) {
						Location destination = ((Player) sender).getLocation();
						destination.setX(Double.valueOf(args[0]));
						destination.setY(Double.valueOf(args[1]));
						destination.setZ(Double.valueOf(args[2]));
						((Player) sender).teleport(destination);
						sender.sendMessage("§aTeleported to co-ordinates");
						sender.sendMessage("§dX: " + args[0]);
						sender.sendMessage("§dY: " + args[1]);
						sender.sendMessage("§dZ: " + args[2]);
					} else {
						sender.sendMessage("§5Incorrect command usage");
						sender.sendMessage("§d/tp [x] [y] [z]");
						sender.sendMessage("§7The X, Y and Z values must be numbers");
					}
				} else {
					sender.sendMessage("§7You can't teleport to co-ordinates in survival");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/tp [player]");
				sender.sendMessage("§d/tp [x] [y] [z]");
				sender.sendMessage("§6Co-Owner §d/tp [player] <toPlayer>");
			}
			return true;
		}
		//
		// Teleport Player Here Command
		//
		if (command.getName().equalsIgnoreCase("tphere") || command.getName().equalsIgnoreCase("teleporthere")) {
			if (args.length == 1) {
				if (getPlayer(args[0]) != null) {
					if (!isInSurvival(((Player) sender)) || getProfile(sender).rank == Rank.ServerOwner) {
						getPlayer(args[0]).teleport(((Player) sender));
					} else {
						sender.sendMessage("§7You can't teleport a player in survival");
					}
				} else {
					sender.sendMessage("§7You can't teleport an offline player");
				}
			} else {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/tphere [player]");
			}
			return true;
		}
		//
		// Buy Shop Item Command
		//
		if (command.getName().equalsIgnoreCase("buy")) {
			if (args.length == 1) {
				if (args[0].endsWith("name") && isOp(sender)) {
					if (getProfile(sender).money >= 500) {
						getProfile(sender).money -= 500;
					} else {
						sender.sendMessage("§5You don't have enough money for this item");
						sender.sendMessage("§dYou need to earn $" + (500 - getProfile(sender).money));
						return true;
					}
					if (args[0].equalsIgnoreCase("blackname")) getProfile(sender).setNameColor("0", ((Player) sender));
					if (args[0].equalsIgnoreCase("darkbluename")) getProfile(sender).setNameColor("1", ((Player) sender));
					if (args[0].equalsIgnoreCase("darkgreenname")) getProfile(sender).setNameColor("2", ((Player) sender));
					if (args[0].equalsIgnoreCase("darkaquaname")) getProfile(sender).setNameColor("3", ((Player) sender));
					if (args[0].equalsIgnoreCase("darkredname")) getProfile(sender).setNameColor("4", ((Player) sender));
					if (args[0].equalsIgnoreCase("darkpurplename")) getProfile(sender).setNameColor("5", ((Player) sender));
					if (args[0].equalsIgnoreCase("goldname")) getProfile(sender).setNameColor("6", ((Player) sender));
					if (args[0].equalsIgnoreCase("greyname")) getProfile(sender).setNameColor("7", ((Player) sender));
					if (args[0].equalsIgnoreCase("darkgreyname")) getProfile(sender).setNameColor("8", ((Player) sender));
					if (args[0].equalsIgnoreCase("bluename")) getProfile(sender).setNameColor("9", ((Player) sender));
					if (args[0].equalsIgnoreCase("greenname")) getProfile(sender).setNameColor("a", ((Player) sender));
					if (args[0].equalsIgnoreCase("aquaname")) getProfile(sender).setNameColor("b", ((Player) sender));
					if (args[0].equalsIgnoreCase("redname")) getProfile(sender).setNameColor("c", ((Player) sender));
					if (args[0].equalsIgnoreCase("pinkname")) getProfile(sender).setNameColor("d", ((Player) sender));
					if (args[0].equalsIgnoreCase("yellowname")) getProfile(sender).setNameColor("e", ((Player) sender));
					if (args[0].equalsIgnoreCase("rainbowname")) getProfile(sender).setNameColor("?", ((Player) sender));
					sender.sendMessage("§7Purchase complete");
					return true;
				} else if (args[0].endsWith("title") && isOp(sender)) {
					if (getProfile(sender).money >= 500) {
						getProfile(sender).money -= 500;
					} else {
						sender.sendMessage("§5You don't have enough money for this item");
						sender.sendMessage("§dYou need to earn $" + (500 - getProfile(sender).money));
						return true;
					}
					if (args[0].equalsIgnoreCase("elftitle")) getProfile(sender).setNameTitle("Elf", ((Player) sender));
					if (args[0].equalsIgnoreCase("mrtitle")) getProfile(sender).setNameTitle("Mr", ((Player) sender));
					if (args[0].equalsIgnoreCase("mrstitle")) getProfile(sender).setNameTitle("Mrs", ((Player) sender));
					if (args[0].equalsIgnoreCase("misstitle")) getProfile(sender).setNameTitle("Miss", ((Player) sender));
					if (args[0].equalsIgnoreCase("lordtitle")) getProfile(sender).setNameTitle("Lord", ((Player) sender));
					if (args[0].equalsIgnoreCase("drtitle")) getProfile(sender).setNameTitle("Dr", ((Player) sender));
					if (args[0].equalsIgnoreCase("proftitle")) getProfile(sender).setNameTitle("Prof", ((Player) sender));
					if (args[0].equalsIgnoreCase("minertitle")) getProfile(sender).setNameTitle("Miner", ((Player) sender));
					if (args[0].equalsIgnoreCase("craftertitle")) getProfile(sender).setNameTitle("Crafter", ((Player) sender));
					if (args[0].equalsIgnoreCase("epictitle")) getProfile(sender).setNameTitle("Epic", ((Player) sender));
					sender.sendMessage("§7Purchase complete");
					return true;
				} else if (isOp(sender) == false) {
					sender.sendMessage("§dThis is an §5Admin §donly item");
					sender.sendMessage("§dPlease type §6/admin §dto learn how to become admin");
					return true;
				}
				sender.sendMessage("§7That item doesn't exist");
				return true;
			} else {
				if (args.length == 2 && args[0].equalsIgnoreCase("customtitle") && isOp(sender)) {
					if (getProfile(sender).money >= 2000) {
						getProfile(sender).money -= 2000;
					} else {
						sender.sendMessage("§5You don't have enough money for this item");
						sender.sendMessage("§dYou need to earn $" + (2000 - getProfile(sender).money));
						return true;
					}
					getProfile(sender).setNameTitle(args[1], ((Player) sender));
					sender.sendMessage("§7Purchase complete");
				} else {
					sender.sendMessage("§5Incorrect command usage");
					sender.sendMessage("§d/buy [item]");
				}
				return true;
			}
		}
		//
		// Give Block Command
		//
		if (command.getName().equalsIgnoreCase("item") || command.getName().equalsIgnoreCase("give")) {
			if (isInSurvival(((Player) sender).getWorld().getName()) && getProfile(sender).rank != Rank.ServerOwner) {
				sender.sendMessage("§7Spawning of items is blocked in survival");
				return true;
			}
			if (args.length == 0 || args.length >= 4) {
				sender.sendMessage("§5Incorrect command usage");
				sender.sendMessage("§d/item [ID] <amount> <data>");
				sender.sendMessage("§d/give [ID] <amount> <data>");
				sender.sendMessage("§d/item [name] <amount> <data>");
				sender.sendMessage("§d/give [name] <amount> <data>");
				return true;
			}
			try {
				int itemID = Integer.valueOf(args[0]);
				ItemStack spawnItem = new ItemStack(itemID);
				if (args.length == 2) {
					try {
						spawnItem.setAmount(Integer.valueOf(args[1]));
					} catch (Exception e) {
						sender.sendMessage("§7Please enter a valid number of items to spawn");
						return true;
					}
				} else if (args.length == 3) {
					try {
						spawnItem.setAmount(Integer.valueOf(args[1]));
					} catch (Exception e) {
						sender.sendMessage("§7Please enter a valid number of items to spawn");
						return true;
					}
					try {
						spawnItem.setData(new MaterialData(itemID, Byte.valueOf(args[2])));
					} catch (Exception e) {
						sender.sendMessage("§7Please enter a valid data number");
						return true;
					}
				}
				((Player)sender).getInventory().addItem(spawnItem);
			} catch (Exception e) {
				for (List<String> item : blockList.values()) {
					for (String subItem : item) {
						if (args[0].equalsIgnoreCase(subItem)) {
							for (int i = 0; i < blockList.values().toArray().length; i++) {
								if (blockList.get(i) == item) {
									ItemStack spawnItem = new ItemStack(i);
									if (args.length == 2) {
										try {
											spawnItem.setAmount(Integer.valueOf(args[1]));
										} catch (Exception ex) {
											sender.sendMessage("§7Please enter a valid number of items to spawn");
											return true;
										}
									} else if (args.length == 3) {
										try {
											spawnItem.setAmount(Integer.valueOf(args[1]));
										} catch (Exception ex) {
											sender.sendMessage("§7Please enter a valid number of items to spawn");
											return true;
										}
										try {
											spawnItem.setData(new MaterialData(i, Byte.valueOf(args[2])));
										} catch (Exception ex) {
											sender.sendMessage("§7Please enter a valid data number");
											return true;
										}
									}
									((Player)sender).getInventory().addItem(spawnItem);
									return true;
								}
							}
						}
					}
				}
				sender.sendMessage("§7Please enter a valid block name");
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Log information to the minecraft server console
	 * @param information The information to log
	 */
	public void logInfo(String information) {
		Logger.getLogger("Minecraft").log(Level.INFO, information);
	}
	
	/**
	 * Log severe information to the minecraft server console
	 * @param information The severe information to log
	 */
	public void logSevere(String information) {
		Logger.getLogger("Minecraft").log(Level.SEVERE, information);
	}
	
	/**
	 * Check if an entity is in the adventure world
	 * @param entity The entity execute the check with
	 * @return If the entity is in an adventure world or not
	 */
	public Boolean isInAdventure(Entity entity) {
		return entity.getWorld().getName() == "AdventureLand" || entity.getWorld().getName() == "NachtDerUntoten" ? true : false;
	}
	
	/**
	 * Check if an entity is in the survival world
	 * @param entity The entity execute the check with
	 * @return If the entity is in an survival world or not
	 */
	public Boolean isInSurvival(Entity entity) {
		return entity.getWorld().getName() == "FreeSurvivalLand" || entity.getWorld().getName() == "SurvivalLand" || entity.getWorld().getName() == "SurvivalLandNether" ? true : false;
	}
	
	/**
	 * Check if a world is a survival world
	 * @param worldName The world name to execute the check with
	 * @return If the world is a survival world or not
	 */
	public Boolean isInSurvival(String worldName) {
		return worldName.equals("FreeSurvivalLand") || worldName.equals("SurvivalLand") || worldName.equals("SurvivalLandNether") ? true : false;
	}
	
	/**
	 * Check if a string can be casted to an integer
	 * @param string The string to execute the cast check with
	 * @return If the string can be casted to an integer
	 */
	public Boolean isInteger(String string) {
		try {Integer.valueOf(string); return true;} catch (Exception exception) {return false;}
	}
	
	/**
	 * Check if a string can be casted to a double
	 * @param string The string to execute the cast check with
	 * @return If the string can be casted to a double
	 */
	public Boolean isDouble(String string) {
		try {Double.valueOf(string); return true;} catch (Exception exception) {return false;}
	}
	
	/**
	 * Check if a string can be casted to a byte
	 * @param string The string to execute the cast check with
	 * @return If the string can be casted to a byte
	 */
	public Boolean isByte(String string) {
		try {Byte.valueOf(string); return true;} catch (Exception exception) {return false;}
	}
	
	/**
	 * Check if the command sender is an op
	 * @param sender The sender to execute the op check with
	 * @return If the sender is an op
	 */
	public Boolean isOp(CommandSender sender) {
		return sender instanceof Player == false || getProfile(sender).rank.getID() >= Rank.Admin.getID() ? true : false;
	}
	
	/**
	 * Returns a player profile for the command sender
	 * @param sender The sender to fetch the profile of
	 * @return The player profile of the sender
	 */
	public PlayerProfile getProfile(CommandSender sender) {
		return playerProfiles.get(sender.getName().toLowerCase());
	}
	
	/**
	 * Returns a player profile for the player
	 * @param player The player name to fetch the profile of
	 * @return The player profile of the player
	 */
	public PlayerProfile getProfile(String player) {
		if (playerProfiles.containsKey(getPlayer(player).getName().toLowerCase())) return playerProfiles.get(getPlayer(player).getName().toLowerCase());
		return null;
	}
	
	/**
	 * Sends a message alert to the server owner
	 * @param alert The message
	 */
	public void alertOwner(String alert) {
		Player player = getServer().getPlayer("EvilPeanut");
		if (player != null) player.sendMessage("§7Alert: §O" + alert);
	}
	
	/**
	 * Returns a player from the player name
	 * @param name The name of the player
	 * @return The player
	 */
	public Player getPlayer(String name) {
		try {
			if (getServer().getPlayer(name) != null) return getServer().getPlayer(name);
			for (Player player : getServer().getOnlinePlayers()) {
				if (player.getName().toLowerCase().startsWith(name.toLowerCase())) return player;
			}
			for (int i = 0; i != playerProfiles.size(); i++) {
				if (((PlayerProfile)playerProfiles.values().toArray()[i]).nameAlias.toLowerCase().startsWith(name.toLowerCase())) return getServer().getPlayer(playerProfiles.keySet().toArray()[i].toString());
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns if a location is in a region
	 * @param region The region to execute the check with
	 * @param loc The location to execute the check with
	 * @return If the location is inside the region
	 */
	public Boolean isInRegion(Region region, Location loc) {
		if (region.locationA.getWorld().getName().equals(loc.getWorld().getName()) == false) return false;
		if (isInRegionXRange(region, loc)) {
			if (isInRegionYRange(region, loc)) {
				if (isInRegionZRange(region, loc)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Returns if the region is protected or not for the player
	 * @param player The player to execute the check with
	 * @param loc The location to execute the check with
	 * @return If the location is in a protected region which the player doesn't have rights to
	 */
	public Boolean isInProtectedRegion(Location loc, Player player) {
		if (regionList.size() == 0) return false;
		if (getProfile(player).rank.getID() >= Rank.Admin.getID()) return false;
		for (int regionNo = 0; regionNo < regionList.size(); regionNo++) {
			if (regionList.get(regionNo).isProtected == false) continue;
			if (regionList.get(regionNo).ownerName.equalsIgnoreCase(player.getName())) continue;
			if (regionList.get(regionNo).locationA.getWorld().getName().equals(loc.getWorld().getName()) == false) continue;
			if (isInRegionXRange(regionList.get(regionNo), loc)) {
				if (isInRegionYRange(regionList.get(regionNo), loc)) {
					if (isInRegionZRange(regionList.get(regionNo), loc)) {
						return true;
					} else {
						continue;
					}
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * Returns if the player is in the region's X range or not
	 * @param region The region to execute the check with
	 * @param location The location to execute the check with
	 * @return If the location is in the region's X range
	 */
	static Boolean isInRegionXRange(Region region, Location location) {
		if (region.locationA.getBlockX() <= region.locationB.getBlockX()) {
			if (location.getBlockX() >= region.locationA.getBlockX() && location.getBlockX() <= region.locationB.getBlockX()) return true;
		} else if (region.locationA.getBlockX() >= region.locationB.getBlockX()) {
			if (location.getBlockX() <= region.locationA.getBlockX() && location.getBlockX() >= region.locationB.getBlockX()) return true;
		}
		return false;
	}

	/**
	 * Returns if the player is in the region's Y range or not
	 * @param region The region to execute the check with
	 * @param location The location to execute the check with
	 * @return If the location is in the region's Y range
	 */
	static Boolean isInRegionYRange(Region region, Location playerLocation) {
		if (region.locationA.getBlockY() <= region.locationB.getBlockY()) {
			if (playerLocation.getBlockY() >= region.locationA.getBlockY() && playerLocation.getBlockY() <= region.locationB.getBlockY()) return true;
		} else if (region.locationA.getBlockY() >= region.locationB.getBlockY()) {
			if (playerLocation.getBlockY() <= region.locationA.getBlockY() && playerLocation.getBlockY() >= region.locationB.getBlockY()) return true;
		}
		return false;
	}

	/**
	 * Returns if the player is in the region's Z range or not
	 * @param region The region to execute the check with
	 * @param location The location to execute the check with
	 * @return If the location is in the region's Z range
	 */
	static Boolean isInRegionZRange(Region region, Location playerLocation) {
		if (region.locationA.getBlockZ() <= region.locationB.getBlockZ()) {
			if (playerLocation.getBlockZ() >= region.locationA.getBlockZ() && playerLocation.getBlockZ() <= region.locationB.getBlockZ()) return true;
		} else if (region.locationA.getBlockZ() >= region.locationB.getBlockZ()) {
			if (playerLocation.getBlockZ() <= region.locationA.getBlockZ() && playerLocation.getBlockZ() >= region.locationB.getBlockZ()) return true;
		}
		return false;
	}
	
	/**
	 * Return the entity type from the entity type name
	 * @param name The name of the entity type
	 * @return The entity type
	 */
	public EntityType getEntity(String name) {
		if (EntityType.fromName(name) != null) return EntityType.fromName(name);
		try {if (EntityType.fromId(Integer.valueOf(name)) != null) return EntityType.fromId(Integer.valueOf(name));} catch (Exception e) {}
		if (name.equalsIgnoreCase("mooshroom")) return EntityType.MUSHROOM_COW;
		if (name.equalsIgnoreCase("ocelot") || name.equalsIgnoreCase("cat")) return EntityType.OCELOT;
		if (name.equalsIgnoreCase("zombiepigman")) return EntityType.PIG_ZOMBIE;
		if (name.equalsIgnoreCase("dog")) return EntityType.WOLF;
		if (name.equalsIgnoreCase("magmacube")) return EntityType.MAGMA_CUBE;
		if (name.equalsIgnoreCase("witherskeleton")) return EntityType.WITHER_SKULL;
		if (name.equalsIgnoreCase("snowgolem")) return EntityType.SNOWMAN;
		if (name.equalsIgnoreCase("irongolem") || name.equalsIgnoreCase("ironman")) return EntityType.IRON_GOLEM;
		if (name.equalsIgnoreCase("wither")) return EntityType.WITHER;
		if (name.equalsIgnoreCase("firework")) return EntityType.FIREWORK;
		if (name.equalsIgnoreCase("lightning")) return EntityType.LIGHTNING;
		return null;
	}
	
	/**
	 * Return the enchantment from the enchantment name
	 * @param name The name of the enchantment
	 * @return The enchantment
	 */
	public Enchantment getEnchantment(String name) {
		if (Enchantment.getByName(name) != null) return Enchantment.getByName(name);
		try {if (Enchantment.getById(Integer.valueOf(name)) != null) return Enchantment.getById(Integer.valueOf(name));} catch (Exception e) {}
		if (name.equalsIgnoreCase("power")) return Enchantment.ARROW_DAMAGE;
		if (name.equalsIgnoreCase("flame")) return Enchantment.ARROW_FIRE;
		if (name.equalsIgnoreCase("infinity")) return Enchantment.ARROW_INFINITE;
		if (name.equalsIgnoreCase("punch")) return Enchantment.ARROW_KNOCKBACK;
		if (name.equalsIgnoreCase("sharpness")) return Enchantment.DAMAGE_ALL;
		if (name.equalsIgnoreCase("baneofarthropods") || name.equalsIgnoreCase("bane")) return Enchantment.DAMAGE_ARTHROPODS;
		if (name.equalsIgnoreCase("smite")) return Enchantment.DAMAGE_UNDEAD;
		if (name.equalsIgnoreCase("efficiency")) return Enchantment.DIG_SPEED;
		if (name.equalsIgnoreCase("unbreaking")) return Enchantment.DURABILITY;
		if (name.equalsIgnoreCase("fireaspect")) return Enchantment.FIRE_ASPECT;
		if (name.equalsIgnoreCase("knockback")) return Enchantment.KNOCKBACK;
		if (name.equalsIgnoreCase("fortune")) return Enchantment.LOOT_BONUS_BLOCKS;
		if (name.equalsIgnoreCase("looting")) return Enchantment.LOOT_BONUS_MOBS;
		if (name.equalsIgnoreCase("respiration")) return Enchantment.OXYGEN;
		if (name.equalsIgnoreCase("protection")) return Enchantment.PROTECTION_ENVIRONMENTAL;
		if (name.equalsIgnoreCase("blastprotection")) return Enchantment.PROTECTION_EXPLOSIONS;
		if (name.equalsIgnoreCase("featherfalling")) return Enchantment.PROTECTION_FALL;
		if (name.equalsIgnoreCase("fireprotection")) return Enchantment.PROTECTION_FIRE;
		if (name.equalsIgnoreCase("projectileprotection")) return Enchantment.PROTECTION_PROJECTILE;
		if (name.equalsIgnoreCase("silktouch")) return Enchantment.SILK_TOUCH;
		if (name.equalsIgnoreCase("thorns")) return Enchantment.THORNS;
		if (name.equalsIgnoreCase("aquaaffinity")) return Enchantment.WATER_WORKER;
		return null;
	}
	
	/**
	 * Returns a colorized string
	 * @param string The string to colorize
	 * @return The colorized string
	 */
	public String colorizeString(String string) {
		String name = "";
		int color = 0;
        for (char c : string.toCharArray()) {
        	if (color == 0) name += "§a" + c;
        	if (color == 1) name += "§b" + c;
        	if (color == 2) name += "§c" + c;
        	if (color == 3) name += "§d" + c;
        	if (color == 4) {
        		name += "§e" + c;
        		color = 0;
        	} else {
        		color++;
        	}
        }
		return name;
	}
	
	/**
	 * Returns an itemstack of a book
	 * @param title The title of the book
	 * @param author The author of the book
	 * @param text The text in the book
	 * @return The book itemstack
	 */
	public ItemStack getBook(String title, String author, List<String> text) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
	    BookMeta meta = (BookMeta)book.getItemMeta();
	    meta.setTitle(title);
	    meta.setAuthor(author);
	    meta.setPages(text);
	    book.setItemMeta(meta);
	    return book;
	}
	
	/**
	 * Returns the minecraft world time
	 * @param world The world to obtain the time of
	 * @return The time of the minecraft world
	 */
	public String getTime(World world) {
		return (int)(Math.floor(world.getTime() / 1000)) + ":" + ((int) ((world.getTime() % 1000) / 1000.0 * 60) < 10 ? "0" + (int) ((world.getTime() % 1000) / 1000.0 * 60) : (int) ((world.getTime() % 1000) / 1000.0 * 60));
	}
	
	/**
	 * Returns a string with characters replaced with other specified characters ignoring case on replacement
	 * @param text The text
	 * @param search The search word to replace
	 * @param replacement The world to replace with
	 * @return The string after all instances of the search word, ignoring case, are replaced
	 */
	public String replaceAllIgnoreCase(final String text, final String search, final String replacement){
		if(search.equals(replacement)) return text;
		final StringBuffer buffer = new StringBuffer(text);
		final String lowerSearch = search.toLowerCase();
		int i = 0;
		int prev = 0;
		while((i = buffer.toString().toLowerCase().indexOf(lowerSearch, prev)) > -1){
			buffer.replace(i, i+search.length(), replacement);
			prev = i + replacement.length();
		}
		return buffer.toString();
	}
	
	/**
	 * Returns a string number as roman numerals
	 * @param number The number to convert into roman numerals
	 * @return The string number as roman numerals
	 */
	public String toRomanNumerals(String number) {
		if (number.equals("1")) return "I";
		if (number.equals("2")) return "II";
		if (number.equals("3")) return "III";
		if (number.equals("4")) return "IV";
		if (number.equals("5")) return "V";
		return null;
	}
	
	/**
	 * Returns a formatted string
	 * @param text The text to format
	 * @return The formatted string
	 */
	public String toFormattedString(String text) {
		//
		// Color formatting
		//
		if (text.contains("&")) {
			text = replaceAllIgnoreCase(text, "&a", "§a");
			text = replaceAllIgnoreCase(text, "&b", "§b");
			text = replaceAllIgnoreCase(text, "&c", "§c");
			text = replaceAllIgnoreCase(text, "&d", "§d");
			text = replaceAllIgnoreCase(text, "&e", "§e");
			text = replaceAllIgnoreCase(text, "&f", "§f");
			text = replaceAllIgnoreCase(text, "&k", "§k");
			text = replaceAllIgnoreCase(text, "&l", "§l");
			text = replaceAllIgnoreCase(text, "&m", "§m");
			text = replaceAllIgnoreCase(text, "&n", "§n");
			text = replaceAllIgnoreCase(text, "&o", "§o");
			text = replaceAllIgnoreCase(text, "&r", "§r");
			text = text.replaceAll("&0", "§0");
			text = text.replaceAll("&1", "§1");
			text = text.replaceAll("&2", "§2");
			text = text.replaceAll("&3", "§3");
			text = text.replaceAll("&4", "§4");
			text = text.replaceAll("&5", "§5");
			text = text.replaceAll("&6", "§6");
			text = text.replaceAll("&7", "§7");
			text = text.replaceAll("&8", "§8");
			text = text.replaceAll("&9", "§9");
		}
		return text;
	}
	
	/**
	 * Returns the weather state in the blocks biome as a string
	 * @param block The block to get the weather data from
	 * @return The weather state as a string at the block
	 */
	public String getWeather(Block block) {
		if (block.getWorld().hasStorm() && block.getWorld().isThundering() && block.getBiome() != Biome.FROZEN_OCEAN && block.getBiome() != Biome.FROZEN_RIVER && block.getBiome() != Biome.ICE_MOUNTAINS && block.getBiome() != Biome.ICE_PLAINS && block.getBiome() != Biome.TAIGA && block.getBiome() != Biome.TAIGA_HILLS) return "Lightning";
		if (block.getWorld().hasStorm() && (block.getBiome() == Biome.FROZEN_OCEAN || block.getBiome() == Biome.FROZEN_RIVER || block.getBiome() == Biome.ICE_MOUNTAINS || block.getBiome() == Biome.ICE_PLAINS || block.getBiome() == Biome.TAIGA || block.getBiome() == Biome.TAIGA_HILLS)) return "Snow";
		if (block.getWorld().hasStorm()) return "Rain";
		return "Sunny";
	}
	
	/**
	 * Log a hanging entity break
	 * @param entity The hanging entity to log
	 * @param playerName The player's name who broke the block
	 */
	public void logHangingEntityBreak(Entity entity, String playerName) {
		// X,Y,Z¶PlayerName¶WorldName¶EditID¶EntityTypeID¶EntityDirection
		writeFileNewLine("plugins/EvilBook/Protection/" + entity.getWorld().getName() + "," + entity.getLocation().getBlockX() + "," + entity.getLocation().getBlockY() + "," + entity.getLocation().getBlockZ() + ".txt", entity.getLocation().getBlockX() + "," + entity.getLocation().getBlockY() + "," + entity.getLocation().getBlockZ() + "¶" + playerName + "¶" + entity.getWorld().getName() + "¶4¶" + entity.getType().getTypeId() + "¶" + entity.getLocation().getDirection());
		writeFileNewLine("plugins/EvilBook/Protection/" + playerName + ".txt", entity.getLocation().getBlockX() + "," + entity.getLocation().getBlockY() + "," + entity.getLocation().getBlockZ() + "¶" + playerName + "¶" + entity.getWorld().getName() + "¶4¶" + entity.getType().getTypeId() + "¶" + entity.getLocation().getDirection());
	}
	
	/**
	 * Log a hanging entity place
	 * @param entity The hanging entity to log
	 * @param playerName The player's name who placed the block
	 */
	public void logHangingEntityPlace(Entity entity, String playerName) {
		// X,Y,Z¶PlayerName¶WorldName¶EditID¶EntityTypeID¶EntityDirection
		writeFileNewLine("plugins/EvilBook/Protection/" + entity.getWorld().getName() + "," + entity.getLocation().getBlockX() + "," + entity.getLocation().getBlockY() + "," + entity.getLocation().getBlockZ() + ".txt", entity.getLocation().getBlockX() + "," + entity.getLocation().getBlockY() + "," + entity.getLocation().getBlockZ() + "¶" + playerName + "¶" + entity.getWorld().getName() + "¶5¶" + entity.getType().getTypeId() + "¶" + entity.getLocation().getDirection());
		writeFileNewLine("plugins/EvilBook/Protection/" + playerName + ".txt", entity.getLocation().getBlockX() + "," + entity.getLocation().getBlockY() + "," + entity.getLocation().getBlockZ() + "¶" + playerName + "¶" + entity.getWorld().getName() + "¶5¶" + entity.getType().getTypeId() + "¶" + entity.getLocation().getDirection());	
	}
	
	/**
	 * Log a block break
	 * @param block The block to log
	 * @param playerName The player's name who broke the block
	 */
	public void logBlockBreak(Block block, String playerName) {
		// X,Y,Z¶PlayerName¶WorldName¶EditID¶BlockTypeID¶BlockData
		writeFileNewLine("plugins/EvilBook/Protection/" + block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ".txt", block.getX() + "," + block.getY() + "," + block.getZ() + "¶" + playerName + "¶" + block.getWorld().getName() + "¶0¶" + block.getTypeId() + "¶" + block.getData());
		writeFileNewLine("plugins/EvilBook/Protection/" + playerName + ".txt", block.getX() + "," + block.getY() + "," + block.getZ() + "¶" + playerName + "¶" + block.getWorld().getName() + "¶0¶" + block.getTypeId() + "¶" + block.getData());
	}
	
	/**
	 * Log a block place
	 * @param block The block to log
	 * @param playerName The player's name who placed the block
	 */
	public void logBlockPlace(Block block, String playerName) {
		// X,Y,Z¶PlayerName¶WorldName¶EditID¶BlockTypeID¶BlockData
		writeFileNewLine("plugins/EvilBook/Protection/" + block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ".txt", block.getX() + "," + block.getY() + "," + block.getZ() + "¶" + playerName + "¶" + block.getWorld().getName() + "¶1¶" + block.getTypeId() + "¶" + block.getData());
		writeFileNewLine("plugins/EvilBook/Protection/" + playerName + ".txt", block.getX() + "," + block.getY() + "," + block.getZ() + "¶" + playerName + "¶" + block.getWorld().getName() + "¶1¶" + block.getTypeId() + "¶0");
	}
	
	/**
	 * Log a liquid placement
	 * @param block The block to log
	 * @param liquidID The type ID of the liquid
	 * @param playerName The player's name who placed the liquid
	 */
	public void logLiquidPlace(Block block, int liquidID, String playerName) {
		// X,Y,Z¶PlayerName¶WorldName¶EditID¶LiquidID¶BlockData
		writeFileNewLine("plugins/EvilBook/Protection/" + block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ".txt", block.getX() + "," + block.getY() + "," + block.getZ() + "¶" + playerName + "¶" + block.getWorld().getName() + "¶2¶" + liquidID + "¶0");
		writeFileNewLine("plugins/EvilBook/Protection/" + playerName + ".txt", block.getX() + "," + block.getY() + "," + block.getZ() + "¶" + playerName + "¶" + block.getWorld().getName() + "¶2¶" + liquidID + "¶0");
	}
	
	/**
	 * Log a sign break
	 * @param sign The sign to log
	 * @param block The block to log
	 * @param playerName The player's name who broke the block
	 */
	public void logSignBreak(Sign sign, Block block, String playerName) {
		// X,Y,Z¶PlayerName¶WorldName¶EditID¶BlockTypeID¶BlockData¶BlockDirection¶Line1¶Line2¶Line3¶Line4
		String[] signLine = new String[4];
		signLine[0] = sign.getLine(0);
		signLine[1] = sign.getLine(1);
		signLine[2] = sign.getLine(2);
		signLine[3] = sign.getLine(3);
		writeFileNewLine("plugins/EvilBook/Protection/" + playerName + ".txt", sign.getX() + "," + sign.getY() + "," + sign.getZ() + "¶" + playerName + "¶" + sign.getWorld().getName() + "¶3¶" + sign.getTypeId() + "¶0¶" + ((Directional) block.getType().getNewData(block.getData())).getFacing().toString() + "¶" + signLine[0] + "¶" + signLine[1] + "¶" + signLine[2] + "¶" + signLine[3]);
		writeFileNewLine("plugins/EvilBook/Protection/" + sign.getWorld().getName() + "," + sign.getX() + "," + sign.getY() + "," + sign.getZ() + ".txt", sign.getX() + "," + sign.getY() + "," + sign.getZ() + "¶" + playerName + "¶" + sign.getWorld().getName() + "¶3¶" + sign.getTypeId() + "¶0¶" + ((Directional) block.getType().getNewData(block.getData())).getFacing().toString() + "¶" + signLine[0] + "¶" + signLine[1] + "¶" + signLine[2] + "¶" + signLine[3]);
	}
	
	/**
	 * Return the location of a logged block
	 * @param line The logged line containing the block information
	 * @return The location of the logged block
	 */
	public Location getLogBlockLocation(String line) {
		return new Location(getServer().getWorld(line.split("¶")[2]), Double.valueOf(line.split(",")[0]), Double.valueOf(line.split(",")[1]), Double.valueOf(line.split(",")[2].split("¶")[0]));
	}
	
	/**
	 * Return the editor's name of a logged block
	 * @param line The logged line containing the block information
	 * @return The editor's name of the logged block
	 */
	public String getLogBlockEditor(String line) {
		return line.split("¶")[1];
	}
	
	/**
	 * Return the edit ID of a logged block
	 * @param line The logged line containing the block information
	 * @return The edit ID of the logged block
	 */
	public byte getLogBlockEditID(String line) {
		return Byte.valueOf(line.split("¶")[3]);
	}
	
	/**
	 * Return the type ID of a logged block
	 * @param line The logged line containing the block information
	 * @return The type ID of the logged block
	 */
	public short getLogBlockTypeID(String line) {
		return Short.valueOf(line.split("¶")[4]);
	}
	
	/**
	 * Return the data of a logged block
	 * @param line The logged line containing the block information
	 * @return The data of the logged block
	 */
	public byte getLogBlockData(String line) {
		return Byte.valueOf(line.split("¶")[5]);
	}
	
	/**
	 * Return the direction of a logged block
	 * @param line The logged line containing the block information
	 * @return The direction of the logged block
	 */
	public BlockFace getLogBlockDirection(String line) {
		return BlockFace.valueOf(line.split("¶")[6]);
	}
	
	/**
	 * Return the extra argument one of a logged block
	 * @param line The logged line containing the block information
	 * @return The extra argument one of the logged block
	 */
	public String getLogBlockArg1(String line) {
		try {
			return line.split("¶")[7];
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Return the extra argument two of a logged block
	 * @param line The logged line containing the block information
	 * @return The extra argument two of the logged block
	 */
	public String getLogBlockArg2(String line) {
		try {
			return line.split("¶")[8];
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Return the extra argument three of a logged block
	 * @param line The logged line containing the block information
	 * @return The extra argument three of the logged block
	 */
	public String getLogBlockArg3(String line) {
		try {
			return line.split("¶")[9];
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Return the extra argument four of a logged block
	 * @param line The logged line containing the block information
	 * @return The extra argument four of the logged block
	 */
	public String getLogBlockArg4(String line) {
		try {
			return line.split("¶")[10];
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Return an array of strings with the blocks edits
	 * @param block The block to gather the logging information on
	 * @return The logging information for the block
	 */
	public List<String> getLogBlockInformation(Block block) {
		List<String> info = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("plugins/EvilBook/Protection/" + block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ".txt"));
			String strLine = null;
			while ((strLine = br.readLine()) != null) {
				if (getLogBlockEditID(strLine) == 5) info.add("§e" + getLogBlockEditor(strLine) + " §fplaced entity " + hangingEntityList.get((byte)getLogBlockTypeID(strLine)).toLowerCase());
				if (getLogBlockEditID(strLine) == 4) info.add("§e" + getLogBlockEditor(strLine) + " §fbroke entity " + hangingEntityList.get((byte)getLogBlockTypeID(strLine)).toLowerCase());
				if (getLogBlockEditID(strLine) == 3) info.add("§e" + getLogBlockEditor(strLine) + " §fbroke sign");
				if (getLogBlockEditID(strLine) == 2) info.add("§e" + getLogBlockEditor(strLine) + " §fplaced liquid " + blockList.get(getLogBlockTypeID(strLine)).get(0).toLowerCase());
				if (getLogBlockEditID(strLine) == 1) info.add("§e" + getLogBlockEditor(strLine) + " §fplaced block " + blockList.get(getLogBlockTypeID(strLine)).get(0).toLowerCase());
				if (getLogBlockEditID(strLine) == 0) info.add("§e" + getLogBlockEditor(strLine) + " §fbroke block " + blockList.get(getLogBlockTypeID(strLine)).get(0).toLowerCase());
			}
			br.close();
		} catch (Exception e) {}
		return info;
	}
	
	/**
	 * Rollback a player's edits
	 * @param player The player executing the rollback
	 * @param badPlayer The player to rollback
	 */
	public void rollbackEdits(Player player, String badPlayer) {
		player.sendMessage("§7Rolling back " + badPlayer);
		try {
			BufferedReader br = new BufferedReader(new FileReader("plugins/EvilBook/Protection/" + badPlayer + ".txt"));
			String brLine;
			while ((brLine = br.readLine()) != null) {
				if (getLogBlockEditID(brLine) == 0) if (getLogBlockTypeID(brLine) != 0) getLogBlockLocation(brLine).getBlock().setTypeIdAndData(getLogBlockTypeID(brLine), getLogBlockData(brLine), false);
				if (getLogBlockEditID(brLine) == 1 || getLogBlockEditID(brLine) == 2) getLogBlockLocation(brLine).getBlock().setTypeId(0);
				if (getLogBlockEditID(brLine) == 3) {
					getLogBlockLocation(brLine).getBlock().setTypeIdAndData(getLogBlockTypeID(brLine), getLogBlockData(brLine), false);
					Sign sign = (Sign) getLogBlockLocation(brLine).getBlock().getState();
					org.bukkit.material.Sign matSign =  new org.bukkit.material.Sign();
					matSign.setFacingDirection(getLogBlockDirection(brLine));
					sign.setData(matSign);
					sign.setLine(0, getLogBlockArg1(brLine));
					sign.setLine(1, getLogBlockArg2(brLine));
					sign.setLine(2, getLogBlockArg3(brLine));
					sign.setLine(3, getLogBlockArg4(brLine));
					sign.update(true);
				}
			}
			br.close();
			player.sendMessage("§7Rolled back " + badPlayer);
		} catch (Exception e) {
			player.sendMessage("§7Nothing to rollback");
		}
	}
	
	/**
	 * Append text to a file
	 * @param file The file
	 * @param text The text to append
	 */
	public void writeFileNewLine(String file, String text) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(text) ;
			writer.newLine();
			writer.close() ;
		} catch (Exception e) {
			logSevere("Failed To Write To File " + file);
		}
	}
	
	/**
	 * Save an object to a file
	 * @param obj The object to save
	 * @param path The path of the file to save to
	 */
	public void saveObject(Object obj, String path) throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeObject(obj);
		oos.flush();
		oos.close();
	}

	/**
	 * Load an object from a file
	 * @param path The path of the file to load from
	 * @return The object loaded from the file
	 */
	public Object loadObject(String path) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		Object result = ois.readObject();
		ois.close();
		return result;
	}
	
	/**
	 * Update the online player web statistic
	 * @param onlinePlayers The number of online players
	 */
	public void updateWebPlayerStatistics(int onlinePlayers) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("C:/Program Files (x86)/Apache Software Foundation/Apache2.2/htdocs/playerStats.htm"));
			out.write("<html><body style='background:transparent'><p style='color:#31AFF5;text-align:center;font-family:Calibri'>" + onlinePlayers + " Players online<p></body></html>");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return if a player profile exists
	 * @param playerName The player name to execute the check with
	 * @return If the player's profile is existant
	 */
	public boolean isProfileExistant(String playerName) {
		return new File("plugins/EvilBook/Players/" + playerName + ".properties").exists();
	}
	
	/**
	 * Gets the players creative inventory and equips it
	 * @param player The player
	 */
	public void getCreativeInventory(Player player) {	
		/*
		try {
			SerializableInventory inv = new SerializableInventory(((Player) sender).getInventory().getContents(), ((Player) sender).getInventory().getArmorContents());
			saveObject(inv, "inventory.inv");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		try {
			SerializableInventory inv = (SerializableInventory) loadObject("plugins/EvilBook/Inventories/Creative/" + player.getName() + ".inv");
			player.getInventory().setContents(inv.getContents());
			player.getInventory().setArmorContents(inv.getArmorContents());
		} catch (Exception e) {
			if (e instanceof FileNotFoundException) return;
			logSevere("Failed to load player creative inventory: " + player.getName() + ".inv");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the players survival inventory and equip it
	 * @param player The player
	 */
	public void getSurvivalInventory(Player player) {	
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		try {
			SerializableInventory inv = (SerializableInventory) loadObject("plugins/EvilBook/Inventories/Survival/" + player.getName() + ".inv");
			player.getInventory().setContents(inv.getContents());
			player.getInventory().setArmorContents(inv.getArmorContents());
		} catch (Exception e) {
			if (e instanceof FileNotFoundException) return;
			logSevere("Failed to load player survival inventory: " + player.getName() + ".inv");
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the players creative inventory
	 * @param player The player
	 */
	public void setCreativeInventory(Player player) {
		try {
			saveObject(new SerializableInventory(player.getInventory().getContents(), player.getInventory().getArmorContents()), "plugins/EvilBook/Inventories/Creative/" + player.getName() + ".inv");
		} catch (Exception e) {
			logSevere("Failed to save player creative inventory: " + player.getName() + ".inv");
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the players survival inventory
	 * @param player The player
	 */
	public void setSurvivalInventory(Player player) {
		try {
			saveObject(new SerializableInventory(player.getInventory().getContents(), player.getInventory().getArmorContents()), "plugins/EvilBook/Inventories/Survival/" + player.getName() + ".inv");
		} catch (Exception e) {
			logSevere("Failed to save player survival inventory: " + player.getName() + ".inv");
			e.printStackTrace();
		}
	}
	
	/**
	 * Square a length
	 * @param x The X length
	 * @param y The Y length
	 * @param z The Z length
	 * @return The squared length
	 */
    private static final double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }
    
    /**
     * Get a property of an offline player's profile
     * @param player The player name
     * @param property The property name
     * @return The property value
     */
    public String getOfflineProperty(String player, String property) {
    	Properties prop = new Properties();
		File file = new File("plugins/EvilBook/Players/" + player + ".properties");
		if (file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				prop.load(inputStream);
				inputStream.close();
				return prop.getProperty(property);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
    }
    
    /**
     * Set a property of an offline player's profile
     * @param player The player name
     * @param property The property name
     * @param value The new value of the property
     */
    public void setOfflineProperty(String player, String property, String value) {
    	try {
			Properties prop = new Properties();
			FileInputStream inputStream = new FileInputStream("plugins/EvilBook/Players/" + player + ".properties");
			prop.load(inputStream);
			inputStream.close();
			prop.setProperty(property, value);
			FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Players/" + player + ".properties");
			prop.store(outputStream, null);
			outputStream.close();
		} catch (Exception e) {
			System.out.println("Failed to save " + player + "'s player profile");
		}
    }
    
    /**
     * Protect a container (Chest, furnace & brewing stand ect...) in the survival world
     * @param location The position of the container in the world
     * @param player The player owner of the container
     */
    public void protectContainer(Location location, Player player) {
    	try {
    		FileInputStream inputStream = new FileInputStream("plugins/EvilBook/ContainerProtection.db");
    		Properties protectionProp = new Properties();
    		protectionProp.load(inputStream);
    		inputStream.close();
    		protectionProp.setProperty(location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ(), player.getName());
    		FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/ContainerProtection.db");
    		protectionProp.store(outputStream, null);
    		outputStream.close();
    	} catch (Exception e) {
    		logSevere("Failed to save ContainerProtection.db");
    		e.printStackTrace();
    	}
    }
    
    /**
     * Unprotect a container (Chest, furnace & brewing stand ect...) in the survival world
     * @param location The position of the container in the world
     */
    public void unprotectContainer(Location location) {
    	try {
    		FileInputStream inputStream = new FileInputStream("plugins/EvilBook/ContainerProtection.db");
    		Properties protectionProp = new Properties();
    		protectionProp.load(inputStream);
    		inputStream.close();
    		protectionProp.remove(location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ());
    		FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/ContainerProtection.db");
    		protectionProp.store(outputStream, null);
    		outputStream.close();
    	} catch (Exception e) {
    		logSevere("Failed to save ContainerProtection.db");
    		e.printStackTrace();
    	}
    }
    
    /**
     * Return if the container (Chest, furnace & brewing stand ect...) is protected to the player
     * @param location The position of the container in the world
     * @param player The player to execute the check with
     * @return If the container is protected to the player
     */
    public Boolean isContainerProtected(Location location, Player player) {
    	try {
    		FileInputStream inputStream = new FileInputStream("plugins/EvilBook/ContainerProtection.db");
    		Properties protectionProp = new Properties();
    		protectionProp.load(inputStream);
    		inputStream.close();
    		String protection = protectionProp.getProperty(location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ());
    		return protection == null || protection.equals(player.getName()) ? false : true;
    	} catch (Exception e) {
    		logSevere("Failed to read ContainerProtection.db");
    		e.printStackTrace();
    		return true;
    	}
    }
    
    /**
     * Return the number of blocks in an area
     * @param topBlockX The top X block
     * @param bottomBlockX The bottom X block
     * @param topBlockY The top Y block
     * @param bottomBlockY The bottom Y block
     * @param topBlockZ The top Z block
     * @param bottomBlockZ The bottom Z block
     * @return
     */
    public int getNumberOfBlocksInArea(int topBlockX, int bottomBlockX, int topBlockY, int bottomBlockY, int topBlockZ, int bottomBlockZ) {
    	return (((topBlockX - bottomBlockX) + 1) * ((topBlockY - bottomBlockY) + 1) * ((topBlockZ - bottomBlockZ) + 1));
    }
}
