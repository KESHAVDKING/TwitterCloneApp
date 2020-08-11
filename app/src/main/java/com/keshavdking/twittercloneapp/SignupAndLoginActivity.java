package com.keshavdking.twittercloneapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignupAndLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView emailId,userName,password;
    private Button signUp,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_and_login);
        emailId=findViewById(R.id.edtEmailIdSignup);
        userName=findViewById(R.id.edtUserName);
        password=findViewById(R.id.edtPassword);
        signUp=findViewById(R.id.btnSignupActivity);
        login=findViewById(R.id.btnLoginSignup);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnSignupActivity:
                if (emailId.getText().toString().equals("") || userName.getText().toString().equals("")
                        || password.getText().toString().equals("")) {
                    FancyToast.makeText(SignupAndLoginActivity.this, "Emailid,Username,Password Can't be empty!!", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();

                } else {

                    ParseUser user = new ParseUser();
                    user.setUsername(userName.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setEmail(emailId.getText().toString());
                    final ProgressDialog progressDialog = new ProgressDialog(SignupAndLoginActivity.this);
                    progressDialog.setMessage("SigningUp " + userName.getText().toString());
                    progressDialog.show();
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignupAndLoginActivity.this, userName.getText().toString() + " is added successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                transitionToWelcomePage();
                            } else {

                                FancyToast.makeText(SignupAndLoginActivity.this, e.getMessage() + "failed to add", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }


                break;
            case R.id.btnLoginSignup:
                Intent i = new Intent(SignupAndLoginActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                break;

        }

    }

    private void transitionToWelcomePage() {

        Intent i = new Intent(SignupAndLoginActivity.this,Welcome.class);
        startActivity(i);
        finish();
    }
}