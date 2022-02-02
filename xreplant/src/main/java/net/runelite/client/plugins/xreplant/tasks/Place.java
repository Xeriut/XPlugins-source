package net.runelite.client.plugins.xreplant.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xreplant.PatchState;
import net.runelite.client.plugins.xreplant.Task;
import net.runelite.client.plugins.xreplant.XReplantPlugin;

import java.util.List;

import static net.runelite.api.MenuAction.ITEM_USE_ON_GAME_OBJECT;

@Slf4j
public class Place extends Task {

    @Override
    public List<PatchState> getPatchState() {
        return List.of(PatchState.PLACE);
    }

    @Override
    public void routine(GameTick event) throws Exception {
        log.info("Placing task routine");
        int sapling = getSaplingID(XReplantPlugin.currentPatch.getType());
        if(sapling != 0) {
            WidgetItem saplingWidget = inventory.getWidgetItem(sapling);
            GameObject closestPatchObject = object.findNearestGameObject(XReplantPlugin.currentPatch.getPatchID());
            if (saplingWidget != null && closestPatchObject != null) {
                targetMenu = new LegacyMenuEntry("", "", closestPatchObject.getId(), ITEM_USE_ON_GAME_OBJECT.getId(), closestPatchObject.getSceneMinLocation().getX(), closestPatchObject.getSceneMinLocation().getY(), false);
                utils.doModifiedActionMsTime(targetMenu, saplingWidget.getId(), saplingWidget.getIndex(), ITEM_USE_ON_GAME_OBJECT.getId(), closestPatchObject.getConvexHull().getBounds(), sleepDelay());
                handled = true;
                return;
            } else {
                throw new Exception("Not placed yet.");
            }
        } else {
            XReplantPlugin.currentState = PatchState.IDLE;
            started = false;
            finished = true;
            return;
        }
    }

    @Override
    public void onFinished(GameTick event) {
        XReplantPlugin.timeout = tickDelay();
        XReplantPlugin.planted = true;
        XReplantPlugin.currentState = PatchState.PLACE_PAY;
        started = false;
        finished = true;
        return;
    }

}