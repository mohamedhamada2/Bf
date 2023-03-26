package com.mz.bf.allbills;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBillsViewModel {
    Context context;
    AllBillsFragment allBillsFragment;
    List<Fatora> fatoraList;
    AllBillsAdapter allBillsAdapter;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;

    public AllBillsViewModel(Context context, AllBillsFragment allBillsFragment) {
        this.context = context;
        this.allBillsFragment = allBillsFragment;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_all_bills(int i, String user_id,String from_date,String to_date,String client_id,String fatora_num) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<AllBillsModel> call = getDataService.get_bills(user_id,i,from_date,to_date,client_id,fatora_num);
            call.enqueue(new Callback<AllBillsModel>() {
                @Override
                public void onResponse(Call<AllBillsModel> call, Response<AllBillsModel> response) {
                    if (response.isSuccessful()){
                        pd.dismiss();
                        fatoraList = response.body().getFatora();
                        allBillsAdapter = new AllBillsAdapter(fatoraList,context,allBillsFragment);
                        allBillsFragment.init_bill(allBillsAdapter);
                    }
                }

                @Override
                public void onFailure(Call<AllBillsModel> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(int page, String user_id,String from_date,String to_date,String client_id,String fatora_num) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<AllBillsModel> call = getDataService.get_bills(user_id,page,from_date,to_date,client_id,fatora_num);
            call.enqueue(new Callback<AllBillsModel>() {
                @Override
                public void onResponse(Call<AllBillsModel> call, Response<AllBillsModel> response) {
                    if (response.isSuccessful()){
                        if (!response.body().getFatora().isEmpty()){
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            fatoraList = response.body().getFatora();
                            allBillsAdapter.add_bill(fatoraList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllBillsModel> call, Throwable t) {

                }
            });
        }
    }
}
