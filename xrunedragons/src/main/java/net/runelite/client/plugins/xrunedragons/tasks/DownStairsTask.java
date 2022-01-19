package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class DownStairsTask extends Task {

    @Override
    public boolean validate() {
        if (XRuneDragonsPlugin.localPlayer.getWorldArea().intersectsWith(lithkrenTele)) {
           return true;
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Go down the stairs";
    }

    @Override
    public void onGameTick() {
        started = true;
        GameObject obj = object.findNearestGameObject(32113);
        utils.doGameObjectActionMsTime(obj, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
    }

    @Override
    public void checkFinished() {
        if (client.getLocalPlayer().getWorldArea().intersectsWith(lithkrenDownStairs)) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}