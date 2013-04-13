package me.evilpeanut;

import java.io.Serializable;
import org.bukkit.Material;

@SuppressWarnings("serial")
public class Planetoid implements Serializable {
	public Material coreBlk;
	public Material shellBlk;
	public byte shellThickness;
	public byte radius;
	public int xPos;
	public int yPos;
	public int zPos;

	public Planetoid() {
	}

	public Planetoid(Material coreID, Material shellID, byte shellThick, byte radius, int x, int y, int z) {
		this.coreBlk = coreID;
		this.shellBlk = shellID;
		this.shellThickness = shellThick;
		this.radius = radius;
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
	}
}