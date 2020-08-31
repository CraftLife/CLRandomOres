package com.github.brun0xp.clrandomores.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
public class Ore {

    private Material material;
    private int priority;
    private double chance = 0;
}

