package io.key.afkPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class enderpearlStasis extends JavaPlugin {

    // Stores UUID and GameMode of the player
    private final HashMap<UUID, GameMode> playerGameModes = new HashMap<>();

    //enable plugin
    @Override
    public void onEnable() {
        getLogger().info("AFK Plugin Enabled!");

    }

    @Override
    public void onDisable() {
        getLogger().info("AFK Plugin Disabled!");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("afk")) {

            // Ensure the sender is a player
            if (sender instanceof Player) {
                Player player = (Player) sender;
                UUID playerId = player.getUniqueId();

                if (player.getGameMode() == GameMode.SPECTATOR) {
                    // unAFK the player.
                    GameMode previousMode = playerGameModes.getOrDefault(playerId, GameMode.SURVIVAL);
                    player.setGameMode(previousMode);
                    player.sendMessage("You are no longer AFK.");
                    playerGameModes.remove(playerId); // Clear their previous mode from memory
                } else {
                    // set player to Spectator mode
                    playerGameModes.put(playerId, player.getGameMode());
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage("You are now AFK. ");
                }
                return true;
            } else {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
        }
        return false;
    }
}
