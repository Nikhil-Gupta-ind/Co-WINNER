package com.NikhilGupta.co_winner.centerlocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.centerlocator.adapters.CentersAdapter;
import com.NikhilGupta.co_winner.centerlocator.models.CentersResponse;
import com.NikhilGupta.co_winner.centerlocator.repository.CentersRepository;
import com.NikhilGupta.co_winner.centerlocator.viewmodel.CentersViewModel;
import com.NikhilGupta.co_winner.centerlocator.viewmodel.CentersViewModelFactory;
import com.NikhilGupta.co_winner.receivers.NetworkBroadcastReceiver;
import com.NikhilGupta.co_winner.retrofit.RequestInterface;
import com.NikhilGupta.co_winner.retrofit.RetrofitHelper;

public class CentersByLocationActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;

    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    final static int PERMISSIONS_ALL = 1;

    final String TAG = "Test";
    TextView tvNoData;
    RecyclerView recyclerView;
    CentersAdapter centersAdapter;
    Handler handler = new Handler();
    ProgressDialog progressDialog;
    private NetworkBroadcastReceiver networkBroadcastReceiver;
    private CentersViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centers_by_location);
        tvNoData     = findViewById(R.id.noData);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        centersAdapter = new CentersAdapter();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setupViewModel();
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(PERMISSIONS, PERMISSIONS_ALL);
        }
        handler.post(() -> {
            progressDialog = new ProgressDialog(CentersByLocationActivity.this);
            progressDialog.setTitle("Please wait.");
            progressDialog.setMessage("Fetching data...");
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.progressbar_back));
            progressDialog.setCancelable(false);
            progressDialog.show();
        });
        requestLocation();
//        displayList();
        recyclerView.setAdapter(centersAdapter);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("Test", "onLocationChanged: " + location.getLatitude() + ", " + location.getLongitude());
        Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();
        displayList((long) location.getLatitude(), (long) location.getLongitude());
        locationManager.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
    }

    private void setupViewModel() {
        RequestInterface requestInterface = RetrofitHelper.getInstance()
                .create(RequestInterface.class);
        CentersRepository repository = new CentersRepository(requestInterface);
        CentersViewModelFactory factory = new CentersViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CentersViewModel.class);
        viewModel.getCentersLiveData().observe(this, new Observer<CentersResponse>() {
            @Override
            public void onChanged(CentersResponse centersResponse) {
                if (centersResponse != null) {
                    centersAdapter.updateDataList(centersResponse.getCenters());
                    if (centersResponse.getCenters().size() == 0) {
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

    }

    public void requestLocation() {
        if (locationManager != null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1000, this);
            }
        }
    }

    private void displayList(long lat, long lon) {
        // pass coordinates to viewModel
//        long lat = (long) 28.686273333333332;
//        long lon = (long) 77.22178166666667;
        viewModel.getCentersByLocation(lat,lon);
    }
}