package com.mz.bf.products;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityProductsActivityBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsActivityActivity extends AppCompatActivity {
    ProductViewModel productViewModel;
    ActivityProductsActivityBinding activityProductsActivityBinding;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,car_num;
    LinearLayoutManager layoutManager;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 20;
    int page =1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_activity);
        activityProductsActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_products_activity);
        productViewModel = new ProductViewModel(this);
        activityProductsActivityBinding.setProductsviewmodel(productViewModel);
        getSharedPreferenceData();
        activityProductsActivityBinding.recyclerSands.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    visibleitemcount = layoutManager.getChildCount();
                    totalitemcount = layoutManager.getItemCount();
                    pastvisibleitem = layoutManager.findFirstVisibleItemPosition();
                    if(dy>0){
                        if(isloading){
                            if(totalitemcount>previous_total){
                                isloading = false;
                                previous_total = totalitemcount;
                            }
                        }
                        if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                            page++;
                            if (activityProductsActivityBinding.etSearch.getText().toString().equals("")){
                                productViewModel.PerformPagination(page,user_id,car_num);
                            }else {
                                productViewModel.PerformSearchPagination(page,car_num,activityProductsActivityBinding.etSearch.getText().toString(),user_id);
                            }

                            isloading = true;
                        }
                    }
                }catch (Exception e){
                    Log.e("exe",e.getMessage());
                }
            }
        });
        activityProductsActivityBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        car_num = loginModel.getCarNumber();
        activityProductsActivityBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = activityProductsActivityBinding.etSearch.getText().toString();
                productViewModel.search_product(1,car_num,search,user_id);
            }
        });
        productViewModel.get_Products(1,user_id,car_num);
    }

    public void init_products(ProductsAdapter productsAdapter) {
        activityProductsActivityBinding.recyclerSands.setAdapter(productsAdapter);
        layoutManager = new LinearLayoutManager(this);
        activityProductsActivityBinding.recyclerSands.setLayoutManager(layoutManager);
        activityProductsActivityBinding.recyclerSands.setHasFixedSize(true);
    }
}