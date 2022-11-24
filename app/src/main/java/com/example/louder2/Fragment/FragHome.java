package com.example.louder2.Fragment;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.louder2.Home.HomePage1;
import com.example.louder2.Home.HomePage2;
import com.example.louder2.Home.HomePage3;

import com.example.louder2.Home.MyCustomPagerAdapter;
import com.example.louder2.MainActivity;
import com.example.louder2.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class FragHome extends Fragment {
    MainActivity activity;
    private String TAG = "프래그먼트";
    ViewPager2 viewPager;
    private ViewGroup viewGroup; //뷰그룹 객체 선언
    ListView listView;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity=null;
    }

//    private List<Integer> imgList=new ArrayList<>();
    ArrayList<String> imgList = new ArrayList<>();
    CircleIndicator indicator;
    int firstImageCount=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.frag_home,container,false);
        Log.i(TAG, "onCreateView");

//        listView = (ListView)this.findViewById(R.id.listView);
//        setInit();
        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        imgList.add("baby");
        imgList.add("mom");
        imgList.add("119");

        firstImageCount =imgList.size();
        viewPager = view.findViewById(R.id.viewPager2);
        viewPager.setAdapter(new MyCustomPagerAdapter(imgList,getActivity() ,viewPager));

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);

        indicator = view.findViewById(R.id.indicator);

        float pageMarginPx = getResources().getDimension(R.dimen.pageMargin) ;// dimen 파일 안에 크기를 정의해두었다.
        float pagerWidth = getResources().getDimension(R.dimen.pageWidth); // dimen 파일이 없으면 생성해야함
        float screenWidth = getResources().getDisplayMetrics().widthPixels ;// 스마트폰의 너비 길이를 가져옴
        final float offsetPx = screenWidth - pageMarginPx - pagerWidth;

        viewPager.setPageTransformer((page, position) -> {
            float myOffset = position * -offsetPx;

            if (position < -1) {
                page.setTranslationX(-myOffset);
            } else if (position <= 1) {
                float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f)) ;
                page.setTranslationX(myOffset) ;
                page.setScaleY(scaleFactor);
                page.setAlpha(scaleFactor);
            }else{
                page.setAlpha(0f) ;
                page.setTranslationX(myOffset);
            }
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                headerHandler.removeCallbacks(headerRunnable);
                headerHandler.postDelayed(headerRunnable, 3000); // Slide duration 3 seconds

                indicator.animatePageSelected(position%firstImageCount);

            }
        });

        initIndicaotor();

    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void initIndicaotor(){
        indicator.createIndicators(3,0);
    }
    private Handler headerHandler = new Handler();

    private Runnable headerRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        headerHandler.removeCallbacks(headerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        headerHandler.postDelayed(headerRunnable, 3000); // Slide duration 3 seconds
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}

