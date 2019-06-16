package io.github.radbuilder.emojichat;

import io.github.radbuilder.emojichat.metrics.MetricsHandler;
import io.github.radbuilder.emojichat.utils.EmojiChatConfigUpdater;
import io.github.radbuilder.emojichat.utils.EmojiChatUpdateChecker;
import io.github.radbuilder.emojichat.utils.EmojiHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * EmojiChat main class.
 *
 * @author RadBuilder
 * @version 1.7
 * @since 1.0
 */
public class EmojiChat extends JavaPlugin {
	/**
	 * The emoji handler that stores emoji data.
	 */
	private EmojiHandler emojiHandler;
	/**
	 * The EmojiChat update checker.
	 */
	EmojiChatUpdateChecker updateChecker;
	/**
	 * The metrics data handler.
	 */
	private MetricsHandler metricsHandler;
	
	@Override
	public void onEnable() {
		if (!new File(getDataFolder(), "config.yml").exists()) { // If there's not a config, make one
			saveDefaultConfig();
		} else {
			new EmojiChatConfigUpdater(this); // If there is a config, see if it can be updated
		}
		
		updateChecker = new EmojiChatUpdateChecker(this);
		emojiHandler = new EmojiHandler(this);
		
		metricsHandler = new MetricsHandler(this); // Creates the metrics handler for metrics gathering
		
		// Register the chat listener
		Bukkit.getPluginManager().registerEvents(new EmojiChatListener(this), this);
		
		// Register the "emojichat" and "ec" commands
		EmojiChatCommand emojiChatCommand = new EmojiChatCommand(this);
		EmojiChatTabComplete emojiChatTabComplete = new EmojiChatTabComplete();
		getCommand("emojichat").setExecutor(emojiChatCommand);
		getCommand("emojichat").setTabCompleter(emojiChatTabComplete);
		getCommand("ec").setExecutor(emojiChatCommand);
		getCommand("ec").setTabCompleter(emojiChatTabComplete);
	}
	
	@Override
	public void onDisable() {
		emojiHandler.disable();
		updateChecker.cancelUpdateTask();
	}
	
	/**
	 * Gets the {@link #metricsHandler}.
	 *
	 * @return The {@link #metricsHandler}.
	 */
	public MetricsHandler getMetricsHandler() {
		return metricsHandler;
	}
	
	/**
	 * Gets the emoji handler.
	 *
	 * @return The emoji handler.
	 */
	public EmojiHandler getEmojiHandler() {
		return emojiHandler;
	}
}
