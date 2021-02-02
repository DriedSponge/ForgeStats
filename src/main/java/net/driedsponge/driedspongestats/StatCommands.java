package net.driedsponge.driedspongestats;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class StatCommands extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        TextComponentString response = new TextComponentString("Nothing to show!");
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (params.length == 1 ) {
                player = server.getPlayerList().getPlayerByUsername(params[0]);
                if (player != null) {
                    response = PlayerStats(player);
                } else {
                    response = new TextComponentString("The player was not found or the player is not online.");
                    response.getStyle().setBold(true).setColor(TextFormatting.RED);
                }
            } else {
                response = PlayerStats(player);
            }
        } else {
            if (params.length == 1 ) {
                EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(params[0]);
                if (player != null) {
                    response = PlayerStats(player);
                } else {
                    response = new TextComponentString("The player was not found or the player is not online.");
                    response.getStyle().setBold(true).setColor(TextFormatting.RED);
                }
            }else{
                response = new TextComponentString("Please pass an online players name into the second argument!");
            }
        }

        sender.sendMessage(response);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/info <player>";
    }

    public static TextComponentString PlayerStats(EntityPlayerMP player){
        Style valueStyle = new Style();
        valueStyle.setColor(TextFormatting.GREEN);
        valueStyle.setBold(false);

        Style keyStyle = new Style();
        keyStyle.setColor(TextFormatting.GOLD);
        keyStyle.setBold(false);

        int deaths = player.getStatFile().readStat(StatList.DEATHS);
        int distancedTraveled = (player.getStatFile().readStat(StatList.WALK_ONE_CM) + player.getStatFile().readStat(StatList.SPRINT_ONE_CM))/100;
        int anmialsBread = player.getStatFile().readStat(StatList.ANIMALS_BRED);

        TextComponentString text = new TextComponentString("----- "+player.getName()+" Stats -----");
        text.getStyle().setColor(TextFormatting.GOLD).setBold(true);

        text.appendSibling(new TextComponentString("\nDeaths: ").setStyle(keyStyle));
        text.appendSibling(new TextComponentString(String.valueOf(deaths)).setStyle(valueStyle));

        text.appendSibling(new TextComponentString("\nDistanced Traveled: ").setStyle(keyStyle));
        text.appendSibling(new TextComponentString(distancedTraveled +" Blocks").setStyle(valueStyle));

        text.appendSibling(new TextComponentString("\nAnimals Bread: ").setStyle(keyStyle));
        text.appendSibling(new TextComponentString(String.valueOf(anmialsBread)).setStyle(valueStyle));

        return text;
    }
}
