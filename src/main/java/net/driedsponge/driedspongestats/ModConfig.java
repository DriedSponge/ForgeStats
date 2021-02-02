package net.driedsponge.driedspongestats;
import net.minecraftforge.common.config.Config;

public class ModConfig {
    @Config(modid = DriedSpongeStats.MODID, category = "api_settings")
    public static class ApiConfig{
        @Config.Comment({
                "The URL the json payload will be sent to",
                "The post request will send three values: uuid, stats, and username"
        })
        public static String ApiURL = "http://localhost:3200/api/mc/stats/52";

        @Config.Comment({
                "This is the token to auth the server to post to the api",
                "It will be sent in the header as a Bearer token (Authorization: Bearer <TOKEN>)"
        })
        public static String TOKEN = "TOKEN";
    }

    @Config(modid = DriedSpongeStats.MODID, category = "command_settings")
    public static class CommandConfig{
        @Config.Comment({
                "The player will click this url to view their stats on the web page.",
                "You can use {player_name} to insert the players name into the string"
        })
        public static String PlayerURL = "http://localhost:3200/mc/players/{player_name}";
    }
}
