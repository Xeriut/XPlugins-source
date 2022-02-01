package net.runelite.client.plugins.xreplant;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.plugins.iutils.scripts.UtilsScript;

import javax.inject.Inject;
import java.util.List;

@Slf4j
public abstract class Task extends UtilsScript {
    @Inject
    public Client client;
    @Inject
    public iUtils utils;
    @Inject
    public CalculationUtils calc;
    @Inject
    public NPCUtils npc;
    @Inject
    public InventoryUtils inventory;
    @Inject
    public PrayerUtils prayerUtils;
    @Inject
    public ObjectUtils object;
    @Inject
    public BankUtils bank;
    @Inject
    public WalkUtils walk;
    @Inject
    public KeyboardUtils keyboard;
    @Inject
    public InterfaceUtils interfaceUtils;
    @Inject
    public MenuUtils menu;
    @Inject
    public MouseUtils mouse;
    public LegacyMenuEntry entry;

    public boolean finished = false;
    public boolean started = false;
    public boolean handled = false;

    protected LegacyMenuEntry targetMenu;

    public Task() {
    }

    public void run(GameTick event, boolean start) {
        if(start) onStart(event);
        try {
            routine(event);
            log.info("routine finished");
            XReplantPlugin.taskIteration++;
            return;
        } catch (Exception e) {
            log.info("Something went wrong. running task " + e.getMessage());
            XReplantPlugin.taskIteration++;
            return;
        }
    }

    public boolean getFinished() {
        return finished;
    }

    public boolean getHandled() {
        return handled;
    }

    public boolean getStarted() {
        return started;
    }

    public void routine(GameTick event) throws Exception {
        return;
    }

    public void onStart(GameTick event) {
        if(!started) started = true;
        finished = false;
        return;
    }

    public void onFinished(GameTick event) {
        if(started) started = false;
        finished = true;
        return;
    }

    public void checkFinished(GameTick event) {
        onFinished(event);
        return;
    }

    public abstract List<PatchState> getPatchState();

    protected long sleepDelay() {
        return calc.randomDelay(XReplantPlugin.taskConfig.sleepWeightedDistribution(), XReplantPlugin.taskConfig.sleepMin(), XReplantPlugin.taskConfig.sleepMax(), XReplantPlugin.taskConfig.sleepDeviation(), XReplantPlugin.taskConfig.sleepTarget());
    }

    protected int tickDelay() {
        return (int) calc.randomDelay(XReplantPlugin.taskConfig.tickDelayWeightedDistribution(), XReplantPlugin.taskConfig.tickDelayMin(), XReplantPlugin.taskConfig.tickDelayMax(), XReplantPlugin.taskConfig.tickDelayDeviation(), XReplantPlugin.taskConfig.tickDelayTarget());
    }

    protected int getSaplingID(Patch.PatchType type) {
        switch(type) {
            case TREE: {
                return XReplantPlugin.taskConfig.treeSapling().getItemID();
            }
            default: {
                return XReplantPlugin.taskConfig.treeSapling().getItemID();
            }
        }
    }
}
