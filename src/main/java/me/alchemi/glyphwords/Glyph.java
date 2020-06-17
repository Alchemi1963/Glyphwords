package me.alchemi.glyphwords;

import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.base.PluginBase;

public class Glyph extends PluginBase {
	
	private static Glyph instance;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		setMessenger(new Messenger(this));
		
		try {
			new Config();
			messenger.print("&6Configs enabled!");
		} catch (IOException | InvalidConfigurationException e) {
			System.err.println("[PLUGIN]: Could not enable config files.\nDisabling plugin...");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
		
		enableCommands();
		
		messenger.print("ENABLED");
	}
	
	@Override
	public void onDisable() {
		messenger.print("DISABLED");
	}
	
	public void enableCommands() {
		//getCommand("COMMAND").setExecutor(new Command());
		
		//getCommand("COMMAND").setTabCompleter(new TabCompleter());
	}
	
	public static Glyph getInstance() {
		return instance;
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
}
