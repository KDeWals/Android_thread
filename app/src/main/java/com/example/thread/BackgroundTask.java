package com.example.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BackgroundTask {

    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;

    private ProgressBar pb_main_progressionTrHa;
    private Button bt_main_startTrHa;
    private View vi_main_ui;

    public BackgroundTask(View v, Button b, ProgressBar p){
        vi_main_ui = v;
        bt_main_startTrHa = b;
        pb_main_progressionTrHa = p;
    }

    public void Start() {
    if(!monThread.isAlive()) monThread.start();

    }

    private void downloadOnPreExecute(){
        bt_main_startTrHa.setVisibility(View.GONE);
        pb_main_progressionTrHa.setVisibility(View.VISIBLE);
        Toast.makeText(vi_main_ui.getContext(), "Traitement en tâche de fond démarré" + "\n",
                Toast.LENGTH_SHORT).show();
    }

    private void downloadOnProgressUpdate(int progress){
        Log.i("var progress : ", String.valueOf(progress));
        pb_main_progressionTrHa.setProgress(progress);
        Log.i("pb_main_progression : ", String.valueOf(pb_main_progressionTrHa.getProgress()));
    }

    private void downloadOnPostExecute(){
        Toast.makeText(vi_main_ui.getContext(),
                "Traitement de la tâche de fond terminé",
                Toast.LENGTH_LONG).show();
        bt_main_startTrHa.setVisibility(View.VISIBLE);
        pb_main_progressionTrHa.setVisibility(View.GONE);
    }

    private Handler monHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute();
                    break;
                case MESSAGE_PROGRESS_UPDATE:
                    downloadOnProgressUpdate(msg.arg1);
                    break;
                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;
                default:
                    break;
            }
        }
    };

    private Thread monThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
               sendPreExecuteMessage();
               for(int i = 1; i <= 10; ++i){
                   sendProgressMessage(i);
                   try {
                       Thread.sleep(1000);
                   }
                   catch (InterruptedException e){
                       e.printStackTrace();
                   }
               }
                sendPostExecuteMessage();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });

    private void sendPostExecuteMessage(){
        Message postExecuteMsg = new Message();
        postExecuteMsg.what = MESSAGE_POST_EXECUTE;
        monHandler.sendMessage(postExecuteMsg);
    }

    private void sendPreExecuteMessage() {
        Message preExecuteMsg = new Message();
        preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
        monHandler.sendMessage(preExecuteMsg);

    }

    private void sendProgressMessage(int i) {
        Message progressMsg = new Message();
        progressMsg.what = MESSAGE_PROGRESS_UPDATE;
        progressMsg.arg1 = i * 10;
        monHandler.sendMessage(progressMsg);
    }
}
