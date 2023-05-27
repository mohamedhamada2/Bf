package com.mz.bf.masroufat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.clients.ClientViewModel;
import com.mz.bf.databinding.ActivityMasroufatBinding;

public class MasroufatActivity extends AppCompatActivity {
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    LinearLayoutManager layoutManager;
    private boolean isloading,isloading2,isloading3;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 20;
    int page = 1;
    MasroufatViewModel masroufatViewModel;
    ActivityMasroufatBinding activityMasroufatBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masroufat);
        activityMasroufatBinding = DataBindingUtil.setContentView(this, R.layout.activity_masroufat);
        masroufatViewModel = new MasroufatViewModel(this);
        activityMasroufatBinding.setMasrofatviewmodel(masroufatViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        masroufatViewModel.get_all_masrofat(user_id,1);
        activityMasroufatBinding.masrofatRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        masroufatViewModel.PerformPagination(user_id,page);
                        isloading = true;
                    }
                }
            }
        });
        activityMasroufatBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void init_recycler(MasroufatAdapter masroufatAdapter) {
        activityMasroufatBinding.masrofatRecycler.setAdapter(masroufatAdapter);
        layoutManager = new LinearLayoutManager(this);
        activityMasroufatBinding.masrofatRecycler.setLayoutManager(layoutManager);
        activityMasroufatBinding.masrofatRecycler.setHasFixedSize(true);
    }
}