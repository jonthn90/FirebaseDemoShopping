package com.johnniesnow.firebasedemoshopping;

import com.google.firebase.auth.FirebaseAuth;
import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.squareup.otto.Bus;

/**
 * Created by Jonathan on 12/06/17.
 */

public class BaseLiveService {
    protected Bus bus;
    protected FirebaseDemoShoppingApplication application;
    protected FirebaseAuth auth;

    public BaseLiveService(FirebaseDemoShoppingApplication application) {
        this.application = application;
        bus = application.getBus();
        bus.register(this);
        auth = FirebaseAuth.getInstance();
    }
}
