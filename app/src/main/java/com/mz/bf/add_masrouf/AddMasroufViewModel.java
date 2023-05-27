package com.mz.bf.add_masrouf;

import android.content.Context;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addclient.SuccessModel;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMasroufViewModel {
    Context context;
    AddMasroufActivity addMasroufActivity;
    List<Masrouf> masroufatlist;
    List<String> masroufattitlelist;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    public AddMasroufViewModel(Context context) {
        this.context = context;
        addMasroufActivity = (AddMasroufActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_masroufat() {
        masroufattitlelist = new ArrayList<>();

        if(Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<List<Masrouf>> call = getDataService.get_masroufat();
            call.enqueue(new Callback<List<Masrouf>>() {
                @Override
                public void onResponse(Call<List<Masrouf>> call, Response<List<Masrouf>> response) {
                    if(response.isSuccessful()){
                        masroufatlist = response.body();
                        for (Masrouf govern:masroufatlist){
                            masroufattitlelist.add(govern.getTitleSetting());
                        }
                        addMasroufActivity.setMasroufatspinnerData(masroufattitlelist,masroufatlist);
                    }
                }

                @Override
                public void onFailure(Call<List<Masrouf>> call, Throwable t) {

                }
            });
        }
    }

    public void add_masrouf(String setting_id, String bill_date, String value, String id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SuccessModel> call = getDataService.add_masrouf(setting_id,bill_date,value,id);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            addMasroufActivity.finish();
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
