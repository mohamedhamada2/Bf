package com.mz.bf.customerservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.mz.bf.R;
import com.mz.bf.addvisit.VisitAdapter;
import com.mz.bf.addvisit.VisitViewModel;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityAllCustomerServicesBinding;
import com.mz.bf.databinding.ActivityVisitBinding;

public class AllCustomerServicesActivity extends AppCompatActivity {
    ActivityAllCustomerServicesBinding activityAllCustomerServicesBinding;
    AllCustomerServicesViewModel allCustomerServicesViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    LinearLayoutManager layoutManager;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 20;
    int page =1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customer_services);
        activityAllCustomerServicesBinding = DataBindingUtil.setContentView(this,R.layout.activity_all_customer_services);
        allCustomerServicesViewModel = new AllCustomerServicesViewModel(this);
        activityAllCustomerServicesBinding.setAllCustomerServicesViewModel(allCustomerServicesViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        allCustomerServicesViewModel.get_all_visits(user_id,1);
        activityAllCustomerServicesBinding.visitsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
                        allCustomerServicesViewModel.PerformPagination(user_id,page);
                        isloading = true;
                    }
                }
            }
        });
        activityAllCustomerServicesBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void init_sand(AllCustomerServicesAdapter visitAdapter) {
        activityAllCustomerServicesBinding.visitsRecycler.setAdapter(visitAdapter);
        layoutManager = new LinearLayoutManager(this);
        activityAllCustomerServicesBinding.visitsRecycler.setLayoutManager(layoutManager);
        activityAllCustomerServicesBinding.visitsRecycler.setHasFixedSize(true);
    }
}