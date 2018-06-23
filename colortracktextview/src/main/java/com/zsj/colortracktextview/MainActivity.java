package com.zsj.colortracktextview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ColorTrackTextView mColorTrackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorTrackTextView = (ColorTrackTextView) findViewById(R.id.colorTrack);
    }


    /**
     * 从左到右
     *
     * @param view
     */
    public void leftToRight(View view) {
        mColorTrackTextView.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mColorTrackTextView.setCurrentProgress(value);
            }
        });
        valueAnimator.start();
    }

    public void rightToLeft(View view) {
        mColorTrackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mColorTrackTextView.setCurrentProgress(value);
            }
        });
        valueAnimator.start();
    }
}
