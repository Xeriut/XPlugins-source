package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.DecorativeObject;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class TeleEdgeTask extends Task {

    @Override
    public boolean validate() {
        if (atPOH()) {
            return shouldRestock(false);
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Teleport to edge";
    }

    @Override
    public void onGameTick(GameTick event) {
        DecorativeObject decObstacle = object.findNearestDecorObject(13523);
        if (decObstacle != null) {
            started = true;
            entry = new LegacyMenuEntry("", "", decObstacle.getId(), MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), decObstacle.getLocalLocation().getSceneX(), decObstacle.getLocalLocation().getSceneY(), false);
            utils.doActionMsTime(entry, decObstacle.getConvexHull().getBounds(), sleepDelay());
        }
    }

    @Override
    public void checkFinished(GameTick event) {
        if (atEdge()) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}