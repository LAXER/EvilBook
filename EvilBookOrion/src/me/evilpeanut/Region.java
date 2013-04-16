package me.evilpeanut;

import org.bukkit.Location;

/**
 * @author Reece Aaron Lecrivain
 */
public class Region {
	public Boolean isProtected;
	public String regionName, welcomeMessage, leaveMessage, warpName, ownerName, allowedPlayers;
	public Location locationA, locationB;

	/**
	 * Define a new region
	 * @param regionName The name of the region
	 * @param locationA The first location point of the region
	 * @param locationB The second location point of the region
	 * @param isProtected If protection is enabled in the region
	 * @param ownerName The region owner's name
	 * @param welcomeMessage The welcome message of the region
	 * @param leaveMessage The leave message of the region
	 * @param allowedPlayers The players who have rights to the region
	 */
	public Region(String regionName, Location locationA, Location locationB, Boolean isProtected, String ownerName, String welcomeMessage, String leaveMessage, String allowedPlayers)
	{
		this.regionName = regionName;
		this.locationA = locationA;
		this.locationB = locationB;
		this.isProtected = isProtected;
		this.ownerName = ownerName;
		this.welcomeMessage = welcomeMessage;
		this.leaveMessage = leaveMessage;
		this.allowedPlayers = allowedPlayers;
	}
}