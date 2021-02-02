package net.driedsponge.driedspongestats;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = DriedSpongeStats.MODID, name = DriedSpongeStats.NAME, version = DriedSpongeStats.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class DriedSpongeStats {
    public static final String MODID = "driedspongestats";
    public static final String NAME = "DriedSponge Stats";
    public static final String VERSION = "1.0";

    public static Logger logger;



    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }
    @EventHandler
    public void init(FMLServerStartingEvent event){
        event.registerServerCommand(new StatCommands());
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

        MinecraftForge.EVENT_BUS.register(new StatsUploader());
    }

}
