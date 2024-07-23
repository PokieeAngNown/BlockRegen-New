package cn.saltedfish.blockregenn.Managers;

import cn.saltedfish.blockregenn.BlockRegenN;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockManager {

    private static final File regenBlockListFile = new File(BlockRegenN.getPlugin().getDataFolder(), "regenBlockList.yml");
    public static File getRegenBlockListFile() {
        return regenBlockListFile;
    }

    public static @NotNull List<String> getBlockList() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
        Set<String> keys = cfg.getKeys(false);
        return new ArrayList<>(keys);
    }

    public static @NotNull List<Material> getBlockMaterialList(String blockName) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
        List<String> list = cfg.getStringList(blockName + ".Material");
        List<Material> materials = new ArrayList<>();
        for (String s : list) {
            materials.add(Material.matchMaterial(s));
        }
        return materials;
    }

    private static @NotNull List<String> getBlockMaterialListAsStringList(String blockName) {
        List<Material> materials = getBlockMaterialList(blockName);
        List<String> list = new ArrayList<>();
        for (Material material : materials) {
            list.add(material.name().toLowerCase());
        }
        return list;
    }

    public static void createBlock(String blockName){
        try {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
            cfg.set(blockName + ".Material", new ArrayList<>());
            cfg.save(getRegenBlockListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeBlock(String blockName) {
        try {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
            cfg.set(blockName, null);
            cfg.save(getRegenBlockListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMaterial(String blockName, @NotNull Material material) {
        try {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
            List<String> list =  cfg.getStringList(blockName + ".Material");
            list.add(material.name().toLowerCase());
            cfg.set(blockName + ".Material", list);
            cfg.save(getRegenBlockListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeMaterial(String blockName, Integer index) {
        try {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
            List<String> list =  cfg.getStringList(blockName + ".Material");
            int i = index;
            list.remove(i);
            cfg.set(blockName + ".Material", list);
            cfg.save(getRegenBlockListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull String getInfo(String blockName) {
        List<String> list = getBlockMaterialListAsStringList(blockName);

        String blockList = String.valueOf(list);

        return BlockRegenN.getLanguage("Block.Info")
                .replaceAll("%MaterialList", blockList);
    }

    public static @NotNull String getList() {
        List<String> list = getBlockList();
        String blockList = String.valueOf(list);
        return BlockRegenN.getLanguage("Block.List")
                .replaceAll("%BlockList", blockList);
    }
}
