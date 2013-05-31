package me.evilpeanut;

import org.bukkit.Location;

/**
 * @author Reece Aaron Lecrivain
 */
public class EvilEditBlock {
	private int typeID;
	private byte data = 0;
	private Location location;

	/**
	 * @return The location of the block
	 */
	Location getLocation(){ return location; }

	/**
	 * @return The data of the block
	 */
	Byte getData(){ return data; }
	
	/**
	 * @return The type ID of the block
	 */
	int getTypeID(){ return typeID; }

	/**
	 * Define a new evil edit block with a data value of 0
	 * @param typeID The type ID of the block
	 * @param location The location of the block
	 */
	public EvilEditBlock(int typeID, Location location) {
		this.typeID = typeID;
		this.location = location;
	}
	
	/**
	 * Define a new evil edit block
	 * @param typeID The type ID of the block
	 * @param data The data of the block
	 * @param location The location of the block
	 */
	public EvilEditBlock(int typeID, byte data, Location location) {
		this.typeID = typeID;
		this.data = data;
		this.location = location;
	}
}
