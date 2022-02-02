package net.runelite.client.plugins.xjadswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup(value = "xjadswapper")
public interface XJadSwapperConfig
        extends Config {

    @ConfigItem(keyName = "hotkey", name = "Hotkey", description = "Hotkey to toggle swapper", position = 1)
    default Keybind hotkey() {
        return Keybind.NOT_SET;
    }


    @ConfigItem(keyName = "offensivePray", name = "Offensive prayer", description = "Use offensive prayers?", position = 2)
    default boolean offensivePray() {
        return false;
    }


}
