package com.example.louder2.Fragment;

import static android.content.Context.NOTIFICATION_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.louder2.MainActivity;
import com.example.louder2.Noti;
import com.example.louder2.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class FragHome extends Fragment {
    private View view;

    private String TAG = "프래그먼트";

    //Volley 변수
    EditText mret;
    TextView mrtext;
    Button mrbtn;
    static RequestQueue requestQueue; //요청 큐

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.frag_home, container, false);
        //Volley
        mret = view.findViewById(R.id.mret);
        mrtext = view.findViewById(R.id.mrtext);
        mrbtn = view.findViewById(R.id.mrbtn);
        mrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest(); //버튼 클릭시 makeRequest
            }
        });
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(view.getContext());
        }

        return view;
    }
    //Volley 요청 보내기
    public void makeRequest(){
        Log.i("info","request 보냈다.");
        String url = mret.getText().toString();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                println("응답 -> " + response);
                Gson gson = new Gson();
                Noti noti = gson.fromJson(response, Noti.class); //json -> object
                println("soundID: "+noti.soundID+ "\ncreated At:"+noti.created_at);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러->"+error.getMessage());
                    }
                }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄.");
    }
    public void println(String data){
        mrtext.append(data+"\n");
    }


}

