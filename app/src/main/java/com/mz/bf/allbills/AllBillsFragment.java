package com.mz.bf.allbills;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.mz.bf.MainActivity;
import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.BillsFragment;
import com.mz.bf.addbill.Client;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.addbill.FatoraDetail;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.DialogBluthoosBinding;
import com.mz.bf.databinding.FragmentHomeBinding;
import com.mz.bf.printer.PrintPicture;
import com.mz.bf.uis.activity_print_bill.PrintActivity;
import com.mz.bf.uis.activity_print_bill.PrintBillActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllBillsFragment extends Fragment {
    WebView webView;
    ProgressBar progressBar;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    FragmentHomeBinding fragmentHomeBinding;
    AllBillsViewModel allBillsViewModel;
    AllBillsAdapter allBillsAdapter;
    LinearLayoutManager layoutManager;
    private boolean isloading;
    Calendar myCalendar,myCalendar2;
    DatePickerDialog date_picker_dialog,date_picker_dialog2;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 20;
    int page =1 ;
    Dialog dialog3;
    MainActivity mainActivity;
    CodeSharedPreferance codeSharedPreferance;
    String base_url,from_date="",to_date="",client_id="",fatora_num="";
    Date d1,d2;
    int day,month,year;
    ClientAdapter clientAdapter;
    LinearLayoutManager layoutManager3;
    Dialog dialog2;
    ///////////////////printer//////



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(getActivity());
        user_id = loginModel.getId();
        allBillsViewModel = new AllBillsViewModel(getActivity(),this);
        fragmentHomeBinding.setAllbillsviewmodel(allBillsViewModel);
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(getActivity()) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(getActivity()).getRecords().getUrl();
        }
        allBillsViewModel.get_all_bills(1,user_id,from_date,to_date,client_id,fatora_num);
        mainActivity = (MainActivity) getActivity();
        myCalendar = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
             day =  myCalendar.get(Calendar.DAY_OF_MONTH);
             year =  myCalendar.get(Calendar.YEAR);
             month = myCalendar.get(Calendar.MONTH);
        }
        /*date_picker_dialog = new DatePickerDialog.OnDateSetListener() {

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
        date_picker_dialog2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart2();

            }

        };*/
        View view = fragmentHomeBinding.getRoot();
        fragmentHomeBinding.allBillsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        allBillsViewModel.PerformPagination(page,user_id,from_date,to_date,client_id,fatora_num);
                        isloading = true;
                    }
                }
            }
        });
        fragmentHomeBinding.etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCalender();   
            }
        });
        fragmentHomeBinding.etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCalender2();
            }
        });
        fragmentHomeBinding.etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openclientpopup(user_id);
            }
        });
        fragmentHomeBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatora_num= fragmentHomeBinding.etRkmFatora.getText().toString();
                page = 1;
                pastvisibleitem =0;
                visibleitemcount=0;
                totalitemcount=0;
                previous_total=0;
                view_threshold = 20;
                allBillsViewModel.get_all_bills(page,user_id,from_date,to_date,client_id,fatora_num);
                /*fragmentHomeBinding.allBillsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                allBillsViewModel.PerformPagination(page,user_id,from_date,to_date);
                                isloading = true;
                            }
                        }
                    }
                });*/
            }
        });
        fragmentHomeBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentHomeBinding.etToDate.setVisibility(View.VISIBLE);
                fragmentHomeBinding.etFromDate.setVisibility(View.VISIBLE);
                fragmentHomeBinding.etClientName.setVisibility(View.VISIBLE);
                fragmentHomeBinding.etRkmFatora.setVisibility(View.VISIBLE);
                fragmentHomeBinding.linearLy.setVisibility(View.VISIBLE);
            }
        });
        fragmentHomeBinding.btnAddAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentHomeBinding.etToDate.setVisibility(View.GONE);
                fragmentHomeBinding.etFromDate.setVisibility(View.GONE);
                fragmentHomeBinding.etClientName.setVisibility(View.GONE);
                fragmentHomeBinding.etRkmFatora.setVisibility(View.GONE);
                fragmentHomeBinding.linearLy.setVisibility(View.GONE);
                page = 1;
                pastvisibleitem =0;
                visibleitemcount=0;
                totalitemcount=0;
                previous_total=0;
                view_threshold = 20;
                allBillsViewModel.get_all_bills(page,user_id,"","","","");
            }
        });












        /*user_id = loginModel.getId()+"";
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://abbgroup.org.uk/project/Demo/representative/Sale/view/"+user_id);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);*/
        return  view;
    }

    private void openclientpopup(String user_id) {
        final Integer[] page = {1};
        final boolean[] isloading = new boolean[1];
        final int[] pastvisibleitem = new int[1];
        final int[] visibleitemcount = new int[1];
        final int[] totalitemcount = new int[1];
        final int[] previous_total = { 0 };
        int view_threshold = 20;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.products_dialog, null);
        RecyclerView clients_recycler = view.findViewById(R.id.product_recycler);
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setHint("إسم العميل");
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,1);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        clientAdapter = new ClientAdapter(response.body().getClients(),getContext(), AllBillsFragment.this);
                        layoutManager3 = new LinearLayoutManager(getContext());
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
                    if (Utilities.isNetworkAvailable(getActivity())){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
                        Call<ClientModel> call = getDataService.search_clients(user_id,charSequence.toString() ,1);
                        call.enqueue(new Callback<ClientModel>() {
                            @Override
                            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                                if (response.isSuccessful()) {
                                    clientAdapter = new ClientAdapter(response.body().getClients(), getContext(), AllBillsFragment.this);
                                    layoutManager3 = new LinearLayoutManager(getContext());
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
                    if (Utilities.isNetworkAvailable(getActivity())){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
                        Call<ClientModel> call = getDataService.get_clients(user_id, 1);
                        call.enqueue(new Callback<ClientModel>() {
                            @Override
                            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                                if (response.isSuccessful()) {
                                    clientAdapter = new ClientAdapter(response.body().getClients(), getContext(), AllBillsFragment.this);
                                    layoutManager3 = new LinearLayoutManager(getContext());
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
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
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

    private void updateLabelStart2(Integer year,Integer month,Integer day) {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        to_date = day+""+"-"+(month+1)+""+"-"+year+"";
        try {
            d2 = sdf.parse(to_date);
            if (d1.compareTo(d2) < 0){
                fragmentHomeBinding.etToDate.setText(to_date);
            }else {
                Toast.makeText(getActivity(), "تاريخ نهاية العرض قبل تاريخ البداية", Toast.LENGTH_SHORT).show();
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
        fragmentHomeBinding.etFromDate.setText(from_date);
    }

    private void OpenCalender2() {
        if (!TextUtils.isEmpty(from_date)){
            date_picker_dialog2 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    updateLabelStart2(year,month,dayOfMonth);
                }
            },year,month,day);
            date_picker_dialog2.getDatePicker().setMaxDate(myCalendar2.getTimeInMillis());
            date_picker_dialog2.show();
        }else {
            fragmentHomeBinding.etFromDate.setError("أدخل تاريخ البداية");
        }
    }

    private void OpenCalender() {
        //Toast.makeText(mainActivity, "click", Toast.LENGTH_SHORT).show();
        date_picker_dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                updateLabelStart(year,month,dayOfMonth);
            }
        },year,month,day);
        date_picker_dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        date_picker_dialog.show();
    }


    public void init_bill(AllBillsAdapter allBillsAdapter) {
        fragmentHomeBinding.allBillsRecycler.setAdapter(allBillsAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentHomeBinding.allBillsRecycler.setLayoutManager(layoutManager);
        fragmentHomeBinding.allBillsRecycler.setHasFixedSize(true);
    }

    public void setBills_product(Fatora fatora) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.details_dialog, null);
        RecyclerView products_recycler = view2.findViewById(R.id.product_recycler);
        TextView txt_client_name = view2.findViewById(R.id.txt_client_name);
        TextView txt_bill_num = view2.findViewById(R.id.txt_bill_num);
        TextView txt_no_data = view2.findViewById(R.id.txt_no_data);
        txt_bill_num.setText(fatora.getRkmFatora());
        txt_client_name.setText(fatora.getClientName());
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
            Call<BillDetailsModel> call = getDataService.get_bill_details(fatora.getId());
            call.enqueue(new Callback<BillDetailsModel>() {
                @Override
                public void onResponse(Call<BillDetailsModel> call, Response<BillDetailsModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getDetails().isEmpty()){
                            txt_no_data.setVisibility(View.VISIBLE);
                            products_recycler.setVisibility(View.GONE);
                        }else {
                            BillDetailsAdapter billDetailsAdapter = new BillDetailsAdapter(response.body().getDetails(),getActivity());
                            products_recycler.setAdapter(billDetailsAdapter);
                            RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getActivity());
                            products_recycler.setLayoutManager(layoutmanager);
                            products_recycler.setHasFixedSize(true);
                            txt_no_data.setVisibility(View.GONE);
                            products_recycler.setVisibility(View.VISIBLE);
                        }

                    }
                }

                @Override
                public void onFailure(Call<BillDetailsModel> call, Throwable t) {

                }
            });
        }
        //fatoraDetailList = databaseClass.getDao().getallbills();
        //billsAdapter = new BillsAdapter(fatoraDetailList,getContext(),this);

        ImageView cancel_img = view2.findViewById(R.id.cancel_img);
        ImageView print_img = view2.findViewById(R.id.print_img);
        print_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrintBillActivity.class);
                intent.putExtra("flag",1);
                intent.putExtra("rkm",fatora.getRkmFatora());
                intent.putExtra("client_name",fatora.getClientName());
                intent.putExtra("id",fatora.getId());
                intent.putExtra("total_before",fatora.getFatoraCostBeforeDiscount());
                intent.putExtra("total_after",fatora.getFatoraCostAfterDiscount());
                intent.putExtra("discount",fatora.getDiscount());
                intent.putExtra("date",fatora.getDate());
                intent.putExtra("paid",fatora.getPaid());
                intent.putExtra("remain",fatora.getRemain());
                startActivity(intent);
            }
        });
        builder.setView(view2);
        dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        //Toast.makeText(homeActivity, loginModel.getData().getUser().getId()+"", Toast.LENGTH_SHORT).show();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
    }

    public void setClientData(Client client) {
        client_id = client.getClientIdFk();
        fragmentHomeBinding.etClientName.setText(client.getClientName());
        dialog2.dismiss();
    }
   /* public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }*/







}