package me.alchemi.glyphwords;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;

import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.al.objects.base.ConfigBase;

public class Config extends ConfigBase {

	public Config() throws FileNotFoundException, IOException, InvalidConfigurationException {
		super(Glyph.getInstance());
		
		config = ConfigEnum.CONFIG.getConfig();
		
	}

	public static enum ConfigEnum implements IConfigEnum{
		CONFIG(new File(Glyph.getInstance().getDataFolder(), "config.yml"), 2),
		ENCHANTMENTS(new File(Glyph.getInstance().getDataFolder(), "enchantments.yml"), 7);

		final File file;
		final int version;
		SexyConfiguration config;
		
		private ConfigEnum(File file, int version) {
			this.file = file;
			this.version = version;
			this.config = SexyConfiguration.loadConfiguration(file);
		}
		
		@Override
		public SexyConfiguration getConfig() {
			return config;
		}

		@Override
		public File getFile() {
			return file;
		}

		@Override
		public int getVersion() {
			return version;
		}
	}
	
	public static SexyConfiguration config;
	
	public static enum Options implements IConfig {
		
		BOOK_ITEM("BookItem"),
		RARITYTIERS_TIER1("RarityTiers.Tier1"),
		RARITYTIERS_TIER2("RarityTiers.Tier2"),
		RARITYTIERS_TIER3("RarityTiers.Tier3"),
		RARITYTIERS_TIER4("RarityTiers.Tier4");
		
		private Object value;
		public final String key;
		
		Options(String key){
			this.key = key;
			get();
		}

		@Override
		public Object value() {
			return value;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@Override
		public void get() {
			value = getConfig().get(key);
		}
	}
	
	public static enum EnchantmentOptions implements IConfig {
		
		XP_BOOST_MAXLEVEL("xp_boost.maxLevel"),
		XP_BOOST_MULTIPLIER("xp_boost.multiplier"),
		XP_BOOST_CHANCE("xp_boost.chance"),
		INFINITY_CROSS_CHANCE("infinity_cross.chance"),
		POWER_CROSS_MAXLEVEL("power_cross.maxLevel"),
		POWER_CROSS_CHANCE("power_cross.chance"),
		POWER_CROSS_POWERINCREASE("power_cross.powerIncrease"),
		EXPLOSIVE_TIP_CHANCE("explosive_tip.chance"),
		EXPLOSIVE_TIP_EXPLOSIONCHANCE("explosive_tip.explosionChance"),
		EXPLOSIVE_TIP_DAMAGEBLOCKS("explosive_tip.damageBlocks"),
		EXPLOSIVE_TIP_SETFIRE("explosive_tip.setFire"),
		EXPLOSIVE_TIP_POWER("explosive_tip.power"),
		DAMAGE_FULLMOON_CHANCE("damage_fullmoon.chance"),
		DAMAGE_FULLMOON_MAXLEVEL("damage_fullmoon.maxLevel"),
		DAMAGE_NOON_CHANCE("damage_noon.chance"),
		DAMAGE_NOON_MAXLEVEL("damage_noon.maxLevel"),
		EXTRA_SPEED_CHANCE("extra_speed.chance"),
		EXTRA_SPEED_MAXLEVEL("extra_speed.maxLevel"),
		EXTRA_SPEED_DIVISION("extra_speed.division"),
		ATTACK_FAST_CHANCE("attack_fast.chance"),
		ATTACK_FAST_MAXLEVEL("attack_fast.maxLevel"),
		ATTACK_FAST_DIVISION("attack_fast.division");
		
		private Object value;
		public final String key;
		
		EnchantmentOptions(String key){
			this.key = key;
			get();
		}
				
		@Override
		public Object value() {
			return value;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.ENCHANTMENTS.getConfig();
		}

		@Override
		public void get() {
			value = getConfig().get(key);
		}
	}
	
	@Override
	protected IConfigEnum[] getConfigs() {
		return ConfigEnum.values();
	}

	@Override
	protected Set<IConfig> getEnums() {
		return new HashSet<ConfigBase.IConfig>() {
			
			{
				addAll(Arrays.asList(Options.values()));
				addAll(Arrays.asList(EnchantmentOptions.values()));
			}
			
		};
	}
}
