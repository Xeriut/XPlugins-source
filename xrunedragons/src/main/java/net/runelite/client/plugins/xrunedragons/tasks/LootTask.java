package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
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
        return atDragons() && !XRuneDragonsPlugin.itemsToLoot.isEmpty() && !inventory.isFull();
    }

    @Override
    public String getTaskDescription() {
        return "Looting";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        lootItem(XRuneDragonsPlugin.itemsToLoot.get(0));
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


    private void lootItem(TileItem lootItem) {
        log.info("We start looting");
        if (lootItem != null) {
            log.info("Loot Item is not null " + lootItem.getTile());
            entry = new LegacyMenuEntry("", "", lootItem.getId(), MenuAction.GROUND_ITEM_THIRD_OPTION.getId(),
                    lootItem.getTile().getSceneLocation().getX(), lootItem.getTile().getSceneLocation().getY(), false);
            menu.setEntry(entry);
            mouse.delayMouseClick(lootItem.getTile().getItemLayer().getCanvasTilePoly().getBounds(), sleepDelay());
            log.info("Loot Item price getting");
            int itemPrice = utils.getItemPrice(lootItem.getId(), true) * lootItem.getQuantity();
            if (itemPrice > 0) {
                log.info("Loot Item price not null");
                XRuneDragonsPlugin.updateLoot(itemPrice);
            }
        }
        XRuneDragonsPlugin.timeout = tickDelay();
    }
}