package ru.maleev.mcmCosmetics;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.google.common.collect.Maps;
import ru.maleev.mcmCosmetics.api.Board;
import ru.maleev.mcmCosmetics.api.BoardTask;
import ru.maleev.mcmCosmetics.utils.MessageUtil;

class ConfigBoard {
    static Map<String, Board> boards;
    static Map<String, BukkitRunnable> runnables;

    static void setScoreboard(Player player) {
        removeScoreboard(player);
        int cooldown = Main.getInstance().getConfig().getInt("Scoreboard.PageChangeCooldown");

        runnables.put(player.getName(), new BukkitRunnable() {
            private int i = 0;
            private Board board;

            public void run() {
                List<String> pages = new ArrayList<>(Main.getInstance().getConfig().getConfigurationSection("Scoreboard.Pages").getKeys(false));
                if (board != null) {
                    board.removeScoreboard(player);
                }
                String page = pages.get(i);
                int updateTicks = Main.getInstance().getConfig().getInt("Scoreboard.Pages." + page + ".Update");
                List<String> displayName = MessageUtil.getList(player, "Scoreboard.Pages." + page + ".DisplayName");
                List<String> lores = MessageUtil.getList(player, "Scoreboard.Pages." + page + ".Scores");
                for (int i = lores.size(); i > 0; --i) {
                    lores.set(i - 1, lores.get(i - 1));
                }
                (board = Board.getBoard(page, displayName.toArray(new String[0]), lores.toArray(new String[0]))).setScoreboard(player);
                board.startUpdater(new BoardTask(player) {
                    @Override
                    public Map<Integer, String> run() {
                        List<String> lores = MessageUtil.getList(player, "Scoreboard.Pages." + page + ".Scores");
                        for (int i = lores.size(); i > 0; --i) {
                            lores.set(i - 1, lores.get(i - 1));
                        }
                        for (int i = lores.size(); i > 0; --i) {
                            scoresMap.put(i, lores.get(lores.size() - i));
                        }
                        return scoresMap;
                    }
                }, updateTicks);
                ConfigBoard.boards.put(player.getName(), board);
                ++i;
                if (i >= pages.size()) {
                    i = 0;
                }
            }
        });
        ConfigBoard.runnables.get(player.getName()).runTaskTimer(Main.getInstance(), 0L, 20L * cooldown);
    }

    static void removeScoreboard(Player player) {
        BukkitRunnable runnable = ConfigBoard.runnables.get(player.getName());
        Board board = ConfigBoard.boards.get(player.getName());
        if (runnable != null) {
            runnable.cancel();
        }
        if (board != null) {
            board.removeScoreboard(player);
        }
    }

    static {
        ConfigBoard.boards = Maps.newHashMap();
        ConfigBoard.runnables = Maps.newHashMap();
    }
}
