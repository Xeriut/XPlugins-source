package net.runelite.client.plugins.xoneclickspell;

import net.runelite.client.config.*;

@ConfigGroup(value = "xoneclickspell")
public interface XOneClickSpellConfig
        extends Config {

    @ConfigSection(keyName = "spellConfig", name = "Spell Configuration", description = "", position = 1)
    String spellConfig = "spellConfig";

    @ConfigItem(keyName = "spellName", name = "Spell name 1", description = "Choose which spell to cast on hotkey", position = 1, section = "spellConfig")
    default Spells spellName() {
        return Spells.ICE_BARRAGE;
    }

    @ConfigItem(keyName = "hotkey", name = "Hotkey 1", description = "Hotkey to cast spell", position = 2, section = "spellConfig")
    default Keybind hotkey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(keyName = "spellName2", name = "Spell name 2", description = "Choose which spell to cast on hotkey", position = 3, section = "spellConfig")
    default Spells spellName2() {
        return Spells.BLOOD_BARRAGE;
    }

    @ConfigItem(keyName = "hotkey2", name = "Hotkey 2", description = "Hotkey to cast spell", position = 4, section = "spellConfig")
    default Keybind hotkey2() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(keyName = "spellName3", name = "Spell name 3", description = "Choose which spell to cast on hotkey", position = 5, section = "spellConfig")
    default Spells spellName3() {
        return Spells.SHADOW_BARRAGE;
    }

    @ConfigItem(keyName = "hotkey3", name = "Hotkey 3", description = "Hotkey to cast spell", position = 6, section = "spellConfig")
    default Keybind hotkey3() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(keyName = "spellName4", name = "Spell name 4", description = "Choose which spell to cast on hotkey", position = 7, section = "spellConfig")
    default Spells spellName4() {
        return Spells.FIRE_SURGE;
    }

    @ConfigItem(keyName = "hotkey4", name = "Hotkey 4", description = "Hotkey to cast spell", position = 8, section = "spellConfig")
    default Keybind hotkey4() {
        return Keybind.NOT_SET;
    }
}
