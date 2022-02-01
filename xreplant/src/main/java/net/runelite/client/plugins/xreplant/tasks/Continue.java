package net.runelite.client.plugins.xreplant.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
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
public class Continue extends Task {

    @Override
    public List<PatchState> getPatchState() {
        return List.of(PatchState.SINGLE_CONTINUE, PatchState.DOUBLE_CONTINUE, PatchState.PLACE_CONTINUE);
    }

    @Override
    public void routine(GameTick event) throws Exception {
        Widget payDialog = client.getWidget(WidgetInfo.DIALOG_OPTION_OPTION1);
        Widget npcDialog = client.getWidget(WidgetInfo.DIALOG_NPC_NAME);
        if (payDialog != null) {
            targetMenu = new LegacyMenuEntry("", "", 0, MenuAction.WIDGET_TYPE_6.getId(), 1, WidgetInfo.DIALOG_OPTION_OPTION1.getId(), false);
            utils.doActionMsTime(targetMenu, new Point(0, 0), sleepDelay());
            handled = true;
            return;
        } else if(npcDialog != null) {
            targetMenu = new LegacyMenuEntry("Continue", "", 0, MenuAction.WIDGET_TYPE_6.getId(),
                    -1, 15138820, false);
            utils.doActionMsTime(targetMenu, new Point(0, 0), sleepDelay());
            handled = true;
            return;
        } else {
            XReplantPlugin.currentState = PatchState.PAY;
            handled = true;
            return;
        }
    }

    @Override
    public void checkFinished(GameTick event) {
        Widget npcDialog = client.getWidget(WidgetInfo.DIALOG_NPC_NAME);
        if(npcDialog != null && XReplantPlugin.currentState == PatchState.DOUBLE_CONTINUE) {
            XReplantPlugin.currentState = PatchState.SINGLE_CONTINUE;
            onFinished(event);
        }
        if(npcDialog == null && XReplantPlugin.currentState == PatchState.PLACE_CONTINUE) {
            XReplantPlugin.currentState = PatchState.PLACE;
            onFinished(event);
        }
        if(npcDialog == null && XReplantPlugin.currentState == PatchState.SINGLE_CONTINUE) {
            XReplantPlugin.reset();
            XReplantPlugin.currentState = PatchState.IDLE;
            onFinished(event);
        }
        return;
    }

    @Override
    public void onFinished(GameTick event) {
        log.info("Finishing continue task");
        XReplantPlugin.timeout = tickDelay();
        started = false;
        finished = true;
        return;
    }

}