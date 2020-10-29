package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

    private ProgressBar pb_main_progressionS7;
    private Button bt_main_connexion_S7;
    private TextView tv_main_plc;
    private ReadTaskS7 readTaskS7;
    private NetworkInfo network;
    private ConnectivityManager connexionStatus;

    private LinearLayout ln_main_ecrireS7;
    private CheckBox ch_main_activerouv;
    private CheckBox ch_main_activerfer;
    private CheckBox ch_main_aru;
    private WriteTaskS7 writeTaskS7;

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

        pb_main_progressionS7 = findViewById(R.id.pb_main_progressionS7);
        bt_main_connexion_S7 = findViewById(R.id.bt_main_connexion_S7);
       tv_main_plc = findViewById(R.id.tv_main_plc);
       connexionStatus = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
       network = connexionStatus.getActiveNetworkInfo();

       ln_main_ecrireS7 = findViewById(R.id.ln_main_ecrireS7);
       ch_main_activerouv = findViewById(R.id.ch_main_activerouv);
       ch_main_activerfer = findViewById(R.id.ch_main_activerfer);
       ch_main_aru = findViewById(R.id.ch_main_aru);

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

            case R.id.bt_main_connexion_S7:

                if(network != null && network.isConnectedOrConnecting()){
                    if(bt_main_connexion_S7.getText().equals("Connexion_S7")){
                        Toast.makeText(this, network.getTypeName(), Toast.LENGTH_SHORT).show();
                        bt_main_connexion_S7.setText("Déconnexion_S7");
                        readTaskS7 = new ReadTaskS7(view, bt_main_connexion_S7, pb_main_progressionS7,
                                tv_main_plc);
                        readTaskS7.Start("10.1.0.110", "0", "1");
                    }
                    else {
                        readTaskS7.Stop();

                        bt_main_connexion_S7.setText("Connexion_S7");
                        Toast.makeText(getApplication(), "Traitement interrompue par l'uti9lisateur !",
                                Toast.LENGTH_LONG).show();
                    }


                }
                if(network != null && network.isConnectedOrConnecting()){
                    if(bt_main_connexion_S7.getText().equals("Connexion_S7")) {

                        ln_main_ecrireS7.setVisibility(View.VISIBLE);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        writeTaskS7 = new WriteTaskS7();
                        writeTaskS7.Start("10.1.0.110", "0", "1");
                    }
                    else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        writeTaskS7.Stop();
                        ln_main_ecrireS7.setVisibility(View.INVISIBLE);
                    }

                    }
                else {
                    Toast.makeText(this, "Connexion au réseau impossible!", Toast.LENGTH_SHORT).show();

                }
                break;


            case R.id.ch_main_activerouv:
                writeTaskS7.setWriteBool(1, ch_main_activerouv.isChecked() ? 1 : 0);
                break;
            case R.id.ch_main_activerfer:
                writeTaskS7.setWriteBool(2, ch_main_activerfer.isChecked() ? 1 : 0);
            case R.id.ch_main_aru:
                writeTaskS7.setWriteBool(4, ch_main_aru.isChecked() ? 1 : 0);
                break;
        }
    }
}