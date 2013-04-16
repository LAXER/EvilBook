package me.evilpeanut;

import java.io.Serializable;
import org.bukkit.enchantments.Enchantment;

/**
 * @author Reece Aaron Lecrivain
 */
@SuppressWarnings("serial")
public class SerializableEnchantment implements Serializable {
	private int Id;
	
	/**
	 * Define a new serializable enchantment
	 * @param enchantment The enchantment to serialize
	 */
	public SerializableEnchantment(Enchantment enchantment) {
		this.Id = enchantment.getId();
	}
	
	/**
	 * Return the serialized enchantment as an enchantment
	 * @return The enchantment
	 */
	public Enchantment toEnchantment() {
		Enchantment enchantment = Enchantment.getById(Id);
		return enchantment;
	}
}
