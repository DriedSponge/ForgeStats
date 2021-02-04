package net.driedsponge.driedspongestats;

import com.google.gson.Gson;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
            Map<String, String> stats = new PlayerStats().stats(player);
            String finalstats = gson.toJson(stats);

            try{

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("uuid", UUID);
                params.put("stats", finalstats);

                String jsonInputString = gson.toJson(params);


                DriedSpongeStats.logger.info("Posting stats for "+name+" to api..");
                URL url = new URL(ApiURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(5000);
                connection.setReadTimeout(50000);
                connection.setRequestProperty("Authorization","Bearer "+TOKEN);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int status = connection.getResponseCode();

                if(ModConfig.ApiConfig.DEBUG){
                    String strCurrentLine;
                    BufferedReader br;

                    if(status >= 200 && status <= 299){
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((strCurrentLine = br.readLine()) != null) {
                            DriedSpongeStats.logger.info(strCurrentLine);
                        }
                    }else{
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        while ((strCurrentLine = br.readLine()) != null) {
                            DriedSpongeStats.logger.info(strCurrentLine);
                        }
                    }

                    DriedSpongeStats.logger.info("Stats: "+finalstats);
                    DriedSpongeStats.logger.info("Player UUID: "+UUID);
                    DriedSpongeStats.logger.info("Player Username: "+name);
                    DriedSpongeStats.logger.info("Entire JSON: "+jsonInputString);
                }

                DriedSpongeStats.logger.info("Api responded with "+status+" ("+connection.getResponseMessage()+")");
                DriedSpongeStats.logger.info("Stats have been posted for "+name);

            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }


        }
    }
}
