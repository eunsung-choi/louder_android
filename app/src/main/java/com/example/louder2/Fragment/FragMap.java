package com.example.louder2.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.louder2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FragMap extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener
{
    private MapView mapView = null;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    public FragMap()
    {
        // required
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_map, container, false);
        mapView = (MapView)layout.findViewById(R.id.map);
        mapView.getMapAsync(this);
        return layout;
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //액티비티가 처음 생성될 때 실행되는 함수
        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        String json = "";
        AssetManager assetManager = getContext().getAssets(); //assets폴더의 파을일 가져오기 위해 창고관리자(AssetManager) 얻어오기
        // assets/test.json 파일을 읽기 위한 InputStream
        try {
            InputStream is = assetManager.open("jsons/test.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }
            String jsonData=buffer.toString(); //Json Array를 파싱해서 String으로 가져옴

            JSONObject jsonObject = new JSONObject(jsonData); // json 객체 생성
            String path = jsonObject.getString("path");
            JSONArray jsonArray = new JSONArray(path);
            int list_cnt = jsonArray.length(); //Json 배열 내 JSON 데이터 개수를 가져옴

            //key의 value를 가져와 저장하기 위한 배열을 생성한다
            String[] getLatitude = new String[list_cnt]; //latitude 저장용
            String[] getLongitude = new String[list_cnt]; //longitude 저장용
            double[] lat = new double[list_cnt];
            double[] lng = new double[list_cnt];

            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                getLatitude[i] = subJsonObject.getString("latitude");
                getLongitude[i] = subJsonObject.getString("longitude");
            }
            //몇초마다 받아오는 위도 경도 데이터
            for (int i=0; i < jsonArray.length(); i++) {
                lng[i] = Double.parseDouble(getLongitude[i]);
                lat[i] = Double.parseDouble(getLatitude[i]);
            }
            //지도 중심 설정(마지막 위도경도로 변경해야함)
            double lat_m = Double.parseDouble(getLatitude[list_cnt-1]);
            double lng_m = Double.parseDouble(getLongitude[list_cnt-1]);
            LatLng position = new LatLng(lat_m, lng_m);

            //지도에 polylines 추가
            Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(lat[0], lng[0]),
                            new LatLng(lat[1], lng[1]),
                            new LatLng(lat[2], lng[2]),
                            new LatLng(lat[3], lng[3]),
                            new LatLng(lat[4], lng[4])));


            //마커 설정
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(position);
            markerOptions.title("서울");
            markerOptions.snippet("수도");
            googleMap.addMarker(markerOptions);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));

            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            // Set listeners for click events.
            googleMap.setOnPolylineClickListener(this);
            googleMap.setOnPolygonClickListener(this);
            polyline1.setTag("A");
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
    }
    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
    }
}