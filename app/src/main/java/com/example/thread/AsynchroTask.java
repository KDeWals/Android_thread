package com.example.thread;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AsynchroTask extends AsyncTask<String, Integer, String> {

    private ProgressBar pb_main_progressionAS;
    private Button bt_main_startAS;
    private View vi_main_ui;

    public AsynchroTask(View v, Button b, ProgressBar pb) {
        this.pb_main_progressionAS = pb;
        this.bt_main_startAS = b;
        this.vi_main_ui = v;
    }

    protected void onPreExecute(){
        super.onPreExecute();
        bt_main_startAS.setVisibility(View.GONE);
        pb_main_progressionAS.setVisibility(View.VISIBLE);
        Toast.makeText(vi_main_ui.getContext(), "Démarrage de la tâche de fond AsynchroTask",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String uri = strings[0];
        Log.i("Paramètre : ", strings[0].toString());

        String result = "";

        for(int i = 1; i <= 20; ++i){
            publishProgress(i*5);
            result += String.valueOf(i);

            SystemClock.sleep(500);
        }
        return result;
    }

    protected void onProgressUpdate(Integer... progress){
        super.onProgressUpdate(progress);
        pb_main_progressionAS.setProgress(progress[0]);
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        Toast.makeText(vi_main_ui.getContext(),
                "Fin de l'exécution de la tâche de fond AsynchroTask :\n" + result,
                Toast.LENGTH_LONG).show();

        bt_main_startAS.setVisibility(View.VISIBLE);
        pb_main_progressionAS.setVisibility(View.GONE);
    }

}
