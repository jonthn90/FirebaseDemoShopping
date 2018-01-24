package com.johnniesnow.firebasedemoshopping.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.squareup.otto.Bus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BaseActivity extends AppCompatActivity{

    protected FirebaseDemoShoppingApplication application;
    protected Bus bus;
    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authStateListener;
    protected String userEmail, userName;
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (FirebaseDemoShoppingApplication) getApplication();
        bus = application.getBus();
        bus.register(this);

        sharedPreferences = getSharedPreferences(Utils.MY_PREFERENCE, MODE_PRIVATE);

        userName = sharedPreferences.getString(Utils.USERNAME, "");
        userEmail = sharedPreferences.getString(Utils.EMAIL, "");

        auth = FirebaseAuth.getInstance();

        if(!((this instanceof LoginActivity) || (this instanceof RegisterActivity) || (this instanceof SplashScreenActivity))){
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if(user == null){
                        //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        //finish();

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(Utils.EMAIL, null).apply();
                        editor.putString(Utils.USERNAME, null).apply();

                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        finish();

                    }
                }
            };

            if (userEmail.equalsIgnoreCase("")){

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(Utils.EMAIL, null).apply();
                editor.putString(Utils.USERNAME, null).apply();

                auth.signOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                finish();

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);

        if(!((this instanceof LoginActivity) || (this instanceof RegisterActivity) || (this instanceof SplashScreenActivity))){
           auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!((this instanceof LoginActivity) || (this instanceof RegisterActivity) || (this instanceof SplashScreenActivity))){
            auth.addAuthStateListener(authStateListener);
        }
    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("jk", "Exception(NameNotFoundException) : " + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("mkm", "Exception(NoSuchAlgorithmException) : " + e);
        }
    }
}
