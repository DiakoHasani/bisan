package com.example.bisan.UI.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bisan.Network.VolleyStringRequest;
import com.example.bisan.Network.onRequestResponseListener;
import com.example.bisan.R;
import com.example.bisan.Tools.CToast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class Map_Act extends FragmentActivity implements OnMapReadyCallback {

    private Button btnSave;
    private ImageView imgLocation;
    private GoogleMap mMap;
    private double lat = 35.315279, lang = 46.995599;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);

        btnSave=(Button)findViewById(R.id.btn_save_map);
        imgLocation=(ImageView)findViewById(R.id.map_location);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent=new Intent();
                    intent.putExtra("lat",String.valueOf(lat));
                    intent.putExtra("lang",String.valueOf(lang));
                    setResult(10,intent);
                    finish();
                }catch (Exception e){}
            }
        });

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.getString("lat") != null && bundle.getString("lat").length() > 0) {
                    lat = Double.valueOf(bundle.getString("lat"));
                    lang = Double.valueOf(bundle.getString("lang"));
                }
            }
        } catch (Exception e) {
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void statusCheck() {
        try {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            }
        }catch (Exception e){}
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("جی پی اس شما خاموش است، مایل هستید آنرا روشن کنید؟")
                .setCancelable(false)
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        statusCheck();

        LatLng Sanandaj = new LatLng(lat, lang);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lang), 18.0f));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Sanandaj));

        EnableCurrentLoc();

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                try {
                    final LatLng target = mMap.getCameraPosition().target;
                    lat=target.latitude;
                    lang=target.longitude;
                } catch (Exception e) {
                }
            }
        });

/*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));

                    Intent intent=new Intent();
                    intent.putExtra("lat",String.valueOf(latLng.latitude));
                    intent.putExtra("lang",String.valueOf(latLng.longitude));
                    setResult(10,intent);
                    finish();

                    *//*if (!Geocoder.isPresent()) {
                        new CToast(Map_Act.this,
                                "در دسترس نیست",1,
                                Toast.LENGTH_LONG).show();
                        return;
                    }*//*

                   *//* Location location = new Location("");
                    location.setLatitude(latLng.latitude);
                    location.setLatitude(latLng.longitude);
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] params) {
                            getAddress getaddress = new getAddress();
                            getaddress.setLatlong(latLng);
                            Thread thread = new Thread(getaddress);
                            thread.run();
                            return null;
                        }
                    }.execute();*//*
            }
        });*/
    }

    class getAddress implements Runnable {
        public LatLng target;

        @Override
        public void run() {
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(Map_Act.this, new Locale("fa"));
                addresses = geocoder.getFromLocation(target.latitude, target.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                final String address = addresses.get(0).getAddressLine(0);
                Log.i("ashkan", "run: " + address);

                //final String address = addresses.get(0).getLocality()+"-"+addresses.get(0).getAdminArea() + "-" + addresses.get(0).getThoroughfare()+"-"+addresses.get(0).getSubAdminArea();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("ashkan", "run: " + address);
                    }
                });
            } catch (Exception e) {
                Log.i("ashkan", "run: " + e.toString());
            }
        }

        public void setLatlong(LatLng latlong) {
            this.target = latlong;
        }

    }

    public void EnableCurrentLoc(){
        try{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }catch (Exception e){}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            EnableCurrentLoc();
        }
    }
}
