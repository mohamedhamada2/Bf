package com.mz.bf.addbill;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.mz.bf.MainActivity;
import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addvisit.AddVisitActivity;
import com.mz.bf.addvisit.LocationTrack;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.data.DatabaseClass;
import com.mz.bf.databinding.FragmentBillsBinding;
import com.mz.bf.uis.activity_print_bill.PrintBillActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BillsFragment extends Fragment implements OnLocationUpdatedListener {
    FragmentBillsBinding fragmentBillsBinding;
    AddBillsViewModel addBillsViewModel;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date_picker_dialog;
    String main_branch_id,sub_branch_id,ware_houses_id,type_id,product_name,product_id,product_price,product_amount,discount,bonous,pay_id,client_id,bill_date,bill_num_dfter,client_name,bill_num2,user_id,car_id,product_discount,product_bounos,more_discount;
    List<SpinnerModel> main_branches_list,sub_branches_list,ware_houses_list;
    List<String> maintitlelist,subtitlelist,warehousestitlelist,typelist,paidlist;
    List<Product> productList;
    Dialog dialog2,dialog3;
    int PERMISSION_ID = 44;
    DatabaseClass databaseClass;
    Double price,total_price,price_after_discount;
    List<FatoraDetail> fatoraDetailList;
    BillsAdapter billsAdapter;
    RecyclerView.LayoutManager layoutManager;
    Double totalPrice,paid,remain,tvLatitude,tvLongitude;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    View view2;
    ClientAdapter clientAdapter;
    LinearLayoutManager layoutManager3,layoutManager2;
    ProductAdapter productAdapter;
    String value = "0";
    Double amount_available;
    LocationTrack locationTrack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBillsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_bills, container, false);
        View view = fragmentBillsBinding.getRoot();
        addBillsViewModel = new AddBillsViewModel(getContext(),this);
        fragmentBillsBinding.setAddbillsviewmodel(addBillsViewModel);
        locationTrack = new LocationTrack(getContext());
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(getActivity());
        user_id = loginModel.getId();
        car_id = loginModel.getCarNumber();
        //Toast.makeText(getActivity(), car_id, Toast.LENGTH_SHORT).show();
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(),DatabaseClass.class,"bills1").allowMainThreadQueries().build();
        maintitlelist = new ArrayList<>();
        subtitlelist = new ArrayList<>();
        warehousestitlelist = new ArrayList<>();
        typelist = new ArrayList<>();
        paidlist = new ArrayList<>();
        main_branch_id = "13";
        sub_branch_id = "16";
        ware_houses_id = "9";
        fragmentBillsBinding.etDiscount2.setText("0");
        if (checkPermissions()){
            if (isLocationEnabled()){
                if (locationTrack.canGetLocation()) {

                    LocationParams params = new LocationParams.Builder()
                            .setAccuracy(LocationAccuracy.HIGH)
                            .setDistance(1f)
                            .setInterval(5 * 1000)
                            .build();
                    SmartLocation smart = new SmartLocation.Builder(getContext()).logging(true).build();
                    smart.location().config(params).start(this);
                }else {
                    locationTrack.showSettingsAlert();
                }
            }else {
                Toast.makeText(getContext(), "الرجاء فتح تحديد الموقع الخاص بك", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        }else {
            requestPermissions();

        }


        /*if (fatoraDetailList.isEmpty()){
            fragmentBillsBinding.etPaid.setText("0");
            fragmentBillsBinding.etRemain2.setText("0");
        }else {
            fragmentBillsBinding.etPaid.setText("0");
            fragmentBillsBinding.etRemain2.setText(totalPrice+"");
        }*/
        //addBillsViewModel.get_main_branches();
        addBillsViewModel.getTypes();
        addBillsViewModel.getPayed();
        addBillsViewModel.getBillnum();
        myCalendar = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(new Date());
        fragmentBillsBinding.etBillDate.setText(bill_date);
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
        fragmentBillsBinding.etBillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date_picker_dialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fragmentBillsBinding.etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openclientpopup(user_id);
            }
        });
        /*fragmentBillsBinding.mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                main_branch_id = main_branches_list.get(i).getId();
                addBillsViewModel.get_sub_branches(main_branch_id);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentBillsBinding.subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sub_branch_id = sub_branches_list.get(i).getId();
                addBillsViewModel.getwarehouses(sub_branch_id);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fragmentBillsBinding.warehouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ware_houses_id = ware_houses_list.get(i).getId();
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.purple_500));
                    fragmentBillsBinding.etProductName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openpopup(ware_houses_id);
                        }
                    });
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
        });*/
        fragmentBillsBinding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (typelist.get(1).equals("قطاعي")){
                        type_id = "2";
                    }else if (typelist.get(0).equals("جملة")){
                        type_id ="1";
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
        fragmentBillsBinding.paidSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (paidlist.get(0).equals("اجل")){
                        pay_id = "1";
                    }else if (typelist.get(0).equals("كاش")){
                        pay_id ="2";
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
        fragmentBillsBinding.etPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    paid = 0.0;
                    remain = totalPrice;
                    fragmentBillsBinding.etRemain2.setText(remain+"");
                }else {
                    try {
                        paid = Double.parseDouble(charSequence.toString());
                        if (price_after_discount.equals(total_price)){
                            remain = totalPrice-paid;
                            fragmentBillsBinding.etRemain2.setText(remain+"");
                        }else {
                            remain = price_after_discount-paid;
                            fragmentBillsBinding.etRemain2.setText(remain+"");
                        }
                    }catch (Exception e){
                        fragmentBillsBinding.etPaid.setText("0");

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

            fragmentBillsBinding.etDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        value = fragmentBillsBinding.etDiscount.getText().toString();
                        price_after_discount = totalPrice-Double.parseDouble(value);
                        fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                        fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                        fragmentBillsBinding.etRemain2.setText(totalPrice+"");
                        fragmentBillsBinding.etPaid.setText("0");
                    }catch (Exception e){
                        value = "0.0";
                        price_after_discount = totalPrice-Double.parseDouble(value);
                        fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                        fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                        fragmentBillsBinding.etRemain2.setText(totalPrice+"");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        fragmentBillsBinding.etDiscount2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    if (totalPrice != 0.0) {
                        if (charSequence.toString().equals("")) {
                            totalPrice = Double.parseDouble(fragmentBillsBinding.etAllTotalPrice.getText().toString());
                            if (fragmentBillsBinding.etDiscount.getText().toString().equals("0")) {
                                price_after_discount = totalPrice;
                                fragmentBillsBinding.etAllTotalPrice.setText(totalPrice + "");
                                fragmentBillsBinding.etAfterDiscount.setText(price_after_discount + "");
                                fragmentBillsBinding.etRemain2.setText(totalPrice + "");
                                fragmentBillsBinding.etPaid.setText("0");
                            } else {
                                price_after_discount = totalPrice - totalPrice * Double.parseDouble(fragmentBillsBinding.etDiscount.getText().toString()) / 100;
                                fragmentBillsBinding.etAllTotalPrice.setText(totalPrice + "");
                                fragmentBillsBinding.etAfterDiscount.setText(price_after_discount + "");
                                fragmentBillsBinding.etRemain2.setText(price_after_discount + "");
                                fragmentBillsBinding.etPaid.setText("0");
                            }
                        } else {
                            fragmentBillsBinding.etPaid.setText("0");
                            price_after_discount = totalPrice - totalPrice * Double.parseDouble(fragmentBillsBinding.etDiscount.getText().toString()) / 100 - Double.parseDouble(String.valueOf(charSequence));
                            fragmentBillsBinding.etAllTotalPrice.setText(totalPrice + "");
                            fragmentBillsBinding.etAfterDiscount.setText(price_after_discount + "");
                            fragmentBillsBinding.etRemain2.setText(price_after_discount + "");
                            fragmentBillsBinding.etPaid.setText("0");
                        }
                    }
                } catch (Exception e) {
                    Log.e("exp2",e.getMessage());
                    fragmentBillsBinding.etDiscount.setText("0");
                    totalPrice = Double.parseDouble(fragmentBillsBinding.etAllTotalPrice.getText().toString());
                    price_after_discount = totalPrice;
                    fragmentBillsBinding.etAllTotalPrice.setText(totalPrice + "");
                    fragmentBillsBinding.etAfterDiscount.setText(price_after_discount + "");
                    fragmentBillsBinding.etRemain2.setText(totalPrice + "");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fragmentBillsBinding.txtProductInCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_products_popup();
            }
        });

        fragmentBillsBinding.etProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openpopup(ware_houses_id);
            }
        });
        return  view;
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    private void create_products_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view2 = inflater.inflate(R.layout.products_dialog, null);
        RecyclerView products_recycler = view2.findViewById(R.id.product_recycler);
        EditText et_search = view2.findViewById(R.id.et_search);
        et_search.setVisibility(View.GONE);
        TextView txt_no_data = view2.findViewById(R.id.txt_no_data);
        if (fatoraDetailList.isEmpty()){
            txt_no_data.setVisibility(View.VISIBLE);
            products_recycler.setVisibility(View.GONE);
        }else {
            txt_no_data.setVisibility(View.GONE);
            products_recycler.setVisibility(View.VISIBLE);
            products_recycler.setAdapter(billsAdapter);
            products_recycler.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            products_recycler.setLayoutManager(layoutManager);

        }
        //fatoraDetailList = databaseClass.getDao().getallbills();
        //billsAdapter = new BillsAdapter(fatoraDetailList,getContext(),this);

        ImageView cancel_img = view2.findViewById(R.id.cancel_img);
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

    private void validation() {

        client_name = fragmentBillsBinding.etClientName.getText().toString();
        bill_num2 = fragmentBillsBinding.etBillNum.getText().toString();
        bill_date = fragmentBillsBinding.etBillDate.getText().toString();
        if (fragmentBillsBinding.etDiscount2.getText().toString().equals("")){
            more_discount = "0";
        }else {
            more_discount = fragmentBillsBinding.etDiscount2.getText().toString();
        }
        if(!TextUtils.isEmpty(client_name)&&!TextUtils.isEmpty(bill_num2)&&!TextUtils.isEmpty(bill_date)&&!fatoraDetailList.isEmpty()){
            if (fragmentBillsBinding.etAfterDiscount.getText().equals("0")){
                addBillsViewModel.add_bill(user_id,fragmentBillsBinding.etBillNum.getText().toString(),bill_date,pay_id,"",client_id,main_branch_id,sub_branch_id,ware_houses_id,fragmentBillsBinding.etAllTotalPrice.getText().toString(),price_after_discount,"0",paid+"",remain+"","byan",fatoraDetailList,tvLatitude,tvLongitude,more_discount);
            }else {
                addBillsViewModel.add_bill(user_id,fragmentBillsBinding.etBillNum.getText().toString(),bill_date,pay_id,"",client_id,main_branch_id,sub_branch_id,ware_houses_id,fragmentBillsBinding.etAllTotalPrice.getText().toString(),price_after_discount,"0",paid+"",remain+"","byan",fatoraDetailList,tvLatitude,tvLongitude,more_discount);
            }

        }else {
            if (TextUtils.isEmpty(bill_num2)){
                Toast.makeText(getContext(), "تأكد من الاتصال بالانترنت لعرض رقم الفاتورة", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(client_name)){
                fragmentBillsBinding.etClientName.setError("ادخل اسم العميل");
                Toast.makeText(getContext(), "ادخل باقي البيانات", Toast.LENGTH_SHORT).show();
            }else {
                fragmentBillsBinding.etClientName.setError(null);
            }
            if (TextUtils.isEmpty(bill_date)){
                fragmentBillsBinding.etBillDate.setError("ادخل تاريخ الفاتورة");
                Toast.makeText(getContext(), "ادخل باقي البيانات", Toast.LENGTH_SHORT).show();
            }else {
                fragmentBillsBinding.etBillDate.setError(null);
            }
            if (fatoraDetailList.isEmpty()){
                Toast.makeText(getActivity(), "عفوا لا يوجد منتجات في الفاتورة", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.products_dialog, null);
        RecyclerView clients_recycler = view.findViewById(R.id.product_recycler);
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setHint("إسم العميل");
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,1);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        clientAdapter = new ClientAdapter(response.body().getClients(),getContext(),BillsFragment.this);
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
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        Call<ClientModel> call = getDataService.search_clients(user_id,charSequence.toString() ,1);
                        call.enqueue(new Callback<ClientModel>() {
                        @Override
                        public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                            if (response.isSuccessful()) {
                                clientAdapter = new ClientAdapter(response.body().getClients(), getContext(), BillsFragment.this);
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
                     GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<ClientModel> call = getDataService.get_clients(user_id, 1);
                    call.enqueue(new Callback<ClientModel>() {
                        @Override
                        public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                            if (response.isSuccessful()) {
                                clientAdapter = new ClientAdapter(response.body().getClients(), getContext(), BillsFragment.this);
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

    @Override
    public void onResume() {
        fatoraDetailList = databaseClass.getDao().getallbills("1");
        getAllBills(fatoraDetailList);
        if (checkPermissions()){
            if (isLocationEnabled()){
                if (locationTrack.canGetLocation()) {

                    LocationParams params = new LocationParams.Builder()
                            .setAccuracy(LocationAccuracy.HIGH)
                            .setDistance(1f)
                            .setInterval(5 * 1000)
                            .build();
                    SmartLocation smart = new SmartLocation.Builder(getContext()).logging(true).build();
                    smart.location().config(params).start(this);
                }else {
                    locationTrack.showSettingsAlert();
                }
            }else {
                Toast.makeText(getContext(), "الرجاء فتح تحديد الموقع الخاص بك", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        }else {
            requestPermissions();

        }
        super.onResume();
    }

    private void PerformClientPagination(Integer page, String user_id) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,page);
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
        fragmentBillsBinding.etBillDate.setText(bill_date);
    }


    private void openpopup(String ware_houses_id) {
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
        EditText et_search = view.findViewById(R.id.et_search);
        RecyclerView product_recycler = view.findViewById(R.id.product_recycler);
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_all_products(1,user_id,car_id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        productAdapter = new ProductAdapter(response.body().getProducts(),getActivity(),BillsFragment.this);
                        layoutManager2 = new LinearLayoutManager(getContext());
                        product_recycler.setLayoutManager(layoutManager2);
                        product_recycler.setAdapter(productAdapter);
                        product_recycler.setHasFixedSize(true);
                    }
                }
                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

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
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        Call<ProductModel> call = getDataService.search_product(1,car_id,charSequence.toString(),user_id);
                        call.enqueue(new Callback<ProductModel>() {
                            @Override
                            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                                if (response.isSuccessful()) {
                                    productAdapter = new ProductAdapter(response.body().getProducts(),getContext(),BillsFragment.this);
                                    layoutManager2 = new LinearLayoutManager(getContext());
                                    product_recycler.setLayoutManager(layoutManager2);
                                    product_recycler.setAdapter(productAdapter);
                                    product_recycler.setHasFixedSize(true);
                                }
                            }
                            @Override
                            public void onFailure(Call<ProductModel> call, Throwable t) {

                            }
                        });
                    }
                }else {
                    if (Utilities.isNetworkAvailable(getActivity())){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        Call<ProductModel> call = getDataService.get_all_products(1,user_id,car_id);
                        call.enqueue(new Callback<ProductModel>() {
                            @Override
                            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                                if (response.isSuccessful()) {
                                    productAdapter = new ProductAdapter(response.body().getProducts(),getContext(),BillsFragment.this);
                                    layoutManager2 = new LinearLayoutManager(getContext());
                                    product_recycler.setLayoutManager(layoutManager2);
                                    product_recycler.setAdapter(productAdapter);
                                    product_recycler.setHasFixedSize(true);
                                }
                            }
                            @Override
                            public void onFailure(Call<ProductModel> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        product_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount[0] = layoutManager2.getChildCount();
                totalitemcount[0] = layoutManager2.getItemCount();
                pastvisibleitem[0] = layoutManager2.findFirstVisibleItemPosition();
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
                        PerformProductPagination(page[0],ware_houses_id);
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

    private void PerformProductPagination(Integer page, String ware_houses_id) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_all_products(page,user_id,car_id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getProducts().isEmpty()){
                            productAdapter.add_product(response.body().getProducts());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

                }
            });
        }
    }

    public void setmainsspinnerData(List<String> maintitlelist, List<SpinnerModel> main_list) {
        this.maintitlelist = maintitlelist;
        main_branches_list = main_list;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item2,maintitlelist);
        fragmentBillsBinding.mainSpinner.setAdapter(arrayAdapter);
    }

    public void setsubspinnerData(List<String> subtitlelist, List<SpinnerModel> sub_list) {
        this.subtitlelist = subtitlelist;
        sub_branches_list = sub_list;
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item2,subtitlelist);
            fragmentBillsBinding.subSpinner.setAdapter(arrayAdapter);
        }catch (Exception e){

        }

    }

    public void setwarehousesspinnerData(List<String> warehousestitlelist, List<SpinnerModel> ware_houses_list) {
        this.warehousestitlelist = warehousestitlelist;
        this.ware_houses_list = ware_houses_list;
        try {
            ArrayAdapter<String> arrayAdapter = new  ArrayAdapter<String>(getActivity(),R.layout.spinner_item2,warehousestitlelist);
            fragmentBillsBinding.warehouseSpinner.setAdapter(arrayAdapter);
        }catch (Exception e){
        }
    }

    public void setData(Product product) {
        product_id = product.getId();
        product_name = product.getProductName();
        product_price="";
        fragmentBillsBinding.typeSpinner.setSelection(0);
        type_id = "1";
        product_amount ="1";
        product_discount = "0";
        product_bounos = "0";
        product_price = product.getPacketSellPrice();
        amount_available = product.getPacketRasied();
        price = Double.parseDouble(product_price);
        total_price = price*Double.parseDouble(product_amount);
        fragmentBillsBinding.etProductPrice.setText(product_price);
        fragmentBillsBinding.etTotalPrice.setText(total_price+"");
        fragmentBillsBinding.etProductAmout.setText(product_amount);

        fragmentBillsBinding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (product != null){
                        if (fragmentBillsBinding.typeSpinner.getSelectedItemPosition()==0){
                            type_id = "1";
                            amount_available = product.getPacketRasied();
                            product_price = product.getPacketSellPrice();
                            product_amount = fragmentBillsBinding.etProductAmout.getText().toString();
                            product_discount = fragmentBillsBinding.etProductDiscount.getText().toString();
                            product_bounos = fragmentBillsBinding.etBounos.getText().toString();
                            price = Double.parseDouble(product_price);
                            if (TextUtils.isEmpty(product_discount)||product_discount.equals("0")){
                                total_price = price*Double.parseDouble(product_amount);
                            }else {
                                total_price = price*Double.parseDouble(product_amount)-(price*Double.parseDouble(product_amount)*(Integer.parseInt(product_discount)/100));
                            }
                            fragmentBillsBinding.etProductPrice.setText(product_price);
                            fragmentBillsBinding.etTotalPrice.setText(total_price+"");

                        }else if (fragmentBillsBinding.typeSpinner.getSelectedItemPosition()==1){
                            type_id ="2";
                            amount_available = product.getOneRasied();
                            product_price = product.getOneSellPrice();
                            product_amount = fragmentBillsBinding.etProductAmout.getText().toString();
                            product_bounos = fragmentBillsBinding.etBounos.getText().toString();
                            product_discount = fragmentBillsBinding.etProductDiscount.getText().toString();
                            price = Double.parseDouble(product_price);
                            if (TextUtils.isEmpty(product_discount)||product_discount.equals("0")){
                                total_price = price*Double.parseDouble(product_amount);
                            }else {
                                total_price = price*Double.parseDouble(product_amount)-(price*Double.parseDouble(product_amount)*Integer.parseInt(product_discount)/100);
                            }
                            fragmentBillsBinding.etProductPrice.setText(product_price);
                            fragmentBillsBinding.etTotalPrice.setText(total_price+"");

                        }
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
        if (TextUtils.isEmpty(product_amount)){
            //product_amount = "0";
        }
        if (TextUtils.isEmpty(discount)){
            //discount = "0";
        }
        if (TextUtils.isEmpty(bonous)){
            //bonous = "0";
        }
        fragmentBillsBinding.etProductName.setText(product.getProductName());
        fragmentBillsBinding.etProductPrice.setVisibility(View.VISIBLE);
        //fragmentBillsBinding.etProductPrice.setText(product_price);
        //fragmentBillsBinding.etDiscount.setText(discount);
        //fragmentBillsBinding.etProductAmout.setText(product_amount);
        fragmentBillsBinding.etProductAmout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    product_amount ="0";
                    total_price = price*Double.parseDouble(product_amount);
                    fragmentBillsBinding.etTotalPrice.setText(total_price+"");
                }else {
                    product_amount = charSequence.toString();
                    total_price = price*Double.parseDouble(product_amount);
                    fragmentBillsBinding.etTotalPrice.setText(total_price+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fragmentBillsBinding.etProductDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    product_discount ="0";
                    total_price = price*Double.parseDouble(product_amount);
                    fragmentBillsBinding.etTotalPrice.setText(total_price+"");
                }else {
                    product_discount = charSequence.toString();
                    total_price = price*Double.parseDouble(product_amount)-price*Double.parseDouble(product_amount)*Integer.parseInt(product_discount)/100;
                    fragmentBillsBinding.etTotalPrice.setText(total_price+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        if (TextUtils.isEmpty(product_amount)){
            total_price = 0.0;
            fragmentBillsBinding.etTotalPrice.setText(total_price+"");
        }else {
            total_price = price*Double.parseDouble(product_amount);
            fragmentBillsBinding.etTotalPrice.setText(total_price+"");
        }
        fragmentBillsBinding.btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(product_name)&&!TextUtils.isEmpty(product_amount)&&!TextUtils.isEmpty(product_price)&& amount_available >= Double.parseDouble(fragmentBillsBinding.etProductAmout.getText().toString())) {
                    add_product();
                }else if( amount_available<Double.parseDouble(fragmentBillsBinding.etProductAmout.getText().toString())){
                    fragmentBillsBinding.etProductAmout.setError("عفوا !الكمية المتاحة: "+amount_available+"");
                    fragmentBillsBinding.etProductAmout.setText(amount_available+"");
                }else {
                    Toast.makeText(getContext(), "أكمل باقي البيانات", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog2.dismiss();
    }

    private void add_product() {
        try {

            FatoraDetail fatoraDetail = new FatoraDetail();
            fatoraDetail.setProduct_id_fk(product_id);
            fatoraDetail.setProduct_name(product_name);
            fatoraDetail.setType(type_id);
            fatoraDetail.setAmount(product_amount);
            fatoraDetail.setSell_price(product_price);
            fatoraDetail.setProduct_discount(product_discount);
            fatoraDetail.setProduct_pouns(product_bounos);
            fatoraDetail.setNotes("");
            fatoraDetail.setFatora_type("1");
            fatoraDetail.setTotal(total_price+"");
            databaseClass.getDao().Addbill(fatoraDetail);
            Toast.makeText(getContext(), "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
            fragmentBillsBinding.etProductName.setText("");
            fragmentBillsBinding.etProductAmout.setText("");
            fragmentBillsBinding.etProductPrice.setText("");
            fragmentBillsBinding.etTotalPrice.setText("");
            getAllBills(fatoraDetailList);
        }catch (Exception e){
            FatoraDetail fatoraDetail = new FatoraDetail();
            fatoraDetail.setProduct_id_fk(product_id);
            fatoraDetail.setProduct_name(product_name);
            fatoraDetail.setType(type_id);
            fatoraDetail.setAmount(product_amount);
            fatoraDetail.setSell_price(product_price);
            fatoraDetail.setProduct_discount("0");
            fatoraDetail.setProduct_pouns("0");
            fatoraDetail.setNotes("");
            fatoraDetail.setFatora_type("1");
            fatoraDetail.setTotal(total_price+"");
            databaseClass.getDao().editproduct(fatoraDetail);
            //fragmentBillsBinding.txtProductInCart.setText(fatoraDetailList.size()+"");
            Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
            fragmentBillsBinding.etProductName.setText("");
            fragmentBillsBinding.etProductAmout.setText("");
            fragmentBillsBinding.etProductPrice.setText("");
            fragmentBillsBinding.etTotalPrice.setText("");

            getAllBills(fatoraDetailList);
        }
    }

    public void getAllBills(List<FatoraDetail> fatoraDetailList) {
        this.fatoraDetailList = databaseClass.getDao().getallbills("1");
        Log.e("get_all_bills","success");
        fragmentBillsBinding.txtProductInCart.setText(this.fatoraDetailList.size()+"");
        billsAdapter = new BillsAdapter(this.fatoraDetailList,getContext(),this);
        layoutManager = new LinearLayoutManager(getContext());
        fragmentBillsBinding.billsRecycler.setAdapter(billsAdapter);
        fragmentBillsBinding.billsRecycler.setLayoutManager(layoutManager);
        fragmentBillsBinding.billsRecycler.setHasFixedSize(true);
        getTotalPrice();
    }
    private Double getTotalPrice() {
        totalPrice =0.0;
            for(int i = 0 ; i < fatoraDetailList.size(); i++) {
                totalPrice += Double.parseDouble(fatoraDetailList.get(i).getTotal());
            }
        try {
            if (value.equals("0")) {
                if (fragmentBillsBinding.etDiscount2.getText().toString().equals("")){
                    fragmentBillsBinding.etDiscount.setText("0");
                    fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                    //totalPrice = Double.parseDouble(fragmentBillsBinding.etAllTotalPrice.getText().toString());
                    price_after_discount = totalPrice;
                    fragmentBillsBinding.etPaid.setText("0");
                    fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentBillsBinding.etRemain2.setText(totalPrice+"");
                }else {
                    fragmentBillsBinding.etDiscount.setText(value);
                    price_after_discount = totalPrice- Double.parseDouble(fragmentBillsBinding.etDiscount2.getText().toString());
                    fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentBillsBinding.etRemain2.setText(price_after_discount+"");
                    fragmentBillsBinding.etPaid.setText("0");
                }
                //Toast.makeText(getActivity(), totalPrice+"", Toast.LENGTH_SHORT).show();

            }else {
                if (fragmentBillsBinding.etDiscount2.getText().toString().equals("")){
                    fragmentBillsBinding.etDiscount.setText(value);
                    price_after_discount = totalPrice-totalPrice*Double.parseDouble(value)/100;
                    fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentBillsBinding.etRemain2.setText(price_after_discount+"");
                    fragmentBillsBinding.etPaid.setText("0");
                }else {
                    fragmentBillsBinding.etDiscount.setText(value);
                    price_after_discount = totalPrice-totalPrice*Double.parseDouble(value)/100- Double.parseDouble(fragmentBillsBinding.etDiscount2.getText().toString());
                    fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentBillsBinding.etRemain2.setText(price_after_discount+"");
                    fragmentBillsBinding.etPaid.setText("0");
                }

            }
        }catch (Exception e){
            fragmentBillsBinding.etDiscount.setText("0");
            //totalPrice = Double.parseDouble(fragmentBillsBinding.etAllTotalPrice.getText().toString());
            price_after_discount = totalPrice;
            fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
            fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
            fragmentBillsBinding.etRemain2.setText(totalPrice+"");
        }
        return totalPrice;
    }

    public void setypespinner(List<String> typelist) {
        this.typelist = typelist;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,this.typelist);
        fragmentBillsBinding.typeSpinner.setAdapter(arrayAdapter);
    }

    public void sepayedspinner(List<String> paid_list) {
        paidlist = paid_list;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,this.paidlist);
        fragmentBillsBinding.paidSpinner.setAdapter(arrayAdapter);
    }

    public void get_bill_num(Integer bill_num) {
        fragmentBillsBinding.etBillNum.setText(bill_num+"");
    }

    public void setClientData(Client client) {
        client_id = client.getClientIdFk();
        fragmentBillsBinding.etClientName.setText(client.getClientName());
        //addBillsViewModel.get_client_discount(client_id);
        dialog2.dismiss();
    }

    public void DeleteProducts(String fatora_id) {
        databaseClass.getDao().deleteAllproduct();
        getAllBills(fatoraDetailList);
        Toast.makeText(getActivity(), "تم إضافة الفاتورة بنجاح", Toast.LENGTH_SHORT).show();
        /*getActivity().finish();
        startActivity(getActivity().getIntent());*/
        Intent intent = new Intent(getActivity(), PrintBillActivity.class);
        intent.putExtra("flag",1);
        intent.putExtra("id",fatora_id);
        startActivity(intent);
    }

    public void delete_product(FatoraDetail fatoraDetail) {
        try {
            databaseClass.getDao().DeleteProduct(Integer.parseInt(fatoraDetail.getProduct_id_fk()));
            getAllBills(fatoraDetailList);
            dialog3.dismiss();
            create_products_popup();
        }catch (Exception e){
            databaseClass.getDao().DeleteProduct(Integer.parseInt(fatoraDetail.getProduct_id_fk()));
            getAllBills(fatoraDetailList);
        }

    }

    public void edit_bill(FatoraDetail fatoraDetail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.edit_bill_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        EditText et_product_name = view.findViewById(R.id.et_product_name);
        EditText et_product_amount =view.findViewById(R.id.et_product_amout);
        EditText et_product_price = view.findViewById(R.id.et_product_price);
        EditText et_product_total_price = view.findViewById(R.id.et_total_price);
        Button btn_add_bill= view.findViewById(R.id.btn_add_bill);
        EditText type_spinner = view.findViewById(R.id.type_spinner);
        product_id = fatoraDetail.getProduct_id_fk();
        if (fatoraDetail.getType().equals("1")){
         type_spinner.setText("جملة");
        }else if (fatoraDetail.getType().equals("2")){
            type_spinner.setText("قطاعي");
        }
        total_price = Double.parseDouble(fatoraDetail.getTotal());
        product_price = fatoraDetail.getSell_price();
        product_amount = fatoraDetail.getAmount();
        discount = fatoraDetail.getProduct_discount();
        product_name = fatoraDetail.getProduct_name();
        price = Double.parseDouble(product_price);
        //Toast.makeText(getActivity(), product_name, Toast.LENGTH_SHORT).show();
        et_product_name.setText(product_name);
        et_product_price.setText(product_price);
        et_product_amount.setText(product_amount);
        et_product_total_price.setText(total_price+"");
        try {
            dialog3.dismiss();
        }catch (Exception e){

        }
        //Toast.makeText(getActivity(),total_price+"",Toast.LENGTH_SHORT).show();

        et_product_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    product_amount ="0";
                    total_price = price*Double.parseDouble(product_amount);
                    et_product_total_price.setText(total_price+"");
                }else {
                    product_amount = charSequence.toString();
                    total_price = price*Double.parseDouble(product_amount);
                    et_product_total_price.setText(total_price+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*if (TextUtils.isEmpty(product_amount)){
            total_price = 0.0;
            et_product_total_price.setText(total_price+"");
        }else {
            //try {
                total_price = price*Double.parseDouble(product_amount);
                et_product_total_price.setText(total_price+"");
            //}catch (Exception e){

            //}
        }*/

        builder.setView(view);
        Dialog dialog2 = builder.create();
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
        btn_add_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( amount_available<Double.parseDouble(fragmentBillsBinding.etProductAmout.getText().toString())) {
                    et_product_amount.setError("عفوا !الكمية المتاحة: " + amount_available + "");
                    et_product_amount.setText(amount_available + "");
                }else {
                    FatoraDetail fatoraDetail = new FatoraDetail();
                    fatoraDetail.setProduct_id_fk(product_id);
                    fatoraDetail.setProduct_name(product_name);
                    fatoraDetail.setType(type_id);
                    fatoraDetail.setAmount(et_product_amount.getText().toString());
                    fatoraDetail.setSell_price(et_product_price.getText().toString());
                    fatoraDetail.setProduct_discount(product_discount);
                    fatoraDetail.setProduct_pouns(product_bounos);
                    fatoraDetail.setFatora_type("1");
                    fatoraDetail.setNotes("");
                    fatoraDetail.setTotal(et_product_total_price.getText().toString());
                    databaseClass.getDao().DeleteProduct(Integer.parseInt(product_id));
                    databaseClass.getDao().Addbill(fatoraDetail);
                    //fragmentBillsBinding.txtProductInCart.setText(fatoraDetailList.size()+"");
                    Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                    dialog2.dismiss();
                    getAllBills(fatoraDetailList);
                }
            }
        });
    }

    public void add_client_discount(String value) {
        fragmentBillsBinding.etPaid.setText("");
        this.value = value;
        if (totalPrice != 0.0){
            try {
                if (value.equals("0")) {
                    fragmentBillsBinding.etDiscount.setText("0");
                    totalPrice = Double.parseDouble(fragmentBillsBinding.etAllTotalPrice.getText().toString());
                    price_after_discount = totalPrice;
                    fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentBillsBinding.etRemain2.setText(totalPrice+"");
                    fragmentBillsBinding.etPaid.setText("0");
                }else {
                    fragmentBillsBinding.etPaid.setText("0");
                    fragmentBillsBinding.etDiscount.setText(value);
                    if (fragmentBillsBinding.etDiscount2.getText().toString().equals("")){
                        price_after_discount = totalPrice-totalPrice*Double.parseDouble(value)/100;
                    }else {
                        price_after_discount = totalPrice-totalPrice*Double.parseDouble(value)/100- Double.parseDouble(fragmentBillsBinding.etDiscount2.getText().toString());
                    }
                    fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentBillsBinding.etRemain2.setText(price_after_discount+"");
                }
            }catch (Exception e){
                fragmentBillsBinding.etDiscount.setText("0");
                totalPrice = Double.parseDouble(fragmentBillsBinding.etAllTotalPrice.getText().toString());
                price_after_discount = totalPrice;
                fragmentBillsBinding.etAllTotalPrice.setText(totalPrice+"");
                fragmentBillsBinding.etAfterDiscount.setText(price_after_discount+"");
                fragmentBillsBinding.etRemain2.setText(totalPrice+"");
            }
        }else {
            Toast.makeText(getActivity(), "لا يوجد فواتير لعرض نسبة الخصم", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        tvLatitude = location.getLatitude();
        tvLongitude = location.getLongitude();
        fragmentBillsBinding.btnAddBill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);

    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}