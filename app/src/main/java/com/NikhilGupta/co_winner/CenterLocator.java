package com.NikhilGupta.co_winner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.NikhilGupta.co_winner.databinding.ActivityMainBinding;

public class CenterLocator extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_locator);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}