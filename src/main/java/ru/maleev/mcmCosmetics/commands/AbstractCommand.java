package ru.maleev.mcmCosmetics.commands;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.maleev.mcmCosmetics.Main;

public abstract class AbstractCommand implements CommandExecutor
{
    private final String permission;

    AbstractCommand(String name, String permission) {
        this.permission = permission;
        Bukkit.getPluginCommand(name).setExecutor(this);
    }

    abstract void execute(CommandSender p0, String[] p1);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (!sender.hasPermission(permission)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.NoPerms")));
                return true;
            }
            execute(sender, args);
        }
        catch (ClassCastException e) {
            sender.sendMessage(ChatColor.RED + "Это команда только для игроков!");
        }
        return false;
    }
}
