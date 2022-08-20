package com.mz.bf.allbills;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.mz.bf.MainActivity;
import com.mz.bf.R;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.addbill.FatoraDetail;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.FragmentHomeBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllBillsFragment extends Fragment {
    WebView webView;
    ProgressBar progressBar;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    FragmentHomeBinding fragmentHomeBinding;
    AllBillsViewModel allBillsViewModel;
    AllBillsAdapter allBillsAdapter;
    LinearLayoutManager layoutManager;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 20;
    int page =1 ;
    Dialog dialog3;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(getActivity());
        user_id = loginModel.getId();
        allBillsViewModel = new AllBillsViewModel(getActivity(),this);
        fragmentHomeBinding.setAllbillsviewmodel(allBillsViewModel);
        allBillsViewModel.get_all_bills(1,user_id);
        mainActivity = (MainActivity) getActivity();
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

    public void init_bill(AllBillsAdapter allBillsAdapter) {
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
            Call<BillDetailsModel> call = getDataService.get_bill_details(fatora.getId());
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
        ImageView print_img = view2.findViewById(R.id.print_img);
        print_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.BLUETOOTH}, 1);
                } else if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 2);
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 4);
                } else {
                    Toast.makeText(mainActivity, "hello", Toast.LENGTH_SHORT).show();
                    // Your code HERE
                    try {
                        EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
                        printer
                                .printFormattedText(
                                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, AllBillsFragment.this.getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                                                "[L]\n" +
                                                "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                                                "[L]\n" +
                                                "[C]================================\n" +
                                                "[L]\n" +
                                                "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                                                "[L]  + Size : S\n" +
                                                "[L]\n" +
                                                "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                                                "[L]  + Size : 57/58\n" +
                                                "[L]\n" +
                                                "[C]--------------------------------\n" +
                                                "[R]TOTAL PRICE :[R]34.98e\n" +
                                                "[R]TAX :[R]4.23e\n" +
                                                "[L]\n" +
                                                "[C]================================\n" +
                                                "[L]\n" +
                                                "[L]<font size='tall'>Customer :</font>\n" +
                                                "[L]Raymond DUPONT\n" +
                                                "[L]5 rue des girafes\n" +
                                                "[L]31547 PERPETES\n" +
                                                "[L]Tel : +33801201456\n" +
                                                "[L]\n" +
                                                "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                                                "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>"
                                );
                        Toast.makeText(mainActivity, "success", Toast.LENGTH_SHORT).show();
                    } catch (EscPosConnectionException e) {
                        Log.e("error6",e.getMessage());
                        e.printStackTrace();
                    } catch (EscPosBarcodeException e) {
                        Log.e("error7",e.getMessage());
                    } catch (EscPosEncodingException e) {
                        Log.e("error8",e.getMessage());
                    } catch (EscPosParserException e) {
                        Log.e("error9",e.getMessage());
                    }

                }
            }
        });
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