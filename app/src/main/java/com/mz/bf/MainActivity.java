package com.mz.bf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Retrofit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mz.bf.addclient.AddClientActivity;
import com.mz.bf.addpayment.PaymentActivity;
import com.mz.bf.addreturns.AddReturnsFragment;
import com.mz.bf.addsalary.AddSalaryActivity;
import com.mz.bf.addvisit.AddVisitActivity;
import com.mz.bf.addvisit.VisitActivity;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginActivity;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.addbill.BillsFragment;
import com.mz.bf.client_safe.MandoubSafeActivity;
import com.mz.bf.clientaccounting.ClientAccountingActivity;
import com.mz.bf.clients.ClientsActivity;
import com.mz.bf.code.CodeActivity;
import com.mz.bf.discountbill.DiscountbillFragment;
import com.mz.bf.allbills.AllBillsFragment;
import com.mz.bf.products.ProductsActivityActivity;
import com.mz.bf.profile.ProfileActivity;
import com.mz.bf.returns.ReturnsFragment;
import com.mz.bf.withoutdiscountbill.WithoutDiscountFragment;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    BottomNavigationView homeNavigationView;
    MySharedPreference mySharedPreference;
    CodeSharedPreferance codeSharedPreferance;
    LoginModel loginModel;
    Fragment homefragment = new AllBillsFragment();
    Fragment billsFragment = new BillsFragment();
    Fragment discountbillFragment = new ReturnsFragment();
    Fragment withoutDiscountFragment = new AddReturnsFragment();
    Fragment active;
    FragmentManager fm = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeNavigationView = findViewById(R.id.homeBottomnavigation);
        mySharedPreference = MySharedPreference.getInstance();
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        fm.beginTransaction().add(R.id.fragment_container, homefragment, "1").hide(homefragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, discountbillFragment, "2").hide(discountbillFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, withoutDiscountFragment, "3").hide(withoutDiscountFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, billsFragment, "4").show(billsFragment).commit();
        active = billsFragment;
        homeNavigationView.setOnNavigationItemSelectedListener(nav_listner);
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        homeNavigationView.setSelectedItemId(R.id.bills_of_month);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener nav_listner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home2:
                    fm.beginTransaction().hide(active).replace(R.id.fragment_container, homefragment).show(homefragment).commit();
                    active =homefragment;
                    return  true;
                case R.id.bills_of_month:
                    fm.beginTransaction().hide(active).replace(R.id.fragment_container, billsFragment).show(billsFragment).commit();
                    active = billsFragment;
                    return  true;
                    //item.setTitle(resources.getString(R.string.home));

                case R.id.invoice_discount:
                    fm.beginTransaction().hide(active).replace(R.id.fragment_container, discountbillFragment).show(discountbillFragment).commit();
                    active = discountbillFragment;
                    //item.setTitle(resources.getString(R.string.home));
                    return  true;
                case R.id.invoice_without_discount:
                    fm.beginTransaction().hide(active).replace(R.id.fragment_container, withoutDiscountFragment).show(withoutDiscountFragment).commit();
                    active = withoutDiscountFragment;
                    //item.setTitle(resources.getString(R.string.home));
                    return  true;
            }
            return true;
        }
    };

    public void showpopup(View view) {
        Context myContext = new ContextThemeWrapper(MainActivity.this,R.style.menuStyle);
        android.widget.PopupMenu menu = new android.widget.PopupMenu(myContext, view);
        menu.setOnMenuItemClickListener(this::onMenuItemClick);
        menu.inflate(R.menu.main_menu);
        menu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.add_client:
                startActivity(new Intent(MainActivity.this, AddClientActivity.class));
                break;
            case R.id.clients:
                startActivity(new Intent(MainActivity.this, ClientsActivity.class));
                break;
            case R.id.clientaccounting:
                startActivity(new Intent(MainActivity.this, ClientAccountingActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.add_salary:
                startActivity(new Intent(MainActivity.this, AddSalaryActivity.class));
                break;
            case R.id.add_payment:
                startActivity(new Intent(MainActivity.this, PaymentActivity.class));
                break;
            case R.id.add_visit:
                startActivity(new Intent(MainActivity.this, AddVisitActivity.class));
                break;
            case R.id.get_visits:
                startActivity(new Intent(MainActivity.this, VisitActivity.class));
                break;
            case R.id.products:
                startActivity(new Intent(MainActivity.this, ProductsActivityActivity.class));
                break;
            case R.id.safe:
                startActivity(new Intent(MainActivity.this, MandoubSafeActivity.class));
                break;
            case R.id.logout:
                mySharedPreference.ClearData(this);
                codeSharedPreferance.ClearData(this);
                RetrofitClientInstance.clear(this);
                startActivity(new Intent(MainActivity.this, CodeActivity.class));
                finish();
                break;
        }
        return true;
    }

}