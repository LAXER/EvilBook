package me.evilpeanut;
import org.bukkit.Location;

/**
 * @author Reece Aaron Lecrivain
 */
public class DynamicSign {
	private Location location;
	private String[] lines = new String[4];
	
	/**
	 * @return The location of the sign
	 */
	Location getLocation(){ return location; }
	
	/**
	 * @param location The location of the sign
	 */
	void setLocation(Location location){ this.location = location; }
	
	/**
	 * @return The lines of text on the sign
	 */
	String[] getLines() { return lines; }
	
	/**
	 * @param lines The lines of text on the sign
	 */
	void setLines(String[] lines){ this.lines = lines; }
	
	/**
	 * Define a new dynamic sign
	 * @param location The location of the sign
	 * @param lines The lines of text on the sign
	 */
	public DynamicSign(Location location, String[] lines) {
		this.location = location;
		this.lines = lines;
	}
}
