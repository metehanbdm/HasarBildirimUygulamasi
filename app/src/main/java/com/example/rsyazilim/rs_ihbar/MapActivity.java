package com.example.rsyazilim.rs_ihbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rsyazilim.rs_ihbar.utils.GeneralUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback , View.OnClickListener, LocationListener {
    private static final int RC_ACCESS_FINE_LOCATION=1;

    private LocationManager locationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String provider;
    private GoogleMap googleMap;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},RC_ACCESS_FINE_LOCATION);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Lokasyon İsteği")
                        .setMessage("Konum Bilgileri için izin verin")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        provider = locationManager.getBestProvider(new Criteria(), false);

                        //Request location updates:

                        onLocationChanged(locationManager.getLastKnownLocation(provider));
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    Toast.makeText(this, "Permisson denied ", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        if (checkLocationPermission())
        {
            provider = locationManager.getBestProvider(new Criteria(), false);
            Location location = locationManager.getLastKnownLocation(provider);
            onLocationChanged(location);
            locationManager.requestLocationUpdates(provider, 400, 1, this);

            if (location != null)
            {
                Log.i("Location Info", "Location achieved!");
            } else
            { Log.i("Location Info", "No location :("); }

        }

    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.map_send_button:
                if (currentLocation !=null)
                {
                    Intent intent = getIntent();
                    intent.putExtra("LAT", currentLocation.getLatitude());
                    intent.putExtra("LNG", currentLocation.getLongitude());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                    Toast.makeText(this, "Konum Bilgilerine Ulasilamadi.\nLutfen Tekrar Deneyin", Toast.LENGTH_SHORT).show();

                break;
                default:
                    break;
        }
        return;

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (googleMap != null)
                locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        if (googleMap == null)
            return;

        this.currentLocation = location;

        googleMap.clear();

        LatLng konumum = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(konumum).title("Konumum"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(konumum , 15));

        Log.i("Location info: Lat", String.valueOf(location.getLatitude()));
        Log.i("Location info: Lng", String.valueOf(location.getLongitude()));

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

}