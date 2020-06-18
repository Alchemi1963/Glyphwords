package me.alchemi.glyphwords;

import me.alchemi.al.configurations.IMessage;

public enum Messages implements IMessage {
	
	RARITYDISPLAYBOOK("rarityDisplayBook"),
	RARITIES_TIER1("rarities.tier1"),
	RARITIES_TIER2("rarities.tier2"),
	RARITIES_TIER3("rarities.tier3"),
	RARITIES_TIER4("rarities.tier4"),
	RARITIES_TIER5("rarities.tier5");

	String value;
	final String key;
	
	private Messages(String key) {
		this.key = key;
	}
	
	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String value() {
		return value;
	}
	
	@Override
	public String key() {
		return key;
	}
	
	@Override
	public String toString() {
		return value();
	}
}
