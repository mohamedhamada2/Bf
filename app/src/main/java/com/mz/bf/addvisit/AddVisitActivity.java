package com.mz.bf.addvisit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.Client;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityAddVisitBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVisitActivity extends AppCompatActivity implements OnLocationUpdatedListener {
    ActivityAddVisitBinding activityAddVisitBinding;
    AddVisitViewModel addVisitViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,bill_date,client_id,type,value,notes;
    ClientAdapter clientAdapter;
    LinearLayoutManager layoutManager3;
    Dialog dialog2;
    double tvLongitude,tvLatitude;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);
        activityAddVisitBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_visit);
        addVisitViewModel = new AddVisitViewModel(this);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       // getLocation();
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(AddVisitActivity.this) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(AddVisitActivity.this).getRecords().getUrl();
        }

        activityAddVisitBinding.etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openclientpopup(user_id);
            }
        });
        activityAddVisitBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void validation() {
        notes = activityAddVisitBinding.etNotes.getText().toString();
        type = "1";
        if (!TextUtils.isEmpty(client_id)) {
            addVisitViewModel.add_visit(client_id,tvLatitude, tvLongitude, notes, user_id);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            LocationParams params = new LocationParams.Builder()
                    .setAccuracy(LocationAccuracy.HIGH)
                    .setDistance(1f)
                    .setInterval(5 * 1000)
                    .build();
            SmartLocation smart = new SmartLocation.Builder(AddVisitActivity.this).logging(true).build();
            smart.location().config(params).start(this);
            //getLocation();
        }
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        SmartLocation.with(this).location().stop();
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
                            tvLatitude =location.getLatitude();
                            tvLongitude = location.getLongitude();
                           // Toast.makeText(AddVisitActivity.this, "hello2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
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
            tvLatitude =  mLastLocation.getLatitude() ;
            tvLongitude = mLastLocation.getLongitude();
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
    private void openclientpopup(String user_id) {
        final Integer[] page = {1};
        final boolean[] isloading = new boolean[1];
        final int[] pastvisibleitem = new int[1];
        final int[] visibleitemcount = new int[1];
        final int[] totalitemcount = new int[1];
        final int[] previous_total = { 0 };
        int view_threshold = 20;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.product_dialog2, null);
        RecyclerView clients_recycler = view.findViewById(R.id.product_recycler);
        CardView cardView = view.findViewById(R.id.card_view);
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setHint("إسم العميل");
        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(AddVisitActivity.this,base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,1);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        clientAdapter = new ClientAdapter(response.body().getClients(), AddVisitActivity.this);
                        layoutManager3 = new LinearLayoutManager(AddVisitActivity.this);
                        clients_recycler.setLayoutManager(layoutManager3);
                        clients_recycler.setAdapter(clientAdapter);
                        clients_recycler.setHasFixedSize(true);
                    }
                }
                @Override
                public void onFailure(Call<ClientModel> call, Throwable t) {

                }
            });
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = et_search.getText().toString();
                if (Utilities.isNetworkAvailable(AddVisitActivity.this)){
                    GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(AddVisitActivity.this,base_url).create(GetDataService.class);
                    Call<ClientModel> call = getDataService.search_clients(user_id,word ,1);
                    call.enqueue(new Callback<ClientModel>() {
                        @Override
                        public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                            if (response.isSuccessful()) {
                                clientAdapter = new ClientAdapter(response.body().getClients(), AddVisitActivity.this);
                                layoutManager3 = new LinearLayoutManager(AddVisitActivity.this);
                                clients_recycler.setLayoutManager(layoutManager3);
                                clients_recycler.setAdapter(clientAdapter);
                                clients_recycler.setHasFixedSize(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ClientModel> call, Throwable t) {

                        }
                    });
                }

            }
        });
        clients_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount[0] = layoutManager3.getChildCount();
                totalitemcount[0] = layoutManager3.getItemCount();
                pastvisibleitem[0] = layoutManager3.findFirstVisibleItemPosition();
                if(dy>0){
                    //Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                    if(isloading[0]){
                        if(totalitemcount[0] > previous_total[0]){
                            isloading[0] = false;
                            previous_total[0] = totalitemcount[0];
                        }
                    }
                    if(!isloading[0] &&(totalitemcount[0] - visibleitemcount[0])<= pastvisibleitem[0] +view_threshold){
                        //Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                        page[0]++;
                        PerformClientPagination(page[0],et_search.getText().toString(),user_id);
                        isloading[0] = true;
                    }
                }
            }
        });
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        builder.setView(view);
        dialog2 = builder.create();
        dialog2.show();
        Window window = dialog2.getWindow();
        //Toast.makeText(homeActivity, loginModel.getData().getUser().getId()+"", Toast.LENGTH_SHORT).show();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
    }

    private void PerformClientPagination(Integer page,String word,String user_id) {
        if (Utilities.isNetworkAvailable(AddVisitActivity.this)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(AddVisitActivity.this,base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.search_clients(user_id, word,page);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getClients().isEmpty()) {
                            clientAdapter.add_client(response.body().getClients());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClientModel> call, Throwable t) {

                }
            });
        }
    }

    public void setClientData(Client client) {
        client_id = client.getClientIdFk();
        activityAddVisitBinding.etClientName.setText(client.getClientName());
        dialog2.dismiss();
    }

    @Override
    public void onLocationUpdated(Location location) {
        Log.e("lat1",location.getLatitude()+"");
        tvLatitude = location.getLatitude();
        tvLongitude = location.getLongitude();
        activityAddVisitBinding.btnAddBill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }
}