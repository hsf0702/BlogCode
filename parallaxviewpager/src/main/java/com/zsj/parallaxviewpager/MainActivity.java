package com.zsj.parallaxviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zsj.parallaxviewpager.parallax.animator.ParallaxViewPager;

public class MainActivity extends AppCompatActivity {

    private int[] fragmentIds = new int[]{
            R.layout.fragment_page_first,
            R.layout.fragment_page_second,
            R.layout.fragment_page_third
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParallaxViewPager parallaxViewPager = (ParallaxViewPager) findViewById(R.id.parallax_vp);
        parallaxViewPager.setLayout(getSupportFragmentManager(), fragmentIds);
    }
}
