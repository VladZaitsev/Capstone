package com.baikaleg.v3.cookingaid.util;

public final class Constants {

    private Constants() {
        throw new AssertionError("No instances for you!");
    }
    //Player parameters
    public static final String PLAYER_POSITION = "position";
    public static final String PLAYER_READY = "ready";

    // Names of dialogs classes
    public static final int IDD_ADD_STORAGE_ITEM = 101;
    public static final int IDD_ADD_SHOPPING_ITEM = 102;
    public static final int IDD_EDIT_STORAGE_ITEM = 103;
    public static final int IDD_EDIT_SHOPPING_ITEM = 104;

    // Request codes
    private static final int REQUEST_PRODUCT_DIALOG_QUANTITY = 101;
    private static final int REQUEST_PRODUCT_DIALOG_UNIT_QUANTITY = 102;
    private static final int REQUEST_PRODUCT_DIALOG_PRICE = 103;
    private static final int REQUEST_PRODUCT_DIALOG_EXPIRATION = 104;
}