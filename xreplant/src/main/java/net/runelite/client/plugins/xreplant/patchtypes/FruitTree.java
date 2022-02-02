package net.runelite.client.plugins.xreplant.patchtypes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;

@Getter
@Slf4j
public enum FruitTree {

    // Fruit tree crops
    APPLE("Apple", ItemID.APPLE_SAPLING, ItemID.SWEETCORN),
    BANANA("Banana", ItemID.BANANA_SAPLING, ItemID.APPLES5),
    ORANGE("Orange", ItemID.ORANGE_SAPLING, ItemID.STRAWBERRIES5),
    CURRY("Curry", ItemID.CURRY_SAPLING, ItemID.BANANAS5),
    PINEAPPLE("Pineapple", ItemID.PINEAPPLE_SAPLING, ItemID.WATERMELON),
    PAPAYA("Papaya", ItemID.PAPAYA_SAPLING, ItemID.PINEAPPLE),
    PALM("Palm", ItemID.PALM_SAPLING, ItemID.PAPAYA_FRUIT),
    DRAGONFRUIT("Dragonfruit", ItemID.DRAGONFRUIT_SAPLING, ItemID.COCONUT);

    /**
     * User-visible name
     */
    private final String name;
    /**
     * User-visible item ID
     */
    private final int itemID;

    private final int paymentID;

    private FruitTree(String name, int itemID, int paymentID) {
        this.name = name;
        this.itemID = itemID;
        this.paymentID = paymentID;
    }
}