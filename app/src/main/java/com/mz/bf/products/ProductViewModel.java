package com.mz.bf.products;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.Product;
import com.mz.bf.addbill.ProductModel;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel {
    Context context;
    List<Product> productList;
    ProductsAdapter productsAdapter;
    ProductsActivityActivity productsActivityActivity;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;

    public ProductViewModel(Context context) {
        this.context = context;
        productsActivityActivity = (ProductsActivityActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }

    }

    public void get_Products(int page, String user_id, String car_num) {
        //Toast.makeText(context, user_id, Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_all_products(page,user_id,car_num);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()){
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        productList = response.body().getProducts();
                        productsAdapter = new ProductsAdapter(productList,context);
                        productsActivityActivity.init_products(productsAdapter);
                    }else {
                        //Toast.makeText(context, response.code()+"", Toast.LENGTH_SHORT).show();
                        Log.e("llll",response.code()+"");
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
                    Log.e("llll",t.getMessage());
                    //Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void PerformPagination(int page, String user_id, String car_num) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_all_products(page,user_id,car_num);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getProducts().isEmpty()) {
                            productList = response.body().getProducts();
                            //productsAdapter = new ProductsAdapter(productList, context);
                            productsAdapter.add_product(productList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

                }
            });
        }
    }

    public void search_product(int page, String car_num, String search, String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<ProductModel> call = getDataService.search_product(page,car_num,search,user_id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()){
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        productList = response.body().getProducts();
                        productsAdapter = new ProductsAdapter(productList,context);
                        productsActivityActivity.init_products(productsAdapter);
                    }else {
                        //Toast.makeText(context, response.code()+"", Toast.LENGTH_SHORT).show();
                        Log.e("llll",response.code()+"");
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
                    Log.e("llll",t.getMessage());
                    //Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void PerformSearchPagination(int page, String car_num, String search, String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<ProductModel> call = getDataService.search_product(page,car_num,search,user_id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getProducts().isEmpty()) {
                            productList = response.body().getProducts();
                            //productsAdapter = new ProductsAdapter(productList, context);
                            productsAdapter.add_product(productList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

                }
            });
        }
    }
}
