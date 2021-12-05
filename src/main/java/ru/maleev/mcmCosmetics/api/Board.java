package ru.maleev.mcmCosmetics.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import ru.maleev.mcmCosmetics.Main;

import java.util.ArrayList;
import java.util.List;

public class Board
{
    private final String name;
    private final String[] displayName;
    private final String[] scores;
    private Scoreboard board;
    private final List<String> oldScores;
    private BukkitRunnable runnable;

    public static Board getBoard(String name, String displayName, String... scores) {
        return getBoard(name, new String[] { displayName }, scores);
    }

    public static Board getBoard(String name, String[] displayName, String... scores) {
        return new Board(name, displayName, scores);
    }

    private Board(String name, String[] displayName, String... scores) {
        this.name = name;
        this.displayName = displayName;
        this.scores = scores;
        this.oldScores = new ArrayList<>();
    }

    public String[] getScores() {
        return scores;
    }

    public void setScoreboard(Player player) {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective o = board.registerNewObjective(name, "dummy");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(displayName[0].replace("&", "§"));
        for (int i = scores.length; i > 0; --i) {
            oldScores.add(scores[scores.length - i].replace("&", "§"));
        }
        StringBuilder emptyLine = new StringBuilder("&r");
        for (int j = scores.length; j > 0; --j) {
            String s = scores[scores.length - j];
            if (s.equals("")) {
                emptyLine.append(" §r");
                s = emptyLine.toString();
            }
            Score score = o.getScore(s.replace("&", "§"));
            score.setScore(j);
            oldScores.set(j - 1, s.replace("&", "§"));
        }
        player.setScoreboard(board);
    }

    public void startUpdater(BoardTask task, int updateTicks) {
        Objective o = board.getObjective(name);
        (this.runnable = new BukkitRunnable() {
            private int i = 0;
            private StringBuilder emptyLine = new StringBuilder(" ");

            public void run() {
                o.setDisplayName(Board.this.displayName[i].replace("&", "§"));
                ++i;
                if (i >= Board.this.displayName.length) {
                    i = 0;
                }
                for (int i : task.run().keySet()) {
                    String s = task.run().get(i).replace("&", "§");
                    String old = Board.this.oldScores.get(i - 1).replace("&", "§");
                    if (s.equals(old)) {
                        continue;
                    }
                    if (s.equals("")) {
                        emptyLine.append(" ");
                        s = emptyLine.toString();
                    }
                    Board.this.board.resetScores(old);
                    Board.this.oldScores.set(i - 1, s);
                    Score score = o.getScore(s);
                    score.setScore(i);
                }
                emptyLine = new StringBuilder(" ");
            }
        }).runTaskTimer(Main.getInstance(), 0L, updateTicks);
    }

    public void removeScoreboard(Player player) {
        if (runnable != null) {
            runnable.cancel();
        }
        final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective o = board.registerNewObjective(name, "dummy");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
    }

    public boolean hasScoreboard(Player player) {
        return player.getScoreboard() == board;
    }

    private abstract static class AbstractEventListener implements Listener
    {
        AbstractEventListener(Plugin plugin) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
    }
}
