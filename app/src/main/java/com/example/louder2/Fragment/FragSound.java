package com.example.louder2.Fragment;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.louder2.ListViewAdapter;
import com.example.louder2.ListViewItem;
import com.example.louder2.Noti;
import com.example.louder2.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragSound extends ListFragment{
    ListViewAdapter adapter;
    //Volley
    static RequestQueue requestQueue; //요청 큐

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //Adapter 생성 및 Adapter 지정
        adapter = new ListViewAdapter();
        setListAdapter(adapter);


        //Volley
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(getContext());
        }
        Log.i("log", "makeRequest");
        makeRequest(); //volley 요청 보냄

        return super.onCreateView(inflater, container, savedInstanceState);
    }
    //item 클릭시 이벤트
    public void onListItemClick(ListView l, View v, int position, long id){
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position);

        String titleStr = item.getTitle();
        String descStr = item.getDesc();
        Drawable iconDrawable = item.getIcon();
        //TODO
        //this대신 getActivity() 사용하기
        Toast.makeText(getActivity(), titleStr, Toast.LENGTH_LONG).show();

    }
    //item 추가를 위한 함수, 앱 실행 중 동적으로, ListFragment 외부에서 데이터 추가방법
    public void addItem(Drawable icon, String title, String desc, String address){
        adapter.addItem(icon, title, desc, address);
    }

    //Volley 요청 보내기
    public void makeRequest(){
        Log.i("info","request 보냈다.");
        ArrayList<Noti> items = new ArrayList<Noti>(); // json 담을 배열
        String url = "http://133.186.146.174:3000/devices/sound"; //요청 보낼 URL
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray array = new JSONArray(response);
                    items.clear();
                    for(int i=0; i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        items.add(new Noti(obj.getInt("soundID"),
                                obj.getDouble("latitude"),
                                obj.getDouble("longitude"),
                                obj.getString("created_at"),
                                obj.getString("address")));
                    }
                    Log.i("json", array.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }

                //리스트에 item 추가
                setListAdapter(adapter);
                for(int i=0; i< items.size();i++){ //items.size()로 바꿀수도 있음
                    adapter.addItem(ContextCompat.getDrawable(getActivity(), items.get(i).icon), items.get(i).name, items.get(i).created_at, items.get(i).address);
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