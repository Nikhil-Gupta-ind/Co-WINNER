package com.NikhilGupta.co_winner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.NikhilGupta.co_winner.databinding.ActivityMainBinding;

import java.util.Calendar;

public class CenterLocator extends AppCompatActivity {

//    ActivityMainBinding binding;
    EditText editPin, editDate;
    private int mm, dd, yy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_locator);
        editPin = findViewById(R.id.editPincode);
        editDate = findViewById(R.id.editDated);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mm = calendar.get(Calendar.DATE);
                dd = calendar.get(Calendar.MONTH);
                yy = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CenterLocator.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String format = "%1$02d"; // two digits
                        editDate.setText(String.format(format,dayOfMonth)+"-"+String.format(format, month)+"-"+year);
                    }
                }, mm, dd, yy);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+1000);
                datePickerDialog.show();
            }
        });
    }
}