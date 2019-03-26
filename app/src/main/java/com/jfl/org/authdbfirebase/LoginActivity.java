package com.jfl.org.authdbfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    private CallbackManager mCallbackManager;
    private Context aContext;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login Firebase");
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
                goToMainApp();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                Toast.makeText(LoginActivity.this,getString(R.string.loginCancel),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
                Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMainApp() {
        userId = AccessToken.getCurrentAccessToken().getUserId();
        finish();
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.putExtra("userId",userId);
        startActivity(mainIntent);
    }

    private void loginWithFacebook(){
        if(AccessToken.getCurrentAccessToken()!=null){
            goToMainApp();
        }else
            LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList(EMAIL));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
