package com.mz.bf.addsalary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.BillsFragment;
import com.mz.bf.addbill.Client;
import com.mz.bf.addpayment.PaymentActivity;
import com.mz.bf.addsalary.ClientAdapter;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.addvisit.AddVisitActivity;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityAddSalaryBinding;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSalaryActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    ImageView back_img;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,bill_date,client_id,type="1",value,notes;
   ActivityAddSalaryBinding activityAddSalaryBinding;
    AddSalaryViewModel addSalaryViewModel;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date_picker_dialog;
    ClientAdapter clientAdapter;
    LinearLayoutManager layoutManager3;
    Dialog dialog2;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    List<String> typelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salary);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        activityAddSalaryBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_salary);
        addSalaryViewModel = new AddSalaryViewModel(this);
        activityAddSalaryBinding.setAddsalary(addSalaryViewModel);
        addSalaryViewModel.getTypes();
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(AddSalaryActivity.this) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(AddSalaryActivity.this).getRecords().getUrl();
        }
        addSalaryViewModel.get_rkm();
        activityAddSalaryBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        myCalendar = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(new Date());
        activityAddSalaryBinding.etBillDate.setText(bill_date);
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
        activityAddSalaryBinding.etBillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddSalaryActivity.this, date_picker_dialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        activityAddSalaryBinding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (activityAddSalaryBinding.spinnerType.getSelectedItemPosition()==0){
                        type = "1";


                    }else if (activityAddSalaryBinding.spinnerType.getSelectedItemPosition()==1){
                        type ="2";

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
        activityAddSalaryBinding.etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openclientpopup(user_id);
            }
        });
        activityAddSalaryBinding.btnAddBill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        activityAddSalaryBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void validation() {
        value = activityAddSalaryBinding.etValue.getText().toString();
        notes = activityAddSalaryBinding.etNotes.getText().toString();
        if (!TextUtils.isEmpty(value)&&!TextUtils.isEmpty(client_id)&&!TextUtils.isEmpty(bill_date)){
            addSalaryViewModel.add_salary(type,user_id,bill_date,client_id,value,notes);
        }else {
            if (TextUtils.isEmpty(value)){
                activityAddSalaryBinding.etValue.setError("أدخل قيمة المبلغ");
            }else {
                activityAddSalaryBinding.etValue.setError(null);
            }
            if (TextUtils.isEmpty(bill_date)){
                activityAddSalaryBinding.etBillDate.setError("أدخل تاريخ سند القبض");
            }else {
                activityAddSalaryBinding.etBillDate.setError(null);
            }
            if (TextUtils.isEmpty(client_id)){
                activityAddSalaryBinding.etClientName.setError("أدخل إسم العميل");
            }else {
                activityAddSalaryBinding.etClientName.setError(null);
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
        CardView cardView = view.findViewById(R.id.card_view);
        RecyclerView clients_recycler = view.findViewById(R.id.product_recycler);
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setHint("إسم العميل");
        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(AddSalaryActivity.this,base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,1);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        clientAdapter = new ClientAdapter(response.body().getClients(),AddSalaryActivity.this);
                        layoutManager3 = new LinearLayoutManager(AddSalaryActivity.this);
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
                if (Utilities.isNetworkAvailable(AddSalaryActivity.this)){
                    GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(AddSalaryActivity.this,base_url).create(GetDataService.class);
                    Call<ClientModel> call = getDataService.search_clients(user_id,word ,1);
                    call.enqueue(new Callback<ClientModel>() {
                        @Override
                        public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                            if (response.isSuccessful()) {
                                clientAdapter = new ClientAdapter(response.body().getClients(), AddSalaryActivity.this);
                                layoutManager3 = new LinearLayoutManager(AddSalaryActivity.this);
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

    private void PerformClientPagination(Integer page,String word ,String user_id) {
        if (Utilities.isNetworkAvailable(AddSalaryActivity.this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(AddSalaryActivity.this,base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.search_clients(user_id,word,page);
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

    private void updateLabelStart() {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(myCalendar.getTime());
        activityAddSalaryBinding.etBillDate.setText(bill_date);
    }

    public void setClientData(Client client) {
        client_id = client.getClientIdFk();
        activityAddSalaryBinding.etClientName.setText(client.getClientName());
        dialog2.dismiss();
    }

    public void set_rkm(Integer rkm) {
        activityAddSalaryBinding.etSandNum.setText(rkm+"");
    }

    public void setypespinner(List<String> typelist) {
        this.typelist = typelist;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddSalaryActivity.this,R.layout.spinner_item2,this.typelist);
        activityAddSalaryBinding.spinnerType.setAdapter(arrayAdapter);
    }
}