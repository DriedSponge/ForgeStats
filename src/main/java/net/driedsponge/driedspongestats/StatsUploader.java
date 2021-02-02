package net.driedsponge.driedspongestats;

import com.google.gson.Gson;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class StatsUploader {

    public static final String ApiURL = ModConfig.ApiConfig.ApiURL;
    public static final String TOKEN = ModConfig.ApiConfig.TOKEN;

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
