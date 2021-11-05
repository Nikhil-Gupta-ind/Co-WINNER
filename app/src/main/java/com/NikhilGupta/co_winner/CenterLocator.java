package com.NikhilGupta.co_winner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
    final String TAG = "Test";
    ImageView imgSearch, imgCal;
    EditText editPin, editDate;
    TextView tvNoData;
//    ListView listView;
    RecyclerView recyclerView;
    CLRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<CenterData> centerDataArrayList;
    private int mm, dd, yy;

//    ArrayList<String> sessionList;
//    ArrayAdapter<String> adapter;
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
        tvNoData = findViewById(R.id.noData);
//        listView = findViewById(R.id.listView);
        recyclerView = findViewById(R.id.recyclerView); //initializing recyclerview
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//adding LayoutManager
        Log.d(TAG, "onCreate: RecyclerView initialized");
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

//        initializeSessionlist();
        centerDataArrayList = new ArrayList<>();
        Log.d(TAG, "onCreate: centerDataArrayList initialized");
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPin.getText().toString().isEmpty() || editDate.getText().toString().isEmpty()){
                    Toast.makeText(CenterLocator.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    // Dummy Data for Testing
//                    centerDataArrayList.add(new CenterData("Center 1","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 2","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 3","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 4","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 5","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 6","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 7","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 8","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 9","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 10","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 11","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 12","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 13","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 14","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 15","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 16","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 17","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 18","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 19","address","block","district","state","vaccine","from","to"));
//                    centerDataArrayList.add(new CenterData("Center 20","address","block","district","state","vaccine","from","to"));
//                    for (CenterData element :
//                            centerDataArrayList) {
//                        Log.d(TAG, "onClick: "+element.getName()+" "+element.getAddress()+" "+element.getBlock());
//                    }
//
//                    recyclerViewAdapter = new CLRecyclerViewAdapter(getApplicationContext(),centerDataArrayList);
//                    Log.d(TAG, "onClick: RecyclerView Adapter initialized");
//                    recyclerView.setAdapter(recyclerViewAdapter);
//                    Log.d(TAG, "onClick: setAdapter done!");
                }else {
                    displayList();
                }
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
                        if (editPin.getText().toString().isEmpty() || editDate.getText().toString().isEmpty()){
                            Toast.makeText(CenterLocator.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                        }else {
                            displayList();
                        }
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
                        if (editPin.getText().toString().isEmpty() || editDate.getText().toString().isEmpty()){
                            Toast.makeText(CenterLocator.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                        }else {
                            displayList();
                        }
                    }
                }, mm, dd, yy);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+1000);
                datePickerDialog.show();
            }
        });
    }

    private void initializeSessionlist() {
//        sessionList = new ArrayList<>();
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,sessionList);
//        listView.setAdapter(adapter);
        centerDataArrayList = new ArrayList<>();
        Log.d(TAG, "initializeSessionlist: centerDataArrayList initialized");
//        recyclerViewAdapter = new CLRecyclerViewAdapter(getApplicationContext(),centerDataArrayList);
//        Log.d(TAG, "initializeSessionlist: RecyclerView Adapter initialized");
//        recyclerView.setAdapter(recyclerViewAdapter);
//        Log.d(TAG, "initializeSessionlist: setAdapter done!");
    }
    private void displayList(){
        Log.d(TAG, "onClick: Initiating fetch Data");
        new FetchData().start();
        Log.d(TAG, "onClick: Fetch Data complete");
        recyclerViewAdapter = new CLRecyclerViewAdapter(getApplicationContext(),centerDataArrayList);
        Log.d(TAG, "onClick: RecyclerView Adapter initialized");
        recyclerView.setAdapter(recyclerViewAdapter);
        Log.d(TAG, "onClick: setAdapter done!");
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
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                pincode = editPin.getText().toString();
                date = editDate.getText().toString();
                urlAdd = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pincode+"&date="+date;
                URL url = new URL(urlAdd);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                Log.d(TAG, "run: reading data from api");
                while ((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                if(!data.isEmpty()){
                    tvNoData.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "run: Starting JSON parsing");
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray sessions = jsonObject.getJSONArray("sessions");
//                    sessionList.clear();
                    centerDataArrayList.clear();
                    for(int i=0;i<sessions.length(); i++){
                        JSONObject names = sessions.getJSONObject(i);
                        String name, address, block, district, state, vaccine, from, to;
                        name = names.getString("name");
                        address = names.getString("address");
                        block = names.getString("block_name");
                        district = names.getString("district_name");
                        state = names.getString("state_name");
                        vaccine = names.getString("vaccine");
                        from = names.getString("from");
                        to = names.getString("to");
//                        sessionList.add(name);
                        Log.d(TAG, "run: "+name+address+block+district+state+vaccine+from+to);
                        centerDataArrayList.add(new CenterData(name,address,block,district,state,vaccine,from,to));
                        for (CenterData data : centerDataArrayList) {
                            Log.d(TAG, "run: "+data.getName()+data.getAddress()+data.getBlock()+"\n");
                        }
                    }
                }else {
                    tvNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
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
//                    adapter.notifyDataSetChanged();
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}