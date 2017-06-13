package com.johnniesnow.firebasedemoshopping.services;

import android.app.ProgressDialog;

import com.johnniesnow.firebasedemoshopping.infrastructure.ServiceResponse;

/**
 * Created by Jonathan on 12/06/17.
 */

public class AccountServices {

    public AccountServices() {
    }

    public static class RegisterUserRequest{
        public String userName;
        public String userEmail;
        public ProgressDialog progressDialog;

        public RegisterUserRequest(String userName, String userEmail, ProgressDialog progressDialog) {
            this.userName = userName;
            this.userEmail = userEmail;
            this.progressDialog = progressDialog;
        }
    }

    public static class RegisterUserResponse extends ServiceResponse{
    }
}
