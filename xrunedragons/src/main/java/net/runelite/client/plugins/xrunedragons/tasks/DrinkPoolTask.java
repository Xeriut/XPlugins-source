package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class DrinkPoolTask extends Task {

    @Override
    public boolean validate() {
        if (atPOH()) {
            if (client.getBoostedSkillLevel(Skill.PRAYER) < client.getRealSkillLevel(Skill.PRAYER) && XRuneDragonsPlugin.taskConfig.usePOHpool()) {
                return true;
            }
            if (client.getBoostedSkillLevel(Skill.HITPOINTS) < client.getRealSkillLevel(Skill.HITPOINTS) && XRuneDragonsPlugin.taskConfig.usePOHpool()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Drink from pool";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        GameObject obj = object.findNearestGameObject(29240, 29241);
        utils.doGameObjectActionMsTime(obj, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}