package mc.EmojiChat2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * EmojiChat command class.
 *
 * @author RadBuilder
 * @version 1.8.1
 * @since 1.0
 */
class EmojiChatCommand implements CommandExecutor {
	/**
	 * EmojiChat main class instance.
	 */
	private final EmojiChat2 plugin;
	
	/**
	 * Creates the EmojiChat command class with the main class instance.
	 *
	 * @param plugin The EmojiChat main class instance.
	 */
	EmojiChatCommand(EmojiChat2 plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.AQUA + "EmojiChat2 v1.0 by RadBuilder");
			sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.GREEN + "/emojichat help" + ChatColor.AQUA + " for help.");
			return true;
		}
		
		// Sub commands
		switch (args[0].toLowerCase()) {
			case "help":
				if (!sender.hasPermission("emojichat.help")) {
					sender.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + "emojichat.help" + ChatColor.RED + " to use this command.");
					return true;
				}
				
				sender.sendMessage(ChatColor.AQUA + "---------- EmojiChat Help ----------");
				sender.sendMessage(ChatColor.GREEN + "/emojichat help: " + ChatColor.AQUA + "Opens up this help menu.");
				sender.sendMessage(ChatColor.GREEN + "/emojichat resourcepack: " + ChatColor.AQUA + "Re-sends the resourcepack.");
				sender.sendMessage(ChatColor.GREEN + "/emojichat reload: " + ChatColor.AQUA + "Reloads the EmojiChat config.");
				sender.sendMessage(ChatColor.GREEN + "/emojichat toggle: " + ChatColor.AQUA + "Toggles emoji shortcuts on or off.");
				return true;
			case "resourcepack":
				if (!sender.hasPermission("emojichat.see")) {
					sender.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + "emojichat.see" + ChatColor.RED + " to use this command.");
					return true;
				}
				
				if (sender instanceof Player) {
					((Player) sender).setResourcePack(plugin.getConfig().getString("ResourcePack-Location"));
					sender.sendMessage(ChatColor.GREEN + "Sent the EmojiChat ResourcePack.");
					sender.sendMessage(ChatColor.AQUA + "If you still can't see emojis, make sure the settings for this server (on the server list) have the resource pack option set to prompt or enabled.");
				}
				return true;
			case "reload":
				if (!sender.hasPermission("emojichat.reload")) {
					sender.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + "emojichat.reload" + ChatColor.RED + " to use this command.");
					return true;
				}
				
				plugin.reloadConfig();
				plugin.getEmojiHandler().load(plugin);
				sender.sendMessage(ChatColor.GREEN + "EmojiChat config reloaded.");
				return true;
			case "toggle":
				if (!sender.hasPermission("emojichat.toggle")) {
					sender.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + "emojichat.toggle" + ChatColor.RED + " to use this command.");
					return true;
				}
				
				if (sender instanceof Player) {
					plugin.getEmojiHandler().toggleShortcutsOff((Player) sender);
					sender.sendMessage(ChatColor.AQUA + "Emoji shortcuts are now " + (plugin.getEmojiHandler().hasShortcutsOff((Player) sender) ? ChatColor.RED + "disabled" : ChatColor.GREEN + "enabled") + ChatColor.AQUA + ".");
				} else {
					sender.sendMessage(ChatColor.RED + "Oops, you have to be a player to toggle shortcuts.");
				}
				return true;
			default:
				sender.sendMessage(ChatColor.RED + "Unknown sub-command '" + args[0] + "'. Use " + ChatColor.GOLD + "/emojichat help" + ChatColor.RED + " for help.");
				return true;
		}
	}
}
