package com.github.brun0xp.clrandomores.listener;

import com.github.brun0xp.clrandomores.CLRandomOres;
import com.github.brun0xp.clrandomores.model.Ore;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBreakListener implements Listener {

    private final CLRandomOres plugin = CLRandomOres.getInstance();

    @EventHandler
    public void onBreak(org.bukkit.event.block.BlockBreakEvent e) {
        ConfigurationSection cs = plugin.getConfig()
                .getConfigurationSection("miner." + e.getBlock().getWorld().getName().toLowerCase());
        if (cs == null) return;
        if (e.getBlock().getY() >= 128) return;
        if (!cs.getStringList("blocks").contains(e.getBlock().getType().name())) return;

        Ore ore = getRandomOre(e.getBlock().getWorld(), e.getBlock().getY(),
                e.getPlayer().getInventory().getItemInMainHand().getType());
        if (ore == null) return;

        e.getBlock().setType(ore.getMaterial());
    }

    private Ore getRandomOre(World world, int layer, Material pickaxeMaterial){
        val cs = plugin.getConfig()
                .getConfigurationSection("miner." + world.getName().toLowerCase() + ".ores");
        if (cs == null) return null;

        List<Ore> ores = new ArrayList<>();
        cs.getKeys(false).forEach(key -> {
            val oreConfig = cs.getConfigurationSection(key);
            val ore = new Ore();
            ore.setMaterial(Material.valueOf(key));
            ore.setPriority(oreConfig.getInt("priority", 0));

            val opIntLayer = oreConfig.getConfigurationSection("layer").getKeys(false).stream()
                    .mapToInt(Integer::parseInt)
                    .filter(layerConfig -> layerConfig >= layer).min();

            ore.setChance(oreConfig.getDouble("layer." + opIntLayer.orElse(0), 0));

            Random random = new SecureRandom();
            double sorted = random.nextDouble();
            if (sorted <= ore.getChance())
                ores.add(ore);
        });
        val bestPriority = ores.stream().mapToInt(Ore::getPriority).min().orElse(0);
        return ores.stream().filter(ore -> ore.getPriority() == bestPriority).findFirst().orElse(null);
    }
}
