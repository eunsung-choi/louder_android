package com.example.louder2.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.louder2.R;

import java.util.ArrayList;

public class MyCustomPagerAdapter extends RecyclerView.Adapter<MyCustomPagerAdapter.ViewHolderPage> {

    private ArrayList<String> listData;
    Context con;
    ViewPager2 pager;

    public MyCustomPagerAdapter(ArrayList<String> data , Context con , ViewPager2 pager) {
        this.listData = data;
        this.pager = pager;
        this.con =con;
    }

    @Override
    public ViewHolderPage onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.home_page1, parent, false);
        return new ViewHolderPage(view);
    }


    @Override
    public void onBindViewHolder(ViewHolderPage holder, int position) {
        if(holder instanceof ViewHolderPage){
// position = position%listData.size();

            ViewHolderPage viewHolder = (ViewHolderPage) holder;
            viewHolder.onBind(listData.get(position));

            if (position == listData.size() - 2) {
                pager.post(runnable);
            }
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            listData.addAll(listData);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolderPage extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView text;
        private RelativeLayout rl_layout;
        String data;



        ViewHolderPage(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.image);

        }

        public void onBind(String data){
            this.data = data;
            if(data == "baby"){
                Glide.with(con).load(R.drawable.babybaby).into(img);
            }else if(data == "mom"){
                Glide.with(con).load(R.drawable.mmom).into(img);
            }else Glide.with(con).load(R.drawable.baby4).into(img);


        }
    }
}