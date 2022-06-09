package com.NikhilGupta.co_winner.centerlocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.centerlocator.adapters.CentersAdapter;
import com.NikhilGupta.co_winner.centerlocator.repository.CentersRepository;
import com.NikhilGupta.co_winner.centerlocator.viewmodel.CentersViewModel;
import com.NikhilGupta.co_winner.centerlocator.viewmodel.CentersViewModelFactory;
import com.NikhilGupta.co_winner.receivers.NetworkBroadcastReceiver;
import com.NikhilGupta.co_winner.retrofit.RequestInterface;
import com.NikhilGupta.co_winner.retrofit.RetrofitHelper;

/**
 * This Activity uses strict price location permission
 */
public class CentersByLocationActivity extends AppCompatActivity implements LocationListener, CentersAdapter.ItemClickListener {
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
    private MapsFragment mapsFragment = new MapsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centers_by_location);
        tvNoData     = findViewById(R.id.noData);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        centersAdapter = new CentersAdapter(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setupViewModel();
        checkLocationPermission();
//        if (Build.VERSION.SDK_INT >= 23) {
//            requestPermissions(PERMISSIONS, PERMISSIONS_ALL);
//        }
        recyclerView.setAdapter(centersAdapter);
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

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            handler.post(() -> {
                progressDialog = new ProgressDialog(CentersByLocationActivity.this);
                progressDialog.setTitle("Fetching location...");
                progressDialog.setMessage("Keep data and gps on");
                progressDialog.setIndeterminate(true);
                progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.progressbar_back));
                progressDialog.setCancelable(false);
                progressDialog.show();
            });
            requestLocation();
        } else {
            // should we show explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Required")
                        .setMessage("To find nearby centres Co-Winner needs your location, allow it")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            requestLocationPermission();
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission();
            }
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_ALL);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // to call update need to wait for onMapReady
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapsFragment.updateCurrentLocation(location.getLatitude(), location.getLongitude());
            }
        },2500);
        if(progressDialog != null) {
            progressDialog.setTitle("Fetching centres...");
        }
        viewModel.getCentersByLocation(location.getLatitude(), location.getLongitude());
//        viewModel.getCentersByLocation(30.69, 76.86); // PKL
        locationManager.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*switch (requestCode) { // use switch for multiple permissions
            case PERMISSIONS_ALL: {
                if (grantResults.length>0 && grantResults[0] == )
            }
            break;
        }*/
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                handler.post(() -> {
                    progressDialog = new ProgressDialog(CentersByLocationActivity.this);
                    progressDialog.setTitle("Fetching location...");
                    progressDialog.setMessage("Keep data and gps on");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.progressbar_back));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                });
                requestLocation();
            } else {
                Toast.makeText(this, "Abe teri to. Precise location de", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", this.getPackageName(),null)));
            }
        } else {
//            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
//            checkLocationPermission();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Required")
                        .setMessage("To find nearby centres Co-Winner needs your location, allow it")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            requestLocationPermission();
                        })
                        .create()
                        .show();
            } else {
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", this.getPackageName(),null)));
            }
        }
    }

    private void setupViewModel() {
        RequestInterface requestInterface = RetrofitHelper.getInstance()
                .create(RequestInterface.class);
        CentersRepository repository = new CentersRepository(requestInterface);
        CentersViewModelFactory factory = new CentersViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CentersViewModel.class);

        viewModel.getCentersLiveData().observe(this, centersResponse -> {
            if (centersResponse != null) {
                centersAdapter.updateDataList(centersResponse.getCenters());
                if (centersResponse.getCenters().size() == 0) {
                    tvNoData.setText(getString(R.string.error_message));
                } else {
                    tvNoData.setText("");
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapsFragment.updateMarkers(centersResponse.getCenters());
                    }
                },2500);
            }
            handler.post(() -> {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                recyclerView.scrollToPosition(0);
            });
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

    @Override
    public void onItemClick(View v, int pos) {
        recyclerView.requestChildFocus(v,v);
        mapsFragment.focusMarker(pos);
    }
}