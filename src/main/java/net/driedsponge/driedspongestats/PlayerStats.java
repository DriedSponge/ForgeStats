package net.driedsponge.driedspongestats;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStats {
    // Method to get map of all stats
    
    public static StatisticsManager statFile;

    // Constructor
    public PlayerStats(EntityPlayerMP playerMP){
        statFile = playerMP.getStatFile();
    }

    // Fetch all stats
    public Map<String, String> allStats(){
        Map<String, String> stats = new HashMap<String, String>();
        List<StatBase> statBaseList = StatList.ALL_STATS;


        statBaseList.forEach(statBase -> {
            if(statFile.readStat(statBase) == 0){ // Ignore stats that are 0 so we don't get a bunch of empty data
                return;
            }
            stats.put(statBase.getStatName().getUnformattedText(),prettyStat(statBase));
        });

        return stats;
    }

    // Method to get map of basic stats
    public Map<String, String> basicStats(){
        Map<String, String> stats = new HashMap<String, String>();
        List<StatBase> statBaseList = StatList.BASIC_STATS;

        statBaseList.forEach(statBase -> {
            if(statFile.readStat(statBase) == 0){ // Ignore stats that are 0 so we don't get a bunch of empty data
                return;
            }
            stats.put(statBase.getStatName().getUnformattedText(),prettyStat(statBase));
        });

        return stats;
    }


    // Format stat values depending on what type the stat is (normal/distance/time)
    public String prettyStat(StatBase statBase){
        int value = statFile.readStat(statBase);
        String finalValue = "";

        if(statBase.statId.toLowerCase().endsWith("cm")){
            value /= 100;
            finalValue = value +" Blocks";
        }else if (statBase.statId.toLowerCase().contains("minute") || statBase.statId.toLowerCase().contains("time")){
            value /= 20; // Divide by 20 because for some reason time played is displayed in the amount of 1/20th seconds
            Duration time = Duration.ofSeconds(value);
            finalValue = String.valueOf(DurationFormatUtils.formatDuration(time.toMillis(), "HH:mm:ss", true));
        }else{
            finalValue = String.valueOf(value);
        }

        return finalValue;
    }


}
