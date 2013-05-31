package me.evilpeanut;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * @author Reece Aaron Lecrivain
 */
@SuppressWarnings("serial")
public class MarketItemStack implements Serializable {
	public String seller;
	public byte itemData;
	public int price, amount, itemID;
	public Map<Integer, Integer> enchantmentList = new HashMap<Integer, Integer>();
	
	public MarketItemStack(String seller, int price, int amount, int itemID, byte itemData, Map<Integer, Integer> enchantmentList) {
		this.seller = seller;
		this.price = price;
		this.amount = amount;
		this.itemID = itemID;
		this.itemData = itemData;
		this.enchantmentList = enchantmentList;
	}
	
	public ItemStack toItemStack(int amount) {
		ItemStack newStack = new ItemStack(itemID, amount);
		newStack.setData(new MaterialData(itemID, itemData));
		for (int i = 0; i < enchantmentList.size(); i++) newStack.addEnchantment(Enchantment.getById((int)enchantmentList.keySet().toArray()[i]), (Integer)enchantmentList.values().toArray()[i]);
		ItemMeta newMeta = newStack.getItemMeta();
		newStack.setItemMeta(newMeta);
		return newStack;
	}
}
