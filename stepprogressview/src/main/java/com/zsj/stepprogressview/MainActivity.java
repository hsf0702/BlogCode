package com.zsj.stepprogressview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final StepProgressView stepProgressView = (StepProgressView) findViewById(R.id.ts_step_progress_view);
        Button tv_Start = (Button) findViewById(R.id.ts_start);
        tv_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 100);
                valueAnimator.setDuration(3000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        stepProgressView.setMaxProgress(100);
                        stepProgressView.setCurrentProgress(animatedValue);
                    }
                });
                valueAnimator.start();
            }
        });

    }
}
