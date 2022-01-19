/*
 * Copyright (c) 2018, SomeoneWithAnInternetConnection
 * Copyright (c) 2018, oplosthee <https://github.com/oplosthee>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.xrunedragons;

import com.google.gson.JsonObject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.plugins.iutils.scripts.iScript;
import net.runelite.client.plugins.xrunedragons.tasks.*;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(
        name = "XRuneDragons",
        enabledByDefault = false,
        description = "Xerit - Rune dragons killer",
        tags = {"xerit", "rune", "dragon", "bot"}
)
@Slf4j
public class XRuneDragonsPlugin extends Plugin {

    @Inject
    private Client client;
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

    public static XRuneDragonsConfig taskConfig;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private XRuneDragonsOverlay overlay;

    @Inject
    private ConfigManager configManager;

    private static String OS = System.getProperty("os.name").toLowerCase();

    private String machineId = null;

    public static TaskSet tasks = new TaskSet();

    Instant botTimer;

    public static boolean running;
    public static LocalPoint beforeLoc = new LocalPoint(0, 0);
    public static String status = "Starting...";
    private Task task;

    public static List<Integer> inventorySetup = new ArrayList<>();
    public static final List<String> lootNamesList = new ArrayList<>();
    public static List<TileItem> itemsToLoot = new ArrayList<>();
    private static String[] lootNames;

    public static NPC currentNPC;
    public static WorldPoint deathLocation;
    public static Player localPlayer;

    public static boolean deposited = false;

    public static LegacyMenuEntry targetMenu;

    public static long sleepLength;
    public static int tickLength;
    public static int timeout;

    static int killCount;

    @Provides
    XRuneDragonsConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(XRuneDragonsConfig.class);
    }

    @Override
    protected void startUp() {
        utils.sendGameMessage("We start");
    }

    @Override
    protected void shutDown() {
        resetPlugin();
        utils.sendGameMessage("We stop");
    }

    private void loadTasks() {
        tasks.clear();
        tasks.addAll(
                injector.getInstance(TimeoutTask.class),
                injector.getInstance(DepositTask.class),
                injector.getInstance(WithdrawTask.class),
                injector.getInstance(TeleHomeTask.class),
                injector.getInstance(PrayTask.class),
                injector.getInstance(EatFoodTask.class),
                injector.getInstance(DrinkTask.class),
                injector.getInstance(LootTask.class),
                injector.getInstance(AttackDragonTask.class),
                injector.getInstance(InCombatTask.class),
                injector.getInstance(DrinkPoolTask.class),
                injector.getInstance(DownStairsTask.class),
                injector.getInstance(EnterDoorTask.class),
                injector.getInstance(WalkDoorTask.class),
                injector.getInstance(EnterDragonsTask.class),
                injector.getInstance(TeleEdgeTask.class),
                injector.getInstance(FindBankTask.class),
                injector.getInstance(TeleLithTask.class),
                injector.getInstance(LogoutTask.class)
        );
    }

    private void resetPlugin() {
        running = false;
        task = null;
        tasks.clear();
        inventorySetup.clear();
        itemsToLoot.clear();
        lootNamesList.clear();
        lootNames = config.lootNames().toLowerCase().split("\\s*,\\s*");
        if (!config.lootNames().isBlank()) {
            lootNamesList.addAll(Arrays.asList(lootNames));
        }
        overlayManager.remove(overlay);
        currentNPC = null;
        deposited = false;
        tickLength = 0;
        sleepLength = 0;
        killCount = 0;
    }

    @Subscribe
    private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked) {
        if (!configButtonClicked.getGroup().equalsIgnoreCase("mruned")) {
            return;
        }

        if (configButtonClicked.getKey().equals("startButton")) {
            if (!running) {
                getMachineID();
                int license = checkLicense();
                switch(license) {
                    case 200: {
                        Player player = client.getLocalPlayer();
                        if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN) {
                            log.info("starting XRuneDragons");
                            initInventory();
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
                            return;
                        } else {
                            log.info("Start logged in");
                            return;
                        }
                    }
                    case 401: {
                        utils.sendGameMessage("HWID is not correct");
                        return;
                    }
                    case 500: {
                        utils.sendGameMessage("License is invalid");
                        return;
                    }
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
            if (!client.isResized()) {
                utils.sendGameMessage("Please set game to resizable mode.");
                resetPlugin();
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
            if(task != null && task.isFinished() && task.isStarted()) {
                task.started = false;
                task.finished = false;
                task = tasks.getValidTask();
                status = task.getTaskDescription();
                task.onGameTick(event);
                if(status == "Logout") resetPlugin();
            } else if (task != null && !task.isFinished() && task.isStarted()) {
                task.checkFinished(event);
            } else if (task != null && !task.isFinished() && !task.isStarted()) {
                status = task.getTaskDescription();
                task.onGameTick(event);
                if(status == "Logout") resetPlugin();
            } else {
                task = tasks.getValidTask();
            }
            beforeLoc = localPlayer.getLocalLocation();
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
        this.itemsToLoot.clear();
        switch (gameStateChanged.getGameState()) {
            case CONNECTION_LOST:
            case LOGGING_IN:
            case HOPPING: {
                resetPlugin();
                break;
            }
        }
        if (gameStateChanged.getGameState() != GameState.LOADING) return;
    }

    private void getMachineID() {
        if(OS.indexOf("win") >= 0) {
            machineId = getWindowsDeviceUUID();
        } else if(OS.indexOf("mac") >= 0) {
            machineId = getMacUUID();
        }
    }

    private String getWindowsDeviceUUID()
    {
        try{
            String command = "wmic csproduct get UUID";
            StringBuffer output = new StringBuffer();

            Process SerNumProcess = Runtime.getRuntime().exec(command);
            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

            String line = "";
            while ((line = sNumReader.readLine()) != null) {
                output.append(line + "\n");
            }
            String uuid=output.toString().substring(output.indexOf("\n"), output.length()).trim();;
            log.debug(uuid);
            return uuid;
        } catch(Exception ex)
        {
            log.error("OutPut Error "+ex.getMessage());
        }
        return null;
    }

    public String getMacUUID()
    {
        try{
            String command = "system_profiler SPHardwareDataType | awk '/UUID/ { print $3; }'";

            StringBuffer output = new StringBuffer();


            Process SerNumProcess = Runtime.getRuntime().exec(command);

            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

            String line = "";

            while ((line = sNumReader.readLine()) != null) {
                output.append(line + "\n");
            }

            String uuid=output.toString().substring(output.indexOf("UUID: "), output.length()).replace("UUID: ", "");

            SerNumProcess.waitFor();

            uuid = uuid.split("\n")[0];

            sNumReader.close();

            log.debug(uuid);

            return uuid;
        } catch(Exception ex)
        {
            log.error("OutPut Error "+ex.getMessage());
        }

        return null;
    }

    private int checkLicense() {
        if(config.key() != "" && machineId != null) {
            String postEndpoint = "http://localhost:8080/auth/verify";

            JsonObject json = new JsonObject();
            json.addProperty("key", config.key());
            json.addProperty("machineId", machineId);

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(postEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            var httpClient = HttpClient.newHttpClient();

            try {
                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                utils.sendGameMessage("Response code: " + response.statusCode());
                utils.sendGameMessage("Body: " + response.body());
                return response.statusCode();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                return 500;
            } catch (IOException e) {
                log.error(e.getMessage());
                return 500;
            }
        }
        return 500;
    }

    private boolean lootableItem(TileItem item) {
        String itemName = client.getItemDefinition(item.getId()).getName().toLowerCase();
        boolean inLoot = lootNamesList.stream().anyMatch(itemName.toLowerCase()::contains);
        return (item.getTile().getWorldLocation().equals(deathLocation) || item.getTile().getWorldLocation().distanceTo(deathLocation) <= 2) && lootNamesList.stream().anyMatch(itemName.toLowerCase()::contains);
    }

    private void initInventory() {
        inventorySetup.clear();
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
        inventorySetup.add(ItemID.PRAYER_POTION4);
        inventorySetup.add(config.foodID());
        log.info("required inventory items: {}", inventorySetup.toString());
    }

}