package com.mz.bf.customerservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addclient.SuccessModel;
import com.mz.bf.addvisit.AddVisitActivity;
import com.mz.bf.addvisit.VisitActivity;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerServiceViewModel {
    Context context;
    CustomerServiceActivity customerServiceActivity;

    public CustomerServiceViewModel(Context context) {
        this.context = context;
        customerServiceActivity = (CustomerServiceActivity) context;
    }

    public void add_customer_service(String user_id, double tvLatitude, double tvLongitude, String customer_name, String customer_phone, String customer_address) {
        if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessModel> call = getDataService.add_customer_service(user_id, customer_name, customer_phone, customer_address, tvLongitude + "", tvLatitude + "");
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            pd.dismiss();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(customerServiceActivity, AllCustomerServicesActivity.class));
                            customerServiceActivity.finish();
                        } else {
                            pd.dismiss();
                            Log.e("distance", tvLatitude + "");
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            //addVisitActivity.refresh();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

                }
            });
        }
    }

    public void end_customer_service(String visit_id, double tvLatitude, double tvLongitude, String customer_name, String notes) {
        if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessModel> call = getDataService.end_customer_service(visit_id, tvLongitude + "", tvLatitude + "",notes);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            pd.dismiss();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(customerServiceActivity, AllCustomerServicesActivity.class));
                            customerServiceActivity.finish();
                        } else {
                            pd.dismiss();
                            Log.e("distance", tvLatitude + "");
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            //addVisitActivity.refresh();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

                }
            });
        }
    }

    public void end_customer_service_with_img(String visit_id, double tvLatitude, double tvLongitude, String customer_name, String notes, Uri front_img) {
        RequestBody rb_visit_id =Utilities.getRequestBodyText(visit_id);
        RequestBody rb_latitude= Utilities.getRequestBodyText(tvLatitude+"");
        RequestBody rb_longitude= Utilities.getRequestBodyText(tvLongitude+"");
        RequestBody rb_notes= Utilities.getRequestBodyText(notes);
        MultipartBody.Part front_card = Utilities.getMultiPart(context,front_img , "img");
        if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessModel> call = getDataService.end_customer_service_with_img(rb_visit_id, rb_longitude, rb_latitude ,rb_notes,front_card);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            pd.dismiss();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(customerServiceActivity, AllCustomerServicesActivity.class));
                            customerServiceActivity.finish();
                        } else {
                            pd.dismiss();
                            Log.e("distance", tvLatitude + "");
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            //addVisitActivity.refresh();
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
