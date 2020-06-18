package me.alchemi.glyphwords.enchantments;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.glyphwords.Config.ConfigEnum;
import me.alchemi.glyphwords.Config.Options;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.Messages;
import me.alchemi.glyphwords.util.Hand;
import me.alchemi.glyphwords.util.Rarity;

public abstract class Enchantment implements Listener {

	protected final String registryName;
	protected final EnchantmentType type;
	protected Set<Material> customType;
	
	protected String displayName;
	
	protected short maxLevel = 1;
	protected boolean inOffHand = false;
	protected double chance = 50d;
	
	public Enchantment(String registryName, EnchantmentType type) {

		this.registryName = registryName;
		this.displayName = ConfigEnum.ENCHANTMENTS.getConfig().getString(String.join(".", registryName, "displayName"));
		if (displayName.isEmpty()) displayName = registryName;
		
		this.type = type;
		
		Bukkit.getPluginManager().registerEvents(this, Glyph.getInstance());
		
		Glyph.getInstance().getMessenger().print("Initialised " + displayName);
		
	}
	
	abstract boolean hasConflicting(ItemStack item);
	
	public boolean canEnchant(ItemStack item) {
		return !(!type.testItem(item) || hasConflicting(item) || getLevel(item) == maxLevel);
	}
	
	public ItemStack apply(ItemStack item, short level) {
		if (!canEnchant(item)) return item;
		
		NBTItem nbti = new NBTItem(item.clone());
		if (nbti.hasNBTData() && nbti.hasKey("glyph.enchanted")) {
			NBTCompound nbtlist = nbti.getCompound("glyph.enchantments");
			if (nbtlist.hasKey(registryName)) {
				short currentLevel = getLevel(item);
				if (currentLevel == level && level + 1 < maxLevel) {
					nbtlist.getCompound(registryName).setShort("level", (short) (level + 1));
				} else if (currentLevel < level) {
					nbtlist.getCompound(registryName).setShort("level", level);
				}
				return nbti.getItem();
			}
		} else {
			nbti.setBoolean("glyph.enchanted", true);
			NBTCompound nbtc = nbti.addCompound("glyph.enchantments").addCompound(registryName);
			nbtc.setShort("level", level);
			return nbti.getItem();
		}
		return item;
	}
	
	public ItemStack createBook(short level) {
		ItemStack book = apply(new ItemStack(Options.BOOK_ITEM.asMaterial()), level);
		book = EnchantmentManager.buildLore(book);
		
		ItemMeta meta = book.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add(Messenger.formatString(Messages.RARITYDISPLAYBOOK.toString() + Rarity.getRarity(chance)));
		meta.setLore(lore);
		book.setItemMeta(meta);
		System.out.println(lore);
		return book;
	}
	
	public short getLevel(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		if (nbti.hasNBTData() && nbti.hasKey("glyph.enchanted")) {
			NBTCompound nbtlist = nbti.getCompound("glyph.enchantments");
			if (nbtlist.hasKey(registryName)) {
				return nbtlist.getCompound(registryName).getShort("level");
			}
		}
		return -1;
	}
	
	public boolean hasItem(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		return EnchantmentManager.isItemEnchanted(nbti) && nbti.getCompound("glyph.enchantments").hasKey(registryName);
	}
	
	public Hand isPlayerHolding(Player player) {
		
		if (inOffHand) {
			return hasItem(player.getInventory().getItemInMainHand()) ? Hand.MAIN_HAND : hasItem(player.getInventory().getItemInOffHand()) ? Hand.OFF_HAND : null;
		} else {
			return hasItem(player.getInventory().getItemInMainHand()) ? Hand.MAIN_HAND : null;
		}
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public short getMaxLevel() {
		return maxLevel;
	}
	
	public String getRegistryName() {
		return registryName;
	}
	
}
