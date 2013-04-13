package me.evilpeanut;

import java.io.Serializable;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("serial")
public class SerializableInventory implements Serializable {
	SerializableItemStack[] contents = new SerializableItemStack[36];
	SerializableItemStack[] armorContents = new SerializableItemStack[4];
	
	public SerializableInventory(ItemStack[] contents, ItemStack[] armorContents) {
		for (int i = 0; i < 36; i++) this.contents[i] = contents[i] == null ? null : new SerializableItemStack(contents[i]);
		for (int i = 0; i < 4; i++) this.armorContents[i] = armorContents[i] == null ? null : new SerializableItemStack(armorContents[i]);
	}
	
	public ItemStack[] getContents() {
		ItemStack[] inventoryContents = new ItemStack[36];
		for (int i = 0; i < 36; i++) inventoryContents[i] = contents[i] == null ? null : contents[i].toItemStack();
		return inventoryContents;
	}
	
	public ItemStack[] getArmorContents() {
		ItemStack[] inventoryArmorContents = new ItemStack[4];
		for (int i = 0; i < 4; i++) inventoryArmorContents[i] = armorContents[i] == null ? null : armorContents[i].toItemStack();
		return inventoryArmorContents;
	}
}
