package me.alchemi.glyphwords.enchantments;

import java.util.Collection;
import java.util.function.BooleanSupplier;

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
import me.alchemi.al.util.ScheduleUtil;
import me.alchemi.glyphwords.Config.EnchantmentOptions;
import me.alchemi.glyphwords.Config.Options;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.events.CrossbowLoadedEvent;

public class EnchantmentInfinityCross extends Enchantment {

	public EnchantmentInfinityCross() {
		super("infinity_cross", EnchantmentType.CUSTOM);
		this.maxLevel = 1;
		this.inOffHand = true;
		this.chance = EnchantmentOptions.INFINITY_CROSS_CHANCE.asDouble();
		this.customType = Sets.newHashSet(Material.CROSSBOW, Options.BOOK_ITEM.asMaterial());
	}

	@Override
	boolean hasConflicting(ItemStack item) {
		return item.containsEnchantment(org.bukkit.enchantments.Enchantment.MENDING);
	}
	
	@EventHandler
	public void onShoot(org.bukkit.event.entity.EntityShootBowEvent e) {
		if (e.getEntity() == null || e.getEntityType() != EntityType.PLAYER || isPlayerHolding(e.getEntity()) == null) return;
	
		CrossbowMeta meta = (CrossbowMeta) e.getBow().getItemMeta();
		if (isProjectileArrow(meta.getChargedProjectiles())) {
			Arrow arrow = (Arrow) e.getProjectile();
			arrow.setPickupStatus(PickupStatus.DISALLOWED);
		}
	}
	
	@EventHandler
	public void onDrawn(CrossbowLoadedEvent e) {
		if (isPlayerHolding(e.getPlayer()) == null && !isProjectileArrow(((CrossbowMeta)e.getBow().getItemMeta()).getChargedProjectiles())) return;
		
		ItemUtil.giveItemStack(new ItemStack(Material.ARROW), e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void interact(PlayerInteractEvent e) {
		if (isPlayerHolding(e.getPlayer()) == null || (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
		
		CrossbowMeta meta = (CrossbowMeta) e.getItem().getItemMeta();
		if (!meta.hasChargedProjectiles()) {
			ScheduleUtil.getInstance().scheduleRepeatingTask(Glyph.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					if (((CrossbowMeta)e.getItem().getItemMeta()).hasChargedProjectiles()) {
						Bukkit.getPluginManager().callEvent(new CrossbowLoadedEvent(e.getItem(), e.getPlayer()));
					}
					
				}
			}, 0, 1, new BooleanSupplier() {
				
				@Override
				public boolean getAsBoolean() {
					return ((CrossbowMeta)e.getItem().getItemMeta()).hasChargedProjectiles();
				}
			});
		}
	}
	
	private boolean isProjectileArrow(Collection<? extends ItemStack> list) {
		for (ItemStack stack : list) {
			if (stack.getType() != Material.ARROW) return false;
		}
		return true;
	}

}
