package me.alchemi.glyphwords.enchantments;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import com.google.common.collect.Sets;

import me.alchemi.al.util.ItemUtil;
import me.alchemi.glyphwords.Config.EnchantmentOptions;
import me.alchemi.glyphwords.Config.Options;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.events.CrossbowLoadedEvent;
import me.alchemi.glyphwords.util.MetaTaskId;

public class EnchantmentInfinityCross extends Enchantment {

	public EnchantmentInfinityCross() {
		super("infinity_cross", EnchantmentType.CUSTOM);
		this.maxLevel = 1;
		this.inOffHand = true;
		this.chance = EnchantmentOptions.INFINITY_CROSS_CHANCE.asDouble();
		this.customType = Sets.newHashSet(Material.CROSSBOW, Options.BOOK_ITEM.asMaterial());
	}

	@Override
	public boolean hasConflicting(ItemStack item) {
		return item.containsEnchantment(org.bukkit.enchantments.Enchantment.MENDING);
	}
	
	@Override
	public boolean isConflicting(org.bukkit.enchantments.Enchantment enchant) {
		return enchant.equals(org.bukkit.enchantments.Enchantment.MENDING);
	}
	
	@Override
	public boolean isConflicting(Enchantment enchant) {
		return true;
	}
	
	@EventHandler
	public void onShoot(org.bukkit.event.entity.EntityShootBowEvent e) {
		if (e.getEntity() == null || e.getEntityType() != EntityType.PLAYER || !isPlayerHolding(e.getEntity())) return;
	
		CrossbowMeta meta = (CrossbowMeta) e.getBow().getItemMeta();
		if (isProjectileArrow(meta.getChargedProjectiles())) {
			Arrow arrow = (Arrow) e.getProjectile();
			arrow.setPickupStatus(PickupStatus.DISALLOWED);
		}
	}
	
	@EventHandler
	public void onDrawn(CrossbowLoadedEvent e) {
		if (!isPlayerHolding(e.getPlayer()) && !isProjectileArrow(e.getProjectiles())) return;
		
		ItemUtil.giveItemStack(new ItemStack(Material.ARROW), e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void interact(PlayerInteractEvent e) {
		if (!isPlayerHolding(e.getPlayer()) || (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
		
		CrossbowMeta meta = (CrossbowMeta) e.getItem().getItemMeta();
		if (!meta.hasChargedProjectiles()) {
			long currentTicks = e.getPlayer().getWorld().getFullTime();
			
			if (e.getPlayer().hasMetadata("charging_crossbow")) {
				Bukkit.getScheduler().cancelTask(e.getPlayer().getMetadata("charging_crossbow").get(0).asInt());
				e.getPlayer().removeMetadata("charging_crossbow", Glyph.getInstance());
			}
			
			e.getPlayer().setMetadata("charging_crossbow", new MetaTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(Glyph.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					if (((CrossbowMeta)e.getItem().getItemMeta()).hasChargedProjectiles()) {
						Bukkit.getScheduler().cancelTask(e.getPlayer().getMetadata("charging_crossbow").get(0).asInt());
						e.getPlayer().removeMetadata("charging_crossbow", Glyph.getInstance());
						Bukkit.getPluginManager().callEvent(new CrossbowLoadedEvent(e.getItem(), e.getPlayer()));
					} else if (currentTicks - e.getPlayer().getWorld().getFullTime() >= 40) {
						Bukkit.getScheduler().cancelTask(e.getPlayer().getMetadata("charging_crossbow").get(0).asInt());
						e.getPlayer().removeMetadata("charging_crossbow", Glyph.getInstance());
					}
					
				}
			}, 0, 1)));
		}
	}
	
	private boolean isProjectileArrow(Collection<? extends ItemStack> list) {
		for (ItemStack stack : list) {
			if (stack.getType() != Material.ARROW) return false;
		}
		return true;
	}

}
