package net.runelite.client.plugins.xreplant;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.plugins.xreplant.tasks.*;
import org.apache.commons.lang3.ArrayUtils;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(name = "XReplant", description = "Replant farming patches for you.",
        tags = {"replant", "patches", "farming"},
        enabledByDefault = false)
@Slf4j
public class XReplantPlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private iUtils utils;
    @Inject
    private NPCUtils npcUtils;
    @Inject
    private ObjectUtils object;
    @Inject
    private CalculationUtils calc;
    @Inject
    private InventoryUtils inventory;
    @Inject
    private XReplantConfig config;

    private ExecutorService executorService;

    public static XReplantConfig taskConfig;

    private LegacyMenuEntry targetMenu;

    public static PatchState currentState = PatchState.IDLE;

    public static Patch currentPatch;

    public static int timeout;
    public static TaskSet tasks = new TaskSet();
    private Task task;
    public static int taskIteration = 0;
    public static boolean checked = false;
    public static boolean planted = false;
    public static boolean paid = false;

    private List<Integer> allPatchIDS = new ArrayList<Integer>();

    @Provides
    XReplantConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(XReplantConfig.class);
    }

    protected void startUp() {
        executorService = Executors.newSingleThreadExecutor();
        setupPatchIDs();
        taskConfig = config;
        loadTasks();
    }

    protected void shutDown() {
        currentState = PatchState.IDLE;
        executorService.shutdown();
    }

    private void loadTasks() {
        tasks.clear();
        tasks.addAll(
                injector.getInstance(Interact.class),
                injector.getInstance(Raking.class),
                injector.getInstance(Place.class),
                injector.getInstance(Pay.class),
                injector.getInstance(Continue.class)
        );
    }

    public static void reset() {
        checked = false;
        planted = false;
        paid = false;
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (client.getGameState() != GameState.LOGGED_IN || currentState == PatchState.IDLE)
        {
            return;
        }
        if(timeout > 0) {
            timeout--;
            return;
        }
        log.info("CurrentState: " + currentState);
        if (task != null && task.getFinished() && task.getHandled()) {
            task = tasks.getValidTask();
            if(task != null) {
                task.run(event, true);
            } else {
                currentState = PatchState.IDLE;
            }
            return;
        } else if (task != null && !task.getFinished() && task.getHandled()) {
            task.checkFinished(event);
            return;
        } else if (task != null && !task.getFinished() && !task.getHandled()) {
            task.run(event, false);
            return;
        } else {
            task = tasks.getValidTask();
            if(task != null) {
                task.run(event, true);
            } else {
                currentState = PatchState.IDLE;
            }
            return;
        }
    }

    @Subscribe
    private void onMenuOptionClicked(MenuOptionClicked event) {
        if (event.getMenuOption().contains("Replant")) {
            currentState = PatchState.IDLE;
            log.info("Started replanting");
            WorldPoint loc = client.getLocalPlayer().getWorldLocation();
            GameObject closestPatchObject = object.findNearestGameObject(ArrayUtils.toPrimitive(allPatchIDS.toArray(new Integer[0])));
            if(closestPatchObject != null) {
                Patch closestPatch = Patch.patchFromObjectID(closestPatchObject.getId());
                if(closestPatch != null) {
                    List<Integer> requiredInventory = initInventory(closestPatch.getType());
                    if(!inventory.containsAllOf(requiredInventory)) {
                        utils.sendGameMessage("Make sure to have all required items in your inventory!");
                        return;
                    }
                    PatchState growthState = closestPatch.getPatchGrowthState().forVarbitValue(client.getVar(closestPatch.getVarbit()));
                    log.info("state : " + growthState);
                    currentPatch = closestPatch;
                    if(growthState == null) {
                        log.info("This patch is still growing.");
                        currentState = PatchState.IDLE;
                    } else {
                        switch(growthState) {
                            case RAKE: {
                                checked = true;
                                planted = false;
                                paid = false;
                            }
                            case PLACE: {
                                checked = true;
                                planted = false;
                                paid = false;
                            }
                            case CHECK: {
                                checked = false;
                                planted = false;
                                paid = false;
                            }
                            case HARVEST: {
                                checked = true;
                                planted = false;
                                paid = false;
                            }
                            default: {
                                checked = true;
                                planted = true;
                                paid = true;
                            }
                        }
                        currentState = growthState;
                    }
                    return;
                }
            }
        }
    }

    @Subscribe
    private void onMenuEntryAdded(MenuEntryAdded event) {
        if (client.isMenuOpen())
        {
            return;
        }
        if(!allPatchIDS.contains(event.getIdentifier())) {
            return;
        }
        if (event.getOption().contains("Check-health") || event.getOption().contains("Chop") || event.getOption().contains("Inspect") || event.getOption().contains("Rake")) {
            addMenuEntry(event, "<col=237d23>Replant");
            MenuEntry[] entries = client.getMenuEntries();
            Collections.reverse(Arrays.asList(entries));
            MenuEntry[] newEntries = Arrays.stream(entries).distinct().toArray(MenuEntry[]::new);
            Collections.reverse(Arrays.asList(newEntries));
            client.setMenuEntries(newEntries);
        }
    }

    private void addMenuEntry(MenuEntryAdded event, String option) {
        client.createMenuEntry(-1).setOption(option)
                .setTarget(event.getTarget())
                .setIdentifier(0)
                .setParam1(0)
                .setParam1(0)
                .setType(MenuAction.GAME_OBJECT_FIRST_OPTION);
    }

    private long sleepDelay() {
        return calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
    }

    private List<Integer> initInventory(Patch.PatchType type) {
        List<Integer> inventorySetup = new ArrayList<>();

        // Required farming items
        inventorySetup.add(ItemID.SPADE);
        inventorySetup.add(ItemID.RAKE);
        inventorySetup.add(ItemID.COINS_995);

        if(type == Patch.PatchType.TREE) {
            // Tree items setup
            inventorySetup.add(config.treeSapling().getItemID());
        }

        log.info("required inventory items: {}", inventorySetup.toString());

        return inventorySetup;
    }
    
    private void setupPatchIDs () {
        for(Patch patch : Patch.values()) {
            allPatchIDS.add(patch.getPatchID());
        }
    }
}