package com.mz.bf.add_masrouf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.addsalary.AddSalaryActivity;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityAddMasroufBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddMasroufActivity extends AppCompatActivity {
    ActivityAddMasroufBinding activityAddMasroufBinding;
    AddMasroufViewModel addMasroufViewModel;
    List<String> masroufattitlelist;
    List<Masrouf> masroufList;
    String setting_id;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date_picker_dialog;
    String bill_date,value;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_masrouf);
        activityAddMasroufBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_masrouf);
        addMasroufViewModel = new AddMasroufViewModel(this);
        activityAddMasroufBinding.setAddmasroufviewmodel(addMasroufViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        id = loginModel.getId();
        addMasroufViewModel.get_masroufat();
        activityAddMasroufBinding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setting_id = masroufList.get(i).getIdSetting();
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        myCalendar = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(new Date());
        activityAddMasroufBinding.etBillDate.setText(bill_date);
        //updateview(language);
        date_picker_dialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();

            }

        };
        activityAddMasroufBinding.etBillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddMasroufActivity.this, date_picker_dialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        activityAddMasroufBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void validation() {
        value = activityAddMasroufBinding.etValue.getText().toString();
        if (!TextUtils.isEmpty(value)){
            addMasroufViewModel.add_masrouf(setting_id,bill_date,value,id);
        }
    }

    private void updateLabelStart() {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(myCalendar.getTime());
        activityAddMasroufBinding.etBillDate.setText(bill_date);
    }

    public void setMasroufatspinnerData(List<String> masroufattitlelist, List<Masrouf> masroufatlist) {
        this.masroufattitlelist = masroufattitlelist;
        masroufList = masroufatlist;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddMasroufActivity.this,R.layout.spinner_item2,masroufattitlelist);
        activityAddMasroufBinding.spinnerType.setAdapter(arrayAdapter);
    }
}