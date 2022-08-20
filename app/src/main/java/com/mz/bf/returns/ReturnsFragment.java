package com.mz.bf.returns;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.allbills.AllBillsAdapter;
import com.mz.bf.allbills.BillDetailsAdapter;
import com.mz.bf.allbills.BillDetailsModel;
import com.mz.bf.allbills.Fatora;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.FragmentReturnsBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReturnsFragment extends Fragment {
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    FragmentReturnsBinding fragmentHomeBinding;
    ReturnsViewModel allBillsViewModel;
    AllBillsAdapter allBillsAdapter;
    LinearLayoutManager layoutManager;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 20;
    int page =1 ;
    Dialog dialog3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_returns, container, false);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(getActivity());
        user_id = loginModel.getId();
        allBillsViewModel = new ReturnsViewModel(getActivity(),this);
        fragmentHomeBinding.setReturnsviewmodel(allBillsViewModel);
        allBillsViewModel.get_all_bills(1,user_id);
        View view = fragmentHomeBinding.getRoot();
        fragmentHomeBinding.allBillsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        allBillsViewModel.PerformPagination(page,user_id);
                        isloading = true;
                    }
                }
            }
        });
        /*user_id = loginModel.getId()+"";
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://abbgroup.org.uk/project/Demo/representative/Sale/view/"+user_id);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);*/
        return  view;
    }

    public void init_bill(com.mz.bf.returns.AllBillsAdapter allBillsAdapter) {
        fragmentHomeBinding.allBillsRecycler.setAdapter(allBillsAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentHomeBinding.allBillsRecycler.setLayoutManager(layoutManager);
        fragmentHomeBinding.allBillsRecycler.setHasFixedSize(true);
    }

    public void setBills_product(Fatora fatora) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.details_dialog, null);
        RecyclerView products_recycler = view2.findViewById(R.id.product_recycler);
        TextView txt_client_name = view2.findViewById(R.id.txt_client_name);
        TextView txt_bill_num = view2.findViewById(R.id.txt_bill_num);
        TextView txt_no_data = view2.findViewById(R.id.txt_no_data);
        txt_bill_num.setText(fatora.getRkmFatora());
        txt_client_name.setText(fatora.getClientName());
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<BillDetailsModel> call = getDataService.get_bill_details2(fatora.getId());
            call.enqueue(new Callback<BillDetailsModel>() {
                @Override
                public void onResponse(Call<BillDetailsModel> call, Response<BillDetailsModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getDetails().isEmpty()){
                            txt_no_data.setVisibility(View.VISIBLE);
                            products_recycler.setVisibility(View.GONE);
                        }else {
                            BillDetailsAdapter billDetailsAdapter = new BillDetailsAdapter(response.body().getDetails(),getActivity());
                            products_recycler.setAdapter(billDetailsAdapter);
                            RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getActivity());
                            products_recycler.setLayoutManager(layoutmanager);
                            products_recycler.setHasFixedSize(true);
                            txt_no_data.setVisibility(View.GONE);
                            products_recycler.setVisibility(View.VISIBLE);
                        }

                    }
                }

                @Override
                public void onFailure(Call<BillDetailsModel> call, Throwable t) {

                }
            });
        }
        //fatoraDetailList = databaseClass.getDao().getallbills();
        //billsAdapter = new BillsAdapter(fatoraDetailList,getContext(),this);

        ImageView cancel_img = view2.findViewById(R.id.cancel_img);
        builder.setView(view2);
        dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        //Toast.makeText(homeActivity, loginModel.getData().getUser().getId()+"", Toast.LENGTH_SHORT).show();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
    }

    public void setVisibilty() {
        fragmentHomeBinding.txtNo.setVisibility(View.VISIBLE);
        fragmentHomeBinding.allBillsRecycler.setVisibility(View.GONE);
    }
   /* public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }*/
}