package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class DrinkTask extends Task {

    @Override
    public boolean validate() {
        if(atDragons()) {
            if (client.getBoostedSkillLevel(Skill.PRAYER) <= XRuneDragonsPlugin.taskConfig.prayerMin()) {
                return true;
            }
            if (client.getBoostedSkillLevel(Skill.STRENGTH) <= XRuneDragonsPlugin.taskConfig.combatMin()) {
                return true;
            }
            if (XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(6101) == 0) {
                if(inventory.containsItem(SUPER_EXTENDED_ANTIFIRE_POTS)) {
                    return true;
                }
            }
            if (!XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(3981) == 0) {
                if(inventory.containsItem(EXTENDED_ANTIFIRE_POTS)) {
                    return true;
                }
            }
        }
        if (XRuneDragonsPlugin.localPlayer.getWorldArea().intersectsWith(lithkrenNextToDoor)) {
            if (client.getBoostedSkillLevel(Skill.STRENGTH) <= XRuneDragonsPlugin.taskConfig.combatMin()) {
                return true;
            }
            if (XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(6101) == 0) {
                return true;
            }
            if (!XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(3981) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Drink potion";
    }

    @Override
    public void onGameTick(GameTick event) {
        if(atDragons()) {
            if (client.getBoostedSkillLevel(Skill.PRAYER) <= XRuneDragonsPlugin.taskConfig.prayerMin()) {
                WidgetItem prayerItem = inventory.getWidgetItem(PRAYER_POTS);
                if (prayerItem != null) {
                    started = true;
                    useItem(prayerItem);
                }
            }
            if (client.getBoostedSkillLevel(Skill.STRENGTH) <= XRuneDragonsPlugin.taskConfig.combatMin()) {
                if (XRuneDragonsPlugin.taskConfig.supercombats()) {
                    WidgetItem combatItem = inventory.getWidgetItem(DIVINE_SUPER_COMBAT_POTS);
                    if (combatItem != null) {
                        started = true;
                        useItem(combatItem);
                    }
                }
                if (!XRuneDragonsPlugin.taskConfig.supercombats()) {
                    WidgetItem combatItem = inventory.getWidgetItem(SUPER_COMBAT_POTS);
                    if (combatItem != null) {
                        started = true;
                        useItem(combatItem);
                    }
                }
            }
            if (XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(6101) == 0) {
                WidgetItem antiFireItem = inventory.getWidgetItem(SUPER_EXTENDED_ANTIFIRE_POTS);
                if (antiFireItem != null) {
                    started = true;
                    useItem(antiFireItem);
                }
            }
            if (!XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(3981) == 0) {
                WidgetItem antiFireItem = inventory.getWidgetItem(EXTENDED_ANTIFIRE_POTS);
                if (antiFireItem != null) {
                    started = true;
                    useItem(antiFireItem);
                }
            }
        }
        if (XRuneDragonsPlugin.localPlayer.getWorldArea().intersectsWith(lithkrenNextToDoor)) {
            if (client.getBoostedSkillLevel(Skill.STRENGTH) <= XRuneDragonsPlugin.taskConfig.combatMin()) {
                if (XRuneDragonsPlugin.taskConfig.supercombats()) {
                    WidgetItem combatItem = inventory.getWidgetItem(DIVINE_SUPER_COMBAT_POTS);
                    if (combatItem != null) {
                        started = true;
                        useItem(combatItem);
                    }
                }
                if (!XRuneDragonsPlugin.taskConfig.supercombats()) {
                    WidgetItem combatItem = inventory.getWidgetItem(SUPER_COMBAT_POTS);
                    if (combatItem != null) {
                        started = true;
                        useItem(combatItem);
                    }
                }
            }
            if (XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(6101) == 0) {
                WidgetItem antiFireItem = inventory.getWidgetItem(SUPER_EXTENDED_ANTIFIRE_POTS);
                if (antiFireItem != null) {
                    started = true;
                    useItem(antiFireItem);
                }
            }
            if (!XRuneDragonsPlugin.taskConfig.superantifire() && client.getVarbitValue(3981) == 0) {
                WidgetItem antiFireItem = inventory.getWidgetItem(EXTENDED_ANTIFIRE_POTS);
                if (antiFireItem != null) {
                    started = true;
                    useItem(antiFireItem);
                }
            }
        }
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}