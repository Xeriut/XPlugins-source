package net.runelite.client.plugins.xreplant.patchtypes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;

@Getter
@Slf4j
public enum Tree {

    // Tree crops
    OAK("Oak", ItemID.OAK_SAPLING, ItemID.TOMATOES5, 5969, 1),
    WILLOW("Willow", ItemID.WILLOW_SAPLING, ItemID.APPLES5, 5387, 1),
    MAPLE("Maple", ItemID.MAPLE_SAPLING, ItemID.ORANGES5, 5397, 1),
    YEW("Yew", ItemID.YEW_SAPLING, ItemID.CACTUS_SPINE, 6017, 10),
    MAGIC("Magic", ItemID.MAGIC_SAPLING, ItemID.COCONUT, 5975, 25);

    /**
     * User-visible name
     */
    private final String name;
    /**
     * User-visible item ID
     */
    private final int itemID;

    private final int paymentID;

    private final int paymentIDNoted;

    private final int paymentAmount;

    private Tree(String name, int itemID, int paymentID, int paymentIDNoted, int paymentAmount) {
        this.name = name;
        this.itemID = itemID;
        this.paymentID = paymentID;
        this.paymentIDNoted = paymentIDNoted;
        this.paymentAmount = paymentAmount;
    }
}