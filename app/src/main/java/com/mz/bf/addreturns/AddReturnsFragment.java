package com.mz.bf.addreturns;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.data.DatabaseClass;
import com.mz.bf.databinding.FragmentAddReturnsBinding;
import com.mz.bf.returns.ReturnsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    DatePickerDialog.OnDateSetListener date_picker_dialog;
    String main_branch_id,sub_branch_id,ware_houses_id,type_id,product_name,product_id,product_price,product_amount,discount,bonous,pay_id,client_id,bill_date,client_name,bill_num2,user_id,value="0";
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
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(),DatabaseClass.class,"bills").allowMainThreadQueries().build();

        maintitlelist = new ArrayList<>();
        subtitlelist = new ArrayList<>();
        warehousestitlelist = new ArrayList<>();
        typelist = new ArrayList<>();
        paidlist = new ArrayList<>();
        addReturnsViewModel.get_main_branches();
        addReturnsViewModel.getTypes();
        addReturnsViewModel.getPayed();
        addReturnsViewModel.getBillnum();
        myCalendar = Calendar.getInstance();
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
        fragmentAddReturnsBinding.mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                main_branch_id = main_branches_list.get(i).getId();
                addReturnsViewModel.get_sub_branches(main_branch_id);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentAddReturnsBinding.subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sub_branch_id = sub_branches_list.get(i).getId();
                addReturnsViewModel.getwarehouses(sub_branch_id);
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fragmentAddReturnsBinding.warehouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ware_houses_id = ware_houses_list.get(i).getId();
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.purple_500));
                    fragmentAddReturnsBinding.etProductName.setOnClickListener(new View.OnClickListener() {
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
                    //Toast.makeText(AddStoreActivity.this, "???? ???????? ??????????", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentAddReturnsBinding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (typelist.get(0).equals("??????????")){
                        type_id = "1";
                    }else if (typelist.get(1).equals("????????")){
                        type_id ="2";
                    }
                    //citytitlelist.clear();
                }catch (Exception e){
                    TextView textView = (TextView) view;
                    textView.setVisibility(View.INVISIBLE);
                    //citytitlelist.clear();
                    //Toast.makeText(AddStoreActivity.this, "???? ???????? ??????????", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentAddReturnsBinding.paidSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (paidlist.get(0).equals("??????")){
                        pay_id = "1";
                    }else if (typelist.get(0).equals("??????")){
                        pay_id ="2";
                    }
                    //citytitlelist.clear();
                }catch (Exception e){
                    TextView textView = (TextView) view;
                    textView.setVisibility(View.INVISIBLE);
                    //citytitlelist.clear();
                    //Toast.makeText(AddStoreActivity.this, "???? ???????? ??????????", Toast.LENGTH_SHORT).show();

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
        fatoraDetailList = databaseClass.getDao().getallbills();
        if(!TextUtils.isEmpty(client_name)&&!TextUtils.isEmpty(bill_num2)&&!TextUtils.isEmpty(bill_date)&&!fatoraDetailList.isEmpty()){
            if (fragmentAddReturnsBinding.etAfterDiscount.getText().equals("0")){
                Toast.makeText(getActivity(), "????", Toast.LENGTH_SHORT).show();
                addReturnsViewModel.add_bill(user_id,fragmentAddReturnsBinding.etBillNum.getText().toString(),bill_date,pay_id,"",client_id,main_branch_id,sub_branch_id,ware_houses_id,totalPrice,"0",paid+"",remain+"","byan",fatoraDetailList);
            }else {
                Toast.makeText(getActivity(), "????", Toast.LENGTH_SHORT).show();
                addReturnsViewModel.add_bill(user_id,fragmentAddReturnsBinding.etBillNum.getText().toString(),bill_date,pay_id,"",client_id,main_branch_id,sub_branch_id,ware_houses_id,price_after_discount,"0",paid+"",remain+"","byan",fatoraDetailList);
            }
        }else {
            if (TextUtils.isEmpty(bill_num2)){
                Toast.makeText(getContext(), "???????? ???? ?????????????? ?????????????????? ???????? ?????? ????????????????", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(client_name)){
                fragmentAddReturnsBinding.etClientName.setError("???????? ?????? ????????????");
                Toast.makeText(getContext(), "???????? ???????? ????????????????", Toast.LENGTH_SHORT).show();
            }else {
                fragmentAddReturnsBinding.etClientName.setError(null);
            }
            if (TextUtils.isEmpty(bill_date)){
                fragmentAddReturnsBinding.etBillDate.setError("???????? ?????????? ????????????????");
                Toast.makeText(getContext(), "???????? ???????? ????????????????", Toast.LENGTH_SHORT).show();
            }else {
                fragmentAddReturnsBinding.etBillDate.setError(null);
            }
            if (fatoraDetailList.isEmpty()){
                Toast.makeText(getActivity(), "???????? ???? ???????? ???????????? ???? ????????????????", Toast.LENGTH_SHORT).show();
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
        et_search.setHint("?????? ????????????");
        if (Utilities.isNetworkAvailable(getActivity())){
            ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("?????????? ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
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
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
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
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
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
        RecyclerView product_recycler = view.findViewById(R.id.product_recycler);
        if (Utilities.isNetworkAvailable(getActivity())){
            ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("?????????? ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_all_products(1,ware_houses_id);
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
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")){
                    if (Utilities.isNetworkAvailable(getActivity())){
                        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        Call<ProductModel> call = getDataService.search_product(1,ware_houses_id,charSequence.toString());
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
                        Call<ProductModel> call = getDataService.get_all_products(1,ware_houses_id);
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
            Call<ProductModel> call = getDataService.get_all_products(page,ware_houses_id);
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2,maintitlelist);
        fragmentAddReturnsBinding.mainSpinner.setAdapter(arrayAdapter);
    }

    public void setsubspinnerData(List<String> subtitlelist, List<SpinnerModel> sub_list) {
        this.subtitlelist = subtitlelist;
        sub_branches_list = sub_list;
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2,subtitlelist);
            fragmentAddReturnsBinding.subSpinner.setAdapter(arrayAdapter);
        }catch (Exception e){

        }

    }

    public void setwarehousesspinnerData(List<String> warehousestitlelist, List<SpinnerModel> ware_houses_list) {
        this.warehousestitlelist = warehousestitlelist;
        this.ware_houses_list = ware_houses_list;
        try {
            ArrayAdapter<String> arrayAdapter = new  ArrayAdapter<String>(getActivity(), R.layout.spinner_item2,warehousestitlelist);
            fragmentAddReturnsBinding.warehouseSpinner.setAdapter(arrayAdapter);
        }catch (Exception e){
        }
    }

    public void setData(Product product) {
        product_id = product.getId();
        product_name = product.getProductName();
        product_price = product.getOneSellPrice();
        product_amount = fragmentAddReturnsBinding.etProductAmout.getText().toString();
        price = Double.parseDouble(product_price);
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
        fragmentAddReturnsBinding.etProductPrice.setText(product.getOneSellPrice());
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
                    Toast.makeText(getContext(), "???????? ???????? ????????????????", Toast.LENGTH_SHORT).show();
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
            databaseClass.getDao().Addbill(fatoraDetail);
            Toast.makeText(getContext(), "?????? ?????????????? ??????????", Toast.LENGTH_SHORT).show();
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
            fatoraDetail.setTotal(total_price+"");
            databaseClass.getDao().editproduct(fatoraDetail);
            //fragmentBillsBinding.txtProductInCart.setText(fatoraDetailList.size()+"");
            Toast.makeText(getContext(), "???? ?????????????? ??????????", Toast.LENGTH_SHORT).show();
            fragmentAddReturnsBinding.etProductName.setText("");
            fragmentAddReturnsBinding.etProductAmout.setText("");
            fragmentAddReturnsBinding.etProductPrice.setText("");
            fragmentAddReturnsBinding.etTotalPrice.setText("");
            getAllBills();
        }
    }

    public void getAllBills() {
        fatoraDetailList = databaseClass.getDao().getallbills();
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

    public void DeleteProducts() {
        databaseClass.getDao().deleteAllproduct();
        getAllBills();
        Toast.makeText(getActivity(), "???? ?????????? ?????????????? ??????????", Toast.LENGTH_SHORT).show();
        getActivity().finish();
        startActivity(getActivity().getIntent());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.edit_bill_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        EditText et_product_name = view.findViewById(R.id.et_product_name);
        EditText et_product_amount =view.findViewById(R.id.et_product_amout);
        EditText et_product_price = view.findViewById(R.id.et_product_price);
        EditText et_product_total_price = view.findViewById(R.id.et_total_price);
        EditText et_product_discount = view.findViewById(R.id.et_discount);
        EditText et_bonous = view.findViewById(R.id.et_bonous);
        Button btn_add_bill= view.findViewById(R.id.btn_add_bill);
        Spinner type_spinner = view.findViewById(R.id.type_spinner);
        product_id = fatoraDetail.getProduct_id_fk();
        List<String> typeslist = new ArrayList<>();
        typeslist.add("??????????");
        typeslist.add("????????");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item2,typeslist);
        type_spinner.setAdapter(arrayAdapter);
        total_price = Double.parseDouble(fatoraDetail.getTotal());
        product_price = fatoraDetail.getSell_price();
        product_amount = fatoraDetail.getAmount();
        discount = fatoraDetail.getProduct_discount();
        product_name = fatoraDetail.getProduct_name();
        price = Double.parseDouble(product_price);
        //Toast.makeText(getActivity(), product_name, Toast.LENGTH_SHORT).show();
        et_product_name.setText(product_name);
        et_product_price.setText(product_price);
        et_product_discount.setText(discount);
        et_product_amount.setText(product_amount);
        et_product_total_price.setText(total_price+"");
        //Toast.makeText(getActivity(),total_price+"",Toast.LENGTH_SHORT).show();
        et_bonous.setText(fatoraDetail.getProduct_pouns());
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
        et_product_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    discount ="0.0";
                    nesba_discount = 0.0;
                    try {
                        total_price = price*Double.parseDouble(product_amount);
                        et_product_total_price.setText(total_price+"");
                    }catch (Exception e){
                        total_price = 0.0;
                        et_product_total_price.setText(total_price+"");
                    }
                }else {
                    discount = charSequence.toString();
                    nesba_discount = Double.parseDouble(discount);
                    try {
                        total_price = price*Double.parseDouble(product_amount)-price*Double.parseDouble(product_amount)*nesba_discount/100;
                        et_product_total_price.setText(total_price+"");
                    }catch (Exception e){
                        total_price = 0.0;
                        et_product_total_price.setText(total_price+"");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_bonous.setText(bonous);
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
                fatoraDetail.setProduct_discount(et_product_discount.getText().toString());
                fatoraDetail.setProduct_pouns("0");
                fatoraDetail.setNotes("");
                fatoraDetail.setTotal(et_product_total_price.getText().toString());
                databaseClass.getDao().DeleteProduct(Integer.parseInt(product_id));
                databaseClass.getDao().Addbill(fatoraDetail);
                //fragmentBillsBinding.txtProductInCart.setText(fatoraDetailList.size()+"");
                Toast.makeText(getContext(), "???? ?????????????? ??????????", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "???? ???????? ???????????? ???????? ???????? ??????????", Toast.LENGTH_SHORT).show();
        }
    }
}