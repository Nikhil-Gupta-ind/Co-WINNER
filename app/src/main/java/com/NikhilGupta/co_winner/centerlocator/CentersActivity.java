package com.NikhilGupta.co_winner.centerlocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.NikhilGupta.co_winner.centerlocator.adapters.CentersRVAdapter;
import com.NikhilGupta.co_winner.centerlocator.models.ResponseData;
import com.NikhilGupta.co_winner.centerlocator.repository.CentersRepository;
import com.NikhilGupta.co_winner.centerlocator.viewmodel.CentersViewModel;
import com.NikhilGupta.co_winner.centerlocator.viewmodel.CentersViewModelFactory;
import com.NikhilGupta.co_winner.receivers.NetworkBroadcastReceiver;
import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.retrofit.RequestInterface;
import com.NikhilGupta.co_winner.retrofit.RetrofitHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class CentersActivity extends AppCompatActivity {

    final String TAG = "Test";
    ImageView             imgSearch, imgCal;
    EditText              editPin, editDate;
    TextView              tvNoData;
    RecyclerView          recyclerView;
    CentersRVAdapter      recyclerViewAdapter;
    private int mm, dd, yy;

    Handler handler = new Handler();
    ProgressDialog progressDialog;

    CardView cardView;
    private NetworkBroadcastReceiver networkBroadcastReceiver;
    private CentersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_locator);
        cardView     = findViewById(R.id.search_bar_card);
        imgSearch    = findViewById(R.id.imgSearch);
        editPin      = findViewById(R.id.editPincode);
        editDate     = findViewById(R.id.editDated);
        imgCal       = findViewById(R.id.imgCal);
        tvNoData     = findViewById(R.id.noData);
        recyclerView = findViewById(R.id.recyclerView); //initializing recyclerview

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//adding LayoutManager
        recyclerViewAdapter = new CentersRVAdapter();
        setupViewModel();
        recyclerView.setAdapter(recyclerViewAdapter);



        startAnimation();
        imgSearch.setOnClickListener(v -> {
            if (editPin.getText().toString().isEmpty() || editDate.getText().toString().isEmpty()) {
                Toast.makeText(CentersActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                // Dummy Data for Testing
//                dummyData();
            } else if (editPin.getText().toString().length() < 6) {
                Toast.makeText(CentersActivity.this, "Incorrect pincode", Toast.LENGTH_SHORT).show();
            } else {
                displayList();
            }
        });
        imgCal.setOnClickListener(v -> {

            final Calendar calendar = Calendar.getInstance();
            mm = calendar.get(Calendar.DATE);
            dd = calendar.get(Calendar.MONTH);
            yy = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(CentersActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1;
                    String format = "%1$02d"; // two digits
                    editDate.setText(String.format(format, dayOfMonth) + "-" + String.format(format, month) + "-" + year);
                    if (editPin.getText().toString().isEmpty() || editDate.getText().toString().isEmpty()) {
                        Toast.makeText(CentersActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    } else if (editPin.getText().toString().length() < 6) {
                        Toast.makeText(CentersActivity.this, "Incorrect pincode", Toast.LENGTH_SHORT).show();
                    } else {
                        displayList();
                    }
                }
            }, mm, dd, yy);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 1000);
            datePickerDialog.show();

        });
        editDate.setOnClickListener(v -> imgCal.performClick());
    }

    private void setupViewModel() {
        RequestInterface requestInterface = RetrofitHelper.getInstance()
                .create(RequestInterface.class);
        CentersRepository repository = new CentersRepository(requestInterface);
        CentersViewModelFactory factory = new CentersViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CentersViewModel.class);
        viewModel.getSessionsLiveData().observe(this, new Observer<ResponseData>() {
            @Override
            public void onChanged(ResponseData responseData) {
                if (responseData != null) {
                    recyclerViewAdapter.updateDataList(responseData.getSessions());
                    if (responseData.getSessions().size() == 0) {
                        tvNoData.setText(getString(R.string.error_message));
                    } else {
                        tvNoData.setText("");
                    }
                }
                handler.post(() -> {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                });
            }
        });
        Log.d(TAG, "setupViewModel: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkBroadcastReceiver = new NetworkBroadcastReceiver(this);
        registerReceiver(networkBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkBroadcastReceiver);
    }
    private void startAnimation() {
        cardView.setCardElevation(16);
        Runnable endAction = new Runnable() {
            @Override
            public void run() {
                cardView.animate().translationZ(8);
            }
        };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cardView.animate().translationZ(32).withEndAction(endAction);
            }
        }, 1000);
    }

    private void displayList() {
//        new FetchData().start();
//        recyclerViewAdapter.updateDataList(responseData.getSessions());

        String pincode = editPin.getText().toString();
        String date = editDate.getText().toString();

        handler.post(() -> {
            progressDialog = new ProgressDialog(CentersActivity.this);
            progressDialog.setTitle("Please wait.");
            progressDialog.setMessage("Fetching data...");
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.progressbar_back));
            progressDialog.setCancelable(false);
            progressDialog.show();
        });
        viewModel.getSessions(pincode, date);
    }

    class FetchData extends Thread {

        String pincode;
        String date;
        String urlAdd;
        String data = "";

        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(CentersActivity.this);
                    progressDialog.setTitle("Please wait.");
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.progressbar_back));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                pincode = editPin.getText().toString();
                date = editDate.getText().toString();
                urlAdd = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=" + pincode + "&date=" + date;

                URL url = new URL(urlAdd);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                Log.d(TAG, "run: reading data from api");
                while ((line = bufferedReader.readLine()) != null) {
                    data = data + line;
                }

                if (!data.isEmpty()) {
//                    tvNoData.setVisibility(View.INVISIBLE);
//                    recyclerView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "run: Starting JSON parsing");
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray sessions = jsonObject.getJSONArray("sessions");
//                    sessionList.clear();
//                    centerDataArrayList.clear();
                    for (int i = 0; i < sessions.length(); i++) {
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
                        Log.d(TAG, "run: " + name + address + block + district + state + vaccine + from + to);
//                        centerDataArrayList.add(new CenterData(name, address, block, district, state, vaccine, from, to));
//                        for (CenterData data : centerDataArrayList) {
//                            Log.d(TAG, "run: " + data.getName() + data.getAddress() + data.getBlock() + "\n");
//                        }
                    }
                }
//                else {
//                    tvNoData.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.INVISIBLE);
//                }
            } catch (IOException | JSONException urlException) {
                tvNoData.setText(getString(R.string.no_internet));
                urlException.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.isShowing();
                    progressDialog.dismiss();
//                    adapter.notifyDataSetChanged();
                    recyclerViewAdapter.notifyDataSetChanged();
                    tvNoData.setText(getString(R.string.error_message));
                }
            });
        }
    }
}