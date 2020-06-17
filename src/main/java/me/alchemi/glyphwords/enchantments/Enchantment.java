package me.alchemi.glyphwords.enchantments;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.util.Hand;

public abstract class Enchantment implements Listener {

	protected final String registryName;
	protected String displayName;
	protected short maxLevel; 
	protected EnchantmentType type;
	protected Set<Material> customType;
	protected boolean inOffHand = false;
	
	public Enchantment(String registryName, short maxLevel, EnchantmentType type) {

		this.registryName = registryName;
		this.displayName = Glyph.getInstance().getMessenger().get("enchantment." + registryName);
		if (displayName.isEmpty()) displayName = registryName;
		this.maxLevel = maxLevel;
		
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
