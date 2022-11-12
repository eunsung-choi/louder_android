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

import com.example.louder2.ListViewAdapter;
import com.example.louder2.ListViewItem;
import com.example.louder2.R;

//public class FragSound extends Fragment {
//    private View view;
//
//    private String TAG = "프래그먼트";
//    public FragSound(){ //빈 생성자
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        Log.i(TAG, "onCreateView");
//        view = inflater.inflate(R.layout.frag_sound, container, false);
//        // ListView 아이템 데이터 정의
//        String[] menuItems = {"number one", "number two", "number three", "number four"};
//
//        ListView listView = (ListView) view.findViewById(R.id.mainMenu);
//
//        // 어댑터 생성(데이터 입력받음)
//        //Activity의 참조 획득이 가능한 getActivity()함수 사용 (Fragment는 this 사용 불가)
//        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, menuItems);
//
//        listView.setAdapter(listViewAdapter);
//        return view;
//    }
//    public void onListItemClick (ListView l, View v, int position, long id) {
//        // get TextView's Text.
//        String strText = (String) l.getItemAtPosition(position).toString() ;
//        // TODO
//        Toast.makeText(getActivity(), strText, Toast.LENGTH_LONG).show();
//    }
//
//}
public class FragSound extends ListFragment{
    ListViewAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Adapter 생성 및 Adapter 지정
        adapter = new ListViewAdapter();
        setListAdapter(adapter);

        //아이템 추가
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_android_black_24dp), "Box", "Account Box Black 36dp");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_android_black_24dp), "Box", "Account Box Black 36dp");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_android_black_24dp), "Box", "Account Box Black 36dp");


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
    public void addItem(Drawable icon, String title, String desc){
        adapter.addItem(icon, title, desc);
    }
}