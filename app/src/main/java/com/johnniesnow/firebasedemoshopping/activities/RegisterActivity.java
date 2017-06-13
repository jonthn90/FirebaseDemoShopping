package com.johnniesnow.firebasedemoshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.services.AccountServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jonathan on 12/06/17.
 */

public class RegisterActivity extends BaseActivity{

    @BindView(R.id.activity_register_loginButton)
    Button loginButton;

    @BindView(R.id.activiy_register_linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.activity_register_userEmail)
    EditText userEmail;

    @BindView(R.id.activity_register_userName)
    EditText userName;

    @BindView(R.id.activity_register_registerButton)
    Button registerButton;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        linearLayout.setBackgroundResource(R.drawable.background_screen_two);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Attempting to register account");
        progressDialog.setCancelable(false);
    }

    @OnClick(R.id.activity_register_loginButton)
    public void setLoginButton(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.activity_register_registerButton)
    public void setRegisterButton(){
        bus.post(new AccountServices.RegisterUserRequest(userName.getText().toString(), userEmail.getText().toString(), progressDialog));
    }

    @Subscribe
    public void RegisterUser(AccountServices.RegisterUserResponse response){
        if (!response.didSuceed()){
            userEmail.setError(response.getPropertyError("email"));
            userName.setError(response.getPropertyError("userName"));
        }
    }
}
