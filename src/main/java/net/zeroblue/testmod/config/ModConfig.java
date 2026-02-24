package net.zeroblue.testmod.config;

public class ModConfig {
    public static final ModConfig INSTANCE = new ModConfig();

    public boolean enabled = false;
    public int weaponSlot = 1; // 0-8
    public int clickCount = 5;
    public int clickDelay = 4; // ticks between clicks
    public boolean antiAfk = true;
    public float antiAfkRange = 0.5f;

    // We can add more like "recast delay" later
}
