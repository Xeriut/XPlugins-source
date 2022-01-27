package net.runelite.client.plugins.xrunedragons;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.plugins.iutils.api.Spells;
import net.runelite.client.plugins.xrunedragons.tasks.*;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(name = "XRuneDragons", enabledByDefault = false, description = "Xeriuk - Rune dragons killer", tags = {
        "xeriuk", "rune", "dragon", "bot" })
@Slf4j
public class XRuneDragonsPlugin extends Plugin {

    public static XRuneDragonsConfig taskConfig;
    public static TaskSet tasks = new TaskSet();
    public static boolean running;
    public static LocalPoint beforeLoc = new LocalPoint(0, 0);
    public static String status = "Starting...";
    public static List<Integer> inventorySetup = new ArrayList<>();
    public static List<TileItem> itemsToLoot = new ArrayList<>();
    public static NPC currentNPC;
    public static WorldPoint deathLocation;
    public static Player localPlayer;
    public static boolean deposited = false;
    public static LegacyMenuEntry targetMenu;
    public static long sleepLength;
    public static int tickLength;
    public static int timeout;
    public static int mainWeaponId = 0;
    public static int shieldId = 0;
    static int killCount;
    static int killsPerH;
    static int totalLoot;
    static int lootPerH;
    Instant botTimer;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private iUtils utils;
    @Inject
    private InventoryUtils inventory;
    @Inject
    private PlayerUtils player;
    @Inject
    private CalculationUtils calc;
    @Inject
    private MouseUtils mouse;
    @Inject
    private MenuUtils menu;
    @Inject
    private XRuneDragonsConfig config;
    @Inject
    private InterfaceUtils interfaceUtils;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private XRuneDragonsOverlay overlay;
    @Inject
    private ConfigManager configManager;
    private Task task;
    private int taskIteration;

    public static void updateLoot(int amount) {
        totalLoot += amount;
    }

    @Provides
    XRuneDragonsConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(XRuneDragonsConfig.class);
    }

    @Override
    protected void startUp() {

    }

    @Override
    protected void shutDown() {
        resetPlugin();
    }

    private void loadTasks() {
        tasks.clear();
        tasks.addAll(
                injector.getInstance(TimeoutTask.class),
                injector.getInstance(DepositTask.class),
                injector.getInstance(WithdrawTask.class),
                injector.getInstance(TeleLithTask.class),
                injector.getInstance(TeleHomeTask.class),
                injector.getInstance(PrayTask.class),
                injector.getInstance(EatFoodTask.class),
                injector.getInstance(DrinkTask.class),
                injector.getInstance(LootTask.class),
                injector.getInstance(SpecTask.class),
                injector.getInstance(EquipTask.class),
                injector.getInstance(AttackDragonTask.class),
                injector.getInstance(InCombatTask.class),
                injector.getInstance(DrinkPoolTask.class),
                injector.getInstance(DownStairsTask.class),
                injector.getInstance(EnterDoorTask.class),
                injector.getInstance(WalkDoorTask.class),
                injector.getInstance(EnterDragonsTask.class),
                injector.getInstance(TeleEdgeTask.class),
                injector.getInstance(FindBankTask.class),
                injector.getInstance(LogoutTask.class));
    }

    private void resetPlugin() {
        running = false;
        task = null;
        tasks.clear();
        inventorySetup.clear();
        itemsToLoot.clear();
        overlayManager.remove(overlay);
        currentNPC = null;
        deposited = false;
        tickLength = 0;
        sleepLength = 0;
        killCount = 0;
        killsPerH = 0;
        totalLoot = 0;
        lootPerH = 0;
    }

    @Subscribe
    private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked) {
        if (!configButtonClicked.getGroup().equalsIgnoreCase("xruned")) {
            return;
        }

        if (configButtonClicked.getKey().equals("startButton")) {
            if (!running) {
                Player player = client.getLocalPlayer();
                if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN) {
                    log.info("Starting XRuneDragons");
                    if (initInventory()) {
                        task = null;
                        taskConfig = config;
                        loadTasks();
                        running = true;
                        tickLength = 0;
                        sleepLength = 0;
                        timeout = 0;
                        targetMenu = null;
                        botTimer = Instant.now();
                        overlayManager.add(overlay);
                        beforeLoc = client.getLocalPlayer().getLocalLocation();
                        killCount = 0;
                        killsPerH = 0;
                        totalLoot = 0;
                        lootPerH = 0;
                        return;
                    } else {
                        resetPlugin();
                        return;
                    }

                } else {
                    log.info("Start logged in");
                    return;
                }
            } else {
                resetPlugin();
            }
        }
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (!running) {
            return;
        }
        localPlayer = client.getLocalPlayer();
        if (client != null && localPlayer != null && client.getGameState() == GameState.LOGGED_IN) {
            if (config.useVengeance() && !canVengeance() && task == null) {
                utils.sendGameMessage("Don't have Vengeance runes in inventory");
                resetPlugin();
                return;
            }
            if (config.useVengeance() && !canVengeance() && task != null) {
                utils.sendGameMessage("Can't cast vengeance. Teleporting and logging out..");
                WidgetItem teleItem = inventory.getWidgetItem(ItemID.TELEPORT_TO_HOUSE);
                if (teleItem != null) {
                    targetMenu = new LegacyMenuEntry("", "", teleItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(),
                            teleItem.getIndex(),
                            WidgetInfo.INVENTORY.getId(), false);
                    utils.doActionMsTime(targetMenu, teleItem.getCanvasBounds(), 0);
                }
                resetPlugin();
                interfaceUtils.logout();
                return;
            }
            if (!client.isResized()) {
                utils.sendGameMessage("Please set game to resizable mode.");
                resetPlugin();
                return;
            }
            if (client.getWidget(WidgetInfo.BANK_PIN_CONTAINER) != null) {
                log.info("Enter bank pin manually");
                utils.sendGameMessage("Enter bank pin manually");
                timeout = 3;
                return;
            }
            if (timeout > 0) {
                timeout--;
                return;
            }
            if ((task != null && task.isFinished() && task.isStarted()) || taskIteration > 7) {
                taskIteration = 0;
                task.started = false;
                task.finished = false;
                task = tasks.getValidTask();
                if (task.getTaskDescription() != null) {
                    status = task.getTaskDescription();
                }
                task.onGameTick(event);
                taskIteration++;
                if (status == "Logout")
                    resetPlugin();
            } else if (task != null && !task.isFinished() && task.isStarted()) {
                task.checkFinished(event);
                taskIteration++;
            } else if (task != null && !task.isFinished() && !task.isStarted()) {
                status = task.getTaskDescription();
                task.onGameTick(event);
                taskIteration++;
                if (status == "Logout")
                    resetPlugin();
            } else {
                taskIteration = 0;
                task = tasks.getValidTask();
            }
            beforeLoc = localPlayer.getLocalLocation();
            updateStats();
        }
    }

    @Subscribe
    private void onActorDeath(ActorDeath event) {
        if (!running) {
            return;
        }
        if (event.getActor() == currentNPC) {
            deathLocation = event.getActor().getWorldLocation();
            log.debug("Our npc died, updating deathLocation: {}", deathLocation.toString());
            currentNPC = null;
            killCount++;
        }
    }

    @Subscribe
    private void onItemSpawned(ItemSpawned event) {
        if (!running) {
            return;
        }
        if (lootableItem(event.getItem())) {
            log.debug("Adding loot item: {}", client.getItemDefinition(event.getItem().getId()).getName());
            itemsToLoot.add(event.getItem());
        }
    }

    @Subscribe
    private void onItemDespawned(ItemDespawned itemDespawned) {
        itemsToLoot.remove(itemDespawned.getItem());
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged gameStateChanged) {
        itemsToLoot.clear();
        switch (gameStateChanged.getGameState()) {
            case CONNECTION_LOST:
            case LOGGING_IN:
            case HOPPING: {
                resetPlugin();
                break;
            }
        }
        if (gameStateChanged.getGameState() != GameState.LOADING)
            return;
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event) {
        if (event.getHitsplat().getHitsplatType() == Hitsplat.HitsplatType.HEAL && event.getActor() == currentNPC
                && config.useVengeance() && canVengeance()) {
            targetMenu = new LegacyMenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1,
                    WidgetInfo.SPELL_VENGEANCE.getId(), false);
            utils.oneClickCastSpell(Spells.VENGEANCE.getInfo(), targetMenu, new Rectangle(0, 0, 0, 0), 0);
            timeout = 5;
        }
    }

    private boolean lootableItem(TileItem item) {
        int haValue = client.getItemDefinition(item.getId()).getHaPrice();
        int itemPrice = utils.getItemPrice(item.getId(), true) * item.getQuantity();
        return (item.getTile().getWorldLocation().equals(deathLocation)
                || item.getTile().getWorldLocation().distanceTo(deathLocation) <= 2)
                && (itemPrice >= config.lootValue() || haValue >= config.lootValue());
    }

    private boolean initInventory() {
        inventorySetup.clear();
        if (config.useVengeance()) {
            inventorySetup.add(ItemID.RUNE_POUCH);
        }
        if (config.superantifire()) {
            inventorySetup.add(ItemID.EXTENDED_SUPER_ANTIFIRE4);
        }
        if (!config.superantifire()) {
            inventorySetup.add(ItemID.EXTENDED_ANTIFIRE4);
        }
        if (config.supercombats()) {
            inventorySetup.add(ItemID.DIVINE_SUPER_COMBAT_POTION4);
        }
        if (!config.supercombats()) {
            inventorySetup.add(ItemID.SUPER_COMBAT_POTION4);
        }
        if (!config.usePOHdigsite()) {
            inventorySetup.add(ItemID.DIGSITE_PENDANT_5);
        }
        if (config.useSpec()) {
            inventorySetup.add(config.specId());
        }
        inventorySetup.add(ItemID.PRAYER_POTION4);
        inventorySetup.add(ItemID.TELEPORT_TO_HOUSE);
        inventorySetup.add(config.foodID());
        log.info("required inventory items: {}", inventorySetup.toString());
        return true;
    }

    public void updateStats() {
        killsPerH = (int) getPerHour(killCount);
        lootPerH = (int) getPerHour(totalLoot);
    }

    public long getPerHour(int quantity) {
        Duration timeSinceStart = Duration.between(botTimer, Instant.now());
        if (!timeSinceStart.isZero()) {
            return (int) ((double) quantity * (double) Duration.ofHours(1).toMillis()
                    / (double) timeSinceStart.toMillis());
        }
        return 0;
    }

    public boolean canVengeance() {
        assert client.isClientThread();
        return client.getBoostedSkillLevel(Skill.MAGIC) >= 94 &&
                client.getVarbitValue(4070) == 2 &&
                (inventory.runePouchQuanitity(ItemID.EARTH_RUNE) >= 10
                        && inventory.runePouchQuanitity(ItemID.ASTRAL_RUNE) >= 4
                        && inventory.runePouchQuanitity(ItemID.DEATH_RUNE) >= 2
                        && inventory.containsItem(ItemID.RUNE_POUCH));
    }
}