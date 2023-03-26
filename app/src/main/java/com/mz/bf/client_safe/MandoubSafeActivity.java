package com.mz.bf.client_safe;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityMandoubSafeBinding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MandoubSafeActivity extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date_picker_dialog;
    String bill_date;
    ActivityMandoubSafeBinding activityMandoubSafeBinding;
    MandoubSafeViewModel mandoubSafeViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    Double all_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandoub_safe);
        activityMandoubSafeBinding = DataBindingUtil.setContentView(this, R.layout.activity_mandoub_safe);
        mandoubSafeViewModel = new MandoubSafeViewModel(this);
        activityMandoubSafeBinding.setSafeviewmodel(mandoubSafeViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        myCalendar = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(new Date());
        mandoubSafeViewModel.get_mandoub_safe(user_id,bill_date);
        activityMandoubSafeBinding.etBillDate.setText(bill_date);
        activityMandoubSafeBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
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
        activityMandoubSafeBinding.etBillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MandoubSafeActivity.this, date_picker_dialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        activityMandoubSafeBinding.btnAddBill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mandoubSafeViewModel.get_mandoub_safe(user_id,bill_date);
            }
        });
    }

    private void updateLabelStart() {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(myCalendar.getTime());
        activityMandoubSafeBinding.etBillDate.setText(bill_date);
        mandoubSafeViewModel.get_mandoub_safe(user_id,bill_date);
    }

    public void setData(SafeModel safeModel) {
        DecimalFormat df = new DecimalFormat("#.00");
        activityMandoubSafeBinding.txtSandPrice.setText(df.format(safeModel.getAllsandatQabd())+"");
        activityMandoubSafeBinding.txtEarnest.setText(df.format(Double.parseDouble(safeModel.getAllsandatArbon()))+"");
        all_price = Double.parseDouble(safeModel.getAllsandatArbon())+safeModel.getAllsandatQabd();
        activityMandoubSafeBinding.txtAllPrice.setText(df.format(all_price)+"");
    }
}