package cn.saltedfish.blockregenn.Commands;

import cn.saltedfish.blockregenn.Managers.AreaManager;
import cn.saltedfish.blockregenn.Managers.BlockManager;
import cn.saltedfish.blockregenn.BlockRegenN;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockRegenCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player;
        String message;
        if  (!(commandSender instanceof Player)) {
            return true;
        }
        player = (Player) commandSender;

        if (args.length < 2){
            message = BlockRegenN.getLanguage("InvalidCommand");
            player.sendMessage(message);
            return true;
        }

        switch (args[0]){
            case "reload": {
                Bukkit.getPluginManager().disablePlugin(BlockRegenN.getPlugin());
                Bukkit.getPluginManager().enablePlugin(BlockRegenN.getPlugin());
                message = BlockRegenN.getLanguage("Reload");
                player.sendMessage(message);
                break;
            }
            case "block": {
                switch (args[1]) {
                    case "create": {
                        //blockregen block create blockName
                        if (args.length != 3){
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String blockName = args[2];

                        if (BlockManager.getBlockList().contains(blockName)){
                            message = BlockRegenN.getLanguage("Block.BlockAlreadyExist").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        BlockManager.createBlock(blockName);

                        message = BlockRegenN.getLanguage("Block.Create");
                        player.sendMessage(message);
                        break;
                    }
                    case "remove": {
                        //blockregen block remove blockName
                        if (args.length != 3){
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String blockName = args[2];

                        if (!BlockManager.getBlockList().contains(blockName)){
                            message = BlockRegenN.getLanguage("Block.BlockNotFound").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        BlockManager.removeBlock(blockName);

                        message = BlockRegenN.getLanguage("Block.Remove");
                        player.sendMessage(message);
                        break;
                    }
                    case "addMaterial": {
                        //blockregen block addMaterial blockName materialName
                        if (args.length != 4){
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String blockName = args[2];
                        String materialName = args[3];

                        if (!BlockManager.getBlockList().contains(blockName)){
                            message = BlockRegenN.getLanguage("Block.BlockNotFound").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        Material material = Material.matchMaterial(materialName);
                        if (material == null){
                            message = BlockRegenN.getLanguage("Block.MaterialNotFound").replaceAll("%MaterialName", materialName);
                            player.sendMessage(message);
                            return true;
                        }
                        BlockManager.addMaterial(blockName, material);
                        message = BlockRegenN.getLanguage("Block.AddMaterial").replaceAll("%MaterialName", materialName).replaceAll("%BlockName", blockName);
                        player.sendMessage(message);
                        break;
                    }
                    case "removeMaterial": {
                        //blockregen block removeMaterial blockName index
                        if (args.length != 4){
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String blockName = args[2];
                        int index = Integer.parseInt(args[3]);

                        if (!BlockManager.getBlockList().contains(blockName)){
                            message = BlockRegenN.getLanguage("Block.BlockNotFound").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        if (index > BlockManager.getBlockMaterialList(blockName).size()){
                            message = BlockRegenN.getLanguage("Block.IndexOutOfBound").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        Material material = BlockManager.getBlockMaterialList(blockName).get(index);
                        String materialName = material.name();
                        BlockManager.removeMaterial(blockName, index);

                        message = BlockRegenN.getLanguage("Block.RemoveMaterial").replaceAll("%MaterialName", materialName).replaceAll("%BlockName", blockName).replaceAll("%Index", String.valueOf(index));
                        player.sendMessage(message);
                        break;
                    }
                    case "info": {
                        //blockregen block info blockName
                        if (args.length != 3){
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String blockName = args[2];

                        if (!BlockManager.getBlockList().contains(blockName)){
                            message = BlockRegenN.getLanguage("Block.BlockNotFound").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        message = BlockManager.getInfo(blockName).replaceAll("%BlockName", blockName);
                        player.sendMessage(message);
                        break;
                    }
                    case "list": {
                        //blockregen block list
                        if (args.length != 2){
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        message = BlockManager.getList();
                        player.sendMessage(message);
                        break;
                    }
                    default: {
                        message = BlockRegenN.getLanguage("InvalidCommand");
                        player.sendMessage(message);
                        break;
                    }
                }
                break;
            }
            case "area": {
                switch (args[1]) {
                    case "create": {
                        //blockregen area create x1 y1 z1 x2 y2 z2 areaName
                        if (args.length != 9) {
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        double x1 = Double.parseDouble(args[2]);
                        double y1 = Double.parseDouble(args[3]);
                        double z1 = Double.parseDouble(args[4]);
                        double x2 = Double.parseDouble(args[5]);
                        double y2 = Double.parseDouble(args[6]);
                        double z2 = Double.parseDouble(args[7]);
                        String areaName = args[8];

                        if (AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegenN.getLanguage("Area.AreaAlreadyExists").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        World world = player.getWorld();
                        Location loc1 = new Location(world, x1, y1, z1);
                        Location loc2 = new Location(world, x2, y2, z2);

                        AreaManager.createArea(areaName, world, loc1, loc2);

                        message = BlockRegenN.getLanguage("Area.Create").replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "remove": {
                        //blockregen area remove areaName
                        if (args.length != 3) {
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegenN.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.removeArea(areaName);
                        message = BlockRegenN.getLanguage("Area.Remove").replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "detect": {
                        //blockregen area detect areaName
                        if (args.length != 3) {
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegenN.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.writeInformation(areaName);
                        message = BlockRegenN.getLanguage("Area.Detect").replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "addBlock": {
                        //blockregen area addBlock areaName blockName
                        if (args.length != 4) {
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];
                        String blockName = args[3];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegenN.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        if (!BlockManager.getBlockList().contains(blockName)) {
                            message = BlockRegenN.getLanguage("Area.BlockNotFoundInServer").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.addRegenBlock(areaName, blockName);
                        message = BlockRegenN.getLanguage("Area.AddBlock").replace("%BlockName", blockName).replace("%AreaName", blockName);
                        player.sendMessage(message);
                        break;
                    }
                    case "removeBlock": {
                        //blockregen area removeBlock areaName blockName
                        if (args.length != 4) {
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];
                        String blockName = args[3];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegenN.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        if (!AreaManager.getAreaRegenBlockList(areaName).contains(blockName)) {
                            message = BlockRegenN.getLanguage("Area.BlockNotFoundInArea").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.removeRegenBlock(areaName, blockName);
                        message = BlockRegenN.getLanguage("Area.RemoveBlock").replace("%BlockName", blockName).replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "info": {
                        //blockregen area info areaName
                        if (args.length != 3) {
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegenN.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        message = AreaManager.getAreaInfo(areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "list": {
                        //blockregen area list
                        if (args.length != 2) {
                            message = BlockRegenN.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        message = AreaManager.getAreaList();
                        player.sendMessage(message);
                        break;
                    }
                    default: {
                        message = BlockRegenN.getLanguage("InvalidCommand");
                        player.sendMessage(message);
                        break;
                    }
                }
                break;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> none = new ArrayList<>();
        if (args.length == 1){
            return Arrays.asList("reload", "area", "block");
        }
        if ("block".equals(args[0])){

            if (args.length == 2){
                return Arrays.asList("create","remove", "addMaterial", "removeMaterial", "info", "list");
            }
            if ("create".equals(args[1])){
                //blockregen block create blockName
                return none;
            }
            if ("remove".equals(args[1])){
                //blockregen block remove blockName
                if (args.length == 3){
                    return BlockManager.getBlockList();
                }
            }
            if ("addMaterial".equals(args[1])){
                //blockregen block addMaterial blockName materialName
                if (args.length == 3){
                    return BlockManager.getBlockList();
                }
                if (args.length == 4){
                    for (Material material : EnumSet.allOf(Material.class)) {
                        none.add(material.name().toLowerCase());
                    }
                    return none;
                }
            }
            if ("removeMaterial".equals(args[1])){
                //blockregen block removeMaterial blockName index
                if (args.length == 3){
                    return BlockManager.getBlockList();
                }
                if (args.length == 4){
                    return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
                }
            }
            if ("info".equals(args[1])){
                //blockregen block info blockName
                if (args.length == 3){
                    return BlockManager.getBlockList();
                }
            }
        }

        if ("area".equals(args[0])) {

            if (args.length == 2) {
                return Arrays.asList("create", "remove", "detect", "addBlock", "removeBlock", "info", "list");
            }
            if ("create".equals(args[1])) {
                //blockregen area create x1 y1 z1 x2 y2 z2 areaName
                Player player;
                double x = 0;
                double y = 0;
                double z = 0;
                if (commandSender instanceof Player) {
                    player = (Player) commandSender;
                    x = player.getLocation().getX();
                    y = player.getLocation().getY();
                    z = player.getLocation().getZ();
                }

                if (args.length == 3) {
                    return Arrays.asList(String.valueOf(x), String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 4) {
                    return Arrays.asList(String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 5) {
                    return Collections.singletonList(String.valueOf(z));
                }
                if (args.length == 6) {
                    return Arrays.asList(String.valueOf(x), String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 7) {
                    return Arrays.asList(String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 8) {
                    return Collections.singletonList(String.valueOf(z));
                }
                if (args.length == 9) {
                    return new ArrayList<>();
                }
            }
            if ("remove".equals(args[1])) {
                //blockregen area remove areaName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
            }
            if ("detect".equals(args[1])) {
                //blockregen area detect areaName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
            }
            if ("addBlock".equals(args[1])) {
                //blockregen area addBlock areaName blockName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
                if (args.length == 4) {
                    return BlockManager.getBlockList();
                }
            }
            if ("removeBlock".equals(args[1])) {
                //blockregen area removeBlock areaName blockName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
                if (args.length == 4) {
                    return AreaManager.getAreaRegenBlockList(args[2]);
                }
            }
            if ("info".equals(args[1])) {
                //blockregen area info areaName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
            }
        }
        return new ArrayList<>();
    }
}
