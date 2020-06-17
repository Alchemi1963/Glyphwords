package me.alchemi.glyphwords;

import me.alchemi.al.configurations.IMessage;

public enum Messages implements IMessage {
	
	;

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
