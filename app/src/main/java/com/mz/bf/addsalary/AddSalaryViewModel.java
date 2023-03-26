package com.mz.bf.addsalary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addclient.SuccessModel;
import com.mz.bf.addpayment.PaymentActivity;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.uis.activity_print_bill.PrintSandActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSalaryViewModel {
    Context context;
    AddSalaryActivity addSalaryActivity;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;
    List<String> typelist;

    public AddSalaryViewModel(Context context) {
        this.context = context;
        addSalaryActivity = (AddSalaryActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void add_salary(String type, String user_id, String bill_date, String client_id, String value, String notes) {
        if (Utilities.isNetworkAvailable(addSalaryActivity)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SuccessModel> call = getDataService.add_sand(type,user_id,bill_date,client_id,value,notes);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            pd.dismiss();
                            Toast.makeText(addSalaryActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(addSalaryActivity, PrintSandActivity.class);
                            intent.putExtra("sand_id",response.body().getId());
                            context.startActivity(intent);
                            addSalaryActivity.finish();
                        }else {
                            pd.dismiss();
                            Toast.makeText(addSalaryActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_rkm() {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<RKM> call = getDataService.get_rkm();
            call.enqueue(new Callback<RKM>() {
                @Override
                public void onResponse(Call<RKM> call, Response<RKM> response) {
                    if (response.isSuccessful()){
                        addSalaryActivity.set_rkm(response.body().getRkm());
                    }
                }

                @Override
                public void onFailure(Call<RKM> call, Throwable t) {

                }
            });
        }
    }

    public void getTypes() {
        typelist = new ArrayList<>();
        typelist.add("سند قبض");
        typelist.add("سند صرف");
        addSalaryActivity.setypespinner(typelist);
    }
}
