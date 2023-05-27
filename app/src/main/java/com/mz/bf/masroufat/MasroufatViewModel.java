package com.mz.bf.masroufat;

import android.app.ProgressDialog;
import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.clients.ClientAdapter;
import com.mz.bf.code.CodeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasroufatViewModel {
    Context context;
    MasroufatActivity masroufatActivity;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    List<Record> recordList;
    MasroufatAdapter masroufatAdapter;

    public MasroufatViewModel(Context context) {
        this.context = context;
        masroufatActivity = (MasroufatActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_all_masrofat(String user_id, int page) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<AllMasroufat> call = getDataService.get_all_masrofat(user_id,page);
            call.enqueue(new Callback<AllMasroufat>() {
                @Override
                public void onResponse(Call<AllMasroufat> call, Response<AllMasroufat> response) {
                    if (response.isSuccessful()){
                        pd.dismiss();
                        recordList = response.body().getRecords();
                        masroufatAdapter = new MasroufatAdapter(recordList,context);
                        masroufatActivity.init_recycler(masroufatAdapter);
                    }
                }

                @Override
                public void onFailure(Call<AllMasroufat> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(String user_id, int page) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<AllMasroufat> call = getDataService.get_all_masrofat(user_id,page);
            call.enqueue(new Callback<AllMasroufat>() {
                @Override
                public void onResponse(Call<AllMasroufat> call, Response<AllMasroufat> response) {
                    if (response.isSuccessful()){
                        if (!response.body().getRecords().isEmpty()){
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            recordList = response.body().getRecords();
                            masroufatAdapter.add_client(recordList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllMasroufat> call, Throwable t) {

                }
            });
        }
    }
}
