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
    private ProgressBar pb_main_progressionAS;
    private Button bt_main_startTH;
    private Button bt_main_startAS;
    private TextView tv_main_txt;
    private ThreadTest monThread;
    private ProgressBar pb_main_progressionTrHa1;
    private ProgressBar pb_main_progressionTrHa2;
    private Button bt_main_startTrHa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_main_progressionTH = findViewById(R.id.pb_main_progressionTH);
        pb_main_progressionAS = findViewById(R.id.pb_main_progressionAS);
        bt_main_startTH = findViewById(R.id.bt_main_startTH);
        bt_main_startAS = findViewById(R.id.bt_main_startAS);
        tv_main_txt = findViewById(R.id.tv_main_txt);
        pb_main_progressionTrHa1 = findViewById(R.id.pb_main_progressionTtHa1);
        pb_main_progressionTrHa2 = findViewById(R.id.pb_main_progressionTtHa2);
        bt_main_startTrHa = findViewById(R.id.bt_main_startTrHa);
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
            case R.id.bt_main_startAS:
            AsynchroTask asynchroTask = new AsynchroTask(view, bt_main_startAS, pb_main_progressionAS);
            asynchroTask.execute("paramètre(s) de traitement");
            break;

            case R.id.bt_main_startTrHa:
                BackgroundTask backgroundTask1 = new BackgroundTask(view, bt_main_startTrHa, pb_main_progressionTrHa1);
                backgroundTask1.Start();
                BackgroundTask backgroundTask2 = new BackgroundTask(view, bt_main_startTrHa, pb_main_progressionTrHa2);
                backgroundTask2.Start();

                break;
        }
    }
}