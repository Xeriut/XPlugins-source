package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class WalkDoorTask extends Task {

    @Override
    public boolean validate() {
        if (XRuneDragonsPlugin.localPlayer.getWorldArea().intersectsWith(lithkrenBehindDoor)) {
            return true;
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Walk to door";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        walk.sceneWalk(new WorldPoint(1573, 5074, 0), 0, sleepDelay());
    }

    @Override
    public void checkFinished(GameTick event) {
        if (client.getLocalPlayer().getWorldLocation().equals(new WorldPoint(1573, 5074, 0))) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}