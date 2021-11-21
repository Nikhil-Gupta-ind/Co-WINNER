package com.NikhilGupta.co_winner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
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

    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startAnimation();


        binding.centerLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CenterLocator.class));
            }
        });

        binding.certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast!=null) toast.cancel();
                toast = Toast.makeText(MainActivity.this, "Working on it!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast!=null) toast.cancel();
                toast=Toast.makeText(MainActivity.this, "Coming Soon!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        binding.more2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast!=null) toast.cancel();
                toast = Toast.makeText(MainActivity.this, "Coming Soon!", Toast.LENGTH_SHORT);
                toast.show();
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
//                Toast.makeText(MainActivity.this, "Link not available", Toast.LENGTH_SHORT).show();
                String textMessage = "https://github.com/Nikhil-Gupta-ind/jCloud";
                // Create the text message with a string.
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
                shareIntent.setType("text/plain");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // Try to invoke the intent.
                try {
                    startActivity(Intent.createChooser(shareIntent, "Share Co-WINNER App"));
                } catch (ActivityNotFoundException e) {
                    // Define what your app should do if no activity can handle the intent.
                }
            }
        });
    }

    private void startAnimation(){
        animation = AnimationUtils.loadAnimation(this, R.anim.atg);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.atg_two);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.atg_three);
        //Pass Animation
        binding.cardView2.setAnimation(animation);
        binding.title.setAnimation(animation2);
        binding.subTitle.setAnimation(animation2);
        binding.button.setAnimation(animation3);
    }
}