package net.runelite.client.plugins.xrunedragons;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

@Singleton
@Slf4j
class XRuneDragonsOverlay
        extends OverlayPanel {
    private final XRuneDragonsPlugin plugin;
    private final XRuneDragonsConfig config;
    String timeRunning;
    private String status = "Starting...";

    @Inject
    private XRuneDragonsOverlay(Client client, XRuneDragonsPlugin xRuneDragonsPlugin, XRuneDragonsConfig xRuneDragonsConfig) {
        super(xRuneDragonsPlugin);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        plugin = xRuneDragonsPlugin;
        config = xRuneDragonsConfig;
    }

    public Dimension render(Graphics2D graphics2D) {
        if (plugin.botTimer == null) {
            log.debug("Bot is not running yet.");
            return null;
        }
        TableComponent tableComponent = new TableComponent();
        tableComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);
        Duration duration = Duration.between(plugin.botTimer, Instant.now());
        timeRunning = duration.toHours() < 1L ? "mm:ss" : "HH:mm:ss";
        tableComponent.addRow("Time running:", DurationFormatUtils.formatDuration(duration.toMillis(), timeRunning));

        if (XRuneDragonsPlugin.status != null) {
            status = XRuneDragonsPlugin.status;
        }


        tableComponent.addRow("Status:", status);
        TableComponent tableColumn = new TableComponent();
        tableColumn.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);
        tableColumn.addRow("Kills (p/h):", XRuneDragonsPlugin.killCount + "(" + XRuneDragonsPlugin.killsPerH + ")");
        tableColumn.addRow("Profit (p/h):", NumberFormat.getNumberInstance(Locale.US).format(XRuneDragonsPlugin.totalLoot)
                + " (" + NumberFormat.getNumberInstance(Locale.US).format(XRuneDragonsPlugin.lootPerH) + ")");

        if (tableComponent.isEmpty()) return super.render(graphics2D);

        panelComponent.setBackgroundColor(ColorUtil.fromHex("#121212"));
        panelComponent.setPreferredSize(new Dimension(270, 200));
        panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
        panelComponent.getChildren().add(TitleComponent.builder().text("XRuneDragons").color(ColorUtil.fromHex("#DDAA11")).build());
        panelComponent.getChildren().add(tableComponent);
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Stats")
                .color(ColorUtil.fromHex("#FFA000"))
                .build());
        panelComponent.getChildren().add(tableColumn);

        return super.render(graphics2D);
    }
}

