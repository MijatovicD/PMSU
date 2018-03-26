package com.example.dimitrije.pmsu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReadPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        TextView titleText = findViewById(R.id.title_Read);
        titleText.setText("Naslov posta");

        TextView descriptionnText = findViewById(R.id.description_Read);
        descriptionnText.setText("Opis posta");

        TextView userText = findViewById(R.id.user_Read);
        userText.setText("pera");

        TextView dateText = findViewById(R.id.date_Read);
        dateText.setText("26-3-2018");

        TextView locationText = findViewById(R.id.location_Read);
        locationText.setText("Novi Sad");

        TextView likeText = findViewById(R.id.like_Read);
        likeText.setText("Likes 8");

        TextView dislikeText = findViewById(R.id.dislike_Read);
        dislikeText.setText("Dislikes 4");

        TextView tagText = findViewById(R.id.tag_Read);
        tagText.setText("#hashtag");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
