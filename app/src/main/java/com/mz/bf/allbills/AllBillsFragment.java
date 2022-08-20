package com.mz.bf.allbills;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.mz.bf.databinding.DialogBluthoosBinding;
import com.mz.bf.databinding.FragmentHomeBinding;
import com.mz.bf.printer.PrintPicture;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    ///////////////////printer//////
    private ActivityResultLauncher<String[]> permissions,permissions2;
    private ActivityResultLauncher<Intent> launcher;
    private static final int D80MMWIDTH = 576;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket;
    private OutputStream mmOutputStream;
    private InputStream inputStream;
    private boolean stopWorker;
    private byte[] readBuffer;
    private int readBufferPosition;
    private Thread workerThread;
    private AlertDialog dialog;
    private  int req;


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

        //////activity/////
        permissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted->{
            if (!isGranted.containsValue(false)){
                findBT();
            }else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        permissions2 = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),isGranted->{
            if (!isGranted.containsValue(false)){
                findBT();
            }else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if (req ==1 &&result.getResultCode()==RESULT_OK){
                @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                createDialogAlertBluetooth(this,pairedDevices);

            }
        });

        // when press on button
        // checkBluetoothPermission();

        ////////////











        /*user_id = loginModel.getId()+"";
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://abbgroup.org.uk/project/Demo/representative/Sale/view/"+user_id);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);*/
        return  view;
    }
    //////activity//////////////////

    private void checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED){
                    findBT();
                }else {
                    permissions2.launch(new String[]{Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.BLUETOOTH_CONNECT});

                }
            }else {
                findBT();
            }

        }else {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
                permissions2.launch(new String[]{Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.BLUETOOTH_CONNECT});

            }else {
                permissions.launch(new String[]{Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN});

            }
        }

    }

    private void printBluetoothBitmap() {
        Toast.makeText(this, "Printing via bluetooth", Toast.LENGTH_SHORT).show();
        Bitmap bitmap = Bitmap.createBitmap(binding.scrollViewBluetooth.getChildAt(0).getWidth(), binding.scrollView.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        binding.scrollViewBluetooth.draw(canvas);

        if (bitmap != null) {

            Toast.makeText(this, "Printing", Toast.LENGTH_SHORT).show();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            double h = bitmap.getWidth() / 576.0;
            double dstH = bitmap.getHeight() / h;


            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 576, (int) dstH, true);


            int width = newBitmap.getWidth();
            int height = newBitmap.getHeight();
            int newWidth = (width/8+1)*8;
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, 1);
            Bitmap b = Bitmap.createBitmap(newBitmap, 0, 0, width, height, matrix, true);
            new Thread(()->{

                if (b!=null){
                    try {
                        sendData(b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }){

            }.start();




        }


    }





    void sendData(Bitmap bitmap) throws IOException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);


            byte[]  bitmapData = PrintPicture.POS_PrintBMP(bitmap, 576, 4);

            mmOutputStream.write(bitmapData);

        }catch (Exception e){

        }


    }

    void beginListenForData() {
        Log.e("sss","ss");
        try {
            final Handler handler = new Handler();
            // activity is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()
                        && !stopWorker) {

                    try {

                        int bytesAvailable = inputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0,
                                            encodedBytes, 0,
                                            encodedBytes.length);
                                    final String data = new String(
                                            encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }

                    } catch (IOException ex) {
                        stopWorker = true;
                        try {
                            closeBT();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
                try {
                    closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("MissingPermission")
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


            if (!mBluetoothAdapter.isEnabled()) {
                req  = 1;
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                launcher.launch(enableBluetooth);
            }else {
                @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                createDialogAlertBluetooth(this,pairedDevices);
            }



        } catch (Exception e) {
            Log.e("ldkkd", e.toString());
        }
    }

    @SuppressLint("MissingPermission")
    public void openBT(BluetoothDevice bluetoothDevice) throws IOException {
        try {
            dialog.dismiss();

            mmDevice = bluetoothDevice;
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

            try {

                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                inputStream = mmSocket.getInputStream();
                beginListenForData();
                printBluetoothBitmap();

            }catch (Exception e){

            }

            Log.e("sada","222");




            // myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            inputStream.close();
            mmSocket.close();
            // myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDialogAlertBluetooth(Context context, Set<BluetoothDevice> bluetoothDeviceList) {
        List<BluetoothDevice> bluetoothDeviceList1 = new ArrayList<>();
        bluetoothDeviceList1.addAll(bluetoothDeviceList);
        dialog = new AlertDialog.Builder(context)
                .create();

        DialogBluthoosBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_bluthoos, null, false);
       com.mz.bf.adapters.BluetoothAdapter bluetoothAdapter = new com.mz.bf.adapters.BluetoothAdapter(context, bluetoothDeviceList1);
        binding.bluthoos.setLayoutManager(new LinearLayoutManager(context));
        binding.bluthoos.setAdapter(bluetoothAdapter);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    ///////////////////////////////












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