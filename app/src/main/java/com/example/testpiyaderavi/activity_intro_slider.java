package com.example.testpiyaderavi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class activity_intro_slider extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout layoutDots;
    Button btnNext, btnPrevious;
    ImageView imageView_slide;
    Animation animation_move;
    SliderPagerAdapter pagerAdapter;
    private SliderPrefManager prefMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        changeStatusBarColor();
        prefMan = new SliderPrefManager(this);
        if(!prefMan.startSlider()){
            launchMainScreen();
            return;
        }
        imageView_slide=findViewById(R.id.slide_image);
        viewPager = findViewById(R.id.view_pager);
        layoutDots = findViewById(R.id.layoutDots);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        pagerAdapter = new SliderPagerAdapter();
        viewPager.setAdapter(pagerAdapter);

//         animation_move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
//        imageView_slide.startAnimation(animation_move);

        showDots(viewPager.getCurrentItem());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // dots
                showDots(position);
 //**************************************************************
 //**************************************************************
                // first page
//                if(position == viewPager.getAdapter().getCount() +2) {
//                    btnPrevious.setVisibility(View.GONE);
//                    btnNext.setText(R.string.go);
//                } else {
//                    btnPrevious.setVisibility(View.VISIBLE);
//                    btnNext.setText(R.string.next);
//                }


                // last page
                 if(position == viewPager.getAdapter().getCount() - 1) {

                    btnNext.setText(R.string.start);
                }
                 else {
                    btnPrevious.setVisibility(View.VISIBLE);
                    btnNext.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem();
                int lastPages = viewPager.getAdapter().getCount()-1;
                if(currentPage == lastPages){
                    prefMan.setStartSlider(false);
                    launchMainScreen();
                } else{
                    viewPager.setCurrentItem(currentPage+1);
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentPage = viewPager.getCurrentItem();
                int lastPages = viewPager.getAdapter().getCount()+1;
                if(currentPage == lastPages){
                    prefMan.setStartSlider(false);

                } else{
                    viewPager.setCurrentItem(currentPage-1);
                }
            }
        });
    }

    private void launchMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDots(int pageNumber){
        TextView[] dots = new TextView[viewPager.getAdapter().getCount()];
        layoutDots.removeAllViews();
        for(int i = 0; i < dots.length ; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            dots[i].setTextColor(ContextCompat.getColor(this,
                    (i == pageNumber ?  R.color.blue : R.color.blue_no_active)
            ));
//*************************************************
//**************************************************
            // لطفا نقطه پایین صفحه رو تغییر شکل بدین
//            dots[i].setBackground(ContextCompat.getDrawable(this,
//                    (i== pageNumber? R.drawable.shape_dots)
//            ));

            layoutDots.addView(dots[i]);
        }
    }

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public class SliderPagerAdapter extends PagerAdapter {
        String [] slideTitles;
        String [] slideDescriptions;
//        int [] bgColorIds = {R.color.blue, R.color.blue,
//                R.color.blue, R.color.blue};
        int [] slideImageIds = {R.drawable.back_go, R.drawable.back_go,
                R.drawable.back_go, R.drawable.back_go};

        public SliderPagerAdapter(){
//            slideTitles = getResources().getStringArray(R.array.slide_titles);
            slideDescriptions = getResources().getStringArray(R.array.slide_descriptions);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(activity_intro_slider.this)
                    .inflate(R.layout.slide, container, false);
            view.findViewById(R.id.bgLayout);
            ((ImageView) view.findViewById(R.id.slide_image)).setImageResource(slideImageIds[position]);
//            ((TextView) view.findViewById(R.id.slide_title)).setText(slideTitles[position]);
            ((TextView) view.findViewById(R.id.slide_desc)).setText(slideDescriptions[position]);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return slideImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    //======================change font===============================
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

}
