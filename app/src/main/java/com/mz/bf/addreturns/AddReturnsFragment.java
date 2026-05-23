package com.mz.bf.addreturns;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.BillsFragment;
import com.mz.bf.addbill.Client;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.addbill.FatoraDetail;
import com.mz.bf.addbill.Product;
import com.mz.bf.addbill.ProductModel;
import com.mz.bf.addbill.SpinnerModel;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.data.DatabaseClass;
import com.mz.bf.databinding.FragmentAddReturnsBinding;
import com.mz.bf.returns.ReturnsFragment;
import com.mz.bf.uis.activity_print_bill.PrintBillActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddReturnsFragment extends Fragment {
    FragmentAddReturnsBinding fragmentAddReturnsBinding;
    AddReturnsViewModel addReturnsViewModel;
    Calendar myCalendar;
    DecimalFormat df;
    DatePickerDialog.OnDateSetListener date_picker_dialog;
    String main_branch_id="0",sub_branch_id="0",ware_houses_id="0",type_id,product_name,product_id,product_price,product_amount,discount="0",bonous="0",pay_id,client_id,bill_date,client_name,bill_num2,user_id,value="0",car_id;
    List<SpinnerModel> main_branches_list,sub_branches_list,ware_houses_list;
    List<String> maintitlelist,subtitlelist,warehousestitlelist,typelist,paidlist;
    List<Product> productList;
    Dialog dialog2,dialog3;
    DatabaseClass databaseClass;
    Double price,total_price,nesba_discount;
    List<FatoraDetail> fatoraDetailList;
    BillsAdapter billsAdapter;
    RecyclerView.LayoutManager layoutManager;
    Double totalPrice,paid,remain,price_after_discount;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    View view2;
    ClientAdapter clientAdapter;
    LinearLayoutManager layoutManager3,layoutManager2;
    ProductAdapter productAdapter;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAddReturnsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_returns, container, false);
        addReturnsViewModel = new AddReturnsViewModel(getContext(),this);
        fragmentAddReturnsBinding.setAddreturnsviewmodel(addReturnsViewModel);
        View view = fragmentAddReturnsBinding.getRoot();
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(getActivity());
        user_id = loginModel.getId();
        car_id = loginModel.getCarNumber();
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(),DatabaseClass.class,"bills1").allowMainThreadQueries().build();
        maintitlelist = new ArrayList<>();
        subtitlelist = new ArrayList<>();
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(getActivity()) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(getActivity()).getRecords().getUrl();
        }
        warehousestitlelist = new ArrayList<>();
        typelist = new ArrayList<>();
        paidlist = new ArrayList<>();
        type_id ="2";
        df = new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US));
        //addReturnsViewModel.get_main_branches();
        addReturnsViewModel.getTypes();
        addReturnsViewModel.getPayed();
        addReturnsViewModel.getBillnum();
        myCalendar = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        bill_date = sdf.format(new Date());
        fragmentAddReturnsBinding.etBillDate.setText(bill_date);
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
        fragmentAddReturnsBinding.etBillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date_picker_dialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fragmentAddReturnsBinding.etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openclientpopup(user_id);
            }
        });
        fragmentAddReturnsBinding.etProductName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openpopup(ware_houses_id);
                        }
        });
        fragmentAddReturnsBinding.paidSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        fragmentAddReturnsBinding.etPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    paid = 0.0;
                    remain = totalPrice;
                    fragmentAddReturnsBinding.etRemain2.setText(remain+"");
                }else {
                    paid = Double.parseDouble(charSequence.toString());
                    remain = totalPrice-paid;
                    fragmentAddReturnsBinding.etRemain2.setText(remain+"");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fragmentAddReturnsBinding.txtProductInCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_products_popup();
            }
        });
        fragmentAddReturnsBinding.btnAddBill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        return  view;
    }

    @Override
    public void onResume() {
        getAllBills();
        super.onResume();
    }

    private void create_products_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view2 = inflater.inflate(R.layout.products_dialog, null);
        RecyclerView products_recycler = view2.findViewById(R.id.product_recycler);
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
        client_name = fragmentAddReturnsBinding.etClientName.getText().toString();
        bill_num2 = fragmentAddReturnsBinding.etBillNum.getText().toString();
        bill_date = fragmentAddReturnsBinding.etBillDate.getText().toString();
        fatoraDetailList = databaseClass.getDao().getallbills("2");
        if(!TextUtils.isEmpty(client_name)&&!TextUtils.isEmpty(bill_num2)&&!TextUtils.isEmpty(bill_date)&&!fatoraDetailList.isEmpty()){
            if (fragmentAddReturnsBinding.etAfterDiscount.getText().equals("0")){
                Toast.makeText(getActivity(), "تم", Toast.LENGTH_SHORT).show();
                addReturnsViewModel.add_bill(user_id,fragmentAddReturnsBinding.etBillNum.getText().toString(),bill_date,pay_id,"",client_id,main_branch_id,sub_branch_id,ware_houses_id,df.format(Double.parseDouble(fragmentAddReturnsBinding.etAllTotalPrice.getText().toString())),df.format(price_after_discount),"0",paid+"",remain+"","byan",fatoraDetailList);
            }else {
                Toast.makeText(getActivity(), "تم", Toast.LENGTH_SHORT).show();
                addReturnsViewModel.add_bill(user_id,fragmentAddReturnsBinding.etBillNum.getText().toString(),bill_date,pay_id,"",client_id,main_branch_id,sub_branch_id,ware_houses_id,df.format(Double.parseDouble(fragmentAddReturnsBinding.etAllTotalPrice.getText().toString())),df.format(price_after_discount),"0",paid+"",remain+"","byan",fatoraDetailList);
            }
        }else {
            if (TextUtils.isEmpty(bill_num2)){
                Toast.makeText(getContext(), "تأكد من الاتصال بالانترنت لعرض رقم الفاتورة", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(client_name)){
                fragmentAddReturnsBinding.etClientName.setError("ادخل اسم العميل");
                Toast.makeText(getContext(), "ادخل باقي البيانات", Toast.LENGTH_SHORT).show();
            }else {
                fragmentAddReturnsBinding.etClientName.setError(null);
            }
            if (TextUtils.isEmpty(bill_date)){
                fragmentAddReturnsBinding.etBillDate.setError("ادخل تاريخ الفاتورة");
                Toast.makeText(getContext(), "ادخل باقي البيانات", Toast.LENGTH_SHORT).show();
            }else {
                fragmentAddReturnsBinding.etBillDate.setError(null);
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
            ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
            Call<ClientModel> call = getDataService.get_clients(user_id,1);
            call.enqueue(new Callback<ClientModel>() {
                @Override
                public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                    if (response.isSuccessful()) {
                        pd.dismiss();
                        clientAdapter = new ClientAdapter(response.body().getClients(),getContext(), AddReturnsFragment.this);
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
                                    clientAdapter = new ClientAdapter(response.body().getClients(), getContext(), AddReturnsFragment.this);
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
                                    clientAdapter = new ClientAdapter(response.body().getClients(), getContext(), AddReturnsFragment.this);
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

    private void PerformClientPagination(Integer page, String user_id) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
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
        fragmentAddReturnsBinding.etBillDate.setText(bill_date);
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
        CardView cardView = view.findViewById(R.id.card_view);
        RecyclerView product_recycler = view.findViewById(R.id.product_recycler);
        if (Utilities.isNetworkAvailable(getActivity())){
            ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_all_products(1,user_id,car_id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        pd.dismiss();
                        productAdapter = new ProductAdapter(response.body().getProducts(),getContext(),AddReturnsFragment.this);
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
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = et_search.getText().toString();
                if (!TextUtils.isEmpty(search)){
                    if (Utilities.isNetworkAvailable(getActivity())) {
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
                        Call<ProductModel> call = getDataService.search_product(1, car_id, search, user_id);
                        call.enqueue(new Callback<ProductModel>() {
                            @Override
                            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                                if (response.isSuccessful()) {
                                    productAdapter = new ProductAdapter(response.body().getProducts(), getContext(), AddReturnsFragment.this);
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
                    } else {
                        if (Utilities.isNetworkAvailable(getActivity())) {
                            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
                            Call<ProductModel> call = getDataService.get_all_products(1, user_id, car_id);
                            call.enqueue(new Callback<ProductModel>() {
                                @Override
                                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                                    if (response.isSuccessful()) {
                                        productAdapter = new ProductAdapter(response.body().getProducts(), getContext(), AddReturnsFragment.this);
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
        });
        /*et_search.addTextChangedListener(new TextWatcher() {
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
                                    productAdapter = new ProductAdapter(response.body().getProducts(),getContext(), AddReturnsFragment.this);
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
                                    productAdapter = new ProductAdapter(response.body().getProducts(),getContext(),AddReturnsFragment.this);
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
        });*/
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
                        PerformProductPagination(page[0],user_id,car_id);
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

    private void PerformProductPagination(Integer page, String user_id,String car_id) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(getActivity(),base_url).create(GetDataService.class);
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
    public void setData(Product product) {
        product_id = product.getId();
        product_name = product.getProductCode();
        fragmentAddReturnsBinding.typeSpinner.setSelection(0);
        type_id = "2";
        product_amount ="1";
        product_price = product.getOneSellPrice();
        price = Double.parseDouble(product_price);
        total_price = price*Double.parseDouble(product_amount);
        fragmentAddReturnsBinding.etProductPrice.setText(product_price);
        fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");
        fragmentAddReturnsBinding.etProductAmout.setText(product_amount);
        fragmentAddReturnsBinding.etProductPrice.setText(product_price);
        fragmentAddReturnsBinding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (product != null){
                        if (fragmentAddReturnsBinding.typeSpinner.getSelectedItemPosition()==0){
                            /*type_id = "1";
                            product_price = product.getPacketSellPrice();
                            product_amount = fragmentAddReturnsBinding.etProductAmout.getText().toString();
                            price = Double.parseDouble(product_price);
                            total_price = price*Double.parseDouble(product_amount);
                            fragmentAddReturnsBinding.etProductPrice.setText(product_price);
                            fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");*/
                            type_id ="2";
                            product_price = product.getOneSellPrice();
                            product_amount = fragmentAddReturnsBinding.etProductAmout.getText().toString();
                            price = Double.parseDouble(product_price);
                            total_price = price * Double.parseDouble(product_amount+"");
                            fragmentAddReturnsBinding.etProductPrice.setText(product_price);
                            fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");
                        }/*else if (fragmentAddReturnsBinding.typeSpinner.getSelectedItemPosition()==1){
                            type_id ="2";
                            product_price = product.getOneSellPrice();
                            product_amount = fragmentAddReturnsBinding.etProductAmout.getText().toString();
                            price = Double.parseDouble(product_price);
                            total_price = price * Double.parseDouble(product_amount+"");
                            fragmentAddReturnsBinding.etProductPrice.setText(product_price);
                            fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");
                        }*/
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
        discount = "0";
        bonous = "0";
        if (TextUtils.isEmpty(product_amount)){
            //product_amount = "0";
        }
        if (TextUtils.isEmpty(discount)){
            //discount = "0";
        }
        if (TextUtils.isEmpty(bonous)){
            //bonous = "0";
        }
        fragmentAddReturnsBinding.etProductName.setText(product.getProductName());
        fragmentAddReturnsBinding.etProductPrice.setVisibility(View.VISIBLE);
        //fragmentBillsBinding.etDiscount.setText(discount);
        //fragmentBillsBinding.etProductAmout.setText(product_amount);
        fragmentAddReturnsBinding.etProductAmout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    product_amount ="0";
                    total_price = price*Double.parseDouble(product_amount);
                    fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");
                }else {
                    product_amount = charSequence.toString();
                    total_price = price*Double.parseDouble(product_amount);
                    fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (TextUtils.isEmpty(product_amount)){
            total_price = 0.0;
            fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");
        }else {
            total_price = price*Double.parseDouble(product_amount);
            fragmentAddReturnsBinding.etTotalPrice.setText(total_price+"");
        }
        fragmentAddReturnsBinding.btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(product_name)&&!TextUtils.isEmpty(product_amount)&&!TextUtils.isEmpty(product_price)){
                    add_product();
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
            fatoraDetail.setProduct_discount(discount);
            fatoraDetail.setProduct_pouns(bonous);
            fatoraDetail.setNotes("");
            fatoraDetail.setTotal(total_price+"");
            fatoraDetail.setFatora_type("2");
            databaseClass.getDao().Addbill(fatoraDetail);
            Toast.makeText(getContext(), "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
            fragmentAddReturnsBinding.etProductName.setText("");
            fragmentAddReturnsBinding.etProductAmout.setText("");
            fragmentAddReturnsBinding.etProductPrice.setText("");
            fragmentAddReturnsBinding.etTotalPrice.setText("");
            getAllBills();
        }catch (Exception e){
            FatoraDetail fatoraDetail = new FatoraDetail();
            fatoraDetail.setProduct_id_fk(product_id);
            fatoraDetail.setProduct_name(product_name);
            fatoraDetail.setType(type_id);
            fatoraDetail.setAmount(product_amount);
            fatoraDetail.setSell_price(product_price);
            fatoraDetail.setProduct_discount(discount);
            fatoraDetail.setProduct_pouns(bonous);
            fatoraDetail.setNotes("");
            fatoraDetail.setFatora_type("2");
            fatoraDetail.setTotal(total_price+"");
            databaseClass.getDao().editproduct(fatoraDetail);
            //fragmentBillsBinding.txtProductInCart.setText(fatoraDetailList.size()+"");
            Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
            fragmentAddReturnsBinding.etProductName.setText("");
            fragmentAddReturnsBinding.etProductAmout.setText("");
            fragmentAddReturnsBinding.etProductPrice.setText("");
            fragmentAddReturnsBinding.etTotalPrice.setText("");
            getAllBills();
        }
    }

    public void getAllBills() {
        fatoraDetailList = databaseClass.getDao().getallbills("2");
        fragmentAddReturnsBinding.txtProductInCart.setText(fatoraDetailList.size()+"");
        billsAdapter = new BillsAdapter(fatoraDetailList,getContext(),this);
        layoutManager = new LinearLayoutManager(getContext());
        fragmentAddReturnsBinding.billsRecycler.setAdapter(billsAdapter);
        fragmentAddReturnsBinding.billsRecycler.setLayoutManager(layoutManager);
        fragmentAddReturnsBinding.billsRecycler.setHasFixedSize(true);
        getTotalPrice();
    }
    private void getTotalPrice() {
        totalPrice =0.0;
        if(fatoraDetailList.isEmpty()){
            totalPrice=0.0;
            Log.e("total",totalPrice+"");
            fragmentAddReturnsBinding.etDiscount.setText("0");
            fragmentAddReturnsBinding.etAllTotalPrice.setText("0");
            //totalPrice = Double.parseDouble(fragmentAddReturnsBinding.etAllTotalPrice.getText().toString());
            price_after_discount = totalPrice;
            //fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
            fragmentAddReturnsBinding.etAfterDiscount.setText("0");
            fragmentAddReturnsBinding.etRemain2.setText("0");
        }else {
            for(int i = 0 ; i < fatoraDetailList.size(); i++) {
                totalPrice += Double.parseDouble(fatoraDetailList.get(i).getTotal());

            }
            try {
                if (value.equals("0")) {
                    Log.e("total",totalPrice+"");
                   // Toast.makeText(getActivity(), totalPrice+"", Toast.LENGTH_SHORT).show();
                    fragmentAddReturnsBinding.etDiscount.setText("0");
                    fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
                    //totalPrice = Double.parseDouble(fragmentAddReturnsBinding.etAllTotalPrice.getText().toString());
                    price_after_discount = totalPrice;
                    //fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentAddReturnsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentAddReturnsBinding.etRemain2.setText(totalPrice+"");
                }else {
                    fragmentAddReturnsBinding.etDiscount.setText(value);
                    price_after_discount = totalPrice-totalPrice*Double.parseDouble(value)/100;
                    fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentAddReturnsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentAddReturnsBinding.etRemain2.setText(price_after_discount+"");
                }
            }catch (Exception e){
                fragmentAddReturnsBinding.etDiscount.setText("0");
                //totalPrice = Double.parseDouble(fragmentBillsBinding.etAllTotalPrice.getText().toString());
                price_after_discount = totalPrice;
                fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
                fragmentAddReturnsBinding.etAfterDiscount.setText(price_after_discount+"");
                fragmentAddReturnsBinding.etRemain2.setText(totalPrice+"");
            }
        }
    }

    public void setypespinner(List<String> typelist) {
        this.typelist = typelist;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item2,this.typelist);
        fragmentAddReturnsBinding.typeSpinner.setAdapter(arrayAdapter);
    }

    public void sepayedspinner(List<String> paid_list) {
        paidlist = paid_list;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item2,this.paidlist);
        fragmentAddReturnsBinding.paidSpinner.setAdapter(arrayAdapter);
    }

    public void get_bill_num(Integer bill_num) {
        fragmentAddReturnsBinding.etBillNum.setText(bill_num+"");
    }

    public void setClientData(Client client) {
        client_id = client.getClientIdFk();
        addReturnsViewModel.get_client_discount(client_id);
        fragmentAddReturnsBinding.etClientName.setText(client.getClientName());
        dialog2.dismiss();
    }

    public void DeleteProducts(String fatora_id) {
        databaseClass.getDao().deleteAllproduct();
        getAllBills();
        Toast.makeText(getActivity(), "تم إضافة المرتجع بنجاح", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), PrintBillActivity.class);
        intent.putExtra("flag",2);
        intent.putExtra("id",fatora_id);
        startActivity(intent);
    }

    public void delete_product(FatoraDetail fatoraDetail) {
        try {
            databaseClass.getDao().DeleteProduct(Integer.parseInt(fatoraDetail.getProduct_id_fk()));
            getAllBills();
            dialog3.dismiss();
            create_products_popup();
        }catch (Exception e){
            databaseClass.getDao().DeleteProduct(Integer.parseInt(fatoraDetail.getProduct_id_fk()));
            getAllBills();
        }

    }

    public void edit_bill(FatoraDetail fatoraDetail) {
//        dialog3.dismiss();
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
        total_price = Double.parseDouble(fatoraDetail.getTotal());
        product_price = fatoraDetail.getSell_price();
        product_amount = fatoraDetail.getAmount();
        discount = fatoraDetail.getProduct_discount();
        product_name = fatoraDetail.getProduct_name();
        price = Double.parseDouble(product_price);
        if (fatoraDetail.getType().equals("1")){
            type_spinner.setText("جملة");
        }else {
            type_spinner.setText("قطاعي");
        }
        //Toast.makeText(getActivity(), product_name, Toast.LENGTH_SHORT).show();
        et_product_name.setText(product_name);
        et_product_price.setText(product_price);
        et_product_amount.setText(product_amount);
        et_product_total_price.setText(total_price+"");
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
                FatoraDetail fatoraDetail = new FatoraDetail();
                fatoraDetail.setProduct_id_fk(product_id);
                fatoraDetail.setProduct_name(product_name);
                fatoraDetail.setType(type_id);
                fatoraDetail.setAmount(et_product_amount.getText().toString());
                fatoraDetail.setSell_price(et_product_price.getText().toString());
                fatoraDetail.setProduct_discount("0");
                fatoraDetail.setProduct_pouns("0");
                fatoraDetail.setNotes("");
                fatoraDetail.setTotal(et_product_total_price.getText().toString());
                fatoraDetail.setFatora_type("2");
                databaseClass.getDao().DeleteProduct(Integer.parseInt(product_id));
                databaseClass.getDao().Addbill(fatoraDetail);
                //fragmentBillsBinding.txtProductInCart.setText(fatoraDetailList.size()+"");
                Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                dialog2.dismiss();
                getAllBills();
            }
        });
    }

    public void add_client_discount(String value) {
        fragmentAddReturnsBinding.etPaid.setText("");
        this.value = value;
        if (totalPrice != 0.0){
            try {
                if (value.equals("0")) {
                    fragmentAddReturnsBinding.etDiscount.setText("0");
                    totalPrice = Double.parseDouble(fragmentAddReturnsBinding.etAllTotalPrice.getText().toString());
                    price_after_discount = totalPrice;
                    fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentAddReturnsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentAddReturnsBinding.etRemain2.setText(totalPrice+"");
                }else {
                    fragmentAddReturnsBinding.etDiscount.setText(value);
                    price_after_discount = totalPrice-totalPrice*Double.parseDouble(value)/100;
                    fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
                    fragmentAddReturnsBinding.etAfterDiscount.setText(price_after_discount+"");
                    fragmentAddReturnsBinding.etRemain2.setText(price_after_discount+"");
                }
            }catch (Exception e){
                fragmentAddReturnsBinding.etDiscount.setText("0");
                totalPrice = Double.parseDouble(fragmentAddReturnsBinding.etAllTotalPrice.getText().toString());
                price_after_discount = totalPrice;
                fragmentAddReturnsBinding.etAllTotalPrice.setText(totalPrice+"");
                fragmentAddReturnsBinding.etAfterDiscount.setText(price_after_discount+"");
                fragmentAddReturnsBinding.etRemain2.setText(totalPrice+"");
            }
        }else {
            Toast.makeText(getActivity(), "لا يوجد فواتير لعرض نسبة الخصم", Toast.LENGTH_SHORT).show();
        }
    }
}