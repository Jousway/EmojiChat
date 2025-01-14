package io.github.radbuilder.emojichat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * EmojiChat listener class.
 *
 * @author RadBuilder
 * @version 1.8
 * @since 1.0
 */
class EmojiChatListener implements Listener {
	/**
	 * EmojiChat main class instance.
	 */
	private final EmojiChat plugin;
	/**
	 * If EmojiChat should automatically download the ResourcePack for the player.
	 */
	private final boolean autoDownloadResourcePack;
	
	/**
	 * Creates the EmojiChat listener class with the main class instance.
	 *
	 * @param plugin The EmojiChat main class instance.
	 */
	EmojiChatListener(EmojiChat plugin) {
		this.plugin = plugin;
		autoDownloadResourcePack = plugin.getConfig().getBoolean("download-resourcepack");
	}
	
	@EventHandler
	void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (!autoDownloadResourcePack) // If auto downloading of the ResourcePack is disabled
			return;
		
		// Send the player the resource pack
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (player.hasPermission("emojichat.see")) { // If the player can see emojis
				player.setResourcePack("http://jousway.co.uk/shit/UKSRTemoji.zip");
			}
		}, 20L); // Give time for the player to join
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	void onChat(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("emojichat.use") || !event.getPlayer().hasPermission("emojichat.use.chat"))
			return; // Don't do anything if they don't have permission
		
		String message = event.getMessage();
		
		// Checks if the user disabled shortcuts via /emojichat toggle
		if (!plugin.getEmojiHandler().hasShortcutsOff(event.getPlayer())) {
			message = plugin.getEmojiHandler().translateShorthand(message);
		}
		
		// Replace shortcuts with emojis
		message = plugin.getEmojiHandler().toEmojiFromChat(message);
		
		event.setMessage(message);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	void onSignChange(SignChangeEvent event) {
		if (!event.getPlayer().hasPermission("emojichat.use") || !event.getPlayer().hasPermission("emojichat.use.sign"))
			return; // Don't do anything if they don't have permission
		
		if (!plugin.getConfig().getBoolean("emojis-on-signs")) // Feature is disabled
			return;
		
		for (int i = 0; i < 4; i++) {
			String line = event.getLine(i);
			
			// Checks if the user disabled shortcuts via /emojichat toggle
			if (!plugin.getEmojiHandler().hasShortcutsOff(event.getPlayer())) {
				line = plugin.getEmojiHandler().translateShorthand(line);
			}
			
			// Replace shortcuts with emojis
			line = plugin.getEmojiHandler().toEmojiFromSign(line);
			
			event.setLine(i, line);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
		if (!event.getPlayer().hasPermission("emojichat.use") || !event.getPlayer().hasPermission("emojichat.use.command"))
			return; // Don't do anything if they don't have permission
		
		if (!plugin.getConfig().getBoolean("emojis-in-commands")) // Feature is disabled
			return;
		
		String command = event.getMessage();
		
		// only-command-list is enabled and the command-list doesn't contain the command being ran
		if (plugin.getConfig().getBoolean("only-command-list") && !plugin.getConfig().getStringList("command-list").contains(command.split(" ")[0].toLowerCase())) {
			return;
		}
		
		// Checks if the user disabled shortcuts via /emojichat toggle
		if (!plugin.getEmojiHandler().hasShortcutsOff(event.getPlayer())) {
			command = plugin.getEmojiHandler().translateShorthand(command);
		}
		
		// Replace shortcuts with emojis
		command = plugin.getEmojiHandler().toEmoji(command);
				
		event.setMessage(command);
	}
}
