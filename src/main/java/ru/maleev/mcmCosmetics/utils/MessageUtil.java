package ru.maleev.mcmCosmetics.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.maleev.mcmCosmetics.Main;

import java.util.List;

public class MessageUtil {
    private static String replace(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getMessage(Player player, String path) {
        String s = replace(Main.getInstance().getConfig().getString(path));
        return PlaceholderAPI.setPlaceholders(player, s);
    }

    public static List<String> getList(Player player, String path) {
        List<String> list = Main.getInstance().getConfig().getStringList(path);
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, replace(list.get(i)));
        }
        return PlaceholderAPI.setPlaceholders(player, list);
    }
}
