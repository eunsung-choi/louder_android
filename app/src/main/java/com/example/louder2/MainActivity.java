package com.example.louder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.louder2.Fragment.FragHome;
import com.example.louder2.Fragment.FragMap;
import com.example.louder2.Fragment.FragSetting;
import com.example.louder2.Fragment.FragSound;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;
    //바텀 네비게이션
    BottomNavigationView bottomNavigationView;
    private String TAG = "메인";

    //프래그먼트 변수
    Fragment fragment_home, fragment_sound, fragment_map, fragment_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //지도
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        //프래그먼트 생성
        fragment_home = new FragHome();
        fragment_sound = new FragSound();
        fragment_map = new FragMap();
        fragment_settings = new FragSetting();
        //바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "바텀 네비게이션 클릭");

                switch (item.getItemId()){
                    case R.id.homeItem:
                        Log.i(TAG, "home 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_home).commitAllowingStateLoss();
                        return true;
                    case R.id.soundItem:
                        Log.i(TAG, "home 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_sound).commitAllowingStateLoss();
                        return true;
                    case R.id.mapItem:
                        Log.i(TAG, "home 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_map).commitAllowingStateLoss();
                        return true;
                    case R.id.settingItem:
                        Log.i(TAG, "home 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_settings).commitAllowingStateLoss();
                        return true;

                }
                return true;
            }
        });

    }

}