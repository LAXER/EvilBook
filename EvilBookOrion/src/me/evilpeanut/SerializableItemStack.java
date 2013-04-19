package me.evilpeanut;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * @author Reece Aaron Lecrivain
 */
@SuppressWarnings("serial")
public class SerializableItemStack implements Serializable {
	private int amount, typeID;
	private byte data;
	private short durability;
	private String displayName;
	private Map<Integer, Integer> enchantmentList = new HashMap<Integer, Integer>();
	private List<String> lore;
	
	/**
	 * Define a new serializable item stack
	 * @param itemStack The item stack to serialize
	 */
	public SerializableItemStack(ItemStack itemStack) {
		amount = itemStack.getAmount();
		durability = itemStack.getDurability();
		for (int i = 0; i < itemStack.getEnchantments().size(); i++) enchantmentList.put(((Enchantment)itemStack.getEnchantments().keySet().toArray()[i]).getId(), (Integer)itemStack.getEnchantments().values().toArray()[i]);
		typeID = itemStack.getTypeId();
		data = itemStack.getData().getData();
		displayName = itemStack.getItemMeta() == null ? null : itemStack.getItemMeta().getDisplayName();
		lore = itemStack.getItemMeta() == null ? null : itemStack.getItemMeta().getLore();
	}
	
	/**
	 * Return the serialized item stack as an item stack
	 * @return The serialized item stack as an item stack
	 */
	public ItemStack toItemStack() {
		ItemStack newStack = new ItemStack(typeID, amount);
		newStack.setData(new MaterialData(typeID, data));
		newStack.setDurability(durability);
		for (int i = 0; i < enchantmentList.size(); i++) newStack.addEnchantment(Enchantment.getById((int)enchantmentList.keySet().toArray()[i]), (Integer)enchantmentList.values().toArray()[i]);
		ItemMeta newMeta = newStack.getItemMeta();
		if (lore != null) newMeta.setLore(lore);
		if (displayName != null) newMeta.setDisplayName(displayName);
		newStack.setItemMeta(newMeta);
		return newStack;
	}
}
