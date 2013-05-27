package me.evilpeanut;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * @author Reece Aaron Lecrivain
 */
public class Scheduler {
	private EvilBook plugin;

	/**
	 * Define a new scheduler
	 * @param evilBook The parent EvilBook plugin
	 */
	public Scheduler(EvilBook evilBook) {
		plugin = evilBook;
	}

	/**
	 * Autosave the player profiles and display a tip
	 */
	public void TipsAutosave() {
		plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
			public void run() {
				Server server = plugin.getServer();
				for (Player p : server.getOnlinePlayers()) {
					if (p.isOp()) {
						p.sendMessage("§dYou can always §l/donate §dagain for a higher rank");
						plugin.playerProfiles.get(p.getName().toLowerCase()).money += 20;
						plugin.playerProfiles.get(p.getName().toLowerCase()).setProperty("Money", Integer.toString(plugin.playerProfiles.get(p.getName().toLowerCase()).money));
					} else {
						p.sendMessage("§dDonate to become an admin §l/donate");
						plugin.playerProfiles.get(p.getName().toLowerCase()).money += 10;
						plugin.playerProfiles.get(p.getName().toLowerCase()).setProperty("Money", Integer.toString(plugin.playerProfiles.get(p.getName().toLowerCase()).money));
					}
					plugin.getProfile(p).saveProfile();
				}
			}
		}, 0L, 6000L);
	}
	
	/**
	 * Update the dynamic signs
	 */
	public void UpdateDynamicSigns() {
		plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
			public void run() {
				for (int i = 0; i < plugin.dynamicSignList.size(); i++) {
					try {
						Block block = plugin.dynamicSignList.get(i).getLocation().getWorld().getBlockAt(plugin.dynamicSignList.get(i).getLocation());
						Sign s = (Sign) block.getState();
						String time = plugin.getTime(plugin.dynamicSignList.get(i).getLocation().getWorld());
						String weather = plugin.getWeather(block);
						s.setLine(0, plugin.dynamicSignList.get(i).getLines()[0].replace("[time]", time).replace("[weather]", weather));
						s.setLine(1, plugin.dynamicSignList.get(i).getLines()[1].replace("[time]", time).replace("[weather]", weather));
						s.setLine(2, plugin.dynamicSignList.get(i).getLines()[2].replace("[time]", time).replace("[weather]", weather));
						s.setLine(3, plugin.dynamicSignList.get(i).getLines()[3].replace("[time]", time).replace("[weather]", weather));
						if (!s.getLine(0).equals(((Sign)block.getState()).getLine(0)) || !s.getLine(1).equals(((Sign)block.getState()).getLine(1)) || !s.getLine(2).equals(((Sign)block.getState()).getLine(2)) || !s.getLine(3).equals(((Sign)block.getState()).getLine(3))) {
							s.update();
						}
					} catch (Exception exception) {
						plugin.dynamicSignList.remove(i);
					}
				}
				if (plugin.EvilEdit.size() == 0) return;
				for (int i = 0; i < (plugin.EvilEdit.size() < 15 ? plugin.EvilEdit.size() : 15); i++) {
					EvilEditBlock block = (EvilEditBlock) plugin.EvilEdit.get(0);
					// TODO: Re-add logging
					if (block.getTypeID() == 0) {
						//plugin.logBlockBreak(block.getLocation().getWorld().getBlockAt(block.getLocation()), block.getPlayer());
						block.getLocation().getWorld().getBlockAt(block.getLocation()).setTypeIdAndData(0, (byte) 0, true);
					} else {
						block.getLocation().getWorld().getBlockAt(block.getLocation()).setTypeIdAndData(block.getTypeID(), block.getData(), true);
						//plugin.logBlockPlace(block.getLocation().getWorld().getBlockAt(block.getLocation()), block.getPlayer());
					}
					plugin.EvilEdit.remove(0);
				}
			}
		}, 0L, 1L);
	}
}