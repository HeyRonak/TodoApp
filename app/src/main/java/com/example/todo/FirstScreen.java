package com.example.todo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstScreen extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        imageView = findViewById(R.id.img);
        textView = findViewById(R.id.txt);

        Animation animation = AnimationUtils.loadAnimation(FirstScreen.this,R.anim.myanim);
        imageView.startAnimation(animation);
        textView.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}
