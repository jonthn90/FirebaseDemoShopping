package com.johnniesnow.firebasedemoshopping;

import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;

/**
 * Created by Jonathan on 12/06/17.
 */

public class Module {

    public static  void Register(FirebaseDemoShoppingApplication application){
        new LiveAccountServices(application);
    }
}
