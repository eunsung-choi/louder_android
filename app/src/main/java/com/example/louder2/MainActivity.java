package com.example.louder2;

import static java.sql.DriverManager.println;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;
    int notinum=0;
    //바텀 네비게이션
    BottomNavigationView bottomNavigationView;
    private String TAG = "메인";

    //프래그먼트 변수
    Fragment fragment_home, fragment_sound, fragment_map, fragment_settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Louder");

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

                switch (item.getItemId()) {
                    case R.id.homeItem:
                        Log.i(TAG, "home 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_home).commitAllowingStateLoss();
                        return true;
                    case R.id.soundItem:
                        Log.i(TAG, "sound 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_sound).commitAllowingStateLoss();
                        return true;
                    case R.id.mapItem:
                        Log.i(TAG, "map 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_map).commitAllowingStateLoss();
                        return true;
                    case R.id.settingItem:
                        Log.i(TAG, "setting 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new FragSetting()).commit();
                        return true;

                }

                return true;
            }
        });


    }

    //알림 기능
    public void createNotification(View view) {
        show();
        Toast.makeText(this, "버튼 클릭 완료", Toast.LENGTH_LONG).show();
    }

    private void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher); //작은 아이콘
        builder.setContentTitle("알림 제목");
        builder.setContentText("알림 세부 텍스트");

        Intent intent = new Intent(this, FragHome.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE); //FLAG_MUTABLE 또는 IMMUTABLE 만 가능
        builder.setContentIntent(pendingIntent);

        //큰 아이콘
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.siren);
        builder.setLargeIcon(largeIcon);

        //색 지정
        builder.setColor(Color.RED);

        //알림음
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        //진동
        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true); //사용자가 알림 클릭시 자동 제거


        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        //오레오 동작
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        //알림마다 고유한 id값이 존재한다(Object 받아오면 그 id값으로 하자)
        manager.notify(notinum++, builder.build());
    }
    //알림 제거 함수
    public void removeNotificaton(View view){
        hide();
    }
    public void hide(){
        //id 별로 noti 지움
        if(notinum>=0){
            NotificationManagerCompat.from(this).cancel(notinum--);
        }
    }
}