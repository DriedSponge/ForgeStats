package net.driedsponge.driedspongestats;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
    // Method to get map of stats
    public Map<String, String> stats(EntityPlayerMP player){
        Map<String, String> stats = new HashMap<String, String>();

        stats.put("Deaths",String.valueOf(player.getStatFile().readStat(StatList.DEATHS)));
        stats.put("Animals Bread",String.valueOf(player.getStatFile().readStat(StatList.ANIMALS_BRED)));
        stats.put("Distance Walked",String.valueOf((player.getStatFile().readStat(StatList.WALK_ONE_CM) + player.getStatFile().readStat(StatList.SPRINT_ONE_CM))/100)+" Blocks");

        return stats;
    }
}
