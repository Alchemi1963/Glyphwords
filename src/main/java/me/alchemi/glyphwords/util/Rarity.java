package me.alchemi.glyphwords.util;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.glyphwords.Config.Options;
import me.alchemi.glyphwords.Messages;

public class Rarity {

	public static String getRarity(double percentage) {
		if (percentage > 100f) throw new IllegalArgumentException("Percentage cannot be higher than 100%");
		
		else if (percentage >= Options.RARITYTIERS_TIER1.asDouble()) return Messenger.formatString(Messages.RARITIES_TIER1.toString());
		else if (percentage >= Options.RARITYTIERS_TIER2.asDouble()) return Messenger.formatString(Messages.RARITIES_TIER2.toString());
		else if (percentage >= Options.RARITYTIERS_TIER3.asDouble()) return Messenger.formatString(Messages.RARITIES_TIER3.toString());
		else if (percentage >= Options.RARITYTIERS_TIER4.asDouble()) return Messenger.formatString(Messages.RARITIES_TIER5.toString());
		else return Messenger.formatString(Messages.RARITIES_TIER5.toString());
	}
	
	
	public static String getRarity(double value, double maxValue) {
		return getRarity(value/maxValue * 100d);
	}
}
