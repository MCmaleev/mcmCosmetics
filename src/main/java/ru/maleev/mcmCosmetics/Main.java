package ru.maleev.mcmCosmetics;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.maleev.mcmCosmetics.commands.BoardCommand;

public class Main extends JavaPlugin {
    private static Main instance;
    public ProtocolManager protocolManager;

    public void onEnable() {
        Main.instance = this;
        saveDefaultConfig();

        protocolManager = ProtocolLibrary.getProtocolManager();

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        new BoardCommand("cosmetics", "mcmcosmetics.admin");
    }

    public static Main getInstance() {
        return Main.instance;
    }
}
