package com.NikhilGupta.co_winner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        binding.imageView.setAnimation(animation);
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
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Link not available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}