package com.johnniesnow.firebasedemoshopping.services;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.johnniesnow.firebasedemoshopping.entities.Users;
import com.johnniesnow.firebasedemoshopping.entities.UsersSharedWith;

public class GetUsersService {
    private GetUsersService() {
    }


    public static class GetUserFriendsRequest{
        public DatabaseReference reference;

        public GetUserFriendsRequest(DatabaseReference reference) {
            this.reference = reference;
        }
    }

    public static class GetUsersFrendsResponse{
        public ValueEventListener listener;
        public Users usersFriends;
    }


    public static class GetSharedWithRequest{
        public DatabaseReference reference;

        public GetSharedWithRequest(DatabaseReference reference) {
            this.reference = reference;
        }
    }

    public static class GetSharedWithResponse{
        public ValueEventListener listener;
        public UsersSharedWith usersSharedWith;
    }
}
