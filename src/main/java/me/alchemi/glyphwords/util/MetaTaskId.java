package me.alchemi.glyphwords.util;

import org.bukkit.metadata.MetadataValueAdapter;
import org.jetbrains.annotations.Nullable;

import me.alchemi.glyphwords.Glyph;

public class MetaTaskId extends MetadataValueAdapter{

	private final int taskId;
	
	public MetaTaskId(int taskId) {
		super(Glyph.getInstance());
		this.taskId = taskId;
	}
	
	@Override
	public @Nullable Object value() {
		return taskId;
	}
	
	@Override
	public void invalidate() {
	}
	
}