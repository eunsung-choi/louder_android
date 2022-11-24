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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
//    private View view;
    private String TAG = "프래그먼트";
//    int i=0;
    ViewPager2 viewPager;
    private ViewGroup viewGroup; //뷰그룹 객체 선언

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

//        setInit();
        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        imgList.add("https://i.postimg.cc/FKn3fLkK/babybaby.jpg");
        imgList.add("https://i.postimg.cc/kXFSQZRz/mmmm.jpg");
        imgList.add("https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/KOCIS_Halla_Mountain_in_Jeju-do_%286387785543%29.jpg/538px-KOCIS_Halla_Mountain_in_Jeju-do_%286387785543%29.jpg");

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
//    private void setInit() { //뷰페이저2 실행 메서드
//
//        /* setup infinity scroll viewpager */
//        ViewPager2 viewPageSetUp = viewGroup.findViewById(R.id.viewPager2); //여기서 뷰페이저를 참조한다.
//        FragPagerAdapter SetupPagerAdapter = new FragPagerAdapter(getActivity()); //프래그먼트에서는 getActivity로 참조하고, 액티비티에서는 this를 사용.
//        viewPageSetUp.setAdapter(SetupPagerAdapter); //FragPagerAdapter를 파라머티로 받고 ViewPager2에 전달 받는다.
//        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL); //방향은 가로로
//        viewPageSetUp.setOffscreenPageLimit(3); //페이지 한계 지정 갯수
//        // 무제한 스크롤 처럼 보이기 위해서는 0페이지 부터가 아니라 1000페이지 부터 시작해서 좌측으로 이동할 경우 999페이지로 이동하여 무제한 처럼 스크롤 되는 것 처럼 표현하기 위함.
//        viewPageSetUp.setCurrentItem(1000);
//
//        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin); //페이지끼리 간격
//        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset); //페이지 보이는 정도
//
//        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//
//
//            }
//        });
//        viewPageSetUp.setPageTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float offset = position * - (2 * pageOffset + pageMargin);
//                if(-1 > position) {
//                    page.setTranslationX(-offset);
//                } else if(1 >= position) {
//                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
//                    page.setTranslationX(offset);
//                    page.setScaleY(scaleFactor);
//                    page.setAlpha(scaleFactor);
//                } else {
//                    page.setAlpha(0f);
//                    page.setTranslationX(offset);
//                }
//            }
//        });
//
//    }
//    public class FragPagerAdapter extends FragmentStateAdapter { //뷰페이저2에서는 FragmentStateAdapter를 사용한다.
//        // Real Fragment Total Count
//        private final int mSetItemCount = 3; //프래그먼트 갯수 지정

//        public FragPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
//            super(fragmentActivity);
//        }


//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            int iViewIdx = getRealPosition(position);
//            switch( iViewIdx ) {
//                case 0    : { return new HomePage1(); } //프래그먼트 순서에 맞게 넣어줌.
//                case 1    : { return new HomePage2(); }
//                case 2    : { return new HomePage3(); }
////            case 3    : { return new Frag4(); }
////            case 4    : { return new Frag5(); }
////            case 5    : { return new Frag6(); }
//                default   : { return new HomePage1(); } //기본으로 나와있는 프래그먼트
//            }
//
//        }

//        public int getRealPosition(int _iPosition){
//            return _iPosition % mSetItemCount;
//        }

//        @Override
//        public long getItemId(int position) {
//            return super.getItemId(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return Integer.MAX_VALUE;
//        }
//    }


}

