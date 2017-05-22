package com.johnniesnow.firebasedemoshopping.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.squareup.otto.Bus;

public class BaseActivity extends AppCompatActivity{

    protected FirebaseDemoShoppingApplication application;
    protected Bus bus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (FirebaseDemoShoppingApplication) getApplication();
        bus = application.getBus();
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
