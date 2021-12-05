package ru.maleev.mcmCosmetics;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (Main.getInstance().getConfig().getStringList("Scoreboard.DisabledWorlds").contains(world.getName())) {
            ConfigBoard.removeScoreboard(player);
        }
        else {
            ConfigBoard.setScoreboard(player);
        }

        // Tab
        String header = String.join("\n", Main.getInstance().getConfig().getStringList("Tab.Header"));
        String footer = String.join("\n", Main.getInstance().getConfig().getStringList("Tab.Footer"));
        int period = Main.getInstance().getConfig().getInt("Tab.Update");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketContainer pc = Main.getInstance().protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
                pc.getChatComponents().write(0, WrappedChatComponent.fromText(PlaceholderAPI.setPlaceholders(player, translate(header)))).write(1, WrappedChatComponent.fromText(PlaceholderAPI.setPlaceholders(player, translate(footer))));

                if (e.getPlayer().isOnline()) {
                    try {
                        Main.getInstance().protocolManager.sendServerPacket(e.getPlayer(), pc);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, 0L, period);


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (ConfigBoard.runnables.containsKey(player.getName())) {
            ConfigBoard.runnables.get(player.getName()).cancel();
            ConfigBoard.runnables.remove(player.getName());
        }
    }

    @EventHandler
    public void onWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (Main.getInstance().getConfig().getStringList("Scoreboard.DisabledWorlds").contains(world.getName())) {
            ConfigBoard.removeScoreboard(player);
        }
        else {
            ConfigBoard.setScoreboard(player);
        }
    }

    public String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
