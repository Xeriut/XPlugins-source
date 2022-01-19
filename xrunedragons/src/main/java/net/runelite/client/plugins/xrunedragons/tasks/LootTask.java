package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.TileItem;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

import java.util.List;

@Slf4j
public class LootTask extends Task {

    @Override
    public boolean validate() {
        if (atDragons() && !XRuneDragonsPlugin.itemsToLoot.isEmpty() && !inventory.isFull()) {
            return true;
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Looting";
    }

    @Override
    public void onGameTick() {
        started = true;
        lootItem(XRuneDragonsPlugin.itemsToLoot);
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }

    private TileItem getNearestTileItem(List<TileItem> tileItems) {
        int currentDistance;
        TileItem closestTileItem = tileItems.get(0);
        int closestDistance = closestTileItem.getTile().getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation());
        for (TileItem tileItem : tileItems) {
            currentDistance = tileItem.getTile().getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation());
            if (currentDistance < closestDistance) {
                closestTileItem = tileItem;
                closestDistance = currentDistance;
            }
        }
        return closestTileItem;
    }


    private void lootItem(List<TileItem> itemList) {
        TileItem lootItem = getNearestTileItem(itemList);
        if (lootItem != null) {
            entry = new LegacyMenuEntry("", "", lootItem.getId(), MenuAction.GROUND_ITEM_THIRD_OPTION.getId(),
                    lootItem.getTile().getSceneLocation().getX(), lootItem.getTile().getSceneLocation().getY(), false);
            menu.setEntry(entry);
            mouse.delayMouseClick(lootItem.getTile().getItemLayer().getCanvasTilePoly().getBounds(), sleepDelay());
        }
        XRuneDragonsPlugin.timeout = tickDelay();
    }
}