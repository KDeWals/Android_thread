package com.example.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thread.SimaticS7.S7;
import com.example.thread.SimaticS7.S7Client;
import com.example.thread.SimaticS7.S7OrderCode;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.LogRecord;

public class ReadTaskS7 {

    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;

    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private ProgressBar pb_main_progressionS7;
    private Button bt_main_connexion_s7;
    private View vi_main_ui;
    private TextView tv_main_plc;

    private AutomateS7 plcS7;
    private Thread readThread;

    private S7Client comS7;
    private String[] param = new String[10];
    private byte[] datasPLC = new byte[512];

    public ReadTaskS7(View v, Button b, ProgressBar pb, TextView tv){
        vi_main_ui = v;
        bt_main_connexion_s7 = b;
        pb_main_progressionS7 = pb;
        tv_main_plc = tv;
        comS7 = new S7Client();
        plcS7 = new AutomateS7();

        readThread = new Thread(plcS7);
    }

    public void Stop(){
        isRunning.set(false);
        comS7.Disconnect();
        readThread.interrupt();
    }

    public void Start(String a, String r, String s){
        if(!readThread.isAlive()){
            param[0] = a;
            param[1] = r;
            param[2] = s;

            readThread.start();
            isRunning.set(true);
        }
    }

    private void downloadOnPreExecute(int t){
        Toast.makeText(vi_main_ui.getContext(),
                "Le traitement de la tâche de fond est démarré !", Toast.LENGTH_SHORT).show();
        tv_main_plc.setText("PLC : " + String.valueOf(t));
    }

    private void downloadOnProgressUpdate(int progress){
        pb_main_progressionS7.setProgress(progress);
    }

    private void downloadOnPostExecute(){
        Toast.makeText(vi_main_ui.getContext(),
        "Le traitement de la tâche de fond est terminé !",
        Toast.LENGTH_LONG).show();
        pb_main_progressionS7.setProgress(0);
        tv_main_plc.setText("PLC : /!\\");
    }

    private Handler monHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);
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

    private class AutomateS7 implements Runnable {
        @Override
        public void run() {
            try{
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res =
                        comS7.ConnectTo(param[0],
                                Integer.valueOf(param[1]),
                                Integer.valueOf(param[2]));

                S7OrderCode orderCode = new S7OrderCode();
                Integer result = comS7.GetOrderCode(orderCode);
                int numCPU=-1;
                if(res.equals(0) && result.equals(0)){

                    //Quelques exemples :
                    // WinAC : 6ES7 611-4SB00-0YB7
                    // S7-315 2DPPN : 6ES7 315-4EH13-0AB0
                    // S7-1214C : 6ES7 214-1BG40-0XB0
                    // Récupérer le code CPU  611 OU 315 OU 214
                    numCPU = Integer.valueOf(orderCode.Code().toString().substring(5, 8));
                }
                else numCPU = 0000;
                sendPreExecuteMessage(numCPU);

                while (isRunning.get()){
                    if(res.equals(0)){
                        int retInfo = comS7.ReadArea(S7.S7AreaDB, 5, 9, 2, datasPLC);
                        int data = 0;
                        if(retInfo == 0){
                            data = S7.GetWordAt(datasPLC, 0);

                            sendProgressMesage(data);
                        }
                        Log.i("Variable A.P.I. => ", String.valueOf(data));
                    }
                    try {
                        Thread.sleep(500);
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

        private void sendPostExecuteMessage() {
            Message postExecuteMsg = new Message();
            postExecuteMsg.what = MESSAGE_POST_EXECUTE;
            monHandler.sendMessage(postExecuteMsg);
        }

        private void sendProgressMesage(int v) {
            Message preExecuteMsg = new Message();
            preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = v;
            monHandler.sendMessage(preExecuteMsg);
        }

        private void sendPreExecuteMessage(int i) {
            Message progressMsg = new Message();
            progressMsg.what = MESSAGE_PROGRESS_UPDATE;
            progressMsg.arg1 = i;
            monHandler.sendMessage(progressMsg);
        }
    }


}
