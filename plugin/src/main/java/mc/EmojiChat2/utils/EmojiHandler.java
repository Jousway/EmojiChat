package mc.EmojiChat2.utils;

import mc.EmojiChat2.EmojiChat2;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Emoji handler class.
 *
 * @author RadBuilder
 * @version 1.7
 * @since 1.4
 */
public class EmojiHandler {
	/**
	 * The emojis.
	 */
	private final LinkedHashMap<String, Character> emojis;
	/**
	 * Shortcuts for the emojis, if specified.
	 */
	private final HashMap<String, String> shortcuts;
	/**
	 * If we should fix the emoji's color (colored chat removes emoji coloring)
	 */
	private boolean fixColoring;
	/**
	 * A list of users (by UUID) who turned shortcuts off.
	 */
	private List<UUID> shortcutsOff;
	/**
	 * EmojiChat main class instance.
	 */
	private final EmojiChat2 plugin;
	
	/**
	 * Creates the emoji handler with the main class instance.
	 *
	 * @param plugin The EmojiChat main class instance.
	 */
	public EmojiHandler(EmojiChat2 plugin) {
		this.plugin = plugin;
		
		emojis = new LinkedHashMap<>();
		shortcuts = new HashMap<>();
		shortcutsOff = new ArrayList<>();
		
		load(plugin);
	}
	
	/**
	 * Gets the {@link #emojis} map.
	 *
	 * @return {@link #emojis}.
	 */
	public LinkedHashMap<String, Character> getEmojis() {
		return emojis;
	}
	
	/**
	 * Gets the {@link #shortcuts} map.
	 *
	 * @return The {@link #shortcuts} map.
	 */
	public HashMap<String, String> getShortcuts() {
		return shortcuts;
	}
	
	/**
	 * Checks if the specified player has emoji shortcuts off.
	 *
	 * @param player The player to check.
	 * @return True if the player has shortcuts off, false otherwise.
	 */
	public boolean hasShortcutsOff(Player player) {
		return shortcutsOff.contains(player.getUniqueId());
	}
	
	/**
	 * Toggles emoji shortcut use on/off for the specified player.
	 *
	 * @param player The player to toggle emoji shortcuts on/off for.
	 */
	public void toggleShortcutsOff(Player player) {
		if (shortcutsOff.contains(player.getUniqueId())) {
			shortcutsOff.remove(player.getUniqueId());
		} else {
			shortcutsOff.add(player.getUniqueId());
		}
	}
	
	/**
	 * Validates the config.
	 *
	 * @param config The config to validate.
	 * @return True if the config is valid, false otherwise.
	 */
	private boolean validateConfig(FileConfiguration config) {
		try {
			return config.get("shortcuts") != null && config.get("fix-emoji-coloring") != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Loads the emoji shortcuts from the config.
	 *
	 * @param config The config to load emoji shortcuts from.
	 */
	private void loadShortcuts(FileConfiguration config) {
		for (String key : config.getConfigurationSection("shortcuts").getKeys(false)) { // Gets all of the headers/keys in the shortcuts section
			for (String shortcutListItem : config.getStringList("shortcuts." + key)) { // Gets all of the shortcuts for the key
				shortcuts.put(shortcutListItem, ":" + key + ":");
			}
		}
	}
	
	/**
	 * If emoji coloring should be fixed.
	 *
	 * @return True if emoji coloring should be fixed, false otherwise.
	 */
	public boolean fixColoring() {
		return fixColoring;
	}
	
	/**
	 * Loads the emojis and their shortcuts into the {@link #emojis}.
	 */
	private void loadEmojis() {
		char emojiChar = '娀';
		
		try {
			InputStream listInput = getClass().getResourceAsStream("/list.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(listInput));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("#")) { // Ignored lines
					continue;
				}
				emojis.put(line, emojiChar++); // Add the emoji we're currently on and switch it to the next char
			}
			bufferedReader.close();
			listInput.close();
		} catch (Exception e) {
			plugin.getLogger().warning("An error occured while loading emojis. More info below.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Clears the {@link #emojis}, {@link #shortcuts}, and {@link #disabledCharacters} maps.
	 */
	public void disable() {
		emojis.clear();
		shortcuts.clear();
		shortcutsOff.clear();
	}
	
	/**
	 * Loads the emoji handler data.
	 *
	 * @param plugin The EmojiChat main class instance.
	 */
	public void load(EmojiChat2 plugin) {
		disable();

		loadEmojis(); // Loads ALL emojis
		
		if (!validateConfig(plugin.getConfig())) { // Make sure the config is valid
			plugin.getLogger().warning("Your config is invalid. No configuation data was loaded.");
			plugin.getLogger().warning("Fix your config, then use /emojichat reload");
			plugin.getLogger().warning("If you're still running into issues after fixing your config, delete it and restart your server.");
		} else { // Config is valid, load config data
			loadShortcuts(plugin.getConfig()); // Loads all of the shortcuts specified in the config
			fixColoring = plugin.getConfig().getBoolean("fix-emoji-coloring");
		}
	}
	
	/**
	 * Converts the specified message's shortcuts (i.e. :100:) to emoji.
	 *
	 * @param message The message to convert.
	 * @return The converted message.
	 */
	public String toEmoji(String message) {
		for (String key : emojis.keySet()) {
			message = message.replace(key, plugin.getEmojiHandler().getEmojis().get(key).toString());
		}
		return message;
	}
	
	/**
	 * Converts the specified line's shortcuts (i.e. :100:) to emoji from sign.
	 *
	 * @param line The line to convert from sign.
	 * @return The converted line from sign.
	 */
	public String toEmojiFromSign(String line) {
		for (String key : emojis.keySet()) {
			line = line.replace(key, ChatColor.WHITE + "" + emojis.get(key) + ChatColor.BLACK); // Sets the emoji color to white for correct coloring
		}
		return line;
	}
	
	/**
	 * Converts the specified message's shortcuts (i.e. :100:) to emoji from chat.
	 *
	 * @param message The message to convert from chat.
	 * @return The converted message from chat.
	 */
	public String toEmojiFromChat(String message) {
		// If we're not fixing the coloring, or the message is too small to have coloring
		if (!fixColoring || message.length() < 3) {
			message = toEmoji(message);
		} else {
			String chatColor = message.substring(0, 2); // Gets the chat color of the message, i.e. §a
			boolean hasColor = chatColor.contains("§");
			for (String key : emojis.keySet()) {
				message = message.replace(key, ChatColor.WHITE + "" + emojis.get(key) + (hasColor ? chatColor : "")); // Sets the emoji color to white for correct coloring
			}
		}
		return message;
	}
	
	/**
	 * Replaces shorthand ("shortcuts" in config) with correct emoji shortcuts.
	 *
	 * @param message The original message.
	 * @return The message with correct emoji shortcuts.
	 */
	public String translateShorthand(String message) {
		for (String key : plugin.getEmojiHandler().getShortcuts().keySet()) {
			message = message.replace(key, plugin.getEmojiHandler().getShortcuts().get(key));
		}
		return message;
	}

}
