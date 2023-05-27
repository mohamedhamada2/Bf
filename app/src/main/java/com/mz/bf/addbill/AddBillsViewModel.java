package com.mz.bf.addbill;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addclient.SuccessModel;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.uis.activity_print_bill.PrintActivity;
import com.mz.bf.uis.activity_print_bill.PrintBillActivity;

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
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    public AddBillsViewModel(Context context, BillsFragment billsFragment) {
        this.context = context;
        this.billsFragment = billsFragment;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {
            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void getTypes() {
        typelist = new ArrayList<>();
        typelist.add("جملة");
        typelist.add("قطاعي");
        billsFragment.setypespinner(typelist);
    }

    public void getPayed() {
        paid_list = new ArrayList<>();
        paid_list.add("اجل");
        paid_list.add("كاش");
        billsFragment.sepayedspinner(paid_list);
    }

    public void getBillnum() {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
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

    public void add_bill(String user_id, String bill_num, String bill_date, String pay_id, String s2, String client_id, String main_branch_id, String sub_branch_id, String ware_houses_id,String price_before_discount ,String totalPrice, String discount, String paid, String remain, String byan, List<FatoraDetail> fatoraDetailList,String mandoub_discount) {
        Bill bill = new Bill();
        bill.setUserId(user_id);
        bill.setFatoraDate(bill_date);
        bill.setRkmFatora(bill_num);
        bill.setTypePaid(pay_id);
        bill.setClientIdFk(client_id);
        bill.setMainBranchIdFk(main_branch_id);
        bill.setSubBranchIdFk(sub_branch_id);
        bill.setStorageIdFk(ware_houses_id);
        bill.setFatoraCostBeforeDiscount(price_before_discount);
        bill.setDiscount(discount);
        bill.setFatoraCostAfterDiscount(totalPrice+"");
        bill.setPaid(paid);
        bill.setRemain(remain);
        bill.setByan(byan);
        bill.setFatoraDetails(fatoraDetailList);
        bill.setMandoub_discount(mandoub_discount);
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SuccessModel> call = getDataService.add_bill(bill);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            pd.dismiss();
                            billsFragment.DeleteProducts(response.body().getFatora_id());
                            //Log.e("kkkk",response.body().getFatora_id());
                            //Toast.makeText(context,response.body().getFatora_id(), Toast.LENGTH_SHORT).show();
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
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
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
