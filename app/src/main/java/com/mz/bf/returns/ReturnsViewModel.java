package com.mz.bf.returns;

import android.app.ProgressDialog;
import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.allbills.AllBillsModel;
import com.mz.bf.allbills.Fatora;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnsViewModel {
    Context context;
    ReturnsFragment returnsFragment;
    List<Fatora> fatoraList;
    AllBillsAdapter allBillsAdapter;

    public ReturnsViewModel(Context context, ReturnsFragment returnsFragment) {
        this.context = context;
        this.returnsFragment = returnsFragment;
    }
    public void get_all_bills(int i, String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<AllBillsModel> call = getDataService.get_bills2(user_id,i);
            call.enqueue(new Callback<AllBillsModel>() {
                @Override
                public void onResponse(Call<AllBillsModel> call, Response<AllBillsModel> response) {
                    if (response.isSuccessful()){
                        pd.dismiss();
                        fatoraList = response.body().getFatora();
                        if (!fatoraList.isEmpty()){
                            allBillsAdapter = new AllBillsAdapter(fatoraList,context,returnsFragment);
                            returnsFragment.init_bill(allBillsAdapter);
                        }else {
                            returnsFragment.setVisibilty();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllBillsModel> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(int page, String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<AllBillsModel> call = getDataService.get_bills2(user_id,page);
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
