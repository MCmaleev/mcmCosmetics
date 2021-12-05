package ru.maleev.mcmCosmetics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.maleev.mcmCosmetics.Main;

public class BoardCommand extends AbstractCommand {
    public BoardCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }
        String lowerCase = args[0].toLowerCase();
        if ("reload".equals(lowerCase)) {
            Main.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.ConfigReloaded")));
        }
        if ("title".equals(lowerCase)) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TitleUsage")));
                return;
            }
            String title = ChatColor.translateAlternateColorCodes('&', args[1]);
            String subtitle = ChatColor.translateAlternateColorCodes('&', args[2]);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(title, subtitle);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TitleSent")));
        }
        if ("chat".equals(lowerCase)) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.ChatUsage")));
                return;
            }

            String message = ChatColor.translateAlternateColorCodes('&', args[1].replace("%n", "\n"));
            for (Player p : Bukkit.getOnlinePlayers()) {
                String[] msg = message.split("\n");
                p.sendMessage(msg);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.ChatSent")));
        }
    }
}
