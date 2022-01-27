package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class EnterDoorTask extends Task {

    @Override
    public boolean validate() {
        return client.getLocalPlayer().getWorldArea().intersectsWith(lithkrenDownStairs);
    }

    @Override
    public String getTaskDescription() {
        return "Enter door";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        GameObject obj = object.findNearestGameObject(32117);
        utils.doGameObjectActionMsTime(obj, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
    }

    @Override
    public void checkFinished(GameTick event) {
        if (client.getLocalPlayer().getWorldArea().intersectsWith(lithkrenBehindDoor)) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}