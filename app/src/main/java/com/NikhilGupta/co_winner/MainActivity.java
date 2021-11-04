package com.NikhilGupta.co_winner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.NikhilGupta.co_winner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    ActivityMainBinding binding;
    Animation animation, animation2 , animation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animation = AnimationUtils.loadAnimation(this, R.anim.atg);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.atg_two);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.atg_three);
        //Pass Animation
        binding.cardView2.setAnimation(animation);
        binding.title.setAnimation(animation2);
        binding.subTitle.setAnimation(animation2);
        binding.button.setAnimation(animation3);


        binding.centerLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CenterLocator.class));
            }
        });

        binding.certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Working on it!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.more2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        binding.button.setBackgroundResource(R.drawable.bg_btn_bordered);
                        binding.button.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.black));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        binding.button.setBackgroundResource(R.drawable.bg_btn);
                        binding.button.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.white));
                        break;
                }
                return false;
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Link not available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}