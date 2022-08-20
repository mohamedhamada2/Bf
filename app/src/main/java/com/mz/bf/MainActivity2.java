package com.mz.bf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements PrintingCallback {
    Printing printing;
    Button btn_unpair_pair,btn_print,btn_print_images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initview();

    }

    private void initview() {
        btn_print = findViewById(R.id.btn_print);
        btn_print_images = findViewById(R.id.btn_print_images);
        btn_unpair_pair = findViewById(R.id.btn_pair_un_pair);
        if (printing != null){
            printing.setPrintingCallback(this);
        }else {
            printing = Printooth.INSTANCE.printer();
        }
        btn_unpair_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Printooth.INSTANCE.hasPairedPrinter())
                    Printooth.INSTANCE.removeCurrentPrinter();
                else {
                    startActivityForResult(new Intent(MainActivity2.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                    changePairAndUnpair();
                }
            }
        });
        btn_print_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Printooth.INSTANCE.hasPairedPrinter()){
                    startActivityForResult(new Intent(MainActivity2.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                }else {
                    //printImages();
                }
            }
        });
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Printooth.INSTANCE.hasPairedPrinter()){
                    startActivityForResult(new Intent(MainActivity2.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                }else {
                    printText();
                }
            }
        });
        changePairAndUnpair();
    }

    private void printText() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(new RawPrintable.Builder(new byte[]{27,100,4}).build());
        printables.add(new TextPrintable.Builder()
        .setText("Hello world")
        .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
        .setNewLinesAfter(1)
        .build());
        printables.add(new TextPrintable.Builder()
                .setText("Hello world")
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
                .setNewLinesAfter(1).build());
        printing.print(printables);
        Toast.makeText(this, printables.size()+"", Toast.LENGTH_SHORT).show();
    }

    private void changePairAndUnpair() {
        if (Printooth.INSTANCE.hasPairedPrinter()){
            btn_unpair_pair.setText(new StringBuilder("Unpair ").append(Printooth.INSTANCE.getPairedPrinter().getName()).toString());
        }else {
            btn_unpair_pair.setText("pair with printer");
        }
    }

    @Override
    public void connectingWithPrinter() {
        Toast.makeText(this, "Connecting to Printer", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionFailed(@NotNull String s) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onError(@NotNull String s) {
        Toast.makeText(this, "Error"+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessage(@NotNull String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(this, "order sent to printer", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == RESULT_OK)
            initprinting();
        changePairAndUnpair();
    }

    private void initprinting() {
        if (!Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();
        if (printing != null){
            printing.setPrintingCallback(this);
        }
    }

    @Override
    public void disconnected() {

    }
}