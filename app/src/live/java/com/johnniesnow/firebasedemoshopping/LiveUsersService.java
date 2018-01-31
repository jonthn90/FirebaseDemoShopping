package com.johnniesnow.firebasedemoshopping;

import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.johnniesnow.firebasedemoshopping.entities.Users;
import com.johnniesnow.firebasedemoshopping.entities.UsersSharedWith;
import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.johnniesnow.firebasedemoshopping.services.GetUsersService;
import com.squareup.otto.Subscribe;

public class LiveUsersService extends BaseLiveService {

    public LiveUsersService(FirebaseDemoShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void getUsersFriends(GetUsersService.GetUserFriendsRequest request){
        final GetUsersService.GetUsersFrendsResponse response = new GetUsersService.GetUsersFrendsResponse();

        response.listener = request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.usersFriends = dataSnapshot.getValue(Users.class);
                bus.post(response);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void getSharedWith(GetUsersService.GetSharedWithRequest request){
        final GetUsersService.GetSharedWithResponse response = new GetUsersService.GetSharedWithResponse();
        response.listener = request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.usersSharedWith = dataSnapshot.getValue(UsersSharedWith.class);
                bus.post(response);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
