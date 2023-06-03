package com.mz.bf.addclient;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mz.bf.MainActivity;
import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.clients.ClientsActivity;
import com.mz.bf.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnLocationUpdatedListener {

    private SupportMapFragment mMap;
    FusedLocationProviderClient client;
    private ActivityMapsBinding binding;
    private static final float Defult_Zoom = 15f;
    double lat,lon;
    String lat1,lon1,client_id,user_id;
    StringBuilder address;
    MarkerOptions markerOptions,markerOptions2;
    Integer flag;
    MySharedPreference mySharedPreference;
    LoginModel userModel;
    Marker marker,marker2;
    Button btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);
        mMap = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap.getMapAsync(this);
        mySharedPreference = MySharedPreference.getInstance();
        userModel = mySharedPreference.Get_UserData(this);
        btn_confirm = findViewById(R.id.btn_confirm);
        user_id = userModel.getId();
        getDataIntent();
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }else {
            //Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

        }

        /*Intent returnIntent = new Intent();
        returnIntent.putExtra("lat",lat);
        returnIntent.putExtra("lat",lon);
        returnIntent.putExtra("address",address);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    private void getDataIntent() {
        flag = getIntent().getIntExtra("flag",0);
        lat1 = getIntent().getStringExtra("lat");
        lon1 = getIntent().getStringExtra("long");
        client_id = getIntent().getStringExtra("client_id");
    }

    private boolean isGpsOpen() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }

        return false;
    }

    private void getCurrentLocation() {

        Log.d(TAG, "getDeviceLocation: get Currnt Location");

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull  Location location) {
                    if (location != null){
                        mMap.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull  GoogleMap googleMap) {

                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        lat = latLng.latitude;
                                        lon = latLng.longitude;
                                        StringBuilder sb = new StringBuilder();

                                        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                                        List<Address> addresses;
                                        try {
                                            addresses = gcd.getFromLocation(latLng.latitude,
                                                    latLng.longitude, 1);
                                            if (addresses.size() > 0) {

                                                sb.append(addresses.get(0).getAddressLine(0)).append("\n");
//                                    sb.append(addresses.get(0).getLocality()).append("\n");
//                                    sb.append(addresses.get(0).getPostalCode()).append("\n");
//                                    sb.append(addresses.get(0).getCountryName());

                                            }

                                        } catch (IOException  e) {
                                            e.printStackTrace();
                                        }
                                        address = sb;
                                        Toast.makeText(MapsActivity.this, sb, Toast.LENGTH_SHORT).show();
                                        // Toast.makeText(MapsActivity.this, lat+"", Toast.LENGTH_SHORT).show();
                                        markerOptions = new MarkerOptions().position(latLng);
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                                        marker = googleMap.addMarker(markerOptions);
                                        marker.showInfoWindow();
                                    }
                                });

                                StringBuilder sb = new StringBuilder();

                                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                                List<Address> addresses;
                                try {
                                    addresses = gcd.getFromLocation(location.getLatitude(),
                                            location.getLongitude(), 1);
                                    if (addresses.size() > 0) {

                                        sb.append(addresses.get(0).getAddressLine(0)).append("\n");
//                                    sb.append(addresses.get(0).getLocality()).append("\n");
//                                    sb.append(addresses.get(0).getPostalCode()).append("\n");
//                                    sb.append(addresses.get(0).getCountryName());

                                    }

                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                                address = sb;
                                if (flag != 3){
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                    Toast.makeText(MapsActivity.this, sb, Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(MapsActivity.this, lat+"", Toast.LENGTH_SHORT).show();
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    markerOptions = new MarkerOptions().position(latLng);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                    marker =googleMap.addMarker(markerOptions);
                                    marker.showInfoWindow();
                                }else {
                                    lat = Double.parseDouble(lat1);
                                    lon = Double.parseDouble(lon1);
                                    LatLng latLng = new LatLng(lat, lon);
                                    markerOptions = new MarkerOptions().position(latLng).title("موقع العميل");
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                    marker = googleMap.addMarker(markerOptions);
                                    marker.showInfoWindow();
                                }
                                // Toast.makeText(MapsActivity.this, lat+"", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });


        }catch (NullPointerException e) {

        }

    }

    @Override
    public void onMapReady(@NonNull  GoogleMap googleMap) {


        // googleMap.setOnInfoWindowClickListener(MapsActivity.this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationParams params = new LocationParams.Builder()
                .setAccuracy(LocationAccuracy.HIGH)
                .setDistance(1f)
                .setInterval(5 * 1000)
                .build();
        SmartLocation smart = new SmartLocation.Builder(MapsActivity.this).logging(true).build();
        smart.location().config(params).start(this);
        //getLocation();

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        SmartLocation.with(this).location().stop();
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    private void getCurrentLocation2() {
        Toast.makeText(this, "location2", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "getDeviceLocation: get Currnt Location");

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull  Location location) {
                    if (location != null){
                        mMap.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull  GoogleMap googleMap) {

                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        lat = latLng.latitude;
                                        lon = latLng.longitude;
                                        StringBuilder sb = new StringBuilder();

                                        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                                        List<Address> addresses;
                                        try {
                                            addresses = gcd.getFromLocation(latLng.latitude,
                                                    latLng.longitude, 1);
                                            if (addresses.size() > 0) {

                                                sb.append(addresses.get(0).getAddressLine(0)).append("\n");
//                                    sb.append(addresses.get(0).getLocality()).append("\n");
//                                    sb.append(addresses.get(0).getPostalCode()).append("\n");
//                                    sb.append(addresses.get(0).getCountryName());

                                            }

                                        } catch (IOException  e) {
                                            e.printStackTrace();
                                        }
                                        address = sb;
                                        Toast.makeText(MapsActivity.this, sb, Toast.LENGTH_SHORT).show();
                                        // Toast.makeText(MapsActivity.this, lat+"", Toast.LENGTH_SHORT).show();
                                        markerOptions2 = new MarkerOptions().position(latLng).title("موقع العميل الجديد");;
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                                        marker2 = googleMap.addMarker(markerOptions2);
                                        marker.remove();
                                        marker2.showInfoWindow();
                                    }
                                });

                                StringBuilder sb = new StringBuilder();

                                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                                List<Address> addresses;
                                try {
                                    addresses = gcd.getFromLocation(location.getLatitude(),
                                            location.getLongitude(), 1);
                                    if (addresses.size() > 0) {

                                        sb.append(addresses.get(0).getAddressLine(0)).append("\n");
//                                    sb.append(addresses.get(0).getLocality()).append("\n");
//                                    sb.append(addresses.get(0).getPostalCode()).append("\n");
//                                    sb.append(addresses.get(0).getCountryName());

                                    }

                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                                address = sb;
                                if (flag != 3){
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                    Toast.makeText(MapsActivity.this, sb, Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(MapsActivity.this, lat+"", Toast.LENGTH_SHORT).show();
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    markerOptions2 = new MarkerOptions().position(latLng).title("موقع العميل الجديد");
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                    marker2 =googleMap.addMarker(markerOptions2);
                                    marker.remove();
                                    marker2.showInfoWindow();
                                }else {
                                    lat = Double.parseDouble(lat1);
                                    lon = Double.parseDouble(lon1);
                                    LatLng latLng = new LatLng(lat, lon);
                                    markerOptions2 = new MarkerOptions().position(latLng).title("موقع العميل الجديد");
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                    marker2 =googleMap.addMarker(markerOptions2);
                                    marker.remove();
                                    marker2.showInfoWindow();
                                }
                                // Toast.makeText(MapsActivity.this, lat+"", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });


        }catch (NullPointerException e) {

        }

    }

    @Override
    public void onLocationUpdated(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_location(lat,lon);
            }
        });
    }

    private void update_location(Double latitude,Double longitude) {
        if (Utilities.isNetworkAvailable(this)){
            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessModel> call = getDataService.update_location(user_id,client_id,latitude+"",longitude+"");
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            Toast.makeText(MapsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MapsActivity.this, ClientsActivity.class));
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

                }
            });
        }
    }
}