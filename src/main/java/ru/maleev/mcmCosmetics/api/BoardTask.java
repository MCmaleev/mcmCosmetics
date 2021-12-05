package ru.maleev.mcmCosmetics.api;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class BoardTask
{
    private final Player player;
    protected Map<Integer, String> scoresMap;

    public BoardTask(Player player) {
        this.scoresMap = Maps.newHashMap();
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract Map<Integer, String> run();
}
