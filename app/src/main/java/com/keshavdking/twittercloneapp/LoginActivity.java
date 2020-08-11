package com.keshavdking.twittercloneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView emailId,password;
    Button login, signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailId=findViewById(R.id.edtEmailLogin);
        password=findViewById(R.id.edtPasswordLogin);
        login=findViewById(R.id.btnLoginActivity);
        signup=findViewById(R.id.btnSignupLogin);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSignupLogin:
                Intent intent = new Intent(LoginActivity.this,SignupAndLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLoginActivity:
                if (emailId.getText().toString().equals("") || password.getText().toString().equals("")) {
                    FancyToast.makeText(LoginActivity.this, "Emailid,Username,Password Can't be empty!!", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();

                } else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setView(R.layout.loading_dialogue);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    ParseUser.logInInBackground(emailId.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(LoginActivity.this, user.get("username") + " is logged in successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                transitionToWelcomePage();
                                dialog.dismiss();

                            } else {
                                FancyToast.makeText(LoginActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                            }
                        }
                    });
                }
                break;
        }
    }

    private void transitionToWelcomePage() {
        Intent i = new Intent(LoginActivity.this,Welcome.class);
        startActivity(i);


    }
}