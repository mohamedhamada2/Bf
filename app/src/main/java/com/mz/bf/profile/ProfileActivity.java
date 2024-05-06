package com.mz.bf.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding activityProfileBinding;
    ProfileViewModel profileViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String name,phone,car_number,national_num,img,user_id,password;
    Integer IMG = 1;
    Integer REQUESTCAMERA= 2;
    Uri filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        activityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        profileViewModel = new ProfileViewModel(this);
        activityProfileBinding.setProfileviewmodel(profileViewModel);
        getSharedPreferanceData();
        activityProfileBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        activityProfileBinding.btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(IMG);
            }
        });

    }
    private void validation() {
        name = activityProfileBinding.etUsername.getText().toString().trim();
        phone = activityProfileBinding.etPhone.getText().toString().trim();
        password = activityProfileBinding.etPassword.getText().toString();
        if (!TextUtils.isEmpty(name)){
            if (filepath != null){
                profileViewModel.update_user_with_img(user_id,name,password,filepath);
            }else {
                profileViewModel.update_user_without_img(user_id,name,password);
            }
        }else {
            activityProfileBinding.etUsername.setError("أدخل اسمك من فضلك");
        }
    }

    private void Check_ReadPermission(int img) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, img);
        }else {
            select_photo(img);
        }
    }

    private void select_photo(int img) {
        final  CharSequence[] items = {"كاميرا","ملفات الصور","الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUESTCAMERA);
                }else if (items[which].equals("ملفات الصور")){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent,"Select File"),img);
                    startActivityForResult(intent,img);

                }else if (items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void getSharedPreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        profileViewModel.getUserData(user_id);
    }

    public void setData(LoginModel body) {
        activityProfileBinding.etUsername.setText(body.getUserName());
        activityProfileBinding.etPhone.setText(body.getMob());
        activityProfileBinding.etCarNum.setText(body.getCarNumber());
        activityProfileBinding.etNationalNum.setText(body.getNationalNum());
        Picasso.get().load("https://abbgroup.org.uk/project/Demo/uploads/images/"+body.getImg()).into(activityProfileBinding.userImg);
        //activityProfileBinding.etPassword.setText(body.getPassword());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            Picasso.get().load(filepath).into(activityProfileBinding.userImg);
        }else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = data.getData();;
            Picasso.get().load(filepath).into(activityProfileBinding.userImg);
        }
    }
}