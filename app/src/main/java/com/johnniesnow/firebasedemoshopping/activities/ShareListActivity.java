package com.johnniesnow.firebasedemoshopping.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.entities.ShoppingList;
import com.johnniesnow.firebasedemoshopping.entities.User;
import com.johnniesnow.firebasedemoshopping.entities.UsersSharedWith;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.services.GetUsersService;
import com.johnniesnow.firebasedemoshopping.services.ShoppingListServices;
import com.johnniesnow.firebasedemoshopping.views.AddFriendView.AddFriendViewHolder;
import com.johnniesnow.firebasedemoshopping.views.ShareListView.ShareListViewHolder;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

public class ShareListActivity extends BaseActivity {

    private String mShoppingListId;

    private DatabaseReference mShareWithReference;
    private DatabaseReference mShoppingListReference;

    private ValueEventListener mShareWIthListener;
    private ValueEventListener mShoppingListListener;

    private UsersSharedWith mSharedWith;
    private ShoppingList mCurrentShoppingList;


    private FirebaseRecyclerAdapter adapter;
    public static String SHOPPING_LIST_EXTRA_INFO = "SHOPPING_LIST_EXTRA_INFO";

    public static Intent newIntent(Context context,String shoppingListId){
        Intent intent = new Intent(context,ShareListActivity.class);
        intent.putExtra(SHOPPING_LIST_EXTRA_INFO,shoppingListId);
        return intent;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share_list);

        mShoppingListId = getIntent().getStringExtra(SHOPPING_LIST_EXTRA_INFO);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_share_list_recyclerView);

        mShareWithReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHARED_WITH_REFERENCE + mShoppingListId);

        mShoppingListReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + userEmail + "/" + mShoppingListId);

        bus.post(new GetUsersService.GetSharedWithRequest(mShareWithReference));
        bus.post(new ShoppingListServices.GetCurrentShoppingListRequest(mShoppingListReference));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_USER_FRIEND_REFERENCE + userEmail + "/usersFriends");

        Query sortQuery = reference.orderByKey();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(sortQuery, User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, ShareListViewHolder>(options) {

            @Override
            public ShareListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);

                return new ShareListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ShareListViewHolder shareListViewHolder, int i, @NonNull final User user) {

                shareListViewHolder.populate(user);

                final ShareListViewHolder shareListViewHolderFinal = shareListViewHolder;

                if (mSharedWith != null) {

                    if (isSharedWith(mSharedWith.getSharedWith(), user)) {
                        shareListViewHolder.userItemView.setImageResource(R.mipmap.ic_check);
                    } else {
                        shareListViewHolder.userItemView.setImageResource(R.mipmap.ic_plus);
                    }
                }

                shareListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference sharedWithReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHARED_WITH_REFERENCE + mShoppingListId+"/" +"sharedWith/"+
                                Utils.encodeEmail(user.getEmail()));

                        DatabaseReference friendsShoppingListReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE +
                                Utils.encodeEmail(user.getEmail()) + "/" + mShoppingListId);

                        if (isSharedWith(mSharedWith.getSharedWith(),user)){
                            sharedWithReference.removeValue();
                            friendsShoppingListReference.removeValue();
                            shareListViewHolderFinal.userItemView.setImageResource(R.mipmap.ic_plus);
                            updateAllShoppingListReference(mSharedWith.getSharedWith(),mCurrentShoppingList,bus,true);

                        } else{
                            sharedWithReference.setValue(user);
                            friendsShoppingListReference.setValue(mCurrentShoppingList);
                            shareListViewHolderFinal.userItemView.setImageResource(R.mipmap.ic_check);
                            updateAllShoppingListReference(mSharedWith.getSharedWith(),mCurrentShoppingList,bus,false);
                        }
                    }
                });


            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_friend:
                startActivity(new Intent(this,AddFriendActivity.class));
                return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
        mShareWithReference.removeEventListener(mShareWIthListener);
        mShoppingListReference.removeEventListener(mShoppingListListener);
    }

    @Subscribe
    public void getUsersSharedWith(GetUsersService.GetSharedWithResponse response){
        mShareWIthListener = response.listener;

        if (response.usersSharedWith!=null){
            mSharedWith = response.usersSharedWith;
        } else{
            mSharedWith = new UsersSharedWith();
        }
    }

    @Subscribe
    public void getCurrentShoppingList(ShoppingListServices.GetCurrentShoppingListResponse response){
        mCurrentShoppingList = response.shoppingList;
        mShoppingListListener = response.valueEventListener;
    }



    public boolean isSharedWith(HashMap<String ,User> usersSharedWIth, User user){
        return usersSharedWIth!=null && usersSharedWIth.size()!=0 &&
                usersSharedWIth.containsKey(Utils.encodeEmail(user.getEmail()));
    }


    public static void updateAllShoppingListReference(HashMap<String,User> usersSharedWith, ShoppingList shoppingList, Bus bus
    ,boolean deletingList){
        if (usersSharedWith !=null && !usersSharedWith.isEmpty()){
            for(User user: usersSharedWith.values()){
                if (usersSharedWith.containsKey(Utils.encodeEmail(user.getEmail())))
                {
                    DatabaseReference friendListsReference =
                            FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE +
                                    Utils.encodeEmail(user.getEmail()) + "/" + shoppingList.getId());

                    if (!deletingList){
                        bus.post(new ShoppingListServices.UpdateShoppingListTimeStampRequest(friendListsReference));
                    }
                }
            }
        }

        DatabaseReference ownerReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + "/" +
                Utils.encodeEmail(shoppingList.getOwnerEmail())  + "/"
                + shoppingList.getId());

        bus.post(new ShoppingListServices.UpdateShoppingListTimeStampRequest(ownerReference));
    }
}
