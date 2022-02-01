package net.runelite.client.plugins.xreplant;

import net.runelite.client.config.*;
import net.runelite.client.plugins.xreplant.patchtypes.Tree;

@ConfigGroup(value = "xautoreplant")
public interface XReplantConfig
        extends Config {

    @ConfigSection(keyName = "delayConfig", name = "Sleep Delays", description = "Configure sleep delays", position = 0, closedByDefault = true)
    String delayConfig = "delayConfig";
    @ConfigSection(keyName = "tickConfig", name = "Tick Delays", description = "Configure tick delays", position = 1, closedByDefault = true)
    String tickConfig = "tickConfig";
    @ConfigSection(keyName = "treeConfig", name = "Tree Configuration", description = "", position = 2)
    String treeConfig = "treeConfig";

    @Range(min = 0, max = 550)
    @ConfigItem(keyName = "sleepMin", name = "Sleep Min", description = "", position = 2, section = "delayConfig")
    default int sleepMin() {
        return 60;
    }

    @Range(min = 0, max = 550)
    @ConfigItem(keyName = "sleepMax", name = "Sleep Max", description = "", position = 3, section = "delayConfig")
    default int sleepMax() {
        return 350;
    }

    @Range(min = 0, max = 550)
    @ConfigItem(keyName = "sleepTarget", name = "Sleep Target", description = "", position = 4, section = "delayConfig")
    default int sleepTarget() {
        return 100;
    }

    @Range(min = 0, max = 550)
    @ConfigItem(keyName = "sleepDeviation", name = "Sleep Deviation", description = "", position = 5, section = "delayConfig")
    default int sleepDeviation() {
        return 10;
    }

    @ConfigItem(keyName = "sleepWeightedDistribution", name = "Sleep Weighted Distribution", description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution", position = 6, section = "delayConfig")
    default boolean sleepWeightedDistribution() {
        return false;
    }

    @Range(min = 0, max = 10)
    @ConfigItem(keyName = "tickDelayMin", name = "Game Tick Min", description = "", position = 8, section = "tickConfig")
    default int tickDelayMin() {
        return 1;
    }

    @Range(min = 0, max = 10)
    @ConfigItem(keyName = "tickDelayMax", name = "Game Tick Max", description = "", position = 9, section = "tickConfig")
    default int tickDelayMax() {
        return 3;
    }

    @Range(min = 0, max = 10)
    @ConfigItem(keyName = "tickDelayTarget", name = "Game Tick Target", description = "", position = 10, section = "tickConfig")
    default int tickDelayTarget() {
        return 2;
    }

    @Range(min = 0, max = 10)
    @ConfigItem(keyName = "tickDelayDeviation", name = "Game Tick Deviation", description = "", position = 11, section = "tickConfig")
    default int tickDelayDeviation() {
        return 1;
    }

    @ConfigItem(keyName = "tickDelayWeightedDistribution", name = "Game Tick Weighted Distribution", description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution", position = 12, section = "tickConfig")
    default boolean tickDelayWeightedDistribution() {
        return false;
    }

    @ConfigItem(keyName = "treeSapling", name = "Tree patch sapling", description = "Choose which sapling to use on tree patches", position = 1, section = "treeConfig")
    default Tree treeSapling() {
        return Tree.MAGIC;
    }

}
