package com.example.project1.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    private TextView locationDetails;
    //private OnMapReadyCallback callback = googleMap -> { gMap = googleMap; };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregmant_map, container, false);
        findViews(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fMap_myMap);


        if (mapFragment == null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.fMap_myMap, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        return view;
    }


    private void findViews(View view) {
        //FrameLayout map = view.findViewById(R.id.fMap_myMap);
        locationDetails = view.findViewById(R.id.fMap_data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
    }


    public void pointOnMap(double x, double y) {
        if (x == 0.0 && y == 0.0) {
            locationDetails.setVisibility(View.VISIBLE);
            locationDetails.setText("No information about location");
        } else {
            locationDetails.setVisibility(View.INVISIBLE);
            LatLng point = new LatLng(x, y);
            gMap.clear();
            gMap.addMarker(new MarkerOptions().position(point).title(""));
            moveToCurrentLocation(point);
        }
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        gMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

    }
}


