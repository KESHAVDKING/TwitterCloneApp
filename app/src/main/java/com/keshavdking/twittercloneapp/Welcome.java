package com.keshavdking.twittercloneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class Welcome extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList<String> mArrayList;
    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mListView=findViewById(R.id.listView);
        mArrayList=new ArrayList<>();
        mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,mArrayList);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);



        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialogue);
        final AlertDialog dialog = builder.create();
        dialog.show();
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
         @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e==null && objects.size()>0){

                    for (ParseUser user : objects){
                        mArrayList.add(user.getUsername());
                    }
                    mListView.setAdapter(mArrayAdapter);
                    if (ParseUser.getCurrentUser().getList("follows")!=null) {
                        for (String followUser : mArrayList) {

                            if (ParseUser.getCurrentUser().getList("follows").contains(followUser)) {
                                mListView.setItemChecked(mArrayList.indexOf(followUser), true);

                            }
                        }
                    }


                }
                dialog.dismiss();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logoutUserItemMenu:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            FancyToast.makeText(Welcome.this,
                                    "User LoggedOut Successfully",
                                    Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                            Intent i = new Intent(Welcome.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            FancyToast.makeText(Welcome.this,"Something Went Wrong While Logout!!", Toast.LENGTH_LONG,FancyToast.WARNING,true).show();
                        }
                    }
                });
                break;
            case R.id.sendTweet:
                Intent i = new Intent(Welcome.this, SendTweet.class);
                startActivity(i);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            FancyToast.makeText(this,checkedTextView.getText()+" is checked",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().add("follows",checkedTextView.getText().toString());

        }else {
            FancyToast.makeText(this,checkedTextView.getText()+" is Unchecked",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().getList("follows").remove(((CheckedTextView) view).getText());
            List usersFollowing = ParseUser.getCurrentUser().getList("follows");
            ParseUser.getCurrentUser().remove("follows");
            ParseUser.getCurrentUser().put("follows",usersFollowing);

        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    FancyToast.makeText(Welcome.this,"Follower added",Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
            }
        });
    }
}