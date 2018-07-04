package com.example.rsyazilim.rs_ihbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rsyazilim.rs_ihbar.utils.CommonUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback , View.OnClickListener, LocationListener
{

    private LocationManager locationManager;
    private String provider;
    private GoogleMap googleMap;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        requestLocationPermission();

    }



    private void requestLocationPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener()
                {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        provider = locationManager.getBestProvider(new Criteria(), false);
                        //Location location = locationManager.getLastKnownLocation(provider);
//                        onLocationChanged(location);
                        locationManager.requestLocationUpdates(provider, 400, 1, MapActivity.this);

                        Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        CommonUtils.showPermissionSettingsDialog(MapActivity.this );
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).check();
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
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

}