package net.driedsponge.driedspongestats;

import com.google.gson.Gson;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.TupleIntJsonSerializable;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.lwjgl.Sys;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class StatsUploader {
    @Config(modid = DriedSpongeStats.MODID)
    public static class StatsUploadConfig{
        public static String ApiURL = "http://localhost:3200/api/mc/stats/52";
        public static String TOKEN = "TOKEN";

    }
    public String ApiURL = StatsUploadConfig.ApiURL;
    public String TOKEN = StatsUploadConfig.TOKEN;
    private static HttpURLConnection connection;
    @SubscribeEvent
    public void postStats(PlayerEvent.PlayerLoggedOutEvent event){
        if(event.player instanceof EntityPlayerMP){
            String name = event.player.getName();
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            DriedSpongeStats.logger.info("Fetching data for "+name+"...");
            String UUID = player.getUniqueID().toString();
            Gson gson = new Gson();
            Map<String, String> stats = new HashMap<String, String>();
            stats.put("deaths",String.valueOf(player.getStatFile().readStat(StatList.DEATHS)));
            String finalstats = gson.toJson(stats);
            try{
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("username", name);
                params.put("uuid", UUID);
                params.put("stats", finalstats);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                DriedSpongeStats.logger.info("Posting stats for "+name+" to api..");
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                URL url = new URL(ApiURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(5000);
                connection.setReadTimeout(50000);
                connection.setRequestProperty("Authorization","Bearer "+TOKEN);
                connection.setDoOutput(true);
                connection.getOutputStream().write(postDataBytes);
                int status = connection.getResponseCode();
                DriedSpongeStats.logger.info("Api responded with "+status);
                DriedSpongeStats.logger.info("Stats have been posted for"+name);

            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }


        }
    }
}
