package net.zeroblue.testmod.config;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public static final ModConfig INSTANCE = new ModConfig();

    public boolean enabled = false;
    public int weaponSlot = 1; // 0-8
    public int defaultClickCount = 5;
    public int clickDelayMin = 3;
    public int clickDelayMax = 7;
    
    public boolean antiAfk = true;
    public float antiAfkRange = 0.5f;

    public List<MobSetting> mobSettings = new ArrayList<>();

    public ModConfig() {
        mobSettings.add(new MobSetting("Fried Chicken", "Smells of burning. Must be a Fried Chicken.", 3));
        mobSettings.add(new MobSetting("Fireproof Witch", "Trouble's brewing, it's a Fireproof Witch!", 5));
        mobSettings.add(new MobSetting("Fiery Scuttler", "A Fiery Scuttler inconspicuously waddles up to you, friends in tow.", 5));
        mobSettings.add(new MobSetting("Ragnarok", "The sky darkens and the air thickens. The end times are upon us: Ragnarok is here.", 20));
        mobSettings.add(new MobSetting("Magma Slug", "From beneath the lava appears a Magma Slug.", 4));
        mobSettings.add(new MobSetting("Moogma", "You hear a faint Moo from the lava... A Moogma appears.", 4));
        mobSettings.add(new MobSetting("Lava Leech", "A small but fearsome Lava Leech emerges.", 3));
        mobSettings.add(new MobSetting("Pyroclastic Worm", "You feel the heat radiating as a Pyroclastic Worm surfaces.", 6));
        mobSettings.add(new MobSetting("Lava Flame", "A Lava Flame flies out from beneath the lava.", 4));
        mobSettings.add(new MobSetting("Fire Eel", "A Fire Eel slithers out from the depths.", 5));
        mobSettings.add(new MobSetting("Taurus", "Taurus and his steed emerge.", 15));
        mobSettings.add(new MobSetting("Thunder", "You hear a massive rumble as Thunder emerges.", 15));
        mobSettings.add(new MobSetting("Lord Jawbus", "You have angered a legendary creature... Lord Jawbus has arrived.", 30));
        mobSettings.add(new MobSetting("Plhlegblast", "WOAH! A Plhlegblast appeared.", 10));
    }

    public static class MobSetting {
        public String name;
        public String message;
        public int clicks;

        public MobSetting(String name, String message, int clicks) {
            this.name = name;
            this.message = message;
            this.clicks = clicks;
        }
    }
}
