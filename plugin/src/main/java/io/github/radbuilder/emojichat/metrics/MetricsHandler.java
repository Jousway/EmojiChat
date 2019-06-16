package io.github.radbuilder.emojichat.metrics;

import io.github.radbuilder.emojichat.EmojiChat;

import java.util.HashMap;
import java.util.Map;

/**
 * Metrics handler class.
 *
 * @author RadBuilder
 * @version 1.8
 * @since 1.4
 */
public class MetricsHandler {
	/**
	 * The number of emojis used.
	 */
	private int emojisUsed;
	/**
	 * The number of shortcuts used.
	 */
	private int shortcutsUsed;
	/**
	 * The {@link MetricsLevel} being used.
	 */
	private MetricsLevel metricsLevel;
	
	/**
	 * Creates the metrics handler class with the main class instance.
	 *
	 * @param plugin The EmojiChat main class instance.
	 */
	public MetricsHandler(EmojiChat plugin) {
		emojisUsed = 0;
		shortcutsUsed = 0;
	}
	
	/**
	 * Adds the specified number of emojis used to {@link #emojisUsed}.
	 *
	 * @param emojisUsed The number of emojis used to add to {@link #emojisUsed}.
	 */
	public void addEmojiUsed(int emojisUsed) {
		this.emojisUsed += emojisUsed;
	}
	
	/**
	 * Adds the specified number of shortcuts used to {@link #shortcutsUsed}.
	 *
	 * @param shortcutsUsed The number of shortcuts used to add to {@link #shortcutsUsed}.
	 */
	public void addShortcutUsed(int shortcutsUsed) {
		this.shortcutsUsed += shortcutsUsed;
	}
}

/**
 * The different levels of metrics data collection.
 *
 * @author RadBuilder
 * @version 1.5
 * @since 1.5
 */
enum MetricsLevel {
	OFF;
}