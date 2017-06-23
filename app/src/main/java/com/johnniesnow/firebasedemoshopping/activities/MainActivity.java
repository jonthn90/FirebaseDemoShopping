package com.johnniesnow.firebasedemoshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.activities.BaseActivity;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String toolBarName;

        if(userName.contains(" ")){
            toolBarName = userName.substring(0, userName.indexOf(" ")) + "'s shopping list";
        } else {
            toolBarName = userName + "'s shopping list";
        }

        getSupportActionBar().setTitle(toolBarName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_logout:

                //SharedPreferences sharedPreferences = getSharedPreferences(Utils.MY_PREFERENCE, MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(Utils.EMAIL, null).apply();
                editor.putString(Utils.USERNAME, null).apply();

                auth.signOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                finish();

                return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
