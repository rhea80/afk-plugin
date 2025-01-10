package io.key.afkPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public final class enderpearlStasis extends JavaPlugin implements @NotNull Listener {

    // Stores UUID and GameMode of the player
    private final HashMap<UUID, GameMode> playerGameModes = new HashMap<>();

    //enable plugin
    @Override
    public void onEnable() {
        getLogger().info("AFK Plugin Enabled!");
        Bukkit.getPluginManager().registerEvents(this, this);

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
                    player.sendMessage("You are no longer AFK");
                    playerGameModes.remove(playerId);
                } else {
                    // set player to Spectator mode
                    playerGameModes.put(playerId, player.getGameMode());
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage("You are now AFK");
                }
                return true;
            } else {
                sender.sendMessage("Unknown person. Try again later.");
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR && playerGameModes.containsKey(player.getUniqueId())) {
            if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
                event.setTo(event.getFrom());
                player.sendMessage("You cannot move while AFK.");
            }
        }
    }
}
