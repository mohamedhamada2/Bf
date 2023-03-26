package com.mz.bf.addvisit;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVisitViewModel {
    Context context;
    AddVisitActivity addVisitActivity;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    public AddVisitViewModel(Context context) {
        this.context = context;
        addVisitActivity = (AddVisitActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void add_visit(String client_id, double tvLatitude, double tvLongitude, String notes, String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SuccessModel> call = getDataService.add_visit(client_id,tvLatitude+"",tvLongitude+"",notes,user_id);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() ==1){
                            pd.dismiss();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(addVisitActivity,VisitActivity.class));
                            addVisitActivity.finish();
                        }else {
                            pd.dismiss();
                            Log.e("distance",tvLatitude+"");
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

                }
            });
        }
    }
}
