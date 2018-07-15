package com.zsj.listdatascreenview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListDataScreenView mListDataScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListDataScreenView = (ListDataScreenView) findViewById(R.id.list_data_screen_view);
        mListDataScreenView.setAdapter(new ListDataScreenAdapter());
    }

    public void click(View view) {
        Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
    }
}
