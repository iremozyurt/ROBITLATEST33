package com.example.mqttdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.java_websocket.client.WebSocketClient;

import java.io.UnsupportedEncodingException;

public class MessageActivity extends AppCompatActivity {


    String clientID;


    MqttAndroidClient client;
    Vibrator vibrator;
    Ringtone ringtone;
    DatabaseHelper db;

    WebSocketClient webSocketClient;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        db= new DatabaseHelper(this);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringtone= RingtoneManager.getRingtone(getApplicationContext(),uri);


        auth=FirebaseAuth.getInstance();

    }












}