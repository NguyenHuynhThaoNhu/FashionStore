package com.fashionstore.fashion.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.fashionstore.fashion.R;
import com.fashionstore.fashion.adapter.MainAdapter;
import com.fashionstore.fashion.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewPager = findViewById(R.id.vpg_main);
        bottomNavigationView = findViewById(R.id.menu_bottom);

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(mainAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.btn_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.btn_shop).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.btn_cart).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.btn_history).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.btn_profile).setChecked(true);
                        break;
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.btn_shop:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.btn_cart:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.btn_history:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.btn_profile:
                        viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }


}
