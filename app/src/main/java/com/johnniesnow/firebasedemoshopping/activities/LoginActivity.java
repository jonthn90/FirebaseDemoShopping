package com.johnniesnow.firebasedemoshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.R2;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.services.AccountServices;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

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

    @BindView(R.id.activity_login_facebook_button)
    LoginButton facebookButton;

    private ProgressDialog progressDialog;

    private CallbackManager mCallbackManager;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        linearLayout.setBackgroundResource(R.drawable.background_screen_two);

        //FirebaseAuth.getInstance().signOut();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Attempting to log in");
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences(Utils.MY_PREFERENCE, MODE_PRIVATE);

        printKeyHash();
    }

    @OnClick(R.id.activity_login_RegisterButton)
    public void setRegisterButton(){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    @OnClick(R.id.activity_login_loginButton)
    public void setLoginButton(){
        bus.post(new AccountServices.LogUserInRequest(userEmail.getText().toString(), userPassword.getText().toString(), progressDialog, sharedPreferences));
    }

    @OnClick(R.id.activity_login_facebook_button)
    public void setFacebookButton(){

        mCallbackManager = CallbackManager.Factory.create();

        facebookButton.setReadPermissions("email", "public_profile");

        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try{
                                    String email = object.getString("email");
                                    String name = object.getString("name");

                                    bus.post(new AccountServices.LogUserFacebookRequest(loginResult.getAccessToken(), name, email, progressDialog, sharedPreferences));

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();

                parameters.putString("fields","id,name,email,gender,birthday");

                graphRequest.setParameters(parameters);

                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "An unknown error occurred", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void LogInUser(AccountServices.LogUserInResponse response){
        if (!response.didSuceed()){
            userEmail.setError(response.getPropertyError("email"));
            userPassword.setError(response.getPropertyError("password"));
        }
    }
}
