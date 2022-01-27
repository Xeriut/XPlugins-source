package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class EnterDragonsTask extends Task {

    @Override
    public boolean validate() {
        return XRuneDragonsPlugin.localPlayer.getWorldArea().intersectsWith(lithkrenNextToDoor);
    }

    @Override
    public String getTaskDescription() {
        return "Enter the dragons area";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        GameObject obj = object.findNearestGameObject(32153);
        utils.doGameObjectActionMsTime(obj, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}