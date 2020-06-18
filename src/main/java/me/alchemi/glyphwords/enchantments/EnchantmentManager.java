package me.alchemi.glyphwords.enchantments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Sets;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.util.NumUtil;
import me.alchemi.glyphwords.Config.Options;

public class EnchantmentManager implements Listener{

	public static EnchantmentManager instance;
	
	public enum Enchantments {
		
		XP_BOOST(EnchantmentXP.class),
		INFINITY_CROSS(EnchantmentInfinityCross.class);
		
		private Enchantment instance;
		private Class<? extends Enchantment> clazz;
		
		private Enchantments(Class<? extends Enchantment> clazz) {

			this.clazz = clazz;
			
		}
		
		public void setInstance() {
			try {
				this.instance = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		public Enchantment getInstance() {
			
			return instance;
			
		}
		
		public static Enchantment getEnchantment(String key) {
			return Enchantments.valueOf(key.toUpperCase()).getInstance();
		}
		
		public static boolean hasKey(String key) {
			return Enchantments.valueOf(key.toUpperCase()) != null;
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void applyToItem(InventoryClickEvent e) {
		if (e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
			if (e.getCursor().getType() == Options.BOOK_ITEM.asMaterial()) {
				
				ItemStack current = e.getCurrentItem().clone();
				Set<Enchantment> enchants = getEnchantments(e.getCursor());
				if (!canEnchantAny(enchants, current)) return;
				
				for (Enchantment ench : enchants) {
					current = ench.apply(current, ench.getLevel(e.getCursor()));
				}
				current = buildLore(current);
				
				e.getWhoClicked().setItemOnCursor(null);
				e.setCurrentItem(current);
				e.setResult(Result.DENY);
				
			} else {
				ItemStack current = e.getCursor().clone();
				Set<Enchantment> enchants = getEnchantments(e.getCurrentItem());
				if (!canEnchantAny(enchants, current)) return;				
				for (Enchantment ench : enchants) {
					current = ench.apply(current, ench.getLevel(e.getCurrentItem()));
				}
				current = buildLore(current);
				
				e.getWhoClicked().setItemOnCursor(current);
				e.setCurrentItem(null);
				e.setResult(Result.DENY);
				
			}
		}
	}
	
	public boolean canEnchantAny(Collection<? extends Enchantment> enchants, ItemStack item) {
		for (Enchantment e : enchants) {
			if (e.canEnchant(item)) return true;
		}
		return false;
	}
	
	public static boolean isItemEnchanted(ItemStack item) {
		return isItemEnchanted(new NBTItem(item));
	}

	public static boolean isItemEnchanted(NBTItem nbti) {
		return nbti.hasNBTData() && nbti.hasKey("glyph.enchanted");
	}
	
	public static Set<Enchantment> getEnchantments(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		if (!isItemEnchanted(nbti)) return Sets.newHashSet();
		
		Set<Enchantment> enchs = new HashSet<Enchantment>();
		NBTCompound nbtlist = nbti.getCompound("glyph.enchantments");
		for (String key : nbtlist.getKeys()) {
			if (Enchantments.hasKey(key)) {
				enchs.add(Enchantments.getEnchantment(key));
			}
		}
		return enchs;
	}
	
	public static ItemStack buildLore(ItemStack item) {
		List<String> lore = new ArrayList<String>();
		
		for (Enchantment ench : getEnchantments(item)) {
			if (ench.getMaxLevel() != (short)1) lore.add(Messenger.formatString("&r&7" + ench.getDisplayName() + " &r&7" + NumUtil.toRoman(ench.getLevel(item))));
			else lore.add(Messenger.formatString("&r&7" + ench.getDisplayName()));
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
