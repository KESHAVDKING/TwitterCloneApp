package com.keshavdking.twittercloneapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweet extends AppCompatActivity {

    private TextView userTweet;
    private Button btnSendTweet,btnShowUsersTweets;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
    userTweet=findViewById(R.id.userTweet);
    btnSendTweet=findViewById(R.id.btnSendTweet);
    btnShowUsersTweets=findViewById(R.id.btnShowTweets);
    mListView=findViewById(R.id.showTweetListView);


    btnSendTweet.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ParseObject parseObject = new ParseObject("Tweets");
            parseObject.put("tweets",userTweet.getText().toString());
            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        FancyToast.makeText(SendTweet.this,"Tweet added!!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                    }
                }
            });

        }
    });

    btnShowUsersTweets.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final ArrayList<HashMap<String,String>> tweetList = new ArrayList<>();
            final SimpleAdapter adapter= new SimpleAdapter(SendTweet.this,tweetList,android.R.layout.simple_list_item_2,new String[]{"tweetUserName","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});
            try {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Tweets");
                parseQuery.whereContainedIn("username",ParseUser.getCurrentUser().getList("follows"));
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e==null && objects.size()>0){
                            for (ParseObject tweetObject : objects){
                                HashMap<String,String> userTweet = new HashMap<>();
                                userTweet.put("tweetUserName",tweetObject.getString("username"));
                                userTweet.put("tweetValue",tweetObject.getString("tweets"));
                                tweetList.add(userTweet);

                            }
                            mListView.setAdapter(adapter);
                        }
                    }
                });

                }catch (Exception e){
                e.printStackTrace();
            }
            }

    });

    }
}