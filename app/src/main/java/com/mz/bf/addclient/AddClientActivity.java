package com.mz.bf.addclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityAddClientBinding;

import java.util.List;

public class AddClientActivity extends AppCompatActivity {
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    ActivityAddClientBinding activityAddClientBinding;
    AddClientViewModel addClientViewModel;
    List<String> governtitlelist,citytitlelist;
    List<Govern> governList;
    List<City> cityList;
    String name,mob1,mob2,address,user_id,national_num,govern_id,city_id;
    Double lon,lat;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        activityAddClientBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_client);
        addClientViewModel = new AddClientViewModel(this);
        activityAddClientBinding.setAddclientviewmodel(addClientViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        addClientViewModel.getgovern();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        activityAddClientBinding.governSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                govern_id = governList.get(i).getId();
                addClientViewModel.getCities(govern_id);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activityAddClientBinding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    city_id = cityList.get(i).getId();
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.purple_500));
                    //citytitlelist.clear();
                }catch (Exception e){
                    TextView textView = (TextView) view;
                    textView.setVisibility(View.INVISIBLE);
                    citytitlelist.clear();
                    Toast.makeText(AddClientActivity.this, "لا يوحد مدينة", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void addclient(View view) {
        validation();
    }

    private void validation() {
        //Toast.makeText(this, lat+"", Toast.LENGTH_SHORT).show();
        name = activityAddClientBinding.etUsername.getText().toString();
        mob1 = activityAddClientBinding.etPhone.getText().toString();
        mob2 = activityAddClientBinding.etPhone2.getText().toString();
        national_num = activityAddClientBinding.etNationalNum.getText().toString();
        address = activityAddClientBinding.etAddress.getText().toString();
        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(mob1)&&!TextUtils.isEmpty(national_num)
        &&!TextUtils.isEmpty(address)){
            addClientViewModel.addclient(name,govern_id,city_id,national_num,mob1,mob2,address,lat+"",lon+"",user_id);
        }else {
            if (TextUtils.isEmpty(name)){
                activityAddClientBinding.etUsername.setError("أدخل إسم المستخدم");
            }else {
                activityAddClientBinding.etUsername.setError(null);
            }
            if (TextUtils.isEmpty(mob1)){
                activityAddClientBinding.etPhone.setError("أدخل رقم الهاتف للمستخدم");
            }else {
                activityAddClientBinding.etPhone.setError(null);
            }
            if (TextUtils.isEmpty(national_num)){
                activityAddClientBinding.etNationalNum.setError("أدخل رقم الهوية للمستخدم");
            }else {
                activityAddClientBinding.etNationalNum.setError(null);
            }
            if (TextUtils.isEmpty(address)){
                activityAddClientBinding.etAddress.setError("أدخل عنوان المستخدم");
            }else {
                activityAddClientBinding.etAddress.setError(null);
            }
        }

    }

    public void setGovernsspinnerData(List<String> governtitlelist, List<Govern> governList) {
        this.governtitlelist = governtitlelist;
        this.governList = governList;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddClientActivity.this,R.layout.spinner_item2,governtitlelist);
        activityAddClientBinding.governSpinner.setAdapter(arrayAdapter);
    }

    public void setCitiesspinnerData(List<String> citytitlelist, List<City> cityList) {
        this.cityList = cityList;
        this.citytitlelist = citytitlelist;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddClientActivity.this,R.layout.spinner_item2,citytitlelist);
        activityAddClientBinding.citySpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLocation();
        }
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                            //Toast.makeText(AddVisitActivity.this, "hello", Toast.LENGTH_SHORT).show();
                        } else {
                            lat =location.getLatitude();
                            lon = location.getLongitude();
                            //Toast.makeText(AddClientActivity.this, lat+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "الرجاء فتح تحديد الموقع الخاص بك", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            //Toast.makeText(this, "hello3", Toast.LENGTH_SHORT).show();
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat =  mLastLocation.getLatitude() ;
            lon = mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }
}