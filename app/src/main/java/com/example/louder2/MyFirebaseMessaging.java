package com.example.louder2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    //생성자
    public MyFirebaseMessaging() {
        super();
        //토큰 확인하기 - 서비스 실행시 토큰 가져옴
        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d("----------FCM Token: ", task.getResult());
                }
            }
        });
    }
    //Push 메세지 수신시 할 작업 작성
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
//        super.onMessageReceived(message);
        //메세지 오면 title과 body로 채워줌
        if(message.getData().size()>0){
            showNotification(message.getData().get("title"), message.getData().get("body"));
        }
    }
    //이 기기의 토큰이 바뀌었을때 할 작업 작성- 대부분 서버로 변경된 키값 전달.
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //Log.d("tag", "Refreshed token: "+token);
    }

    //알림 레이아웃 호출 함수
    private RemoteViews getCustomDesign(String title, String message){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.fcm_noti);
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViews.setTextViewText(R.id.noti_message, message);
        remoteViews.setImageViewResource(R.id.logo, R.drawable.siren);
        return remoteViews;
    }

    //알림 팝업 함수
    public void showNotification(String title, String message){
        //알림 터치시 이동할 액티비티
        Intent intent = new Intent(this, MainActivity.class);
        //알림 채널 아이디
        String channel_id = "CN_ID";
        //Flag 설정
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //기본 소리로 알림음 설정, 커스텀 하려면 소리 파일의 uri 설정하기
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //소리
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.siren)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000}) //알림시 진동, 1초 울리고 1초 쉬고 1초 후 다시 진동
                .setOnlyAlertOnce(true) //동일한 알림음 한번만
                .setContentIntent(pendingIntent);

        //버전에 따라 Custom으로 할지 기본으로 할지 정하기 (호환성 해결)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            builder = builder.setContent(getCustomDesign(title, message));
        } else{
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.siren);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //일부 안드로이드 버전은 알림 채널이 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "CH_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //알림 표시
        notificationManager.notify(0, builder.build());

    }

}
