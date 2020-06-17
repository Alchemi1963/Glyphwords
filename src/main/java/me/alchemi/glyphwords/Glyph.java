package me.alchemi.glyphwords;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.base.PluginBase;
import me.alchemi.glyphwords.commands.GiveCommand;
import me.alchemi.glyphwords.enchantments.EnchantmentManager;
import me.alchemi.glyphwords.enchantments.EnchantmentManager.Enchantments;

public class Glyph extends PluginBase {
	
	private static Glyph instance;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		setMessenger(new Messenger(this));
		messenger.setMessages(Messages.values());
		
		try {
			new Config();
			messenger.print("&6Configs enabled!");
		} catch (IOException | InvalidConfigurationException e) {
			System.err.println("[Glyphwords]: Could not enable config files.\nDisabling plugin...");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
		
		enableCommands();
		
		EnchantmentManager.instance = new EnchantmentManager();
		Bukkit.getPluginManager().registerEvents(EnchantmentManager.instance, this);		
		
		enableEnchantments();
		
		messenger.print("Enabled Glyphwords");
	}
	
	public void enableEnchantments() {
		
		for (Enchantments enchant : Enchantments.values()) {
			enchant.setInstance();
		}
		
	}

	@Override
	public void onDisable() {
		messenger.print("Disabled Glyphwords");
	}
	
	public void enableCommands() {
		getCommand("giveenchant").setExecutor(new GiveCommand());
		
		//getCommand("COMMAND").setTabCompleter(new TabCompleter());
	}
	
	public static Glyph getInstance() {
		return instance;
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
}
