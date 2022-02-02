package net.runelite.client.plugins.xreplant.patchtypes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;

@Getter
@Slf4j
public enum Tree {

    // Tree crops
    OAK("Oak", ItemID.OAK_SAPLING, ItemID.TOMATOES5),
    WILLOW("Willow", ItemID.WILLOW_SAPLING, ItemID.APPLES5),
    MAPLE("Maple", ItemID.MAPLE_SAPLING, ItemID.ORANGES5),
    YEW("Yew", ItemID.YEW_SAPLING, ItemID.CACTUS_SPINE),
    MAGIC("Magic", ItemID.MAGIC_SAPLING, ItemID.COCONUT);

    /**
     * User-visible name
     */
    private final String name;
    /**
     * User-visible item ID
     */
    private final int itemID;

    private final int paymentID;

    private Tree(String name, int itemID, int paymentID) {
        this.name = name;
        this.itemID = itemID;
        this.paymentID = paymentID;
    }
}