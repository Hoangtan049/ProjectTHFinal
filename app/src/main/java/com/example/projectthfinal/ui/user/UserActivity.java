package com.example.projectthfinal.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectthfinal.R;
import com.example.projectthfinal.ui.CustomersFragment;
import com.example.projectthfinal.ui.OrdersFragment;
import com.example.projectthfinal.ui.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        bottomNavigationView=findViewById(R.id.bottommenuUser);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuProductUser) {
                    setFragment(new ProductUserFragment());
                    return true;
                }
                if (item.getItemId() == R.id.menuOrdersUser) {
                    setFragment(new OrderUserFragment());
                    return true;
                }
                if (item.getItemId() == R.id.menuInfomation) {
                    setFragment(new InfomationFragment());
                    return true;
                }
                return false;
            }
        });
        setFragment(new ProductUserFragment());
    }
    void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.userlayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}