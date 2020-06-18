package me.alchemi.glyphwords.listeners;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.alchemi.glyphwords.enchantments.Enchantment;
import me.alchemi.glyphwords.util.EnchantUtil;

public class EnchantListener implements Listener {
	
	@EventHandler
	public void onEnchant(PrepareItemEnchantEvent e) {
		if (EnchantUtil.isItemEnchanted(e.getItem())) {
			Set<Enchantment> enchants = EnchantUtil.getEnchantments(e.getItem());
			
			for (Enchantment ench : enchants) {
				for (EnchantmentOffer offer : e.getOffers()) {
					if (ench.isConflicting(offer.getEnchantment())) {
						offer.setEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY);
						if (offer.getEnchantmentLevel() > 3) offer.setEnchantmentLevel(3);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onAnvil(PrepareAnvilEvent e) {
		ItemStack[] stacks = e.getInventory().getContents();
		for (int id = stacks.length-1; id >= 0; id--) {
			ItemStack i = stacks[id];
			if (i != null) {
				NBTItem n = new NBTItem(i.clone());
				if (n.hasKey("RepairCost") && n.getInteger("RepairCost") > 20) {
					n.setInteger("RepairCost", 20);
					e.getInventory().setItem(id, n.getItem());
				}
			}
	    }
		
		AnvilInventory inv = e.getInventory();
		if (!inv.getViewers().isEmpty()) {
			inv.setMaximumRepairCost(Integer.MAX_VALUE - 10);
			int rep = inv.getRepairCost();
			if (rep < Integer.MAX_VALUE - 9) {
				if (rep > 38) {
					inv.setRepairCost(38);
					rep = 38;
				}
			}
		}
	}
	
	@EventHandler
	public void onAnvilClick(InventoryClickEvent e) {
		if (e.getClickedInventory() == null || !(e.getClickedInventory() instanceof AnvilInventory) || e.getSlot() != 2) return;
		
		ItemStack item0 = e.getClickedInventory().getItem(0);
		ItemStack item1 = e.getClickedInventory().getItem(1);
		
		if (item0 != null && item1 != null) {

			if (EnchantUtil.isItemEnchanted(item0) && (!item1.getEnchantments().isEmpty() || item1.getType() == Material.ENCHANTED_BOOK)) {
				
				Set<Enchantment> enchs = EnchantUtil.getEnchantments(item0);
				Set<org.bukkit.enchantments.Enchantment> enchants;
				if (item1.getType() != Material.ENCHANTED_BOOK) enchants = item1.getEnchantments().keySet();
				else enchants = ((EnchantmentStorageMeta)item1.getItemMeta()).getStoredEnchants().keySet();
				
				for (org.bukkit.enchantments.Enchantment enchant : enchants) {
					if (e.getResult() == null) break;
					for (Enchantment ench : enchs) {
						if (ench.isConflicting(enchant)) {
							((AnvilInventory)e.getClickedInventory()).setRepairCost(0);
							e.setResult(Result.DENY);
							break;
						}
					}
				}
				
			} else if (EnchantUtil.isItemEnchanted(item1) && (!item0.getEnchantments().isEmpty() || item0.getType() == Material.ENCHANTED_BOOK)) {
				
				Set<Enchantment> enchs = EnchantUtil.getEnchantments(item1);
				Set<org.bukkit.enchantments.Enchantment> enchants;
				if (item0.getType() != Material.ENCHANTED_BOOK) enchants = item0.getEnchantments().keySet();
				else enchants = ((EnchantmentStorageMeta)item0.getItemMeta()).getStoredEnchants().keySet();
				
				for (org.bukkit.enchantments.Enchantment enchant : enchants) {
					if (e.getResult() == null) break;
					for (Enchantment ench : enchs) {
						if (ench.isConflicting(enchant)) {
							((AnvilInventory)e.getClickedInventory()).setRepairCost(0);
							e.setResult(Result.DENY);
							break;
						}
					}
				}
			}
			
		}
	}

}
