package me.evilpeanut;

import org.bukkit.TreeType;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public enum Spell {
	LightningStrikeI("Lightning Strike I"),
	InstantGrow("Instant Grow"),
	ShockWaveI("Shock Wave I");
	
	String name;
	
	Spell (String name) {
		this.name = name;
	}
	
	public void preformSpell(PlayerInteractEvent event) {
		//
		// Lightning Strike I
		//
		if (name == "Lightning Strike I") {
			event.getPlayer().getWorld().strikeLightning(event.getClickedBlock().getLocation());
		}
		//
		// Instant Grow
		//
		if (name == "Instant Grow") {
			if (event.getClickedBlock().getTypeId() == 39) {
				event.getClickedBlock().setTypeId(0);
				if (event.getPlayer().getWorld().generateTree(event.getClickedBlock().getLocation(), TreeType.BROWN_MUSHROOM) == false) {
					event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(39));
				}
			}
			if (event.getClickedBlock().getTypeId() == 40) {
				event.getClickedBlock().setTypeId(0);
				if (event.getPlayer().getWorld().generateTree(event.getClickedBlock().getLocation(), TreeType.RED_MUSHROOM) == false) {
					event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(40));
				}
			}
			if (event.getClickedBlock().getTypeId() == 6) {
				if (event.getClickedBlock().getData() == 0) {
					event.getClickedBlock().setTypeId(0);
					if (event.getPlayer().getWorld().generateTree(event.getClickedBlock().getLocation(), TreeType.TREE) == false) {
						event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(6));
					}
				}
				if (event.getClickedBlock().getData() == 1) {
					event.getClickedBlock().setTypeId(0);
					if (event.getPlayer().getWorld().generateTree(event.getClickedBlock().getLocation(), TreeType.REDWOOD) == false) {
						event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(6, 1, (byte) 1));
					}
				}
				if (event.getClickedBlock().getData() == 2) {
					event.getClickedBlock().setTypeId(0);
					if (event.getPlayer().getWorld().generateTree(event.getClickedBlock().getLocation(), TreeType.BIRCH) == false) {
						event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(6, 1, (byte) 2));
					}
				}
				if (event.getClickedBlock().getData() == 3) {
					event.getClickedBlock().setTypeId(0);
					if (event.getPlayer().getWorld().generateTree(event.getClickedBlock().getLocation(), TreeType.JUNGLE) == false) {
						event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(6, 1, (byte) 3));
					}
				}
			}
		}
		//
		// Shockwave I
		//
		if (name == "Shock Wave I") {
			for (Entity e : event.getPlayer().getNearbyEntities(3, 3, 3)) {
				e.setVelocity(new Vector(e.getLocation().getX() >= event.getPlayer().getLocation().getX() ? 2 : -2, 
						e.getLocation().getY() >= event.getPlayer().getLocation().getY() ? 2 : -2, 
								e.getLocation().getZ() >= event.getPlayer().getLocation().getZ() ? 2 : -2));
			}
		}
	}
}
