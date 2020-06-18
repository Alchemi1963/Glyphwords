package me.alchemi.glyphwords.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.jetbrains.annotations.NotNull;

public class CrossbowLoadedEvent extends Event {

	public static final HandlerList handlers = new HandlerList();

	private final ItemStack bow;
	private final Player player;
	
	public CrossbowLoadedEvent(ItemStack bow, Player player) {
		this.bow = bow;
		this.player = player;
	}
	
	@Override
	public @NotNull String getEventName() {
		return "CrossbowLoadedEvent";
	}
	
	public ItemStack getBow() {
		return bow;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public List<ItemStack> getProjectiles() {
		return ((CrossbowMeta)bow.getItemMeta()).getChargedProjectiles();
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
	
	@NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
}
