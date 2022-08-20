package com.mz.bf.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mz.bf.R;

import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_profile, container ,false);
        return  view;
    }

    /*private void validation() {
        name = fragmentProfileBinding.etUsername.getText().toString();
        phone = fragmentProfileBinding.etPhone.getText().toString();
        password = fragmentProfileBinding.etPassword.getText().toString();
        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(password)){
            if (filepath != null){
                profileViewModel.update_user_with_img(user_id,name,password,filepath);
            }else {
                profileViewModel.update_user_without_img(user_id,name,password);
            }
        }
    }

    private void Check_ReadPermission(int img) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(getActivity(), new String[]{
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        loginModel = mySharedPreference.Get_UserData(getActivity());
        user_id = loginModel.getId();
        profileViewModel.getUserData(user_id);
    }

    public void setData(LoginModel body) {
        fragmentProfileBinding.etUsername.setText(body.getUserName());
        fragmentProfileBinding.etPhone.setText(body.getMob());
        fragmentProfileBinding.etCarNum.setText(body.getCarNumber());
        fragmentProfileBinding.etNationalNum.setText(body.getNationalNum());
        fragmentProfileBinding.etPassword.setText(body.getPassword());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            Picasso.get().load(filepath).into(fragmentProfileBinding.userImg);
        }else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = data.getData();;
            Picasso.get().load(filepath).into(fragmentProfileBinding.userImg);
        }
    }*/
}