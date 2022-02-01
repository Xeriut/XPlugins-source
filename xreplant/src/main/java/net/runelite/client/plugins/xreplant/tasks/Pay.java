package net.runelite.client.plugins.xreplant.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xreplant.PatchState;
import net.runelite.client.plugins.xreplant.Task;
import net.runelite.client.plugins.xreplant.XReplantPlugin;

import java.util.List;

@Slf4j
public class Pay extends Task {

    @Override
    public List<PatchState> getPatchState() {
        return List.of(PatchState.PAY, PatchState.HARVEST, PatchState.CHECK_PAY, PatchState.HARVEST_PAY, PatchState.PLACE_PAY);
    }

    @Override
    public void routine(GameTick event) throws Exception {
        NPC farmer = npc.findNearestNpc(XReplantPlugin.currentPatch.getFarmerID());
        if(farmer != null) {
            targetMenu = new LegacyMenuEntry("", "", farmer.getIndex(), MenuAction.NPC_THIRD_OPTION.getId(), 0, 0, false);
            utils.doActionMsTime(targetMenu, new Point(0, 0), sleepDelay());
            handled = true;
            return;
        } else {
            throw new Exception("No farmer found.");
        }
    }

    @Override
    public void checkFinished(GameTick event) {
        Widget payDialog = client.getWidget(WidgetInfo.DIALOG_OPTION_OPTION1);
        Widget npcDialog = client.getWidget(WidgetInfo.DIALOG_NPC_NAME);
        if(npcDialog != null || payDialog != null) {
            onFinished(event);
        } else {
            handled = false;
        }
        return;
    }

    @Override
    public void onFinished(GameTick event) {
        log.info("Finishing pay task");
        if(XReplantPlugin.currentState == PatchState.PLACE_PAY) {
            XReplantPlugin.currentState = PatchState.DOUBLE_CONTINUE;
        } else if(XReplantPlugin.currentState == PatchState.CHECK_PAY || XReplantPlugin.currentState == PatchState.HARVEST_PAY || XReplantPlugin.currentState == PatchState.HARVEST){
            XReplantPlugin.currentState = PatchState.PLACE_CONTINUE;
        } else {
            XReplantPlugin.currentState = PatchState.SINGLE_CONTINUE;
        }
        started = false;
        finished = true;
        return;
    }

}