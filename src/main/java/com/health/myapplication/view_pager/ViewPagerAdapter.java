package com.health.myapplication.view_pager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.health.myapplication.R;

public class ViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context context=null;
    private Drawable[] images;

    public ViewPagerAdapter( Context context, Drawable[] images) {
        this.context=context;
        this.images=images;
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v=null;
        if(context!=null){
            Log.d("context",context.toString());
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.activity_information_slider,container,false);
            ImageView imageView = v.findViewById(R.id.imageView);
            imageView.setImageDrawable(images[position]);
        }
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }
}
