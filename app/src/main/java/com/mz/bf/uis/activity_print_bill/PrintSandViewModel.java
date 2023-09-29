package com.mz.bf.uis.activity_print_bill;

import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintSandViewModel {
    Context context;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;

    public PrintSandViewModel(Context context) {
        this.context = context;
        printSandActivity = (PrintSandActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

   PrintSandActivity printSandActivity;

    public void get_easl_details(String sand_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<EsalModel> call = getDataService.get_esal_details(sand_id);
            call.enqueue(new Callback<EsalModel>() {
                @Override
                public void onResponse(Call<EsalModel> call, Response<EsalModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() == 1){
                            printSandActivity.setData(response.body().getEsal());
                        }
                    }
                }

                @Override
                public void onFailure(Call<EsalModel> call, Throwable t) {

                }
            });
        }
    }
}
