package com.mz.bf.customerservice;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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

import android.Manifest;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.Client;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.addvisit.AddVisitActivity;
import com.mz.bf.addvisit.AddVisitViewModel;
import com.mz.bf.addvisit.ClientAdapter;
import com.mz.bf.addvisit.LocationTrack;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityAddVisitBinding;
import com.mz.bf.databinding.ActivityCustomerServiveBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerServiceActivity extends AppCompatActivity implements OnLocationUpdatedListener {
        ActivityCustomerServiveBinding activityCustomerServiveBinding;
        CustomerServiceViewModel customerServiceViewModel;
        MySharedPreference mySharedPreference;
        LoginModel loginModel;
        String user_id,bill_date,client_id,type,value,notes,customer_name,customer_phone,customer_address,visit_id;
        ClientAdapter clientAdapter;
        LinearLayoutManager layoutManager3;
        Dialog dialog2;
        double tvLongitude,tvLatitude;
        private ArrayList permissionsToRequest;
        private ArrayList permissionsRejected = new ArrayList();
        private ArrayList permissions = new ArrayList();
        ActivityResultLauncher<Intent> resultLauncher;
        ActivityResultLauncher<Intent> resultLauncher2;

        private final static int ALL_PERMISSIONS_RESULT = 101;
        LocationTrack locationTrack;
        Uri front_img;
        int flag ;
    Integer IMG=2,IMG2=3;
    int REQUESTCAMERA = 4,REQUESTCAMERA2 = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_servive);
        activityCustomerServiveBinding = DataBindingUtil.setContentView(this,R.layout.activity_customer_servive);
        customerServiceViewModel = new CustomerServiceViewModel(this);
        flag = getIntent().getIntExtra("flag",0);
        if (flag == 2){
            visit_id = getIntent().getStringExtra("id");
            activityCustomerServiveBinding.etClientName.setVisibility(View.GONE);
            activityCustomerServiveBinding.etClientPhone.setVisibility(View.GONE);
            activityCustomerServiveBinding.etAddress.setVisibility(View.GONE);
            registerResult();
        }else {
            activityCustomerServiveBinding.etNotes.setVisibility(View.GONE);
            activityCustomerServiveBinding.frontImg.setVisibility(View.GONE);
        }
        mySharedPreference = MySharedPreference.getInstance();
        locationTrack = new LocationTrack(CustomerServiceActivity.this);
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        activityCustomerServiveBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        activityCustomerServiveBinding.frontImg.setOnClickListener(new View.OnClickListener() {
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

    private void registerResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),result.getData().getData());
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    String file_name= String.format("%d.jpg",System.currentTimeMillis());
                    File finalfile = new File(path,file_name);
                    FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    front_img = Uri.fromFile(finalfile);
                    activityCustomerServiveBinding.frontImg.setImageURI(front_img);
                }catch (Exception e){
                    front_img = result.getData().getData();
                    Picasso.get().load(front_img).into(activityCustomerServiveBinding.frontImg);
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
                    activityCustomerServiveBinding.frontImg.setImageURI(front_img);
                } catch (Exception e) {
                    front_img = result.getData().getData();
                    Picasso.get().load(front_img).into(activityCustomerServiveBinding.frontImg);
                }
            }
        });
    }

    private void Check_ReadPermission() {
        if (ContextCompat.checkSelfPermission(CustomerServiceActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(CustomerServiceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(CustomerServiceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(CustomerServiceActivity.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, IMG);
        }else {
            select_photo();
        }
    }

    private void select_photo() {
        final  CharSequence[] items = {"كاميرا","ملفات الصور","الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerServiceActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT > 32){
                        resultLauncher2.launch(intent);
                    }else {
                        startActivityForResult(intent,REQUESTCAMERA);
                    }
                }else if (items[which].equals("ملفات الصور")){
                    Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                    if (Build.VERSION.SDK_INT > 32){
                        resultLauncher.launch(intent);
                    }else {
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

    private void validation() {
        Log.e("lat",tvLatitude+"");
        Log.e("lon",tvLongitude+"");

        type = "1";
        if (flag == 1){
            customer_address = activityCustomerServiveBinding.etAddress.getText().toString();
            customer_name = activityCustomerServiveBinding.etClientName.getText().toString();
            customer_phone = activityCustomerServiveBinding.etClientPhone.getText().toString();
            if (!TextUtils.isEmpty(customer_name)&&!TextUtils.isEmpty(customer_phone)) {
                customerServiceViewModel.add_customer_service(user_id,tvLatitude, tvLongitude, customer_name, customer_phone,customer_address);
            }else {
                if (TextUtils.isEmpty(customer_name)){
                    activityCustomerServiveBinding.etClientName.setError("أدخل إسم العميل");
                }else {
                    activityCustomerServiveBinding.etClientName.setError(null);
                }
                if (TextUtils.isEmpty(customer_phone)){
                    activityCustomerServiveBinding.etClientPhone.setError("أدخل رقم الهاتف");
                }else {
                    activityCustomerServiveBinding.etClientPhone.setError(null);
                }
            }
        }else {
            notes = activityCustomerServiveBinding.etNotes.getText().toString();
            if (front_img != null){
                customerServiceViewModel.end_customer_service_with_img(visit_id,tvLatitude, tvLongitude, customer_name, notes, front_img);
            }else {
                customerServiceViewModel.end_customer_service(visit_id,tvLatitude, tvLongitude, customer_name, notes);
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
            SmartLocation smart = new SmartLocation.Builder(CustomerServiceActivity.this).logging(true).build();
            smart.location().config(params).start(this);
        }else {
            locationTrack.showSettingsAlert();
        }
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
        new AlertDialog.Builder(CustomerServiceActivity.this)
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
        activityCustomerServiveBinding.btnAddBill2.setOnClickListener(new View.OnClickListener() {
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
                activityCustomerServiveBinding.frontImg.setImageURI(front_img);
                //Picasso.get().load(front_img).into(activityAddInvestorBinding.frontImg);
            }catch (Exception e){
                front_img = data.getData();
                Picasso.get().load(front_img).into(activityCustomerServiveBinding.frontImg);
            }

        }else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            front_img = Uri.parse(path);
            activityCustomerServiveBinding.frontImg.setImageURI(front_img);
            // Picasso.get().load(front_img).into(activityAddInvestorBinding.frontImg);
        }
    }
}