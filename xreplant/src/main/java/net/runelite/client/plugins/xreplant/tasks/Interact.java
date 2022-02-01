package net.runelite.client.plugins.xreplant.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xreplant.PatchState;
import net.runelite.client.plugins.xreplant.Task;
import net.runelite.client.plugins.xreplant.XReplantPlugin;

import java.util.List;

@Slf4j
public class Interact extends Task {

    @Override
    public List<PatchState> getPatchState() {
        return List.of(PatchState.RAKE, PatchState.CHECK);
    }

    @Override
    public void routine(GameTick event) throws Exception {
        GameObject patchObject = object.findNearestGameObject(XReplantPlugin.currentPatch.getPatchID());
        if(patchObject != null) {
            utils.doGameObjectActionMsTime(patchObject, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), 0);
            handled = true;
            return;
        } else {
            throw new Exception("No patch object found.");
        }
    }

    @Override
    public void onFinished(GameTick event) {
        log.info("Finishing interact task");
        XReplantPlugin.timeout = tickDelay();
        if(XReplantPlugin.currentState == PatchState.RAKE) {
            XReplantPlugin.currentState = PatchState.RAKING;
        }
        if(XReplantPlugin.currentState == PatchState.CHECK) {
            XReplantPlugin.currentState = PatchState.CHECK_PAY;
        }
        started = false;
        finished = true;
        return;
    }

}