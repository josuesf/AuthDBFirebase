package com.jfl.org.authdbfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";

    private CallbackManager mCallbackManager;
    private Context aContext;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        aContext=this;
        mCallbackManager = CallbackManager.Factory.create();

        Button mLoginButton = findViewById(R.id.login_button);

        //Register event onClick Facebook Login Button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });
        // Register a callback to respond to the user
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                userId = AccessToken.getCurrentAccessToken().getUserId();
                finish();
                goToMainApp();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
//                finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
            }
        });
        Log.i("INFORMATION - FACEBOOK", String.valueOf(AccessToken.getCurrentAccessToken().getUserId()));
    }

    private void goToMainApp() {
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.putExtra("userId",userId);
        startActivity(mainIntent);
    }

    private void loginWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList(EMAIL, USER_POSTS));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
