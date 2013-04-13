package me.evilpeanut;

import org.bukkit.Location;

public class Region {
	public Boolean isProtected;
	public String regionName, welcomeMessage, leaveMessage, warpName, ownerName, allowedPlayers;
	public Location locationA, locationB;


	public Region()
	{
	}

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