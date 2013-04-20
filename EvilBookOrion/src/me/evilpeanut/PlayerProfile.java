package me.evilpeanut;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Reece Aaron Lecrivain
 */
public class PlayerProfile {
	public Location deathLocation, homeLocation, actionLocationA, actionLocationB, creativeLocation, survivalLocation;
	public String name, nameColor, nameTitle, nameAlias, lastMessage, mutedPlayers, warps, teleportantName;
	public List<EvilEditBlock> EvilEditUndo = new ArrayList<EvilEditBlock>();
	public List<EvilEditBlock> EvilEditCopy = new ArrayList<EvilEditBlock>();
	public Boolean isLogging = false;
	public long lastMessageTime;
	public int money = 0, runAmplifier = 4, survivalXP = 0, strengthLevel = 0, miningLevel = 0, fishingLevel = 0;
	public double jumpAmplifier = 0;
	public Rank rank = Rank.Visitor;
	public Spell selectedSpell;
	public List<Spell> spellBook = new ArrayList<Spell>();
	private EvilBook plugin;
	
	/**
	 * Load the profile of an online player
	 * @param plugin The parent EvilBook plugin
	 * @param playerName The name of the player
	 */
	public PlayerProfile(EvilBook plugin, Player newPlayer) {
		this.plugin = plugin;
		name = newPlayer.getName();
		Properties prop = new Properties();
		File file = new File("plugins/EvilBook/Players/" + name + ".properties");
		if (file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				prop.load(inputStream);
				inputStream.close();
				rank = Rank.valueOf(prop.getProperty("Rank"));
				if (prop.getProperty("Money") != null) {
					money = Integer.valueOf(prop.getProperty("Money"));
				} else {
					money = 0;
					plugin.alertOwner(name + " failed to load money");
				}
				if (prop.getProperty("Spells") != null) {
					String[] spell = prop.getProperty("Spells").split(">");
					for (String s : spell) spellBook.add(Spell.valueOf(s));
					selectedSpell = spellBook.get(0);
				}
				if (prop.getProperty("HomeLocation") != null) {
					String[] location = prop.getProperty("HomeLocation").split(">");
					homeLocation = new Location(plugin.getServer().getWorld(location[3]), Double.valueOf(location[0]), Double.valueOf(location[1]), Double.valueOf(location[2]));
				}
				if (prop.getProperty("SurvivalLocation") != null) {
					String[] location = prop.getProperty("SurvivalLocation").split(">");
					survivalLocation = new Location(plugin.getServer().getWorld(location[3]), Double.valueOf(location[0]), Double.valueOf(location[1]), Double.valueOf(location[2]));
				}
				if (prop.getProperty("CreativeLocation") != null) {
					String[] location = prop.getProperty("CreativeLocation").split(">");
					creativeLocation = new Location(plugin.getServer().getWorld(location[3]), Double.valueOf(location[0]), Double.valueOf(location[1]), Double.valueOf(location[2]));
				}
				if (prop.getProperty("NameColor") != null) {
					nameColor = prop.getProperty("NameColor");
				} else {
					nameColor = "f";
					plugin.alertOwner(name + " failed to load name color");
				}
				if (prop.getProperty("Warps") != null) warps = prop.getProperty("Warps");
				if (prop.getProperty("RunAmplifier") != null) runAmplifier = Integer.valueOf(prop.getProperty("RunAmplifier"));
				if (prop.getProperty("JumpAmplifier") != null) jumpAmplifier = Double.valueOf(prop.getProperty("JumpAmplifier"));
				if (prop.getProperty("SurvivalXP") != null) survivalXP = Integer.valueOf(prop.getProperty("SurvivalXP"));
				nameTitle = prop.getProperty("NameTitle");
				nameAlias = prop.getProperty("NameAlias");
				if (nameAlias != null && !nameAlias.equals("null")) {
					if (nameColor.equals("?")) {
						if (nameTitle == null || nameTitle.equals("")) {
							newPlayer.setDisplayName(plugin.colorizeString(nameAlias) + "§f");
						} else {
							newPlayer.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(nameAlias) + "§f");
						}
					} else {
						if (nameTitle == null || nameTitle.equals("")) {
							newPlayer.setDisplayName("§" + nameColor + nameAlias + "§f");
						} else {
							newPlayer.setDisplayName("§d" + nameTitle + " §" + nameColor + nameAlias + "§f");
						}
					}
					newPlayer.setPlayerListName("§" + rank.color + (nameAlias.length() > 14 ? nameAlias.substring(0, 14) : nameAlias));
				} else {
					if (nameColor.equals("?")) {
						if (nameTitle == null || nameTitle.equals("")) {
							newPlayer.setDisplayName(plugin.colorizeString(name) + "§f");
						} else {
							newPlayer.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(name) + "§f");
						}
					} else {
						if (nameTitle == null || nameTitle.equals("")) {
							newPlayer.setDisplayName("§" + nameColor + name + "§f");
						} else {
							newPlayer.setDisplayName("§d" + nameTitle + " §" + nameColor + name + "§f");
						}
					}
					newPlayer.setPlayerListName("§" + rank.color + (name.length() > 14 ? name.substring(0, 14) : name));
				}
				mutedPlayers = prop.getProperty("MutedPlayers");
			} catch (Exception e) {
				Logger.getLogger("Minecraft").log(Level.SEVERE, "Failed to load player profile: " + name);
				e.printStackTrace();
			}
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (p.getName() == name) continue;
				p.sendMessage("§9[§6" + plugin.getServer().getOnlinePlayers().length + "/" + plugin.getServer().getMaxPlayers() + "§9] §6Everyone welcome " + newPlayer.getDisplayName() + "§6 back to the game!");
			}
		} else {
			try {
				file.createNewFile();
				FileInputStream inputStream = new FileInputStream(file);
				prop.load(inputStream);
				inputStream.close();
				prop.setProperty("Rank", "Visitor");
				rank = Rank.Visitor;
				newPlayer.setPlayerListName("§" + rank.color + (name.length() > 14 ? name.substring(0, 14) : name));
				prop.setProperty("Money", "0");
				money = 0;
				prop.setProperty("NameColor", "f");
				nameColor = "f";
				prop.setProperty("MutedPlayers", "");
				mutedPlayers = "";
				prop.setProperty("NameAlias", "null");
				nameAlias = "null";
				newPlayer.setDisplayName("§f" + name);
				prop.setProperty("ChallengeRankUp", "0");
				FileOutputStream outputStream = new FileOutputStream(file);
				prop.store(outputStream, null);
				outputStream.close();
				newPlayer.getInventory().addItem(plugin.getBook("Welcome to Amentrix", "EvilPeanut", Arrays.asList("Welcome to the Amentrix Server\n\nRules:\n§a[§b1§a] §6Dont Greif\n§a[§b2§a] §6Dont Advertise\n§a[§b3§a] §6Dont Hack\n\n§0Website:\n§7http://amentrix.no-ip.org\n\n§0Enjoy your stay!\n§7 - EvilPeanut")));
			} catch (FileNotFoundException e) {
				Logger.getLogger("Minecraft").log(Level.SEVERE, "Failed to create player profile: " + name);
			} catch (IOException e) {
				Logger.getLogger("Minecraft").log(Level.SEVERE, "Failed to create player profile: " + name);
			}
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (p.getName() == name) continue;
				p.sendMessage("§d" + new File("plugins/EvilBook/Players").list().length + " §9players have joined the server");
				p.sendMessage("§9[§6" + plugin.getServer().getOnlinePlayers().length + "/" + plugin.getServer().getMaxPlayers() + "§9] §6Everyone welcome " + newPlayer.getDisplayName() + "§6 for the first time!");
			}
		}
		newPlayer.sendMessage("§2Welcome to the Amentrix server");
		if (newPlayer.isOp() == false) {
			newPlayer.sendMessage("§3Type §6/admin §3to discover how to become an admin or OP");
			newPlayer.sendMessage("§3Type §6/survival §3to enter the survival world");
		} else {
			newPlayer.sendMessage("§3Type §6/shop §3to display the shop catalogue");
		}
		newPlayer.sendMessage("§3Type §6/ranks §3to display the list of ranks");
		newPlayer.sendMessage("§3Type §6/donate §3to display instructions on how to donate");
		newPlayer.sendMessage("§3Type §6/help §3for help");
	}
	
	/**
	 * Save the player profile
	 */
	public void saveProfile() {
		try {
			Properties prop = new Properties();
			FileInputStream inputStream = new FileInputStream(new File("plugins/EvilBook/Players/" + name + ".properties"));
			prop.load(inputStream);
			inputStream.close();
			// Player profile properties to save
			if (homeLocation != null) prop.setProperty("HomeLocation", homeLocation.getX() + ">" + homeLocation.getY() + ">" + homeLocation.getZ() + ">" + homeLocation.getWorld().getName());
			if (survivalLocation != null) prop.setProperty("SurvivalLocation", survivalLocation.getX() + ">" + survivalLocation.getY() + ">" + survivalLocation.getZ() + ">" + survivalLocation.getWorld().getName());
			if (creativeLocation != null) prop.setProperty("CreativeLocation", creativeLocation.getX() + ">" + creativeLocation.getY() + ">" + creativeLocation.getZ() + ">" + creativeLocation.getWorld().getName());
			prop.setProperty("NameColor", nameColor);
			if (nameTitle != null) prop.setProperty("NameTitle", nameTitle);
			prop.setProperty("NameAlias", nameAlias);
			if (mutedPlayers != null) prop.setProperty("MutedPlayers", mutedPlayers);
			prop.setProperty("Rank", rank.toString());
			prop.setProperty("Money", Integer.toString(money));
			if (warps != null) prop.setProperty("Warps", warps);
			prop.setProperty("RunAmplifier", Integer.toString(runAmplifier));
			prop.setProperty("JumpAmplifier", Double.toString(jumpAmplifier));
			prop.setProperty("SurvivalXP", Integer.toString(survivalXP));
			//
			FileOutputStream outputStream = new FileOutputStream(new File("plugins/EvilBook/Players/" + name + ".properties"));
			prop.store(outputStream, null);
			outputStream.close();
		} catch (Exception e) {
			System.out.println("Failed to save " + name + "'s player profile");
		}
	}
	
	/**
	 * Set the name color of the player
	 * @param color The name color
	 * @param player The player
	 */
	public void setNameColor(String color, Player player) {
		setProperty("NameColor", color);
		nameColor = color;
		if (nameAlias.equals("null") == false) {
			if (nameColor.equals("?")) {
				if (nameTitle == null) {
					player.setDisplayName(plugin.colorizeString(nameAlias) + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(nameAlias) + "§f");
				}
			} else {
				if (nameTitle == null) {
					player.setDisplayName("§" + nameColor + nameAlias + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " §" + nameColor + nameAlias + "§f");
				}
			}
		} else {
			if (nameColor.equals("?")) {
				if (nameTitle == null) {
					player.setDisplayName(plugin.colorizeString(name) + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(name) + "§f");
				}
			} else {
				if (nameTitle == null) {
					player.setDisplayName("§" + nameColor + name + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " §" + nameColor + name + "§f");
				}
			}
		}
	}
	
	/**
	 * Set the title of the player
	 * @param title The title
	 * @param player The player
	 */
	public void setNameTitle(String title, Player player) {
		setProperty("NameTitle", title);
		nameTitle = title;
		if (nameAlias.equals("null") == false) {
			if (nameColor.equals("?")) {
				if (nameTitle == null) {
					player.setDisplayName(plugin.colorizeString(nameAlias) + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(nameAlias) + "§f");
				}
			} else {
				if (nameTitle == null) {
					player.setDisplayName("§" + nameColor + nameAlias + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " §" + nameColor + nameAlias + "§f");
				}
			}
		} else {
			if (nameColor.equals("?")) {
				if (nameTitle == null) {
					player.setDisplayName(plugin.colorizeString(name) + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(name) + "§f");
				}
			} else {
				if (nameTitle == null) {
					player.setDisplayName("§" + nameColor + name + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " §" + nameColor + name + "§f");
				}
			}
		}
	}

	/**
	 * Set the name alias of the player
	 * @param alias The name alias
	 * @param player The player
	 */
	public void setNameAlias(String alias, Player player) {
		if (alias == null) {
			setProperty("NameAlias", "null");
			nameAlias = "null";
			if (nameColor.equals("?")) {
				if (nameTitle == null || nameTitle.equals("")) {
					player.setDisplayName(plugin.colorizeString(name) + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(name) + "§f");
				}
			} else {
				if (nameTitle == null || nameTitle.equals("")) {
					player.setDisplayName("§" + nameColor + name + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " §" + nameColor + name + "§f");
				}
			}
			player.setPlayerListName("§" + rank.color + player.getName());
		} else {
			setProperty("NameAlias", alias);
			nameAlias = alias;
			if (nameColor.equals("?")) {
				if (nameTitle == null || nameTitle.equals("")) {
					player.setDisplayName(plugin.colorizeString(nameAlias) + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(nameAlias) + "§f");
				}
			} else {
				if (nameTitle == null || nameTitle.equals("")) {
					player.setDisplayName("§" + nameColor + nameAlias + "§f");
				} else {
					player.setDisplayName("§d" + nameTitle + " §" + nameColor + nameAlias + "§f");
				}
			}
			player.setPlayerListName("§" + rank.color + nameAlias);
		}
	}

	/**
	 * Set a property on the player profile file
	 * @param property The property to set
	 * @param value The value of the property
	 */
	public void setProperty(String property, String value) {
		File file = new File("plugins/EvilBook/Players/" + name + ".properties");
		if (file.exists()) {
			try {
				Properties prop = new Properties();
				FileInputStream inputStream = new FileInputStream(file);
				prop.load(inputStream);
				inputStream.close();
				prop.setProperty(property, value);
				FileOutputStream outputStream = new FileOutputStream(file);
				prop.store(outputStream, null);
				outputStream.close();
			} catch (Exception e) {
				System.out.println("Failed to edit " + name + "'s player profile");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Add a spell to the player profile
	 * @param spell The spell to add
	 */
	public void addSpell(Spell spell) {
		spellBook.add(spell);
		try {
			Properties prop = new Properties();
			FileInputStream inputStream = new FileInputStream(new File("plugins/EvilBook/Players/" + name + ".properties"));
			prop.load(inputStream);
			inputStream.close();
			// Player profile properties to save
			if (spellBook.size() == 1) {
				prop.setProperty("Spells", spell.toString());
			} else {
				String spells = "";
				for (Spell s : spellBook) spells += spells == "" ? s.toString() : ">" + s.toString();
				prop.setProperty("Spells", spells);
			}
			//
			FileOutputStream outputStream = new FileOutputStream(new File("plugins/EvilBook/Players/" + name + ".properties"));
			prop.store(outputStream, null);
			outputStream.close();
		} catch (Exception e) {
			System.out.println("Failed to save " + name + "'s player profile");
		}
	}
}
