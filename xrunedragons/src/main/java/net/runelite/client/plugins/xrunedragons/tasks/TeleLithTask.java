package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.DecorativeObject;
import net.runelite.api.MenuAction;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
public class TeleLithTask extends Task {

    @Override
    public boolean validate() {
        if (!atLith()) {
            if (XRuneDragonsPlugin.taskConfig.usePOHdigsite()) {
                if (atPOH()) {
                    return !shouldRestock(false);
                }
            } else {
                return !shouldRestock(false) && inventory.containsItem(DIGSITE_PENDANTS);
            }
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Teleport to lith";
    }

    @Override
    public void onGameTick(GameTick event) {
        if (XRuneDragonsPlugin.taskConfig.usePOHdigsite()) {
            DecorativeObject decObstacle = object.findNearestDecorObject(33418);
            if (decObstacle != null) {
                started = true;
                entry = new LegacyMenuEntry("", "", decObstacle.getId(), MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), decObstacle.getLocalLocation().getSceneX(), decObstacle.getLocalLocation().getSceneY(), false);
                utils.doActionMsTime(entry, decObstacle.getConvexHull().getBounds(), sleepDelay());
                XRuneDragonsPlugin.deposited = false;
            }
        } else {
            Collection<WidgetItem> items = inventory.getAllItems();
            Optional<WidgetItem> teleItem = items.stream().filter(e -> DIGSITE_PENDANTS.stream().anyMatch(x -> x == e.getId())).min(Comparator.comparing(WidgetItem::getId));
            if (teleItem.isPresent()) {
                started = true;
                entry = new LegacyMenuEntry("", "", teleItem.get().getId(), MenuAction.ITEM_FOURTH_OPTION.getId(), teleItem.get().getIndex(),
                        WidgetInfo.INVENTORY.getId(), false);
                utils.doActionGameTick(entry, teleItem.get().getCanvasBounds(), 0);
                entry = new LegacyMenuEntry("", "", 0, MenuAction.WIDGET_TYPE_6.getId(), 3, WidgetInfo.DIALOG_OPTION_OPTION1.getId(), false);
                utils.doActionGameTick(entry, new Point(0, 0), 2);
                XRuneDragonsPlugin.deposited = false;
            }
        }

    }

    @Override
    public void checkFinished(GameTick event) {
        if (atLith()) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}