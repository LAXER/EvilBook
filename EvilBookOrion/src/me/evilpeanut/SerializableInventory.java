package me.evilpeanut;

import java.io.Serializable;
import org.bukkit.inventory.ItemStack;

/**
 * @author Reece Aaron Lecrivain
 */
@SuppressWarnings("serial")
public class SerializableInventory implements Serializable {
	private SerializableItemStack[] contents = new SerializableItemStack[36];
	private SerializableItemStack[] armorContents = new SerializableItemStack[4];
	
	/**
	 * Define a new serializable inventory
	 * @param contents The contents of the inventory
	 * @param armorContents The armor contents of the inventory
	 */
	public SerializableInventory(ItemStack[] contents, ItemStack[] armorContents) {
		for (int i = 0; i < 36; i++) this.contents[i] = contents[i] == null ? null : new SerializableItemStack(contents[i]);
		for (int i = 0; i < 4; i++) this.armorContents[i] = armorContents[i] == null ? null : new SerializableItemStack(armorContents[i]);
	}
	
	/**
	 * Return the contents of the inventory
	 * @return The contents of the inventory
	 */
	public ItemStack[] getContents() {
		ItemStack[] inventoryContents = new ItemStack[36];
		for (int i = 0; i < 36; i++) inventoryContents[i] = contents[i] == null ? null : contents[i].toItemStack();
		return inventoryContents;
	}
	
	/**
	 * Return the armor contents of the inventory
	 * @return The armor contents of the inventory
	 */
	public ItemStack[] getArmorContents() {
		ItemStack[] inventoryArmorContents = new ItemStack[4];
		for (int i = 0; i < 4; i++) inventoryArmorContents[i] = armorContents[i] == null ? null : armorContents[i].toItemStack();
		return inventoryArmorContents;
	}
}
