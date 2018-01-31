package com.johnniesnow.firebasedemoshopping.activities;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.johnniesnow.firebasedemoshopping.dialog.ChangeItemNameDialogFragment;
import com.johnniesnow.firebasedemoshopping.dialog.DeleteItemDialogFragment;
import com.johnniesnow.firebasedemoshopping.entities.User;
import com.johnniesnow.firebasedemoshopping.entities.Users;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.services.GetUsersService;
import com.johnniesnow.firebasedemoshopping.services.ItemService;
import com.johnniesnow.firebasedemoshopping.views.AddFriendView.AddFriendViewHolder;
import com.johnniesnow.firebasedemoshopping.views.ItemListViews.ListItemViewHolder;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

public class AddFriendActivity extends BaseActivity {
    private FirebaseRecyclerAdapter adapter;

    private DatabaseReference friendsReference;

    private ValueEventListener listener;

    private RecyclerView recyclerView;


    private Users currentUserFriends = new Users();;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        recyclerView = (RecyclerView) findViewById(R.id.activity_add_friend_list_recyclerView);

        friendsReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIRE_BASE_USER_FRIEND_REFERENCE + userEmail);

        bus.post(new GetUsersService.GetUserFriendsRequest(friendsReference));


        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_USER_REFERENCE);

        Query sortQuery = usersReference.orderByKey();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(sortQuery, User.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<User, AddFriendViewHolder>(options) {

            @Override
            public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);

                return new AddFriendViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AddFriendViewHolder addFriendViewHolder, int i, @NonNull final User user) {

                addFriendViewHolder.populate(user);

                final AddFriendViewHolder addFriendViewHolderFinal = addFriendViewHolder;

                if (currentUserFriends.getUsersFriends() != null){
                    if (isFriend(currentUserFriends.getUsersFriends(),user)){
                        addFriendViewHolder.userItemView.setImageResource(R.mipmap.ic_check);
                    } else{
                        addFriendViewHolder.userItemView.setImageResource(R.mipmap.ic_plus);
                    }
                }

                addFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userEmail.equals(Utils.encodeEmail(user.getEmail()))){
                            Toast.makeText(getApplicationContext(),"You can not add yourself",Toast.LENGTH_LONG).show();

                        } else{
                            DatabaseReference friendsReference = FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl(Utils.FIRE_BASE_USER_FRIEND_REFERENCE + userEmail +
                                            "/" + "usersFriends/" + Utils.encodeEmail(user.getEmail()));

                            if (isFriend(currentUserFriends.getUsersFriends(),user)){
                                friendsReference.removeValue();
                                addFriendViewHolderFinal.userItemView.setImageResource(R.mipmap.ic_plus);
                            } else{
                                friendsReference.setValue(user);
                                addFriendViewHolderFinal.userItemView.setImageResource(R.mipmap.ic_check);
                            }

                        }
                    }
                });


            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
        friendsReference.removeEventListener(listener);
    }

    @Subscribe
    public void getCurrentUsersFriends(GetUsersService.GetUsersFrendsResponse response){
        listener = response.listener;
        if (response.usersFriends != null){
            currentUserFriends = response.usersFriends;
        } else{
            currentUserFriends = new Users();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }

    private boolean isFriend(HashMap<String,User> userFriends, User user){
        return userFriends != null && userFriends.size() != 0
                && userFriends.containsKey(Utils.encodeEmail(user.getEmail()));
    }

}
