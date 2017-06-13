package com.johnniesnow.firebasedemoshopping.infrastructure;

/**
 * Created by Jonathan on 12/06/17.
 */

public class Utils {
    public static final String FIRE_BASE_URL = "https://fir-demoshopping.firebaseio.com/";

    public static final String FIRE_BASE_USER_REFERENCE = FIRE_BASE_URL + "users/";

    public static String encodeEmail(String userEmail){
        return userEmail.replace(".",",");
    }
}
