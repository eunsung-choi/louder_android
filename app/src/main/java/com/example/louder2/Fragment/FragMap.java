package com.example.louder2.Fragment;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.louder2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragMap extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private View view;

    private String TAG = "프래그먼트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.frag_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        LatLng SEOUL = new LatLng(37.56, 126.97); //마커 이름

        MarkerOptions markerOptions = new MarkerOptions();// 마커 생성
        markerOptions.position(SEOUL);
        markerOptions.title("서울");     // 마커 제목
        markerOptions.snippet("한국의 수도"); // 마커 설명
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));  // 초기 위치
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));  // 줌의 정도
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);      // 지도 유형 설정

    }

}
