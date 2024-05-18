package com.mz.bf.customerservice;

import android.app.ProgressDialog;
import android.content.Context;

import com.facebook.all.All;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addvisit.Visit;
import com.mz.bf.addvisit.VisitActivity;
import com.mz.bf.addvisit.VisitAdapter;
import com.mz.bf.addvisit.VisitModel;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCustomerServicesViewModel {
    Context context;
    AllCustomerServicesActivity allCustomerServicesActivity;
    List<Visit> visitList;
    AllCustomerServicesAdapter visitAdapter;
    public AllCustomerServicesViewModel(Context context) {
        this.context = context;
        this.allCustomerServicesActivity = (AllCustomerServicesActivity) context;
    }

    public void get_all_visits(String user_id, int i) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<VisitModel> call = getDataService.getCustomerService(user_id,i);
            call.enqueue(new Callback<VisitModel>() {
                @Override
                public void onResponse(Call<VisitModel> call, Response<VisitModel> response) {
                    if (response.isSuccessful()){
                        pd.dismiss();
                        visitList = response.body().getVisits();
                        visitAdapter = new AllCustomerServicesAdapter(visitList,context);
                        allCustomerServicesActivity.init_sand(visitAdapter);
                    }
                }

                @Override
                public void onFailure(Call<VisitModel> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(String user_id, int page) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<VisitModel> call = getDataService.getCustomerService(user_id,page);
            call.enqueue(new Callback<VisitModel>() {
                @Override
                public void onResponse(Call<VisitModel> call, Response<VisitModel> response) {
                    if (response.isSuccessful()){
                        if (!response.body().getVisits().isEmpty()){
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            visitList = response.body().getVisits();
                            visitAdapter.add_visit(visitList);
                            allCustomerServicesActivity.init_sand(visitAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<VisitModel> call, Throwable t) {

                }
            });
        }
    }
}
