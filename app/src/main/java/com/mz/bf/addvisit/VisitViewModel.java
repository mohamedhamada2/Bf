package com.mz.bf.addvisit;

import android.app.ProgressDialog;
import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitViewModel {
    Context context;
    VisitActivity visitActivity;
    List<Visit> visitList;
    VisitAdapter visitAdapter;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    public VisitViewModel(Context context) {
        this.context = context;
        this.visitActivity = (VisitActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_all_visits(String user_id, int i) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<VisitModel> call = getDataService.get_all_visits(user_id,i);
            call.enqueue(new Callback<VisitModel>() {
                @Override
                public void onResponse(Call<VisitModel> call, Response<VisitModel> response) {
                    if (response.isSuccessful()){
                        pd.dismiss();
                        visitList = response.body().getVisits();
                        visitAdapter = new VisitAdapter(visitList,context);
                        visitActivity.init_sand(visitAdapter);
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
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<VisitModel> call = getDataService.get_all_visits(user_id,page);
            call.enqueue(new Callback<VisitModel>() {
                @Override
                public void onResponse(Call<VisitModel> call, Response<VisitModel> response) {
                    if (response.isSuccessful()){
                        if (!response.body().getVisits().isEmpty()){
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            visitList = response.body().getVisits();
                            visitAdapter.add_visit(visitList);
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
