package me.evilpeanut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

@SuppressWarnings("serial")
public class Book implements Serializable {
	public String title, author;
	public List<String> pages = new ArrayList<String>();
	
	public Book(String title, String author, List<String> pages) {
		this.title = title;
		this.author = author;
		this.pages = pages;
	}
	
	public ItemStack toItemStack() {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
	    BookMeta meta = (BookMeta)book.getItemMeta();
	    meta.setTitle(title);
	    meta.setAuthor(author);
	    meta.setPages(pages);
	    book.setItemMeta(meta);
		return book;
	}
}
