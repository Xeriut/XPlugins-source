package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class TeleHomeTask extends Task {

    @Override
    public boolean validate() {
        if(inventory.containsItem(ItemID.TELEPORT_TO_HOUSE)) {
            if (!atPOH()) {
                if (atDragons()) {
                    if (shouldRestock(true)) {
                        log.info("We teleport home from dragons.");
                        return true;
                    }
                    if (client.getBoostedSkillLevel(Skill.HITPOINTS) <= XRuneDragonsPlugin.taskConfig.eatMin() && !inventory.containsItem(XRuneDragonsPlugin.taskConfig.foodID())) {
                        return true;
                    }
                    return false;
                }
                if (atEdge()) {
                    if (!shouldRestock(false)) {
                        return true;
                    }
                }
                if (!atEdge() && shouldRestock(false)) {
                    return true;
                }
                if (!atDragons() && !atEdge() && !atLith()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Teleport home";
    }

    @Override
    public void onGameTick(GameTick event) {
        WidgetItem teleItem = inventory.getWidgetItem(ItemID.TELEPORT_TO_HOUSE);
        if (teleItem != null) {
            started = true;
            useItem(teleItem);
            log.debug("Teleporting home");
            XRuneDragonsPlugin.deposited = false;
        }
        log.debug("Can't teleport home");
        XRuneDragonsPlugin.deposited = false;
    }

    @Override
    public void checkFinished(GameTick event) {
        if (atPOH()) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}