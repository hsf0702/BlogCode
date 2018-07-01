package com.zsj.letterfilterlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LetterFilterListView mLetterFilterListView;
    private TextView mTvCenterLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTvCenterLetter = (TextView) findViewById(R.id.tvCenterLetter);
        mLetterFilterListView = (LetterFilterListView) findViewById(R.id.letterFilterListView);
        mLetterFilterListView.setOnTouchLetterListener(new LetterFilterListView.OnTouchLetterListener() {
            @Override
            public void OnTouchLetter(String touchLetter) {
                if (TextUtils.isEmpty(touchLetter)){
                    mTvCenterLetter.setVisibility(View.INVISIBLE);
                }else {
                    mTvCenterLetter.setVisibility(View.VISIBLE);
                    mTvCenterLetter.setText(touchLetter);
                }
            }
        });
    }
}
