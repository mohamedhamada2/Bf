package com.mz.bf.addvisit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.Client;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.customerservice.CustomerServiceActivity;
import com.mz.bf.databinding.ActivityAddVisitBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AddVisitActivity extends AppCompatActivity implements OnLocationUpdatedListener  {
    ActivityAddVisitBinding activityAddVisitBinding;
    AddVisitViewModel addVisitViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,bill_date,client_id,type,value,notes,visit_id;
    ClientAdapter clientAdapter;
    LinearLayoutManager layoutManager3;
    Dialog dialog2;
    double tvLongitude,tvLatitude;
    int flag;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    Integer IMG=2,IMG2=3;
    int REQUESTCAMERA = 4,REQUESTCAMERA2 = 5;

    ActivityResultLauncher<Intent> resultLauncher;
    ActivityResultLauncher<Intent> resultLauncher2;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    Uri front_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);
        activityAddVisitBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_visit);
        addVisitViewModel = new AddVisitViewModel(this);
        flag = getIntent().getIntExtra("flag",0);
        if (flag == 2){
            visit_id = getIntent().getStringExtra("id");
            activityAddVisitBinding.etClientName.setVisibility(View.GONE);
            registerResult();
        }else {
            activityAddVisitBinding.etNotes.setVisibility(View.GONE);
            activityAddVisitBinding.frontImg.setVisibility(View.GONE);
        }
        mySharedPreference = MySharedPreference.getInstance();
        locationTrack = new LocationTrack(AddVisitActivity.this);
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
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
        activityAddVisitBinding.frontImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 32) {
                    select_photo();
                }else {
                    Check_ReadPermission();
                }
                //

            }
        });
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

    }

    private void Check_ReadPermission() {
        if (ContextCompat.checkSelfPermission(AddVisitActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddVisitActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(AddVisitActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, IMG);
        }else {
            select_photo();
        }
    }

    private void select_photo() {
        final  CharSequence[] items = {"كاميرا","ملفات الصور","الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddVisitActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")){
                    if (Build.VERSION.SDK_INT > 32){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        resultLauncher2.launch(intent);
                    }else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,REQUESTCAMERA);
                    }
                }else if (items[which].equals("ملفات الصور")){

                    if (Build.VERSION.SDK_INT > 32){
                        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                        resultLauncher.launch(intent);
                    }else {
                        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        //startActivityForResult(intent.createChooser(intent,"Select File"),img);
                        startActivityForResult(intent,IMG);
                    }
                    //intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent,"Select File"),img);
                    //startActivityForResult(intent,img);

                }else if (items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() != RESULT_CANCELED){
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getData().getData());
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        String file_name = String.format("%d.jpg", System.currentTimeMillis());
                        File finalfile = new File(path, file_name);
                        FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        front_img = Uri.fromFile(finalfile);
                        activityAddVisitBinding.frontImg.setImageURI(front_img);
                    } catch (Exception e) {
                        front_img = result.getData().getData();
                        Picasso.get().load(front_img).into(activityAddVisitBinding.frontImg);
                    }
                }
            }
        });
        resultLauncher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    Bundle bundle = result.getData().getExtras();
                    final Bitmap bitmap = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                    front_img = Uri.parse(path);
                    activityAddVisitBinding.frontImg.setImageURI(front_img);
                } catch (Exception e) {
                    front_img = result.getData().getData();
                    Picasso.get().load(front_img).into(activityAddVisitBinding.frontImg);
                }
            }
        });
    }

    private void validation() {
        Log.e("lat",tvLatitude+"");
        Log.e("lon",tvLongitude+"");
        type = "1";
        if (flag == 1){
            if (!TextUtils.isEmpty(client_id)) {
                addVisitViewModel.add_visit(client_id,tvLatitude, tvLongitude, notes, user_id);
            }
        }else {
            notes = activityAddVisitBinding.etNotes.getText().toString();
            if (front_img != null){
                addVisitViewModel.end_customer_visit_with_img(visit_id,tvLatitude, tvLongitude, notes, front_img);
            }else {
                addVisitViewModel.end_customer_visit(visit_id,tvLatitude, tvLongitude, notes);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationTrack.canGetLocation()) {
            LocationParams params = new LocationParams.Builder()
                    .setAccuracy(LocationAccuracy.HIGH)
                    .setDistance(1f)
                    .setInterval(5 * 1000)
                    .build();
            SmartLocation smart = new SmartLocation.Builder(AddVisitActivity.this).logging(true).build();
            smart.location().config(params).start(this);
        }else {
            locationTrack.showSettingsAlert();
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
        final View view = inflater.inflate(R.layout.products_dialog, null);
        RecyclerView clients_recycler = view.findViewById(R.id.product_recycler);
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setHint("إسم العميل");
        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
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
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")){
                    if (Utilities.isNetworkAvailable(AddVisitActivity.this)){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        Call<ClientModel> call = getDataService.search_clients(user_id,charSequence.toString() ,1);
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
                }else {
                    if (Utilities.isNetworkAvailable(AddVisitActivity.this)){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        Call<ClientModel> call = getDataService.get_clients(user_id, 1);
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                        PerformClientPagination(page[0],user_id);
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

    private void PerformClientPagination(Integer page, String user_id) {
        if (Utilities.isNetworkAvailable(AddVisitActivity.this)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id, page);
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
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddVisitActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }
    @Override
    public void onPause() {
        SmartLocation.with(this).location().stop();
        super.onPause();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                front_img = Uri.fromFile(finalfile);
                activityAddVisitBinding.frontImg.setImageURI(front_img);
                //Picasso.get().load(front_img).into(activityAddInvestorBinding.frontImg);
            }catch (Exception e){
                front_img = data.getData();
                Picasso.get().load(front_img).into(activityAddVisitBinding.frontImg);
            }

        }else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            front_img = Uri.parse(path);
            activityAddVisitBinding.frontImg.setImageURI(front_img);
            // Picasso.get().load(front_img).into(activityAddInvestorBinding.frontImg);
        }
    }
}