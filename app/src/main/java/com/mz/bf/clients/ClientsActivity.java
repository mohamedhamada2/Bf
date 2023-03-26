package com.mz.bf.clients;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.BillsFragment;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityClientsBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsActivity extends AppCompatActivity {
    ActivityClientsBinding activityClientsBinding;
    ClientViewModel clientViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    LinearLayoutManager layoutManager;
    private boolean isloading,isloading2,isloading3;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
   // private int pastvisibleitem2,visibleitemcount2,totalitemcount2,previous_total2=0;
   // private int pastvisibleitem3,visibleitemcount3,totalitemcount3,previous_total3=0;
    int view_threshold = 20;
    int page = 1,page2=1,page3=1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        activityClientsBinding = DataBindingUtil.setContentView(this, R.layout.activity_clients);
        clientViewModel = new ClientViewModel(this);
        activityClientsBinding.setClientsviewmodel(clientViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getId();
        clientViewModel.get_all_clients(user_id,1,"");
        /*activityClientsBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")){
                    if (Utilities.isNetworkAvailable(ClientsActivity.this)){
                        page2 =1;
                        clientViewModel.get_all_clients(user_id,1,charSequence.toString());
                        activityClientsBinding.visitsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                visibleitemcount2 = layoutManager.getChildCount();
                                totalitemcount2 = layoutManager.getItemCount();
                                pastvisibleitem2 = layoutManager.findFirstVisibleItemPosition();
                                if(dy>0){
                                    if(isloading2){
                                        if(totalitemcount2>previous_total2){
                                            isloading2 = false;
                                            previous_total2 = totalitemcount2;
                                        }
                                    }
                                    if(!isloading2 &&(totalitemcount2-visibleitemcount2)<= pastvisibleitem2+view_threshold){
                                        page2++;
                                        clientViewModel.PerformPagination(user_id,page2,charSequence.toString());
                                        isloading2 = true;
                                    }
                                }
                            }
                        });
                    }
                }else {
                    if (Utilities.isNetworkAvailable(ClientsActivity.this)){
                        page3 =1;
                        clientViewModel.get_all_clients(user_id,page3,"");
                        activityClientsBinding.visitsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                visibleitemcount3 = layoutManager.getChildCount();
                                totalitemcount3 = layoutManager.getItemCount();
                                pastvisibleitem3 = layoutManager.findFirstVisibleItemPosition();
                                if(dy>0){
                                    if(isloading3){
                                        if(totalitemcount3>previous_total3){
                                            isloading3 = false;
                                            previous_total3 = totalitemcount3;
                                        }
                                    }
                                    if(!isloading3 &&(totalitemcount3-visibleitemcount3)<= pastvisibleitem3+view_threshold){
                                        page3++;
                                        clientViewModel.PerformPagination(user_id,page3,charSequence.toString());
                                        isloading3 = true;
                                    }
                                }
                            }
                        });
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });*/
        activityClientsBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                pastvisibleitem=0;
                visibleitemcount=0;
                totalitemcount=0;
                previous_total=0;
                String word= activityClientsBinding.etSearch.getText().toString();
                clientViewModel.get_all_clients(user_id,1,word);
            }
        });
        activityClientsBinding.visitsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        clientViewModel.PerformPagination(user_id,page,activityClientsBinding.etSearch.getText().toString());
                        isloading = true;
                    }
                }
            }
        });
        activityClientsBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void init_clients(ClientAdapter clientAdapter) {
        activityClientsBinding.visitsRecycler.setAdapter(clientAdapter);
        layoutManager = new LinearLayoutManager(this);
        activityClientsBinding.visitsRecycler.setLayoutManager(layoutManager);
        activityClientsBinding.visitsRecycler.setHasFixedSize(true);
    }
}