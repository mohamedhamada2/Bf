package com.mz.bf.addpayment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.Client;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.addsalary.AddSalaryActivity;
import com.mz.bf.allbills.AllBillsFragment;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityPaymentBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding activityPaymentBinding;
    PaymentViewModel paymentViewModel;
    ImageView back_img;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,type_id,client_id;
    LinearLayoutManager layoutManager;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 20;
    int page =1 ;
    Calendar myCalendar,myCalendar2;
    DatePickerDialog date_picker_dialog,date_picker_dialog2;
    List<String> typelist;
    String from_date,to_date,base_url;
    Date d1,d2;
    Integer day,month,year;
    ClientAdapter clientAdapter;
    LinearLayoutManager layoutManager3;
    Dialog dialog2;
    CodeSharedPreferance codeSharedPreferance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        activityPaymentBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment);
        paymentViewModel = new PaymentViewModel(this);
        activityPaymentBinding.setPaymentviewmodel(paymentViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        typelist = new ArrayList<>();
        paymentViewModel.getTypes();
        user_id = loginModel.getId();
        myCalendar = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            day =  myCalendar.get(Calendar.DAY_OF_MONTH);
            year =  myCalendar.get(Calendar.YEAR);
            month = myCalendar.get(Calendar.MONTH);
        }
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(PaymentActivity.this) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(PaymentActivity.this).getRecords().getUrl();
        }
        paymentViewModel.get_all_sandat(user_id,type_id,1,from_date,to_date,client_id);
        activityPaymentBinding.recyclerSands.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount = layoutManager.getChildCount();
                totalitemcount = layoutManager.getItemCount();
                pastvisibleitem = layoutManager.findFirstVisibleItemPosition();
                if(dy>0){
                    if(isloading){
                        if(totalitemcount>previous_total){
                            isloading = false;
                            previous_total = totalitemcount;
                        }
                    }
                    if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                        page++;
                        paymentViewModel.PerformPagination(user_id,type_id,page,from_date,to_date,client_id);
                        isloading = true;
                    }
                }
            }
        });
        activityPaymentBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        activityPaymentBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                pastvisibleitem =0;
                visibleitemcount=0;
                totalitemcount=0;
                previous_total=0;
                view_threshold = 20;
                paymentViewModel.get_all_sandat(user_id,type_id,1,from_date,to_date,client_id);
            }
        });
        activityPaymentBinding.etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCalender();
            }
        });
        activityPaymentBinding.etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCalender2();
            }
        });
        activityPaymentBinding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (activityPaymentBinding.spinnerType.getSelectedItemPosition()==0){
                        type_id = "1";


                    }else if (activityPaymentBinding.spinnerType.getSelectedItemPosition()==1){
                        type_id ="2";

                    }
                    //citytitlelist.clear();
                }catch (Exception e){
                    TextView textView = (TextView) view;
                    textView.setVisibility(View.INVISIBLE);
                    //citytitlelist.clear();
                    //Toast.makeText(AddStoreActivity.this, "لا يوحد مدينة", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activityPaymentBinding.etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openclientpopup(user_id);
            }
        });
        activityPaymentBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityPaymentBinding.etToDate.setVisibility(View.VISIBLE);
                activityPaymentBinding.etFromDate.setVisibility(View.VISIBLE);
                activityPaymentBinding.etClientName.setVisibility(View.VISIBLE);
                activityPaymentBinding.spinnerType.setVisibility(View.VISIBLE);
                activityPaymentBinding.linearLy.setVisibility(View.VISIBLE);
            }
        });
        activityPaymentBinding.btnAddAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityPaymentBinding.etToDate.setVisibility(View.GONE);
                activityPaymentBinding.etFromDate.setVisibility(View.GONE);
                activityPaymentBinding.etClientName.setVisibility(View.GONE);
                activityPaymentBinding.spinnerType.setVisibility(View.GONE);
                activityPaymentBinding.linearLy.setVisibility(View.GONE);
                page = 1;
                pastvisibleitem =0;
                visibleitemcount=0;
                totalitemcount=0;
                previous_total=0;
                view_threshold = 20;
                paymentViewModel.get_all_sandat(user_id,"1",1,"","","");
            }
        });

    }

    private void openclientpopup(String user_id) {
        final Integer[] page = {1};
        final boolean[] isloading = new boolean[1];
        final int[] pastvisibleitem = new int[1];
        final int[] visibleitemcount = new int[1];
        final int[] totalitemcount = new int[1];
        final int[] previous_total = { 0 };
        int view_threshold = 20;
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.products_dialog, null);
        RecyclerView clients_recycler = view.findViewById(R.id.product_recycler);
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setHint("إسم العميل");
        if (Utilities.isNetworkAvailable(PaymentActivity.this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(PaymentActivity.this,base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,1);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        clientAdapter = new ClientAdapter(response.body().getClients(),PaymentActivity.this);
                        layoutManager3 = new LinearLayoutManager(PaymentActivity.this);
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
                    if (Utilities.isNetworkAvailable(PaymentActivity.this)){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(PaymentActivity.this,base_url).create(GetDataService.class);
                        Call<ClientModel> call = getDataService.search_clients(user_id,charSequence.toString() ,1);
                        call.enqueue(new Callback<ClientModel>() {
                            @Override
                            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                                if (response.isSuccessful()) {
                                    clientAdapter = new ClientAdapter(response.body().getClients(),PaymentActivity.this);
                                    layoutManager3 = new LinearLayoutManager(PaymentActivity.this);
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
                    if (Utilities.isNetworkAvailable(PaymentActivity.this)){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(PaymentActivity.this,base_url).create(GetDataService.class);
                        Call<ClientModel> call = getDataService.get_clients(user_id, 1);
                        call.enqueue(new Callback<ClientModel>() {
                            @Override
                            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                                if (response.isSuccessful()) {
                                    clientAdapter = new ClientAdapter(response.body().getClients(), PaymentActivity.this);
                                    layoutManager3 = new LinearLayoutManager(PaymentActivity.this);
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

    private void PerformClientPagination(Integer integer, String user_id) {
        if (Utilities.isNetworkAvailable(PaymentActivity.this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(PaymentActivity.this,base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,integer);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getClients().isEmpty()){
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

    public void init_sand(SandsAdapter sandsAdapter) {
        activityPaymentBinding.recyclerSands.setAdapter(sandsAdapter);
        layoutManager = new LinearLayoutManager(this);
        activityPaymentBinding.recyclerSands.setLayoutManager(layoutManager);
        activityPaymentBinding.recyclerSands.setHasFixedSize(true);
    }
    private void updateLabelStart2(Integer year,Integer month,Integer day) {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        to_date = day+""+"-"+(month+1)+""+"-"+year+"";
        try {
            d2 = sdf.parse(to_date);
            if (d1.compareTo(d2) < 0){
                activityPaymentBinding.etToDate.setText(to_date);
            }else {
                Toast.makeText(this, "تاريخ نهاية العرض قبل تاريخ البداية", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void updateLabelStart(Integer year,Integer month,Integer day) {

        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        from_date = day+""+"-"+(month+1)+""+"-"+year+"";
        try {
            d1 = sdf.parse(from_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        activityPaymentBinding.etFromDate.setText(from_date);
    }

    private void OpenCalender2() {
        if (!TextUtils.isEmpty(from_date)){
            date_picker_dialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    updateLabelStart2(year,month,dayOfMonth);
                }
            },year,month,day);
            date_picker_dialog2.getDatePicker().setMaxDate(myCalendar2.getTimeInMillis());
            date_picker_dialog2.show();
        }else {
            activityPaymentBinding.etFromDate.setError("أدخل تاريخ البداية");
        }
    }

    private void OpenCalender() {
        //Toast.makeText(mainActivity, "click", Toast.LENGTH_SHORT).show();
        date_picker_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                updateLabelStart(year,month,dayOfMonth);
            }
        },year,month,day);
        date_picker_dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        date_picker_dialog.show();
    }

    public void setypespinner(List<String> typelist) {
        this.typelist = typelist;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentActivity.this,R.layout.spinner_item2,this.typelist);
        activityPaymentBinding.spinnerType.setAdapter(arrayAdapter);
    }

    public void setClientData(Client client) {
        client_id = client.getClientIdFk();
        activityPaymentBinding.etClientName.setText(client.getClientName());
        dialog2.dismiss();
    }
}