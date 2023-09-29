package com.mz.bf.uis.activity_print_bill;

import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.allbills.BillDetailsModel;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintViewModel {
    Context context;
    PrintBillActivity printBillActivity;
    String base_url;
    CodeSharedPreferance codeSharedPreferance;

    public PrintViewModel(Context context) {
        this.context = context;
        printBillActivity = (PrintBillActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_fatora(String fatora_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<BillDetailsModel> call = getDataService.get_bill_details(fatora_id);
            call.enqueue(new Callback<BillDetailsModel>() {
                @Override
                public void onResponse(Call<BillDetailsModel> call, Response<BillDetailsModel> response) {
                    if (response.isSuccessful()){
                        printBillActivity.setData(response.body().getRecord());
                        printBillActivity.setrecyclerView(response.body());
                    }
                }

                @Override
                public void onFailure(Call<BillDetailsModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_headback(String fatora_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<BillDetailsModel> call = getDataService.get_bill_details2(fatora_id);
            call.enqueue(new Callback<BillDetailsModel>() {
                @Override
                public void onResponse(Call<BillDetailsModel> call, Response<BillDetailsModel> response) {
                    if (response.isSuccessful()){
                        printBillActivity.setData(response.body().getRecord());
                        printBillActivity.setrecyclerView(response.body());
                    }
                }

                @Override
                public void onFailure(Call<BillDetailsModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_fatora_details(String fatora_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<BillDetailsModel> call = getDataService.get_bill_details(fatora_id);
            call.enqueue(new Callback<BillDetailsModel>() {
                @Override
                public void onResponse(Call<BillDetailsModel> call, Response<BillDetailsModel> response) {
                    if (response.isSuccessful()){
                       printBillActivity.setData(response.body().getRecord());
                    }
                }

                @Override
                public void onFailure(Call<BillDetailsModel> call, Throwable t) {

                }
            });
        }
    }
}
