package com.zsj.messagebubbleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BubbleMessageTouchListener.BubbleDisappearListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.tv);
        MessageBubbleView.attach(tv,this);
    }

    @Override
    public void dismiss() {
        Toast.makeText(getApplicationContext(), "消失", Toast.LENGTH_SHORT).show();
    }
}
