package com.riyagayasen.easyaccordiondemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.riyagayasen.easyaccordion.AccordionView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccordionView accordionView = (AccordionView) findViewById(R.id.test_accordion);
        TextView textView = (TextView) accordionView.findViewById(R.id.textView);
        textView.setText("This is a demo accordion with text added dynamically This is a demo accordion with text added dynamically This is a demo accordion with text added dynamically This is a demo accordion with text added dynamically This is a demo accordion with text added dynamically This is a demo accordion with text added dynamically This is a demo accordion with text added dynamically");
    }
}
