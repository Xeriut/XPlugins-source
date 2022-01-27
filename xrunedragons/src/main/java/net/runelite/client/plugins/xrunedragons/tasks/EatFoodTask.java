package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class EatFoodTask extends Task {

    @Override
    public boolean validate() {
        if (inventory.containsItem(XRuneDragonsPlugin.taskConfig.foodID())) {
            if (atDragons()) {
                if (client.getBoostedSkillLevel(Skill.HITPOINTS) <= XRuneDragonsPlugin.taskConfig.eatMin()) {
                    return true;
                }
                return inventory.isFull() && !XRuneDragonsPlugin.itemsToLoot.isEmpty();
            }
            return false;
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Eat food";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        WidgetItem foodItem = inventory.getWidgetItem(XRuneDragonsPlugin.taskConfig.foodID());
        if (foodItem != null) {
            useItem(foodItem);
        }
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}