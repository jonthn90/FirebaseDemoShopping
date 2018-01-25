package com.johnniesnow.firebasedemoshopping.infrastructure;

/**
 * Created by Jonathan on 12/06/17.
 */

public class Utils {
    public static final String FIRE_BASE_URL = "https://fir-demoshopping.firebaseio.com/";

    public static final String FIRE_BASE_USER_REFERENCE = FIRE_BASE_URL + "users/";

    public static final String FIRE_BASE_USER_SHOPPING_LIST_REFERENCE = FIRE_BASE_URL + "userShoppingLists/";

    public static final String FIRE_BASE_SHOPPING_LIST_REFERENCE = FIRE_BASE_URL + "userShoppingLists/";
    public static final String FIRE_BASE_LIST_ITEMS_REFERENCE = FIRE_BASE_URL + "shoppingListItems/";
    public static final String FIRE_BASE_USER_FRIEND_REFERENCE = FIRE_BASE_URL + "usersFriends/";
    public static final String FIRE_BASE_SHARED_WITH_REFERENCE = FIRE_BASE_URL +"sharedWith/";

    public static final String MY_PREFERENCE = "MY_PREFERENCE";
    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";

    public static final String LIST_ODER_PREFERENCE = "LIST_ODER_PREFERENCE";
    public static final String ORDER_BY_KEY = "oderByPushKey";


    public static String encodeEmail(String userEmail){
        return userEmail.replace(".",",");
    }

    public static String decodeEmail(String userEmail){
        return userEmail.replace(",",".");
    }
}
