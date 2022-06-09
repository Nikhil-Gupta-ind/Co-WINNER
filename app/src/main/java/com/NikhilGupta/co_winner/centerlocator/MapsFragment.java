package com.NikhilGupta.co_winner.centerlocator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.centerlocator.models.CentersItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private static GoogleMap mGoogleMap;
    private static ArrayList<Marker> markers = new ArrayList<>(); //use hashmap
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;
            LatLng india = new LatLng(22.82998958970051, 79.08286368264126);
//            LatLng pkl = new LatLng(30.69, 76.86);
//            mGoogleMap.addMarker(new MarkerOptions().position(india));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(india));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(5), 1, null);
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    return false;
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void updateCurrentLocation(double lat, double lon) {
        LatLng currentLocation = new LatLng(lat, lon);
        mGoogleMap.addMarker(new MarkerOptions().position(currentLocation).title("You're here"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 1500, null);
    }

    // mark the current location marker in Red and centers in blue
    public void updateMarkers(ArrayList<CentersItem> centers) {
        markers.clear();
        for (CentersItem center : centers) {
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(center.getLat()),
                                    Double.parseDouble(center.getLongitude())))
                            .title(center.getName())
                            .snippet(center.getLocation())
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
            markers.add(marker);
        }
    }

    public void focusMarker(int pos) {
        if (!markers.isEmpty()) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(markers.get(pos).getPosition()));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1500, null);
            markers.get(pos).showInfoWindow();
        }
    }
}