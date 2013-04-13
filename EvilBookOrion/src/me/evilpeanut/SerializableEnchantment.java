package me.evilpeanut;

import java.io.Serializable;
import org.bukkit.enchantments.Enchantment;

@SuppressWarnings("serial")
public class SerializableEnchantment implements Serializable {
	public int Id;
	
	public SerializableEnchantment(Enchantment enchantment) {
		this.Id = enchantment.getId();
	}
	
	public Enchantment toEnchantment() {
		Enchantment enchantment = Enchantment.getById(Id);
		return enchantment;
	}
}
