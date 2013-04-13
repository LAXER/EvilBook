package me.evilpeanut;

import org.bukkit.Location;

public class EvilEditBlock {
	int typeID;
	byte data;
	Location location;
	
	public EvilEditBlock(int typeID, Location location) {
		this.typeID = typeID;
		this.data = 0;
		this.location = location;
	}
	
	public EvilEditBlock(int typeID, byte data, Location location) {
		this.typeID = typeID;
		this.data = data;
		this.location = location;
	}
}
