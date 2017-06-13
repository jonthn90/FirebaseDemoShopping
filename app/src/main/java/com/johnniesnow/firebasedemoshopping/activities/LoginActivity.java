package com.johnniesnow.firebasedemoshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.R2;
import com.johnniesnow.firebasedemoshopping.services.AccountServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jonathan on 12/06/17.
 */

public class LoginActivity extends BaseActivity{

    @BindView(R.id.activity_login_linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.activity_login_RegisterButton)
    Button registerButton;

    @BindView(R.id.activity_login_loginButton)
    Button loginButton;

    @BindView(R.id.activity_login_userEmail)
    EditText userEmail;

    @BindView(R.id.activity_login_userPassword)
    EditText userPassword;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        linearLayout.setBackgroundResource(R.drawable.background_screen_two);

        FirebaseAuth.getInstance().signOut();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Attempting to log in");
        progressDialog.setCancelable(false);

    }

    @OnClick(R.id.activity_login_RegisterButton)
    public void setRegisterButton(){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    @OnClick(R.id.activity_login_loginButton)
    public void setLoginButton(){
        bus.post(new AccountServices.LogUserInRequest(userEmail.getText().toString(), userPassword.getText().toString(), progressDialog));
    }

    @Subscribe
    public void LogInUser(AccountServices.LogUserInResponse response){
        if (!response.didSuceed()){
            userEmail.setError(response.getPropertyError("email"));
            userPassword.setError(response.getPropertyError("password"));
        }
    }
}
