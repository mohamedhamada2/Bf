package com.mz.bf.addclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClientViewModel {
    Context context;
    List<String> governtitlelist;
    List<Govern> governList;
    List<String> citytitlelist;
    List<City> cityList;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    public AddClientViewModel(Context context) {
        this.context = context;
        addClientActivity = (AddClientActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    AddClientActivity addClientActivity;
    public void getgovern() {
        governtitlelist = new ArrayList<>();
        if(Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<List<Govern>> call = getDataService.get_govern();
            call.enqueue(new Callback<List<Govern>>() {
                @Override
                public void onResponse(Call<List<Govern>> call, Response<List<Govern>> response) {
                    if(response.isSuccessful()){
                        governList = response.body();
                        for (Govern govern:governList){
                            governtitlelist.add(govern.getTitle());
                        }
                        addClientActivity.setGovernsspinnerData(governtitlelist,governList);
                    }
                }

                @Override
                public void onFailure(Call<List<Govern>> call, Throwable t) {

                }
            });
        }
    }

    public void getCities(String govern_id) {
        citytitlelist = new ArrayList<>();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<List<City>> call = getDataService.get_city(govern_id);
            call.enqueue(new Callback<List<City>>() {
                @Override
                public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                    if (response.isSuccessful()) {
                        cityList = response.body();
                        for (City city : cityList) {
                            citytitlelist.add(city.getTitle());
                        }
                        addClientActivity.setCitiesspinnerData(citytitlelist, cityList);
                    }
                }

                @Override
                public void onFailure(Call<List<City>> call, Throwable t) {

                }
            });
        }
    }

    public void addclient(String name, String govern_id, String city_id, String national_num,String mob1,String mob2 ,String address,String lat,String lon ,String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(addClientActivity);
            pd.setMessage("تحميل ...");
            pd.show();
            Toast.makeText(context, lon+"", Toast.LENGTH_SHORT).show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SuccessModel> call = getDataService.add_client(name,govern_id,city_id,national_num,mob1,mob2,address,lat,lon,user_id);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            pd.dismiss();
                            Toast.makeText(addClientActivity, "تم إضافة العميل بنجاح", Toast.LENGTH_SHORT).show();
                            addClientActivity.finish();
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
