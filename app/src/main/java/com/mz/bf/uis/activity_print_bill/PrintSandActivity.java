package com.mz.bf.uis.activity_print_bill;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.databinding.ActivityPrintSandBinding;
import com.mz.bf.databinding.DialogBluthoosBinding;
import com.mz.bf.printer.PrintPicture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PrintSandActivity extends AppCompatActivity {
    private ActivityPrintSandBinding binding;
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
    String sand_id,client_name,user_name,total_price,rkm_fatora,total_after,date,discount,paid,remain;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    PrintSandViewModel printViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_sand);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_print_sand);
        printViewModel = new PrintSandViewModel(this);
        binding.setPrintsandviewmodel(printViewModel);
        initViews();
    }

    private void initViews() {
        getDataIntent();
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

        binding.btnPrint.setOnClickListener(view -> checkBluetoothPermission());


    }

    private void getDataIntent() {
        sand_id = getIntent().getStringExtra("sand_id");
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_name = loginModel.getUserName();
        binding.txtUserName.setText(user_name);
        printViewModel.get_easl_details(sand_id);

        /*if (flag == 1) {
            fatora_id = getIntent().getStringExtra("id");
            Log.e("love",fatora_id);
            printViewModel.get_fatora(fatora_id);
        } else if (flag == 2) {
            fatora_id = getIntent().getStringExtra("id");
            printViewModel.get_headback(fatora_id);
        }*/

    }

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
        Bitmap bitmap = Bitmap.createBitmap(binding.scrollViewBluetooth.getChildAt(0).getWidth(), binding.scrollViewBluetooth.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        binding.scrollViewBluetooth.draw(canvas);

        if (bitmap != null) {

            Toast.makeText(this, "Printing", Toast.LENGTH_SHORT).show();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            double h = bitmap.getWidth() / 357.0;
            double dstH = bitmap.getHeight() / h;


            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 357, (int) dstH, true);


            int width = newBitmap.getWidth();
            int height = newBitmap.getHeight();
            int newWidth = (width/5+1)*5;
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
        /*View view=findViewById(R.id.scrollViewBluetooth);
        Printama.with(this).connect(printama->{
            printama.printFromView(view);
            printama.close();
        });*/
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

    public void setData(Esal esal) {
        binding.txtClientName.setText(esal.getClientName());
        binding.txtSandDate.setText(esal.getDateAr());
        binding.txtSandNum.setText(esal.getRkmEsal());
        binding.txtValue.setText(esal.getValue());
    }
}