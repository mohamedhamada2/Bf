package com.mz.bf.addbill;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addclient.SuccessModel;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBillsViewModel {
    Context context;
    BillsFragment billsFragment;
    List<String> maintitlelist,subtitlelist,warehousestitlelist,typelist,paid_list;
    List<SpinnerModel> main_list,sub_list,ware_houses_list;
    public AddBillsViewModel(Context context, BillsFragment billsFragment) {
        this.context = context;
        this.billsFragment = billsFragment;
    }

    public void get_main_branches() {
        maintitlelist = new ArrayList<>();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<SpinnerModel>> call = getDataService.get_main_branches();
            call.enqueue(new Callback<List<SpinnerModel>>() {
                @Override
                public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {
                    if (response.isSuccessful()){
                        main_list = response.body();
                        for (SpinnerModel spinnerModel:main_list){
                            maintitlelist.add(spinnerModel.getTitle());
                        }
                        try {
                            billsFragment.setmainsspinnerData(maintitlelist,main_list);
                        }catch (Exception e){
                            Log.e("error",e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<SpinnerModel>> call, Throwable t) {

                }
            });
        }
    }

    public void get_sub_branches(String main_branch_id) {
        subtitlelist = new ArrayList<>();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<SpinnerModel>> call = getDataService.get_sub_branches(main_branch_id);
            call.enqueue(new Callback<List<SpinnerModel>>() {
                @Override
                public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {
                    if (response.isSuccessful()){
                        sub_list = response.body();
                        for (SpinnerModel spinnerModel:sub_list){
                            subtitlelist.add(spinnerModel.getTitle());
                        }
                        billsFragment.setsubspinnerData(subtitlelist,sub_list);
                    }
                }

                @Override
                public void onFailure(Call<List<SpinnerModel>> call, Throwable t) {

                }
            });
        }
    }

    public void getwarehouses(String sub_branch_id) {
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
        warehousestitlelist = new ArrayList<>();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<SpinnerModel>> call = getDataService.get_warehouses(sub_branch_id);
            call.enqueue(new Callback<List<SpinnerModel>>() {
                @Override
                public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {
                    if (response.isSuccessful()){
                        ware_houses_list = response.body();
                        for (SpinnerModel spinnerModel:ware_houses_list){
                            warehousestitlelist.add(spinnerModel.getTitle());
                        }
                        billsFragment.setwarehousesspinnerData(warehousestitlelist,ware_houses_list);
                    }
                }

                @Override
                public void onFailure(Call<List<SpinnerModel>> call, Throwable t) {

                }
            });
        }
    }


    public void getTypes() {
        typelist = new ArrayList<>();
        typelist.add("??????????");
        typelist.add("????????");
        billsFragment.setypespinner(typelist);
    }

    public void getPayed() {
        paid_list = new ArrayList<>();
        paid_list.add("??????");
        paid_list.add("??????");
        billsFragment.sepayedspinner(paid_list);
    }

    public void getBillnum() {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LastBill> call = getDataService.get_last_bill();
            call.enqueue(new Callback<LastBill>() {
                @Override
                public void onResponse(Call<LastBill> call, Response<LastBill> response) {
                    if (response.isSuccessful()){
                        billsFragment.get_bill_num(response.body().getRkmFatora());
                    }
                }

                @Override
                public void onFailure(Call<LastBill> call, Throwable t) {

                }
            });
        }
    }

    public void add_bill(String user_id, String bill_num, String bill_date, String pay_id, String s2, String client_id, String main_branch_id, String sub_branch_id, String ware_houses_id, Double totalPrice, String discount, String paid, String remain, String byan, List<FatoraDetail> fatoraDetailList) {
        Bill bill = new Bill();
        bill.setUserId(user_id);
        bill.setFatoraDate(bill_date);
        bill.setRkmFatora(bill_num);
        bill.setTypePaid(pay_id);
        bill.setClientIdFk(client_id);
        bill.setMainBranchIdFk(main_branch_id);
        bill.setSubBranchIdFk(sub_branch_id);
        bill.setStorageIdFk(ware_houses_id);
        bill.setFatoraCostBeforeDiscount(totalPrice+"");
        bill.setDiscount("0");
        bill.setFatoraCostAfterDiscount(totalPrice+"");
        bill.setPaid(paid);
        bill.setRemain(remain);
        bill.setByan(byan);
        bill.setFatoraDetails(fatoraDetailList);
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("?????????? ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessModel> call = getDataService.add_bill(bill);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            pd.dismiss();
                            billsFragment.DeleteProducts();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_client_discount(String client_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ClientDiscount> call = getDataService.get_client_discount(client_id);
            call.enqueue(new Callback<ClientDiscount>() {
                @Override
                public void onResponse(Call<ClientDiscount> call, Response<ClientDiscount> response) {
                    if (response.isSuccessful()){
                        billsFragment.add_client_discount(response.body().getValue());
                    }
                }

                @Override
                public void onFailure(Call<ClientDiscount> call, Throwable t) {

                }
            });

        }
    }
}
