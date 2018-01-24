package com.johnniesnow.firebasedemoshopping.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.activities.BaseActivity;
import com.johnniesnow.firebasedemoshopping.dialog.AddListDialogFragment;
import com.johnniesnow.firebasedemoshopping.entities.ShoppingList;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.views.ShoppingListViews.ShoppingListviewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main_FAB)
    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;

    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        recyclerView = (RecyclerView) findViewById(R.id.activity_main_listRecyclerView);

        String toolBarName;

        if(userName.contains(" ")){
            toolBarName = userName.substring(0, userName.indexOf(" ")) + "'s shopping list";
        } else {
            toolBarName = userName + "'s shopping list";
        }

        getSupportActionBar().setTitle(toolBarName);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIRE_BASE_USER_SHOPPING_LIST_REFERENCE + userEmail);



        FirebaseRecyclerOptions<ShoppingList> options = new FirebaseRecyclerOptions.Builder<ShoppingList>()
                .setQuery(ref, ShoppingList.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ShoppingList, ShoppingListviewHolder>(options) {

            @Override
            public ShoppingListviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shopping_list, parent, false);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                return new ShoppingListviewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ShoppingListviewHolder shoppingListviewHolder, int i, @NonNull ShoppingList shoppingList) {

                shoppingListviewHolder.populate(shoppingList);

                final ShoppingList shoppingListFinal = shoppingList;

                shoppingListviewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),shoppingListFinal.getListName() + " was clicked" ,Toast.LENGTH_LONG).show();
                    }
                });

            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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

                disconnectFromFacebook();

                auth.signOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                finish();

                return true;
        }



        return super.onOptionsItemSelected(item);
    }
    

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.stopListening();
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        LoginManager.getInstance().logOut();

        /*
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();

        */
    }

    @OnClick(R.id.activity_main_FAB)
    public void setFloatingActionButton(){
        DialogFragment dialogFragment = AddListDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(), AddListDialogFragment.class.getSimpleName());
    }
}
