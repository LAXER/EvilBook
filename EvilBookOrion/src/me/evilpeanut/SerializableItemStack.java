package me.evilpeanut;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

@SuppressWarnings("serial")
public class SerializableItemStack implements Serializable {
	public int amount, typeID;
	public byte data;
	public short durability;
	public String displayName;
	public Map<SerializableEnchantment, Integer> enchantmentList = new HashMap<SerializableEnchantment, Integer>();
	public List<String> lore;
	
	public SerializableItemStack(ItemStack itemStack) {
		amount = itemStack.getAmount();
		durability = itemStack.getDurability();
		for (int i = 0; i < itemStack.getEnchantments().size(); i++) enchantmentList.put(new SerializableEnchantment((Enchantment)itemStack.getEnchantments().keySet().toArray()[i]), (Integer)itemStack.getEnchantments().values().toArray()[i]);
		typeID = itemStack.getTypeId();
		data = itemStack.getData().getData();
		displayName = itemStack.getItemMeta() == null ? null : itemStack.getItemMeta().getDisplayName();
		lore = itemStack.getItemMeta() == null ? null : itemStack.getItemMeta().getLore();
	}
	
	public ItemStack toItemStack() {
		ItemStack newStack = new ItemStack(typeID, amount);
		newStack.setData(new MaterialData(typeID, data));
		newStack.setDurability(durability);
		
		for (int i = 0; i < enchantmentList.size(); i++) newStack.addEnchantment(((SerializableEnchantment)enchantmentList.keySet().toArray()[i]).toEnchantment(), (Integer)enchantmentList.values().toArray()[i]);
		
		ItemMeta newMeta = newStack.getItemMeta();
		if (lore != null) newMeta.setLore(lore);
		if (displayName != null) newMeta.setDisplayName(displayName);
		newStack.setItemMeta(newMeta);
		return newStack;
	}
}
