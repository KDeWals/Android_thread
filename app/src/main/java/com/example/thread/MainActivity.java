package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pb_main_progressionTH;
    private Button bt_main_startTH;
    private TextView tv_main_txt;
    private ThreadTest monThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_main_progressionTH = findViewById(R.id.pb_main_progressionTH);
        bt_main_startTH = findViewById(R.id.bt_main_startTH);
        tv_main_txt = findViewById(R.id.tv_main_txt);
    }

    public void onMainClickManager(View view) {

        switch (view.getId()){
            case R.id.bt_main_txtchange:
                if(tv_main_txt.getText().equals("Hello World!")) tv_main_txt.setText("Hey Android!");
                else tv_main_txt.setText("Hello World!");
                break;
            case R.id.bt_main_startTH:
                if(bt_main_startTH.getText().equals("Thread Go")){
                    monThread = new ThreadTest(pb_main_progressionTH);
                    monThread.Go();
                    bt_main_startTH.setText("Thread Stop");
                    Toast.makeText(this, "Activation du thread", Toast.LENGTH_SHORT).show();
                }
                else {
                    monThread.Stop();
                    Toast.makeText(this, "Désactivation du thread !", Toast.LENGTH_SHORT).show();
                    bt_main_startTH.setText("Thread Go");
                }
                break;

        }
    }
}