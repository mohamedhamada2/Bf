package com.mz.bf.addpayment;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.allbills.AllBillsAdapter;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewModel {
    Context context;
    PaymentActivity paymentActivity;
    List<Client> clientList;
    SandsAdapter sandsAdapter;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    List<String> typelist;

    public PaymentViewModel(Context context) {
        this.context = context;
        paymentActivity = (PaymentActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {
            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_all_sandat(String user_id, String s, int i,String from_dt,String to_dt,String client_id) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SandModel> call = getDataService.get_all_sandat(user_id,s,i,from_dt,to_dt,client_id);
            call.enqueue(new Callback<SandModel>() {
                @Override
                public void onResponse(Call<SandModel> call, Response<SandModel> response) {
                    if (response.isSuccessful()){
                        pd.dismiss();
                        clientList = response.body().getClients();
                        sandsAdapter = new SandsAdapter(clientList,context);
                        paymentActivity.init_sand(sandsAdapter);
                    }
                }

                @Override
                public void onFailure(Call<SandModel> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(String user_id, String s, int page,String from_dt,String to_dt,String client_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SandModel> call = getDataService.get_all_sandat(user_id,s,page,from_dt,to_dt,client_id);
            call.enqueue(new Callback<SandModel>() {
                @Override
                public void onResponse(Call<SandModel> call, Response<SandModel> response) {
                    if (response.isSuccessful()){
                        if (!response.body().getClients().isEmpty()){
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            clientList = response.body().getClients();
                            sandsAdapter.add_sand(clientList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SandModel> call, Throwable t) {

                }
            });
        }
    }

    public void getTypes() {
        typelist = new ArrayList<>();
        typelist.add("سند قبض");
        typelist.add("سند صرف");
        paymentActivity.setypespinner(typelist);
    }
}
