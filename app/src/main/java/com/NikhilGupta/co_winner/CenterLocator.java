package com.NikhilGupta.co_winner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.NikhilGupta.co_winner.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class CenterLocator extends AppCompatActivity {

//    ActivityMainBinding binding;
    EditText editPin, editDate;
    ListView listView;
    ImageView imgSearch, imgCal;
    private int mm, dd, yy;

    ArrayList<String> sessionList;
    ArrayAdapter<String> adapter;
    Handler handler = new Handler();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_locator);
        imgSearch = findViewById(R.id.imgSearch);
        editPin = findViewById(R.id.editPincode);
        editDate = findViewById(R.id.editDated);
        imgCal = findViewById(R.id.imgCal);
        listView = findViewById(R.id.listView);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        initializeSessionlist();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchData().start();
            }
        });
        imgCal.setOnClickListener(new View.OnClickListener() {
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
                        month = month+1;
                        String format = "%1$02d"; // two digits
                        editDate.setText(String.format(format,dayOfMonth)+"-"+String.format(format, month)+"-"+year);
                    }
                }, mm, dd, yy);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+1000);
                datePickerDialog.show();
            }
        });
    }

    private void initializeSessionlist() {
        sessionList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,sessionList);
        listView.setAdapter(adapter);
    }

    class FetchData extends Thread{

        String pincode;
        String date;
        String urlAdd;
        String data = "";
        @Override
        public void run() {


            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(CenterLocator.this);
                    progressDialog.setMessage("Fetch data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                if (!editPin.getText().toString().isEmpty() && !editDate.getText().toString().isEmpty()){
                    pincode = editPin.getText().toString();
                    date = editDate.getText().toString();
                    urlAdd = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pincode+"&date="+date;
                }else {
                    Toast.makeText(CenterLocator.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                }
                URL url = new URL(urlAdd);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;

                while ((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                if(!data.isEmpty()){

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray sessions = jsonObject.getJSONArray("sessions");
                    sessionList.clear();
                    for(int i=0;i<sessions.length(); i++){
                        JSONObject names = sessions.getJSONObject(i);
                        String name = names.getString("name");
                        sessionList.add(name);
                    }
                }
            } catch (MalformedURLException urlException) {
                urlException.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.isShowing();
                    progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}