package me.evilpeanut;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Reece Aaron Lecrivain
 */
public class PlayerProfile {
	public Location deathLocation, homeLocation, actionLocationA, actionLocationB, creativeLocation, survivalLocation;
	public String name, nameColor, nameTitle, nameAlias, lastMessage, mutedPlayers, warps, teleportantName, customRankPrefix = "Custom", customRankColor = "6";
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
				if (rank == Rank.Custom) {
					String prefix = prop.getProperty("CustomRankPrefix");
					if (prefix != null) {
						customRankColor = prefix.substring(1, 2);
						customRankPrefix = "§0[" + prefix.replaceAll("&", "§") + "§0]";
					}
				}
				if (prop.getProperty("Money") != null) {
					money = Integer.valueOf(prop.getProperty("Money"));
				} else {
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
				if (prop.getProperty("Warps") != null) warps = prop.getProperty("Warps");
				if (prop.getProperty("RunAmplifier") != null) runAmplifier = Integer.valueOf(prop.getProperty("RunAmplifier"));
				if (prop.getProperty("JumpAmplifier") != null) jumpAmplifier = Double.valueOf(prop.getProperty("JumpAmplifier"));
				if (prop.getProperty("SurvivalXP") != null) survivalXP = Integer.valueOf(prop.getProperty("SurvivalXP"));
				if (rank.getID() >= Rank.Admin.getID()) {
					nameColor = prop.getProperty("NameColor");
					nameTitle = prop.getProperty("NameTitle");
					nameAlias = prop.getProperty("NameAlias");
					if (nameAlias != null) {
						if (nameColor.equals("?")) {
							if (nameTitle == null) {
								newPlayer.setDisplayName(plugin.colorizeString(nameAlias) + "§f");
							} else {
								newPlayer.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(nameAlias) + "§f");
							}
						} else {
							if (nameTitle == null) {
								newPlayer.setDisplayName("§" + nameColor + nameAlias + "§f");
							} else {
								newPlayer.setDisplayName("§d" + nameTitle + " §" + nameColor + nameAlias + "§f");
							}
						}
						newPlayer.setPlayerListName("§" + rank.getColor(this) + (nameAlias.length() > 14 ? nameAlias.substring(0, 14) : nameAlias));
					} else {
						if (nameColor.equals("?")) {
							if (nameTitle == null) {
								newPlayer.setDisplayName(plugin.colorizeString(name) + "§f");
							} else {
								newPlayer.setDisplayName("§d" + nameTitle + " " + plugin.colorizeString(name) + "§f");
							}
						} else {
							if (nameTitle == null) {
								newPlayer.setDisplayName("§" + nameColor + name + "§f");
							} else {
								newPlayer.setDisplayName("§d" + nameTitle + " §" + nameColor + name + "§f");
							}
						}
						newPlayer.setPlayerListName("§" + rank.getColor(this) + (name.length() > 14 ? name.substring(0, 14) : name));
					}
				} else {
					newPlayer.setDisplayName("§f" + name);
					newPlayer.setPlayerListName("§" + rank.getColor(this) + (name.length() > 14 ? name.substring(0, 14) : name));
				}
				mutedPlayers = prop.getProperty("MutedPlayers");
			} catch (Exception exception) {
				plugin.logSevere("Failed to load " + name + "'s player profile");
				exception.printStackTrace();
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
				newPlayer.setPlayerListName("§" + rank.getColor(this) + (name.length() > 14 ? name.substring(0, 14) : name));
				prop.setProperty("Money", "0");
				money = 0;
				prop.setProperty("NameColor", "f");
				nameColor = "f";
				prop.setProperty("MutedPlayers", "");
				mutedPlayers = "";
				newPlayer.setDisplayName("§f" + name);
				prop.setProperty("ChallengeRankUp", "0");
				FileOutputStream outputStream = new FileOutputStream(file);
				prop.store(outputStream, null);
				outputStream.close();
				newPlayer.getInventory().addItem(plugin.getBook("Welcome to Amentrix", "EvilPeanut", Arrays.asList("Welcome to the Amentrix Server\n\nRules:\n§a[§b1§a] §6Dont Greif\n§a[§b2§a] §6Dont Advertise\n§a[§b3§a] §6Dont Spam\n\n§0Website:\n§7http://amentrix.no-ip.org\n\n§0Enjoy your stay!\n§7 - EvilPeanut")));
			} catch (Exception exception) {
				plugin.logSevere("Failed to create " + name + "'s player profile");
				exception.printStackTrace();
			}
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (p.getName() == name) continue;
				p.sendMessage("§d" + new File("plugins/EvilBook/Players").list().length + " §9players have joined the server");
				p.sendMessage("§9[§6" + plugin.getServer().getOnlinePlayers().length + "/" + plugin.getServer().getMaxPlayers() + "§9] §6Everyone welcome " + newPlayer.getDisplayName() + "§6 for the first time!");
			}
		}
		newPlayer.sendMessage("§2Welcome to the Amentrix server");
		if (rank.getID() < Rank.Admin.getID()) {
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
			if (nameColor != null) prop.setProperty("NameColor", nameColor);
			if (nameTitle != null) prop.setProperty("NameTitle", nameTitle);
			if (nameAlias != null) prop.setProperty("NameAlias", nameAlias);
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
		} catch (Exception exception) {
			plugin.logSevere("Failed to save " + name + "'s player profile");
			exception.printStackTrace();
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
		if (nameAlias == null) {
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
		} else {
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
		if (nameAlias == null) {
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
		} else {
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
		}
	}

	/**
	 * Set the name alias of the player
	 * @param alias The name alias
	 * @param player The player
	 */
	public void setNameAlias(String alias, Player player) {
		if (alias == null) {
			removeProperty("NameAlias");
			nameAlias = null;
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
			player.setPlayerListName("§" + rank.getColor(this) + player.getName());
		} else {
			setProperty("NameAlias", alias);
			nameAlias = alias;
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
			player.setPlayerListName("§" + rank.getColor(this) + nameAlias);
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
			} catch (Exception exception) {
				plugin.logSevere("Failed to edit " + name + "'s player profile");
				exception.printStackTrace();
			}
		}
	}
	
	/**
	 * Remove a property on the player profile file
	 * @param property The property to remove
	 */
	public void removeProperty(String property) {
		File file = new File("plugins/EvilBook/Players/" + name + ".properties");
		if (file.exists()) {
			try {
				Properties prop = new Properties();
				FileInputStream inputStream = new FileInputStream(file);
				prop.load(inputStream);
				inputStream.close();
				prop.remove(property);
				FileOutputStream outputStream = new FileOutputStream(file);
				prop.store(outputStream, null);
				outputStream.close();
			} catch (Exception exception) {
				plugin.logSevere("Failed to edit " + name + "'s player profile");
				exception.printStackTrace();
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
		} catch (Exception exception) {
			plugin.logSevere("Failed to edit " + name + "'s player profile");
			exception.printStackTrace();
		}
	}
}
