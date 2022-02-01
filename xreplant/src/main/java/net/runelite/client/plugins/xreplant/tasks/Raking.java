package net.runelite.client.plugins.xreplant.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xreplant.PatchState;
import net.runelite.client.plugins.xreplant.Task;
import net.runelite.client.plugins.xreplant.XReplantPlugin;

import java.util.List;

@Slf4j
public class Raking extends Task {

    @Override
    public List<PatchState> getPatchState() {
        return List.of(PatchState.RAKING);
    }

    @Override
    public void routine(GameTick event) throws Exception {
        log.info("Raking task routine");
        int currentAnimation = client.getLocalPlayer().getAnimation();
        if(currentAnimation == -1) {
            handled = true;
            return;
        } else {
            throw new Exception("Still raking.");
        }
    }

    @Override
    public void onFinished(GameTick event) {
        log.info("Finishing raking task");
        XReplantPlugin.timeout = tickDelay();
        XReplantPlugin.currentState = PatchState.PLACE;
        started = false;
        finished = true;
        return;
    }

}