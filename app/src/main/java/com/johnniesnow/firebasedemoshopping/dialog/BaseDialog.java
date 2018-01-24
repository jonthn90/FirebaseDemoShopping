package com.johnniesnow.firebasedemoshopping.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.squareup.otto.Bus;

/**
 * Created by jonathan on 22/05/17.
 */

public class BaseDialog extends DialogFragment {

    protected FirebaseDemoShoppingApplication application;
    protected Bus bus;
    protected String userEmail, userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (FirebaseDemoShoppingApplication) getActivity().getApplication();
        bus = application.getBus();
        bus.register(this);


        userName = getActivity().getSharedPreferences(Utils.MY_PREFERENCE, Context.MODE_PRIVATE).getString(Utils.USERNAME, "");
        userEmail = getActivity().getSharedPreferences(Utils.MY_PREFERENCE, Context.MODE_PRIVATE).getString(Utils.EMAIL, "");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
