package com.mz.bf.clients;

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

public class ClientViewModel {
    Context context;
    ClientsActivity clientsActivity;
    List<Client> clientList;
    ClientAdapter clientAdapter;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    public ClientViewModel(Context context) {
        this.context = context;
        clientsActivity = (ClientsActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_all_clients(String user_id, int page,String word) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<ClientLocation> call = getDataService.get_all_clients(user_id,page,word);
            call.enqueue(new Callback<ClientLocation>() {
                @Override
                public void onResponse(Call<ClientLocation> call, Response<ClientLocation> response) {
                    if (response.isSuccessful()){
                        pd.dismiss();
                        clientList = response.body().getClients();
                        clientAdapter = new ClientAdapter(clientList,context);
                        clientsActivity.init_clients(clientAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ClientLocation> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(String user_id, int page,String word) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<ClientLocation> call = getDataService.get_all_clients(user_id,page,word);
            call.enqueue(new Callback<ClientLocation>() {
                @Override
                public void onResponse(Call<ClientLocation> call, Response<ClientLocation> response) {
                    if (response.isSuccessful()){
                        if (!response.body().getClients().isEmpty()){
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            clientList = response.body().getClients();
                            clientAdapter.add_client(clientList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClientLocation> call, Throwable t) {

                }
            });
        }
    }
}
