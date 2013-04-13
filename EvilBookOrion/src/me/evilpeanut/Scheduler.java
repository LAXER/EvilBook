package me.evilpeanut;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class Scheduler {
	private EvilBook plugin;

	public Scheduler(EvilBook evilBook) {
		plugin = evilBook;
	}

	public void TipsAutosave() {
		plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
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
	
	public void EvilEditSigns() {
		plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
			public void run() {
				for (int i = 0; i < plugin.dynamicSignList.size(); i++) {
					try {
						Block block = plugin.dynamicSignList.get(i).location.getWorld().getBlockAt(plugin.dynamicSignList.get(i).location);
						Sign s = (Sign) block.getState();
						String time = plugin.getTime(plugin.dynamicSignList.get(i).location);
						String weather = plugin.getWeather(block);
						s.setLine(0, plugin.dynamicSignList.get(i).text[0].replace("[time]", time).replace("[weather]", weather));
						s.setLine(1, plugin.dynamicSignList.get(i).text[1].replace("[time]", time).replace("[weather]", weather));
						s.setLine(2, plugin.dynamicSignList.get(i).text[2].replace("[time]", time).replace("[weather]", weather));
						s.setLine(3, plugin.dynamicSignList.get(i).text[3].replace("[time]", time).replace("[weather]", weather));
						if (!s.getLine(0).equals(((Sign)block.getState()).getLine(0)) || !s.getLine(1).equals(((Sign)block.getState()).getLine(1)) || !s.getLine(2).equals(((Sign)block.getState()).getLine(2)) || !s.getLine(3).equals(((Sign)block.getState()).getLine(3))) {
							s.update();
						}
					} catch (Exception e) {
						plugin.dynamicSignList.remove(i);
					}
				}
				for (int i = 0; i < 15; i++) {
					if (plugin.EvilEdit.size() == 0) return;
					EvilEditBlock block = (EvilEditBlock) plugin.EvilEdit.get(0);
					block.location.getWorld().getBlockAt(block.location).setTypeIdAndData(block.typeID, block.data, true);
					// TODO: Add log support
					plugin.EvilEdit.remove(0);
				}
			}
		}, 0L, 1L);
	}
}