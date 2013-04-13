package me.evilpeanut;
import org.bukkit.Location;

public class DynamicSign {
	public Location location;
	public String[] text = new String[4];
	
	public DynamicSign(Location location, String[] text) {
		this.location = location;
		this.text = text;
	}
}
