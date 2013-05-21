package me.evilpeanut;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Spider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Stairs;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

/**
 * @author Reece Aaron Lecrivain
 */
public class EventListener implements Listener {
	private EvilBook plugin;

	/**
	 * Define a new event listener
	 * @param evilBook The parent evilbook plugin
	 */
	public EventListener(EvilBook evilBook) {
		plugin = evilBook;
	}

	/**
	 * Called when a player moves
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getTo().getBlockX() > 12550820 || event.getTo().getBlockX() < -12550820 || event.getTo().getBlockZ() > 12550820 || event.getTo().getBlockZ() < -12550820) {
			event.getPlayer().sendMessage("§7The far-lands are blocked");
			event.setCancelled(true);
			return;
		}
		if (!plugin.isInSurvival(event.getPlayer()) && !plugin.isInAdventure(event.getPlayer())) {
			if (plugin.getProfile(event.getPlayer()).jumpAmplifier != 0 && !event.getPlayer().isFlying() && event.getFrom().getY() < event.getTo().getY() && event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getTypeId() != 0) event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(plugin.getProfile(event.getPlayer()).jumpAmplifier));
			if (plugin.getProfile(event.getPlayer()).runAmplifier != 0 && event.getPlayer().isSprinting()) event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, plugin.getProfile(event.getPlayer()).runAmplifier), true);
		}
		for (Region region : plugin.regionList) {
			if (region.leaveMessage != null && plugin.isInRegion(region, event.getFrom()) && plugin.isInRegion(region, event.getTo()) == false) {
				event.getPlayer().sendMessage(region.leaveMessage.replace("&", "§"));
			} else if (region.welcomeMessage != null && plugin.isInRegion(region, event.getFrom()) == false && plugin.isInRegion(region, event.getTo())) {
				event.getPlayer().sendMessage(region.welcomeMessage.replace("&", "§"));
			}
			if (region.warpName != null && plugin.isInRegion(region, event.getFrom()) == false && plugin.isInRegion(region, event.getTo())) event.getPlayer().teleport(plugin.warpList.get(region.warpName));
		}
	}
	
	/**
	 * Called when a splash potion hits an area
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPotionSplash(PotionSplashEvent event) {
		Entity entity = event.getEntity().getShooter();
		if (entity instanceof Player == false || plugin.isInSurvival(entity)) return;
		Player shooter = (Player) entity;
		if (plugin.getProfile(shooter).rank.getID() >= Rank.Admin.getID()) return;
		if (Potion.fromItemStack(event.getPotion().getItem()).getType() == PotionType.INVISIBILITY) {
			shooter.sendMessage("§7Only admins can use invisibility potions");
			event.setCancelled(true);
		}
	}
	
	/**
	 * Called when a player finishes consuming an item
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDrinkPotion(PlayerItemConsumeEvent event) {
		if (plugin.isInSurvival(event.getPlayer()) || plugin.getProfile(event.getPlayer()).rank.getID() >= Rank.Admin.getID()) return;
		if (event.getItem().getType() == Material.POTION && event.getItem().getDurability() != 0 && Potion.fromItemStack(event.getItem()).getType() == PotionType.INVISIBILITY) {
			event.getPlayer().sendMessage("§7Only admins can use invisibility potions");
			event.setCancelled(true);
		}
	}
	
	/**
	 * Called when a player does a fishing related action
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerFish(PlayerFishEvent event) {
		if (event.getCaught() != null) {
			Player player = event.getPlayer();
			int fishID = new Random().nextInt(plugin.fishList.size());
			String fishName = (String) plugin.fishList.keySet().toArray()[fishID];
			ItemStack fish = new ItemStack(Material.RAW_FISH, 1);
			ItemMeta meta = fish.getItemMeta();
			meta.setDisplayName(fishName);
			meta.setLore(Arrays.asList("$" + plugin.fishList.get(fishName)));
			fish.setItemMeta(meta);
			player.getInventory().addItem(fish);
			player.updateInventory();
			player.sendMessage("§7You have caught a " + fishName);
			event.getCaught().remove();
		}
	}
	
	/**
	 * Called when a player throws an egg
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerEggThrow(PlayerEggThrowEvent event) {
		if (plugin.getProfile(event.getPlayer()).rank.getID() < Rank.Admin.getID()) event.setHatching(false);
	}

	/**
	 * Called when a player is about to be teleported by a portal
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPortal(PlayerPortalEvent event) {
		if (event.getFrom().getWorld().getName().equals("SurvivalLand")) event.getPlayer().teleport(plugin.getServer().getWorld("SurvivalLandNether").getSpawnLocation());
		if (event.getFrom().getWorld().getName().equals("SurvivalLandNether")) event.getPlayer().teleport(plugin.getServer().getWorld("SurvivalLand").getSpawnLocation());
		if (event.getTo() == null || event.getFrom() == null) return;
		for (Region region : plugin.regionList) {
			if (region.warpName != null && plugin.isInRegion(region, event.getFrom()) == false && plugin.isInRegion(region, event.getTo())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	/**
	 * Called when a player drops an item
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (plugin.isInSurvival(event.getPlayer())) return;
		int dropCount = 0;
		for (Entity e : event.getPlayer().getLocation().getWorld().getEntities()) {
			if (e.getType() == EntityType.DROPPED_ITEM) dropCount++;
		}
		if (dropCount >= 128 && plugin.getProfile(event.getPlayer()).rank.getID() < Rank.Moderator.getID()) event.setCancelled(true);
	}
	
	/**
	 * Called when a player empties a bucket
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		//
		// Liquid protection
		//
		if (plugin.getProfile(player).rank.getID() < Rank.Admin.getID()) {
			player.sendMessage((event.getBucket().getId() == 327 ?  "§dLava buckets" : "§dWater buckets") + " are an §5Admin §donly feature");
			player.sendMessage("§dPlease type §6/admin §dto learn how to become admin");
			event.setCancelled(true);
			return;
		} else {
			if (event.getBlockFace() == BlockFace.NORTH) {
				if (event.getBucket().getId() == 326) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX() - 1, event.getBlockClicked().getY(), event.getBlockClicked().getZ()), 8, player.getName());
				if (event.getBucket().getId() == 327) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX() - 1, event.getBlockClicked().getY(), event.getBlockClicked().getZ()), 10, player.getName());
				return;
			}
			if (event.getBlockFace() == BlockFace.EAST) {
				if (event.getBucket().getId() == 326) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY(), event.getBlockClicked().getZ() - 1), 8, player.getName());
				if (event.getBucket().getId() == 327) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY(), event.getBlockClicked().getZ() - 1), 10, player.getName());
				return;
			}
			if (event.getBlockFace() == BlockFace.SOUTH) {
				if (event.getBucket().getId() == 326) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX() + 1, event.getBlockClicked().getY(), event.getBlockClicked().getZ()), 8, player.getName());
				if (event.getBucket().getId() == 327) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX() + 1, event.getBlockClicked().getY(), event.getBlockClicked().getZ()), 10, player.getName());
				return;
			}
			if (event.getBlockFace() == BlockFace.WEST) {
				if (event.getBucket().getId() == 326) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY(), event.getBlockClicked().getZ() + 1), 8, player.getName());
				if (event.getBucket().getId() == 327) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY(), event.getBlockClicked().getZ() + 1), 10, player.getName());
				return;
			}
			if (event.getBlockFace() == BlockFace.UP) {
				if (event.getBucket().getId() == 326) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY() + 1, event.getBlockClicked().getZ()), 8, player.getName());
				if (event.getBucket().getId() == 327) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY() + 1, event.getBlockClicked().getZ()), 10, player.getName());
				return;
			}
			if (event.getBlockFace() == BlockFace.DOWN) {
				if (event.getBucket().getId() == 326) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY() - 1, event.getBlockClicked().getZ()), 8, player.getName());
				if (event.getBucket().getId() == 327) plugin.logLiquidPlace(player.getWorld().getBlockAt(event.getBlockClicked().getX(), event.getBlockClicked().getY() - 1, event.getBlockClicked().getZ()), 10, player.getName());
				return;
			}
		}
	}
	
	/**
	 * Called when a player dies
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		plugin.getProfile(player).deathLocation = player.getLocation();
		if (player.getLastDamageCause() != null)
		{
			EntityDamageEvent damageEvent = player.getLastDamageCause();
			EntityDamageEvent.DamageCause damageCause = damageEvent.getCause();
			if (damageEvent instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent kie = (EntityDamageByEntityEvent)damageEvent;
				Entity damager = kie.getDamager();
				if (damageCause == DamageCause.ENTITY_ATTACK) {
					if (damager instanceof Player)
					{
						Player attackPlayer = (Player) damager;
						event.setDeathMessage(player.getDisplayName() + " was murdered by " + attackPlayer.getDisplayName() + " wielding their " + (attackPlayer.getItemInHand().getType() == Material.AIR ? "fists" : attackPlayer.getItemInHand().getType() == Material.WOOD_SWORD ? "wooden sword" : attackPlayer.getItemInHand().getType() == Material.STONE_SWORD ? "stone sword" : attackPlayer.getItemInHand().getType() == Material.IRON_SWORD ? "iron sword" : attackPlayer.getItemInHand().getType() == Material.GOLD_SWORD ? "gold sword" : attackPlayer.getItemInHand().getType() == Material.DIAMOND_SWORD ? "diamond sword" : attackPlayer.getItemInHand().getTypeId() < 256 ? "block" : attackPlayer.getItemInHand().getTypeId() >= 256 ? "item" : "unknown object"));
						return;
					} else if (damager instanceof Zombie) {
						event.setDeathMessage(player.getDisplayName() + " had their brains eaten by a Zombie");
					} else if (damager instanceof Spider) {
						event.setDeathMessage(player.getDisplayName() + " had their flesh devoured by a Spider");
					} else if (damager instanceof CaveSpider) {
						event.setDeathMessage(player.getDisplayName() + " had their flesh devoured by a Cave-Spider");
					} else if (damager instanceof Enderman) {
						event.setDeathMessage(player.getDisplayName() + " had their soul consumed by an Enderman");
					} else if (damager instanceof Silverfish) {
						event.setDeathMessage(player.getDisplayName() + " had their insides ripped out by a Silverfish");
					} else if (damager instanceof MagmaCube) {
						event.setDeathMessage(player.getDisplayName() + " had their corpse burnt to ashes by a Magma-Cube");
					} else if (damager instanceof Slime) {
						event.setDeathMessage(player.getDisplayName() + " had their corpse dissolved in a Slime");
					} else if (damager instanceof Wolf) {
						event.setDeathMessage(player.getDisplayName() + " had their corpse ripped appart by a Wolf");
					} else if (damager instanceof PigZombie) {
						event.setDeathMessage(player.getDisplayName() + " had their corpse cut in two by a Pigman-Zombie");
					} else if (damager instanceof IronGolem) {
						event.setDeathMessage(player.getDisplayName() + " had their corpse crushed by an Iron-Golem");
					} else if (damager instanceof Giant) {
						event.setDeathMessage(player.getDisplayName() + " had their corpse crushed by a Giant");
					}
				} else if (damageCause == DamageCause.PROJECTILE) {
					Projectile pro = (Projectile)damager;
					if (pro.getShooter() instanceof Player) {
						Player attackPlayer = (Player) pro.getShooter();
						if (pro instanceof Arrow) {
							event.setDeathMessage(player.getDisplayName() + " was killed by " + attackPlayer.getDisplayName() + "'s arrow");
						} else if (pro instanceof Snowball) {
							event.setDeathMessage(player.getDisplayName() + " was killed by " + attackPlayer.getDisplayName() + "'s snowball");
						} else if (pro instanceof Egg) {
							event.setDeathMessage(player.getDisplayName() + " was killed by " + attackPlayer.getDisplayName() + "'s egg");
						} else {
							event.setDeathMessage(player.getDisplayName() + " was killed by " + attackPlayer.getDisplayName() + "'s projectile");
						}
						return;
					}
					if (pro instanceof Arrow)
					{
						if ((pro.getShooter() instanceof Skeleton)) {
							event.setDeathMessage(player.getDisplayName() + " had their bones smashed by a Skeleton's arrow");
						} else {
							event.setDeathMessage(player.getDisplayName() + " had their bones smashed by an arrow trap");
						}
					} else if (pro instanceof Snowball) {
						event.setDeathMessage(player.getDisplayName() + " had their blood frozen by a Snowman's snowball");
					} else if (pro instanceof Fireball) {
						if (pro.getShooter() instanceof Ghast) {
							event.setDeathMessage(player.getDisplayName() + " had their insides boiled by a Ghast's fireball");
						} else if ((pro.getShooter() instanceof Blaze)) {
							event.setDeathMessage(player.getDisplayName() + " had their insides boiled by a Blazes's fireball");
						} else if ((pro.getShooter() instanceof Wither)) {
							event.setDeathMessage(player.getDisplayName() + " had their insides boiled by a Wither's fireball");
						} else {
							event.setDeathMessage(player.getDisplayName() + " had their insides boiled by a fireball");
						}
					}
				} else if (damageCause == DamageCause.ENTITY_EXPLOSION) {
					if (damager instanceof Creeper) {
						event.setDeathMessage(player.getDisplayName() + " was blown up by a creeper");
					} else if (damager instanceof TNTPrimed) {
						event.setDeathMessage(player.getDisplayName() + " was blown up by TNT");
					}
				}
			} else {
				if (damageCause == DamageCause.DROWNING) {
					event.setDeathMessage(player.getDisplayName() + " drowned to death");
				} else if (damageCause == DamageCause.STARVATION) {
					event.setDeathMessage(player.getDisplayName() + " starved to death");
				} else if (damageCause == DamageCause.CONTACT) {
					event.setDeathMessage(player.getDisplayName() + " was playing with a cactus when it pricked back");
				} else if (damageCause == DamageCause.CUSTOM) {
					event.setDeathMessage(player.getDisplayName() + " died");
				} else if (damageCause == DamageCause.FIRE) {
					event.setDeathMessage(player.getDisplayName() + " had their corpse burnt to ashes by fire");
				} else if (damageCause == DamageCause.FIRE_TICK) {
					event.setDeathMessage(player.getDisplayName() + " had their corpse burnt to ashes by fire");
				} else if (damageCause == DamageCause.LAVA) {
					event.setDeathMessage(player.getDisplayName() + " had their corpse burnt to ashes by lava");
				} else if (damageCause == DamageCause.LIGHTNING) {
					event.setDeathMessage(player.getDisplayName() + " had their corpse burnt to ashes by lightning");
				} else if (damageCause == DamageCause.POISON) {
					event.setDeathMessage(player.getDisplayName() + " died from poisoning");
				} else if (damageCause == DamageCause.SUFFOCATION) {
					event.setDeathMessage(player.getDisplayName() + " suffocated to death");
				} else if (damageCause == DamageCause.VOID) {
					event.setDeathMessage(player.getDisplayName() + " was crushed by the never ending black hole");
				} else if (damageCause == DamageCause.FALL) {
					event.setDeathMessage(player.getDisplayName() + " fell to their death");
				} else if (damageCause == DamageCause.SUICIDE) {
					event.setDeathMessage(player.getDisplayName() + " committed suicide");
				} else if (damageCause == DamageCause.MAGIC) {
					event.setDeathMessage(player.getDisplayName() + " was destroyed at the hands of a sourcerer");
				} else if (damageCause == DamageCause.WITHER) {
					event.setDeathMessage(player.getDisplayName() + " was crushed by a wither");
				}
			}
		}
	}
	
	/**
	 * Called when a player attempts to login
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event) {
		//
		// Ban message
		//
		if (event.getResult().equals(Result.KICK_BANNED)) {
			event.setKickMessage("§cYou are banned! E-mail us for support at buddypeanut@hotmail.co.uk");
			return;
		}
		//
		// Check the players location is valid to prevent integer overflow
		//
		if (event.getPlayer().getLocation().getBlockX() > 12550820 || event.getPlayer().getLocation().getBlockX() < -12550820 || event.getPlayer().getLocation().getBlockZ() > 12550820 || event.getPlayer().getLocation().getBlockZ() < -12550820) {
			event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
		}
		//
		// Update playerStats.htm
		//
		plugin.updateWebPlayerStatistics(plugin.getServer().getOnlinePlayers().length + 1);
	}
	
	/**
	 * Called when a player joins the server after login
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (plugin.isInSurvival(event.getPlayer())) event.getPlayer().setScoreboard(plugin.survivalStatsScoreboard);
		plugin.playerProfiles.put(event.getPlayer().getName().toLowerCase(), new PlayerProfile(plugin, event.getPlayer()));
		event.setJoinMessage(null);
	}
	
	/**
	 * Called when a player respawns
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (plugin.isInSurvival(event.getPlayer())) {
			event.getPlayer().getInventory().clear();
			plugin.setSurvivalInventory(event.getPlayer());
			if (plugin.isInSurvival(event.getPlayer().getWorld().getName())) event.setRespawnLocation(plugin.getServer().getWorld("SurvivalLand").getSpawnLocation());
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
	}
	
	/**
	 * Called when a player leaves the server
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent event) {
		//
		// Profile Save
		//
		plugin.getProfile(event.getPlayer()).saveProfile();
		plugin.playerProfiles.remove(event.getPlayer().getName().toLowerCase());
		event.setQuitMessage(ChatColor.GRAY + event.getPlayer().getName() + " has left the game");
		//
		// Update playerStats.htm
		//
		plugin.updateWebPlayerStatistics(plugin.getServer().getOnlinePlayers().length - 1);
	}
	
	/**
	 * Called when a player is kicked from the server
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(ChatColor.GRAY + event.getPlayer().getName() + " has left the game");
	}
	
	/**
	 * Called when a player sends a chat message
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		// Mute functionality
		e.setCancelled(true);
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (plugin.getProfile(p).mutedPlayers != null && plugin.getProfile(p).mutedPlayers.contains(player.getName().toLowerCase()) == false) {
				p.sendMessage(plugin.getProfile(player).rank.getPrefix(plugin.getProfile(player)) + " §" + plugin.getProfile(player).rank.getColor(plugin.getProfile(player)) + "<" + player.getDisplayName() + "§" + plugin.getProfile(player).rank.getColor(plugin.getProfile(player)) + "> §f" + plugin.toFormattedString(e.getMessage()));
			}
		}
	}
	
	/**
	 * Called when a player interacts with an object or air
	 */
	public Map<String, Object> sitting = new HashMap<String, Object>();
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (plugin.isInSurvival(player) && plugin.getProfile(player.getName()).rank != Rank.ServerOwner) {
				if (block.getTypeId() == 130) {
					player.sendMessage("§7Ender chests are blocked in survival");
					event.setCancelled(true);
					return;
				}
				if ((block.getTypeId() == 23 || block.getTypeId() == 54 || block.getTypeId() == 58 || block.getTypeId() == 61 || block.getTypeId() == 62 || block.getTypeId() == 117 || block.getTypeId() == 146 || block.getTypeId() == 158) && plugin.isContainerProtected(event.getClickedBlock().getLocation(), player)) {
					player.sendMessage(ChatColor.GRAY + "You don't have permission to open the " + plugin.blockList.get((short)block.getTypeId()).get(0).toLowerCase());
					event.setCancelled(true);
					return;
				}
			}
			if (event.hasItem()) {
				if (plugin.getProfile(player).rank.getID() >= Rank.Admin.getID() && (plugin.isInSurvival(player.getName()) == false || plugin.getProfile(player).rank == Rank.ServerOwner)) {
					if (event.getItem().getTypeId() == 284) {
						plugin.getProfile(player).actionLocationB = block.getLocation();
						player.sendMessage("§7Second point selected (" + block.getX() + ", " + block.getY() + ", " + block.getZ() + ")");
						event.setCancelled(true);
						return;
					}
				}
				if ((block.getTypeId() == 63 || block.getTypeId() == 68) && event.getItem().getTypeId() == 351) {
					ChatColor dyeTextColor;
					switch (event.getItem().getData().getData()) {
					case 1: dyeTextColor = ChatColor.RED; break;
					case 2: dyeTextColor = ChatColor.DARK_GREEN; break;
					case 4: dyeTextColor = ChatColor.BLUE; break;
					case 5: dyeTextColor = ChatColor.DARK_PURPLE; break;
					case 6: dyeTextColor = ChatColor.DARK_AQUA; break;
					case 7: dyeTextColor = ChatColor.GRAY; break;
					case 8: dyeTextColor = ChatColor.DARK_GRAY; break;
					case 9: dyeTextColor = ChatColor.LIGHT_PURPLE; break;
					case 10: dyeTextColor = ChatColor.GREEN; break;
					case 11: dyeTextColor = ChatColor.YELLOW; break;
					case 12: dyeTextColor = ChatColor.AQUA; break;
					case 13: dyeTextColor = ChatColor.LIGHT_PURPLE; break;
					case 14: dyeTextColor = ChatColor.GOLD; break;
					case 15: dyeTextColor = ChatColor.WHITE; break;
					default: dyeTextColor = ChatColor.BLACK; break;
					}
					Sign s = (Sign) event.getClickedBlock().getState();
					if (s.getLine(0).length() != 0) s.setLine(0, dyeTextColor + (s.getLine(0).startsWith("§") && !s.getLine(0).startsWith("§l") && !s.getLine(0).startsWith("§k") && !s.getLine(0).startsWith("§n") && !s.getLine(0).startsWith("§m") && !s.getLine(0).startsWith("§o") && !s.getLine(0).startsWith("§r") ? s.getLine(0).substring(2, s.getLine(0).length()) : s.getLine(0)));
					if (s.getLine(1).length() != 0) s.setLine(1, dyeTextColor + (s.getLine(1).startsWith("§") && !s.getLine(1).startsWith("§l") && !s.getLine(1).startsWith("§k") && !s.getLine(1).startsWith("§n") && !s.getLine(1).startsWith("§m") && !s.getLine(1).startsWith("§o") && !s.getLine(1).startsWith("§r") ? s.getLine(1).substring(2, s.getLine(1).length()) : s.getLine(1)));
					if (s.getLine(2).length() != 0) s.setLine(2, dyeTextColor + (s.getLine(2).startsWith("§") && !s.getLine(2).startsWith("§l") && !s.getLine(2).startsWith("§k") && !s.getLine(2).startsWith("§n") && !s.getLine(2).startsWith("§m") && !s.getLine(2).startsWith("§o") && !s.getLine(2).startsWith("§r") ? s.getLine(2).substring(2, s.getLine(2).length()) : s.getLine(2)));
					if (s.getLine(3).length() != 0) s.setLine(3, dyeTextColor + (s.getLine(3).startsWith("§") && !s.getLine(3).startsWith("§l") && !s.getLine(3).startsWith("§k") && !s.getLine(3).startsWith("§n") && !s.getLine(3).startsWith("§m") && !s.getLine(3).startsWith("§o") && !s.getLine(3).startsWith("§r") ? s.getLine(3).substring(2, s.getLine(3).length()) : s.getLine(3)));
					s.update();
				}
			} else {
				if (sitting.containsKey(player.getName().toLowerCase())) {
					Arrow arrow = (Arrow)sitting.get(player.getName().toLowerCase());
					arrow.remove();
					sitting.remove(player.getName().toLowerCase());
				} else if (player.isSneaking() && ((block.getType() == Material.WOOD_STAIRS) || (block.getType() == Material.BIRCH_WOOD_STAIRS) || (block.getType() == Material.SPRUCE_WOOD_STAIRS) || (block.getType() == Material.JUNGLE_WOOD_STAIRS) || (block.getType() == Material.SANDSTONE_STAIRS) || (block.getType() == Material.BRICK_STAIRS) || (block.getType() == Material.COBBLESTONE_STAIRS) || (block.getType() == Material.SMOOTH_STAIRS) || (block.getType() == Material.QUARTZ_STAIRS))) {
					World world = player.getWorld();
					Stairs stairs = (Stairs)block.getState().getData();
					final Arrow arrow = world.spawnArrow(new Location(world, block.getLocation().getX() + 0.6D, block.getLocation().getY(), block.getLocation().getZ() + 0.6D), new Vector(block.getLocation().getX(), block.getLocation().getY() - 1.0D, block.getLocation().getZ()), 0.0F, 0.0F);
					switch (stairs.getDescendingDirection().ordinal()) {
					case 1:
						player.getLocation().setYaw(90.0F);
						break;
					case 2:
						player.getLocation().setYaw(180.0F);
						break;
					case 3:
						player.getLocation().setYaw(270.0F);
						break;
					case 4:
						player.getLocation().setYaw(0.0F);
					}
					player.teleport(player.getLocation());
					sitting.put(player.getName().toLowerCase(), arrow);
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{
						public void run()
						{
							arrow.setPassenger(player);
						}
					}
					, 1L);
				}
			}
		} else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (event.hasItem() && event.getItem().getTypeId() == 280 && event.getItem().getItemMeta().getLore() != null && event.getItem().getItemMeta().getLore().get(0).equals("Preforms ancient magical arts")) {
				if (plugin.getProfile(player).spellBook.size() == 0) {
					player.sendMessage("§7You don't have any spells in your spellbook");
				} else {
					plugin.getProfile(player).selectedSpell.preformSpell(event);
				}
			}
		} else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			if (event.hasItem() && event.getItem().getTypeId() == 280 && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getLore().get(0).equals("Preforms ancient magical arts")) {
				if (plugin.getProfile(player).spellBook.size() == 0) {
					player.sendMessage("§7You don't have any spells in your spellbook");
				} else {
					if (plugin.getProfile(player).selectedSpell == plugin.getProfile(player).spellBook.get(plugin.getProfile(player).spellBook.size() - 1)) {
						plugin.getProfile(player).selectedSpell = plugin.getProfile(player).spellBook.get(0);
						player.sendMessage("§5Changed spell to §d" + plugin.getProfile(player).selectedSpell.name);
					} else {
						plugin.getProfile(player).selectedSpell = plugin.getProfile(player).spellBook.get(plugin.getProfile(player).spellBook.indexOf(plugin.getProfile(player).selectedSpell) + 1);
						player.sendMessage("§5Changed spell to §d" + plugin.getProfile(player).selectedSpell.name);
					}
				}
			}
		}
		/* TODO: Add ledgit way to obtain wands
		if (player.getItemInHand().getTypeId() == 280 && player.getName().equals("EvilPeanut")) {
			ItemMeta meta = player.getItemInHand().getItemMeta();
			meta.setLore(Arrays.asList("Preforms ancient magical arts"));
			player.getItemInHand().setItemMeta(meta);
			
			plugin.getProfile(player).addSpell(Spell.LightningStrikeI);
			plugin.getProfile(player).addSpell(Spell.ShockWaveI);
			plugin.getProfile(player).addSpell(Spell.ShockWaveII);
			plugin.getProfile(player).addSpell(Spell.InstantGrow);
		}
		*/
	}
	
	/**
	 * Called when a player sends a command
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase("/evilbook:stop")) {
			event.setCancelled(true); 
			return;
		}
		Player player = event.getPlayer();
		if (plugin.getProfile(player).rank == Rank.ServerOwner) return;
		// Anti-Spam Hack Protection
		if (System.currentTimeMillis() - plugin.getProfile(player).lastMessageTime <= 250 && event.getMessage().equals(plugin.getProfile(player).lastMessage)) {
			player.kickPlayer("§cSpam is not tollerated");
			event.setCancelled(true);
			return;
		}
		plugin.getProfile(player).lastMessage = event.getMessage();
		plugin.getProfile(player).lastMessageTime = System.currentTimeMillis();
		//
		if (plugin.commandBlacklist.containsKey(event.getMessage().toLowerCase().split(" ")[0]) == false) return;
		if (plugin.getProfile(player).rank.getID() < plugin.commandBlacklist.get(event.getMessage().toLowerCase().split(" ")[0])) {
			if (plugin.getProfile(player).rank.getID() < Rank.Admin.getID() && plugin.commandBlacklist.get(event.getMessage().toLowerCase().split(" ")[0]) >= Rank.Admin.getID()) {
				player.sendMessage("§dThis is an §5Admin §donly command");
				player.sendMessage("§dPlease type §6/admin §dto learn how to become admin");
			} else if (plugin.commandBlacklist.get(event.getMessage().toLowerCase().split(" ")[0]) == Rank.ServerOwner.getID()) {
				player.sendMessage("§7This command is blocked for security reasons");
			} else {
				player.sendMessage("§7This command is for higher ranks only");
			}
			event.setCancelled(true);
			return;
		}
    }
	
	/**
	 * Called when a player teleports
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (event.getTo().getBlockX() > 12550820 || event.getTo().getBlockX() < -12550820 || event.getTo().getBlockZ() > 12550820 || event.getTo().getBlockZ() < -12550820) {
			player.sendMessage("§7The far-lands are blocked");
			event.setCancelled(true);
			return;
		}
		if (plugin.isInSurvival(event.getTo().getWorld().getName())) {
			if (plugin.getProfile(player).rank.getID() >= Rank.Architect.getID()) {
				player.sendMessage("§7Welcome to the survival world");
			} else {
				player.sendMessage("§7The survival world requires architect rank");
				event.setCancelled(true);
				return;
			}
			if (!plugin.isInSurvival(event.getFrom().getWorld().getName())) plugin.getProfile(player).creativeLocation = event.getFrom();
		} else if (plugin.isInSurvival(event.getFrom().getWorld().getName())) {
			plugin.getProfile(player).survivalLocation = event.getFrom();
		}
		try {
			if (event.getTo().getWorld().getName().equals("FlatLand")) {
				if (plugin.getProfile(player).rank.getID() < plugin.commandBlacklist.get("/flatland")) {
					player.sendMessage("§7You have to be a higher rank to access the flat lands");
					event.setCancelled(true);
					return;
				} else {
					player.sendMessage("§7Welcome to the flat lands");
				}
				return;
			}
			if (event.getTo().getWorld().getName().equals("SpaceLand")) {
				if (plugin.getProfile(player).rank.getID() < plugin.commandBlacklist.get("/spaceland")) {
					player.sendMessage("§7You have to be a higher rank to access the space lands");
					event.setCancelled(true);
				} else {
					player.sendMessage("§7Welcome to the space lands");
				}
			}
		} catch (Exception e) {
			player.sendMessage("§7Failed to teleport");
			event.setCancelled(true);
		}
	}
	
	/**
	 * Called when a player has their game mode changed
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		if (plugin.getProfile(event.getPlayer()).rank == Rank.ServerOwner) return;
		if (event.getNewGameMode() != GameMode.SURVIVAL && plugin.isInSurvival(event.getPlayer())) event.setCancelled(true);
		if (event.getNewGameMode() != GameMode.ADVENTURE && plugin.isInAdventure(event.getPlayer())) event.setCancelled(true);
	}
	
	/**
	 * Called when a player changes world
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		if (plugin.isInSurvival(event.getPlayer())) {
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
			plugin.setCreativeInventory(event.getPlayer());
			plugin.getSurvivalInventory(event.getPlayer());
			event.getPlayer().setScoreboard(plugin.survivalStatsScoreboard);
			ExperienceManager exp = new ExperienceManager(event.getPlayer());
			exp.setExp(plugin.getProfile(event.getPlayer()).survivalXP);
		} else {
			if (plugin.isInSurvival(event.getFrom().getName())) {
				plugin.setSurvivalInventory(event.getPlayer());
				plugin.getCreativeInventory(event.getPlayer());
				event.getPlayer().setGameMode(GameMode.CREATIVE);
				event.getPlayer().setScoreboard(plugin.scoreboardManager.getNewScoreboard());
				ExperienceManager exp = new ExperienceManager(event.getPlayer());
				plugin.getProfile(event.getPlayer()).survivalXP = exp.getCurrentExp();
				exp.setExp(0);
			}
			if (plugin.isInAdventure(event.getPlayer())) {
				ExperienceManager exp = new ExperienceManager(event.getPlayer());
				event.getPlayer().setGameMode(GameMode.ADVENTURE);
				event.getPlayer().getInventory().clear();
				exp.setExp(0);
			}
		}
	}
	
	/**
	 * Called when a block is dispensed
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockDispense(BlockDispenseEvent event) {
		if (event.getItem().getTypeId() == 326 || event.getItem().getTypeId() == 327) event.setCancelled(true);
	}

	/**
	 * Called when a block is ignited
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) event.setCancelled(true);
	}
	
	/**
	 * Called when a block is formed
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockForm(BlockFormEvent event) {
		if (event.getNewState().getTypeId() == 78 && plugin.isInSurvival(event.getBlock().getWorld().getName()) == false) {
			event.setCancelled(true);
			return;
		}
	}
	
	/**
	 * Called when a block is broken
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if (plugin.isInSurvival(event.getPlayer()) && event.getPlayer().getItemInHand().getTypeId() == 280 && event.getPlayer().getItemInHand().getItemMeta().getLore() != null && event.getPlayer().getItemInHand().getItemMeta().getLore().get(0).equals("Preforms ancient magical arts")) {
			event.setCancelled(true);
			return;
		}
		//
		// Block logging
		//
		if (plugin.getProfile(player).isLogging) {
			List<String> info = plugin.getLogBlockInformation(event.getBlock());
			if (info.size() != 0) {
				player.sendMessage("§b" + Integer.toString(info.size()) + " edits on this block");
				for (int i = 0; i < info.size(); i++) player.sendMessage(info.get(i));
			} else {
				player.sendMessage("§7No edits on this block");
			}
			event.setCancelled(true);
			return;
		} else {
			if (block.getTypeId() == 63 || block.getTypeId() == 68) {
				plugin.logSignBreak((Sign) block.getState(), block, player.getName());
			} else {
				if (block.getTypeId() != 0) plugin.logBlockBreak(block, player.getName());
			}
		}
		//
		// Action location selection
		//
		if (player.isOp()) {
			if (plugin.isInSurvival(player.getWorld().getName()) == false || plugin.getProfile(player).rank == Rank.ServerOwner) {
				if (player.getItemInHand().getTypeId() == 284) {
					plugin.getProfile(player).actionLocationA = block.getLocation();
					player.sendMessage("§7First point selected (" + block.getX() + ", " + block.getY() + ", " + block.getZ() + ")");
					event.setCancelled(true);
					return;
				}
			}
		}
		//
		// Region Protection
		//
		if (plugin.isInProtectedRegion(block.getLocation(), player) == true) {
			player.sendMessage("§cYou don't have permission to build here");
			event.setCancelled(true);
			return;
		}
		//
		// Survival Container Protection
		//
		if (plugin.isInSurvival(player) && (block.getTypeId() == 23 || block.getTypeId() == 54 || block.getTypeId() == 58 || block.getTypeId() == 61 || block.getTypeId() == 62 || block.getTypeId() == 117 || block.getTypeId() == 146 || block.getTypeId() == 158)) {
			if (plugin.isContainerProtected(block.getLocation(), player)) {
				player.sendMessage(ChatColor.GRAY + "You don't have permission to break the " + plugin.blockList.get((short)block.getTypeId()).get(0).toLowerCase());
				event.setCancelled(true);
				return;
			} else {
				plugin.unprotectContainer(event.getBlock().getLocation());
				player.sendMessage(ChatColor.GRAY + plugin.blockList.get((short)block.getTypeId()).get(0) + " protection removed");
			}
		}
		//
		// Dynamic Sign Removal
		//
		if (block.getTypeId() == 63 || block.getTypeId() == 68) {
			File timeSignFile = new File("plugins/EvilBook/Dynamic Signs/" + block.getWorld().getName() + block.getLocation().getBlockX() + block.getLocation().getBlockY() + block.getLocation().getBlockZ() + ".db");
			if (timeSignFile.exists()) timeSignFile.delete();
		}
		//
		// Anti-nuke Protection
		//
		//TODO: Re-add
		/*
		plugin.blockProtection.put(player.getName(), plugin.blockProtection.get(player.getName()) + 1);
		if (plugin.blockProtection.get(player.getName()) > 4) {
			player.kickPlayer("§cKicked by nuke hack protection");
			event.setCancelled(true);
			return;
		}
		*/
		//
		// Sitting on stairs
		//
		for (int i = 0; i < sitting.size(); i++) {
			if (((Arrow)sitting.values().toArray()[i]).getLocation().getBlock().getLocation().equals(block.getLocation())) {
				((Arrow)sitting.values().toArray()[i]).setPassenger(null);
				((Arrow)sitting.values().toArray()[i]).remove();
				sitting.remove(sitting.keySet().toArray()[i]);
			}
		}
	}
	
	/**
	 * Called when a block is placed
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		int ID = event.getBlock().getType().getId();
		//
		// Block Protection
		//
		if (plugin.getProfile(player).rank.getID() < Rank.AdvancedBuilder.getID() && (ID == Material.ANVIL.getId() || ID == 6 || ID == 12 || ID == 13 || ID == 31 || ID == 32 || ID == 39 || ID == 40 || ID == 106)) {
			player.sendMessage("§dThis block requires §5Advanced Builder §drank or higher");
			event.setCancelled(true);
			return;
		}
		if (plugin.getProfile(player).rank.getID() < Rank.Admin.getID() && (ID == 8 || ID == 9 || ID == 10 || ID == 11 || ID == 46 || ID == 51 || ID == 52 || ID == 79 || ID == 90 || ID == 122 || ID == 259)) {
			player.sendMessage("§dThis block is §5Admin §donly");
			player.sendMessage("§dPlease type §6/admin §dto learn how to become admin");
			event.setCancelled(true);
			return;
		}
		//
		// Region Protection
		//
		if (plugin.isInProtectedRegion(event.getBlock().getLocation(), player) == true) {
			player.sendMessage("§cYou don't have permission to build here");
			event.setCancelled(true);
			return;
		}
		//
		//
		// Anti-build hack Protection
		//
		//TODO: Re-add
		/*
		plugin.blockProtection.put(player.getName(), plugin.blockProtection.get(player.getName()) + 1);
		if (plugin.blockProtection.get(player.getName()) > 4) {
			player.kickPlayer("§cKicked by build hack protection");
			event.setCancelled(true);
			return;
		}
		*/
		//
		// Block logging
		//
		if (plugin.getProfile(player).isLogging) {
			List<String> info = plugin.getLogBlockInformation(event.getBlock());
			if (info.size() != 0) player.sendMessage("§b" + Integer.toString(info.size()) + " edits on this block");
			if (info.size() == 0) player.sendMessage(ChatColor.GRAY + "No edits on this block");
			for (int i = 0; i < info.size(); i++) player.sendMessage(info.get(i));
			event.setCancelled(true);
			return;
		} else {
			plugin.logBlockPlace(event.getBlock(), player.getName());
		}
		//
		// Survival Container Protection
		//
		if (plugin.isInSurvival(player) && (ID == 23 || ID == 54 || ID == 58 || ID == 61 || ID == 62 || ID == 117 || ID == 146 || ID == 158)) {
			plugin.protectContainer(event.getBlock().getLocation(), player);
			//TODO: Display the container type eg. Furnace, Chest ect...
			player.sendMessage(ChatColor.GRAY + "Container protected");
		}
	}
	
	/**
	 * Called when a sign is changed by a player
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent e) {
		if (e.getLines().length == 0) return;
		//
		// Sign Colors
		//
		for (int i = 0; i < 4; i++) e.setLine(i, plugin.toFormattedString(e.getLine(i)));
		plugin.alertOwner(e.getPlayer().getPlayer().getName() + " placed a sign [" + e.getLine(0) + "|" + e.getLine(1) + "|" + e.getLine(2) + "|" + e.getLine(3) + "]");
		//
		// Dynamic Signs
		//
		if (e.getLine(0).toLowerCase().contains("[time]") || e.getLine(1).toLowerCase().contains("[time]") || e.getLine(2).toLowerCase().contains("[time]") || e.getLine(3).toLowerCase().contains("[time]") ||
				e.getLine(0).toLowerCase().contains("[weather]") || e.getLine(1).toLowerCase().contains("[weather]") || e.getLine(2).toLowerCase().contains("[weather]") || e.getLine(3).toLowerCase().contains("[weather]")) {
			String[] text = new String[4];
			text[0] = plugin.replaceAllIgnoreCase(e.getLine(0), "[Time]", "[time]");
			text[1] = plugin.replaceAllIgnoreCase(e.getLine(1), "[Time]", "[time]");
			text[2] = plugin.replaceAllIgnoreCase(e.getLine(2), "[Time]", "[time]");
			text[3] = plugin.replaceAllIgnoreCase(e.getLine(3), "[Time]", "[time]");
			text[0] = plugin.replaceAllIgnoreCase(text[0], "[Weather]", "[weather]");
			text[1] = plugin.replaceAllIgnoreCase(text[1], "[Weather]", "[weather]");
			text[2] = plugin.replaceAllIgnoreCase(text[2], "[Weather]", "[weather]");
			text[3] = plugin.replaceAllIgnoreCase(text[3], "[Weather]", "[weather]");
			plugin.dynamicSignList.add(new DynamicSign(e.getBlock().getLocation(), text));
			try {
				Properties prop = new Properties();
				prop.setProperty("Location", e.getBlock().getLocation().getWorld().getName() + "," + e.getBlock().getLocation().getBlockX() + "," + e.getBlock().getLocation().getBlockY() + "," + e.getBlock().getLocation().getBlockZ());
				prop.setProperty("Text1", text[0]);
				prop.setProperty("Text2", text[1]);
				prop.setProperty("Text3", text[2]);
				prop.setProperty("Text4", text[3]);
				FileOutputStream outputStream = new FileOutputStream("plugins/EvilBook/Dynamic Signs/" + e.getBlock().getLocation().getWorld().getName() + e.getBlock().getLocation().getBlockX() + e.getBlock().getLocation().getBlockY() + e.getBlock().getLocation().getBlockZ() + ".db");
				prop.store(outputStream, null);
				outputStream.close();
			} catch (Exception e1) {
				plugin.logSevere("Failed to save dynamic sign database '" + e.getBlock().getLocation().getWorld().getName() + e.getBlock().getLocation().getBlockX() + e.getBlock().getLocation().getBlockY() + e.getBlock().getLocation().getBlockZ() + ".db'");
				e1.printStackTrace();
			}
			String time = plugin.getTime(e.getBlock().getWorld());
			String weather = plugin.getWeather(e.getBlock());
			e.setLine(0, text[0].replace("[time]", time).replace("[weather]", weather));
			e.setLine(1, text[1].replace("[time]", time).replace("[weather]", weather));
			e.setLine(2, text[2].replace("[time]", time).replace("[weather]", weather));
			e.setLine(3, text[3].replace("[time]", time).replace("[weather]", weather));
		}
	}
	
	/**
	 * Called when a hanging entity is created
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlaceEvent(HangingPlaceEvent event) {
		//
		// Region Protection
		//
		if (plugin.isInProtectedRegion(event.getBlock().getLocation(), event.getPlayer()) == true) {
			event.getPlayer().sendMessage("§cYou don't have permission to build here");
			event.setCancelled(true);
			return;
		}
		//
		// Block logging
		//
		//TODO: Re-add
	}
	
	/**
	 * Called when a vehicle moves
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onVehicleMove(VehicleMoveEvent event) {
		if (event.getVehicle().getPassenger() instanceof Player == false) return;
		Player p = (Player) event.getVehicle().getPassenger();
		for (Region region : plugin.regionList) {
			if (region.leaveMessage != null && plugin.isInRegion(region, event.getFrom()) && plugin.isInRegion(region, event.getTo()) == false) {
				p.sendMessage(region.leaveMessage.replace("&", "§"));
			} else if (region.welcomeMessage != null && plugin.isInRegion(region, event.getFrom()) == false && plugin.isInRegion(region, event.getTo())) {
				p.sendMessage(region.welcomeMessage.replace("&", "§"));
			}
			if (region.warpName != null && plugin.isInRegion(region, event.getFrom()) == false && plugin.isInRegion(region, event.getTo())) {
				//TODO: Make the player warp and come out the other side of the warp still with vehicle and momentum
				event.getVehicle().remove();
			}
		}
	}
	
	/**
	 * Called when an entity forms a block
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityBlockForm(EntityBlockFormEvent event) {
		if (!plugin.isInSurvival(event.getEntity()) && event.getEntity().getType() == EntityType.SNOWMAN) event.setCancelled(true);
	}
	
	/**
	 * Called when a hanging entity is removed
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreak(HangingBreakEvent event) {
		if (plugin.isInAdventure(event.getEntity())) {
			event.setCancelled(true);
			return;
		}
		if (!plugin.isInSurvival(event.getEntity()) && event.getCause() == RemoveCause.EXPLOSION) event.setCancelled(true);
	}
	
	/**
	 * Called when a hanging entity is removed by an entity
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		//
		// Region Protection
		//
		if (event.getRemover() instanceof Player && plugin.isInProtectedRegion(event.getEntity().getLocation(), ((Player) event.getRemover()).getPlayer()) == true) {
			((Player) event.getRemover()).getPlayer().sendMessage("§cYou don't have permission to build here");
			event.setCancelled(true);
			return;
		}
		//
		// Block logging
		//
		//TODO: Re-add
	}
	
	/**
	 * Called when an entity explodes
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (plugin.isInSurvival(event.getEntity()) == false) event.setCancelled(true);
	}
	
	/**
	 * Called when an entity changes a block except players
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.ENDERMAN || event.getEntityType() == EntityType.SILVERFISH) event.setCancelled(true);
	}
	
	/**
	 * Called when a creature is spawned
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!plugin.isInSurvival(event.getEntity()) && event.getEntityType() == EntityType.SHEEP) ((Sheep)event.getEntity()).setColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length)]);
	}

	/**
	 * Called when a vehicle is created
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onVehicleCreate(VehicleCreateEvent event) {
		//
		// Region Protection
		//
		// TODO: Make this work
		//if (plugin.isRegionProtected(event.getVehicle().getLocation(), event.getPlayer()) == true) {
			//event.getPlayer().sendMessage("§cYou don't have permission to build here");
			//event.setCancelled(true);
			//return;
		//}
		//
		// Make the boat work on land
		//
		if (event.getVehicle() instanceof Boat) {
			Boat boat = (Boat) event.getVehicle();
			boat.setWorkOnLand(true);
		}
	}
}
