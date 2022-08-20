package com.mz.bf.addclient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

    public void gotoMap(View view) {
        startActivityForResult(new Intent(AddClientActivity.this,MapsActivity.class),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            address = (String) data.getSerializableExtra("address");
            lat = data.getDoubleExtra("lat",0);
            lon = data.getDoubleExtra("lon",0);
            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            activityAddClientBinding.etAddress.setText(address);
        }
    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }
}