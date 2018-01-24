package com.johnniesnow.firebasedemoshopping.infrastructure;

import android.app.Application;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.johnniesnow.firebasedemoshopping.Module;
import com.squareup.otto.Bus;

/**
 * Created by jonathan on 22/05/17.
 */

public class FirebaseDemoShoppingApplication extends Application {

    private Bus bus;

    public FirebaseDemoShoppingApplication() {
        bus = new Bus();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Newer version of Firebase
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        Module.Register(this);
    }

    public Bus getBus() {
        return bus;
    }
}
