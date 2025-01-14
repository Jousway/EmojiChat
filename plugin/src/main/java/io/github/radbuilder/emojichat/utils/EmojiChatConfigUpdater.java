package io.github.radbuilder.emojichat.utils;

import io.github.radbuilder.emojichat.EmojiChat;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * EmojiChat config updater.
 *
 * @author RadBuilder
 * @version 1.8
 * @since 1.5
 */
public class EmojiChatConfigUpdater {
	/**
	 * The current config version number.
	 */
	private final int CONFIG_VERSION = 5;
	
	/**
	 * Creates the EmojiChat config updater with the main class instance.
	 *
	 * @param plugin The EmojiChat main class instance.
	 */
	public EmojiChatConfigUpdater(EmojiChat plugin) {
		int configVersion = plugin.getConfig().getInt("config-version");
		
		if (configVersion < CONFIG_VERSION) {
			plugin.getLogger().info("Updating your config now (old: " + configVersion + ", new: " + CONFIG_VERSION + ")...");
			updateConfig(plugin, plugin.getConfig(), configVersion);
		}
	}
	
	/**
	 * Updates the EmojiChat config.
	 *
	 * @param plugin The EmojiChat main class instance.
	 * @param config The EmojiChat config.
	 * @param configVersion The current config version number.
	 */
	private void updateConfig(EmojiChat plugin, FileConfiguration config, int configVersion) {
		// Config v1 & v2 values
		boolean fixEmojiColoring = config.getBoolean("fix-emoji-coloring");
		List<String> disabledEmojis = config.getStringList("disabled-emojis");
		LinkedHashMap<String, List<String>> shortcuts = new LinkedHashMap<>();
		for (String key : config.getConfigurationSection("shortcuts").getKeys(false)) { // Gets all of the headers/keys in the shortcuts section
			shortcuts.put(key, config.getStringList("shortcuts." + key));
		}
		
		// Config v2 values
		boolean downloadResourcePack = true;
		if (configVersion > 1) {
			downloadResourcePack = config.getBoolean("download-resourcepack");
		}
		
		// Config v4 values
		boolean emojisOnSigns = config.contains("emojis-on-signs") ? config.getBoolean("emojis-on-signs") : false;
		boolean emojisInCommands = config.contains("emojis-in-commands") ? config.getBoolean("emojis-in-commands") : false;
		boolean onlyCommandList = config.contains("only-command-list") ? config.getBoolean("only-command-list") : false;
		List<String> commandList = config.contains("command-list") ? config.getStringList("command-list") : Arrays.asList("/msg", "/tell");
		int packVariant = config.contains("pack-variant") ? config.getInt("pack-variant") : 1;
		boolean disableEmojis = config.contains("disable-emojis") ? config.getBoolean("disable-emojis") : true;
		
		// Config lines
		List<String> configLines = new ArrayList<>();
		configLines.add("# Configuration file for EmojiChat by RadBuilder");
		configLines.add("");
		configLines.add("# If you're using chat color plugins, this will remove the coloring for emojis to be displayed correctly.");
		configLines.add("fix-emoji-coloring: " + fixEmojiColoring);
		configLines.add("");
		configLines.add("# If emojis should be displayed on signs (shortcuts and full names supported).");
		configLines.add("emojis-on-signs: " + emojisOnSigns);
		configLines.add("");
		configLines.add("# If commands should have emojis (shortcuts and full names supported).");
		configLines.add("emojis-in-commands: " + emojisInCommands);
		configLines.add("# If emojis should ONLY work with commands in 'command-list'.");
		configLines.add("only-command-list: " + onlyCommandList);
		configLines.add("# If 'only-command-list' is true, the commands here will be the only commands where emojis are allowed.");
		configLines.add("command-list:");
		for (String command : commandList) {
			configLines.add("- '" + command + "'");
		}
		configLines.add("");
		configLines.add("# If EmojiChat should auto download the ResourcePack. If you'd rather have your players manually");
		configLines.add("# download or use /emojichat resourcepack, set this to false.");
		configLines.add("download-resourcepack: " + downloadResourcePack);
		configLines.add("");
		configLines.add("# Shortcuts will replace the items in the list with the correct emoji name.");
		configLines.add("# For example, :) will be replaced with :grinning:, which then will turn it into the emoji.");
		configLines.add("shortcuts:");
		for (String shortcutKey : shortcuts.keySet()) {
			configLines.add("  " + shortcutKey + ":");
			for (String shortcutListItem : shortcuts.get(shortcutKey)) {
				configLines.add("  - '" + shortcutListItem + "'");
			}
		}
		if (configVersion == 1) {
			configLines.add("  crazy_face:");
			configLines.add("  - ':crazy:'");
			configLines.add("  face_with_raised_eyebrow:");
			configLines.add("  - ':hmm:'");
			configLines.add("  shushing_face:");
			configLines.add("  - ':shh:'");
		}
		if (configVersion < 3) {
			configLines.add("  1st_place_medal:");
			configLines.add("  - ':first:'");
			configLines.add("  - ':1st:'");
			configLines.add("  2nd_place_medal:");
			configLines.add("  - ':second:'");
			configLines.add("  - ':2nd:'");
			configLines.add("  3rd_place_medal:");
			configLines.add("  - ':third:'");
			configLines.add("  - ':3rd:'");
		}
		if (configVersion < 4) {
			configLines.add("  microphone:");
			configLines.add("  - ':mic:'");
			configLines.add("  musical_keyboard:");
			configLines.add("  - ':piano:'");
			configLines.add("  video_game:");
			configLines.add("  - ':controller:'");
			configLines.add("  dart:");
			configLines.add("  - ':target:'");
			configLines.add("  game_die:");
			configLines.add("  - ':dice:'");
			configLines.add("  - ':die:'");
			configLines.add("  heart:");
			configLines.add("  - '<3'");
			configLines.add("  broken_heart:");
			configLines.add("  - '</3'");
			configLines.add("  zero:");
			configLines.add("  - ':0:'");
			configLines.add("  one:");
			configLines.add("  - ':1:'");
			configLines.add("  two:");
			configLines.add("  - ':2:'");
			configLines.add("  three:");
			configLines.add("  - ':3:'");
			configLines.add("  four:");
			configLines.add("  - ':4:'");
			configLines.add("  five:");
			configLines.add("  - ':5:'");
			configLines.add("  six:");
			configLines.add("  - ':6:'");
			configLines.add("  seven:");
			configLines.add("  - ':7:'");
			configLines.add("  eight:");
			configLines.add("  - ':8:'");
			configLines.add("  nine:");
			configLines.add("  - ':9:'");
			configLines.add("  keycap_ten:");
			configLines.add("  - ':ten:'");
			configLines.add("  - ':10:'");
			configLines.add("  asterisk:");
			configLines.add("  - ':*:'");
			configLines.add("  oncoming_police_car:");
			configLines.add("  - ':fbi:'");
			configLines.add("  - ':police:'");
		}		
		// Update the config
		setConfig(plugin, configLines);
		// Clear non-used lists
		disabledEmojis.clear();
		shortcuts.clear();
	}
	
	/**
	 * Sets the config to be the specified set of lines.
	 *
	 * @param plugin The EmojiChat main class instance.
	 * @param configLines The list of lines to set the config to.
	 */
	private void setConfig(EmojiChat plugin, List<String> configLines) {
		try {
			File configFile = new File(plugin.getDataFolder() + "/config.yml");
			if (!configFile.delete()) { // Delete the old config
				plugin.getLogger().warning("Failed to delete the old config. If this continues: back up your config, manually delete it, then restart.");
			}
			
			// Create the new config
			configFile = new File(plugin.getDataFolder() + "/config.yml");
			FileOutputStream fileOutputStream = new FileOutputStream(configFile);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			Writer writer = new BufferedWriter(outputStreamWriter);
			for (String configLine : configLines) {
				writer.write(configLine + "\n");
			}
			
			// Cleanup
			writer.close();
			outputStreamWriter.close();
			fileOutputStream.close();
			configLines.clear();
			plugin.getLogger().info("Config successfully updated.");
		} catch (Exception e) {
			plugin.getLogger().severe("An error occured while updating your config. More details below.");
			e.printStackTrace();
		}
	}
}
