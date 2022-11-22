package com.example.louder2.Fragment;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.louder2.Noti;
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

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragMap extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener
{
    private MapView mapView = null;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    static RequestQueue requestQueue; //요청 큐
    ArrayList<Noti> items = new ArrayList<Noti>(); // json 담을 배열
    public int array_length;
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
        //Volley
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(getContext());
        }
        makeRequest();

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
            double[] lat = new double[array_length];
            double[] lng = new double[array_length];

            //몇초마다 받아오는 위도 경도 데이터
            for (int i=0; i < array_length; i++) {
                lng[i] = items.get(i).longitude;
                lat[i] = items.get(i).latitude;
            }
            //지도 중심 설정(마지막 위도경도로 변경해야함) => index를 0으로 설정해야함.
            double lat_m = lat[0];
            double lng_m = lng[0];
            LatLng position = new LatLng(lat_m, lng_m);

            //지도에 polyline 추가
            List<LatLng> latLngList = new ArrayList<>();
            for (int i = 0; i <array_length; i++) {
                latLngList.add(i, new LatLng(lat[i], lng[i]));
            }
            Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .addAll(latLngList)
                    .width(4).color(Color.BLACK).geodesic(true));


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
            polyline.setTag("A");
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
    }
    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
    }
    //Volley 요청 보내기
    public void makeRequest(){
        Log.i("info","request 보냈다.");

        String url = "http://133.186.146.174:3000/devices/path"; //요청 보낼 URL
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray array = new JSONArray(response);
                    items.clear();
                    array_length = array.length();
                    for(int i=0; i<array_length;i++){
                        JSONObject obj=array.getJSONObject(i);
                        items.add(new Noti(
                                obj.getDouble("latitude"),
                                obj.getDouble("longitude")
                                ));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "에러 발생");
                    }
                }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

}