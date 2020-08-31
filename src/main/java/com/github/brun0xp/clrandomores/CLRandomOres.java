package com.github.brun0xp.clrandomores;

import com.github.brun0xp.clrandomores.listener.BlockBreakListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class CLRandomOres extends JavaPlugin implements Listener {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static CLRandomOres instance;

    @Override
    public void onEnable() {
        CLRandomOres.setInstance(this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
