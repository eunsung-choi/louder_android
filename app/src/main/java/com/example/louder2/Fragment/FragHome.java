package com.example.louder2.Fragment;

import static android.content.Context.NOTIFICATION_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

    int notinum=0;
    Button makenoti;

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
        //알림
        makenoti = view.findViewById(R.id.makenoti);
        makenoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification(view, null);
            }
        });

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
                //알림 보냄
                Log.i("info", "알림 보냄");
                createNotification(view, noti); //알림 보냄

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


    //알림 기능
    public void createNotification(View view, Noti noti) {
        if(noti != null){
            show(noti);
        }

        Toast.makeText(getActivity(), "버튼 클릭 완료", Toast.LENGTH_LONG).show();
    }

    private void show(Noti noti) {
        String title, detail;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "default");

        builder.setSmallIcon(R.mipmap.ic_launcher); //작은 아이콘
        title = "";
        detail="";
        Bitmap largeIcon=null;
        switch (noti.soundID){
            //soundID==1
            case 1:
                title = "Louder : \"살려주세요\"";
                detail = noti.created_at;
                largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.boy);
                break;
            case 2:
                title="Louder : \"도와주세요\"";
                detail= noti.created_at;
                largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.help);
                break;
            case 3:
                title="Louder : \"울음 소리\"";
                detail = noti.created_at;
                largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.crying); //아이콘
                break;
        }

        builder.setContentTitle(title);
        builder.setContentText(detail);

        Intent intent = new Intent(getActivity(), FragHome.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_MUTABLE); //FLAG_MUTABLE 또는 IMMUTABLE 만 가능
        builder.setContentIntent(pendingIntent);

        //큰 아이콘
        //largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.siren);
        builder.setLargeIcon(largeIcon);

        //색 지정
        builder.setColor(Color.RED);

        //알림음
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        //진동
        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true); //사용자가 알림 클릭시 자동 제거


        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
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
            NotificationManagerCompat.from(getActivity()).cancel(notinum--);
        }
    }


}

