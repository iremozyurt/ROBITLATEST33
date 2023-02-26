package com.example.mqttdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;


public class mqttActivity extends AppCompatActivity    {


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
        setContentView(R.layout.activity_mqtt);

       db= new DatabaseHelper(this);
       vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
       Uri uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       ringtone= RingtoneManager.getRingtone(getApplicationContext(),uri);


        auth=FirebaseAuth.getInstance();

        ImageView whiteHome=findViewById(R.id.white_home);
        ImageView white_exit=findViewById(R.id.exit_home);


        whiteHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mqttActivity.this,HomeActivity.class));
                finish();
            }
        });

        white_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(mqttActivity.this,LoginActivity.class));
                finish();
            }
        });


    }



    public void submit(View view){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        EditText editTextTopic=(EditText) findViewById(R.id.topic);
        String topicName= editTextTopic.getText().toString();

        final EditText recv=(EditText) findViewById(R.id.recevMsg);

        EditText editTextUrl=(EditText) findViewById(R.id.url);
        String urlName=editTextUrl.getText().toString();

        EditText editTextPort=(EditText) findViewById(R.id.port);
        String portName=editTextPort.getText().toString();


        if(client.isConnected() && !topicName.equals("") && !urlName.equals("") && !portName.equals("")){
            if(radioGroup.getCheckedRadioButtonId()==R.id.subscribe){
            subscribe(topicName,recv);
            } else if (radioGroup.getCheckedRadioButtonId()==R.id.unsubscribe) {
                unsubscribe(topicName);
            } else if (radioGroup.getCheckedRadioButtonId()==R.id.publish) {
                publish(topicName);
            }
        }



    }



    public  void unsubscribe(String topicName){
        try {
            IMqttToken  unsubToken = client.unsubscribe(topicName);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(mqttActivity.this, "unsubscribed successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(mqttActivity.this,"Unsubscribed NOT successful",Toast.LENGTH_LONG).show();
                }
            });

        }catch (MqttException e){
         e.printStackTrace();
        }
    }

    public void subscribe(final String topicName,final EditText recv){
      try{
          client.subscribe(topicName,0);
          client.setCallback(new MqttCallback() {
              @Override
              public void connectionLost(Throwable cause) {
                  Toast.makeText(mqttActivity.this,"Connection lost",Toast.LENGTH_LONG).show();
              }

              @Override
              public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                  recv.setText(new String(mqttMessage.toString()));
                  vibrator.vibrate(600);
                  ringtone.play();
                  db.insertData(topicName,mqttMessage.toString());
              }

              @Override
              public void deliveryComplete(IMqttDeliveryToken token) {

              }
          });
      } catch (MqttException e) {
          e.printStackTrace();
      }
    }


    public void publish(String topicName){

        Switch switchMsg= (Switch) findViewById(R.id.switchmsg);


        String payload;
        if(switchMsg.isChecked())
            payload="I'm in Safe";
        else
            payload="I'm not okay. I need help ";
        byte[] encodedPayload= new byte[0];



        try{

            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topicName, message);
            Toast.makeText(mqttActivity.this,"message published successfully",Toast.LENGTH_LONG).show();

        }catch (UnsupportedEncodingException | MqttException e){
            Toast.makeText(mqttActivity.this, "message DIDN'T publish successfully", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    public void clientConnect(View view){

        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGr2);
        clientID= MqttClient.generateClientId();

        EditText editTextUrl=(EditText) findViewById(R.id.url);
        String urlName=editTextUrl.getText().toString();

        EditText editTextPort=(EditText) findViewById(R.id.port);
        String portName=editTextPort.getText().toString();


        if(radioGroup2.getCheckedRadioButtonId()==R.id.ws){
            String brokerUrl = "ws://" + urlName + ":" + portName + "/mqtt";
            client= new MqttAndroidClient(this.getApplicationContext(),brokerUrl,clientID);
        }


         if(radioGroup2.getCheckedRadioButtonId()==R.id.mqtt) {
             client = new MqttAndroidClient(this.getApplicationContext(), "tcp://" + urlName + ":" + portName, clientID);

        }


        MqttConnectOptions options= new MqttConnectOptions();
        options.setUserName("uacksnkb");
        options.setPassword("ciJOnSCD3khG".toCharArray());


        try{
            final  IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(mqttActivity.this, "connected successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(mqttActivity.this,"failed to connect",Toast.LENGTH_LONG).show();
                }
            });
        }catch (MqttException e){
e.printStackTrace();
        }

    }




    public void clientDisconnect(View view){
        try{
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(mqttActivity.this,"disconnected successfully",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(mqttActivity.this,"failed to disconnect",Toast.LENGTH_LONG).show();
                }
            });
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

public void showHistory(View view){
        Cursor res = db.retrieveData();
        if(res.getCount()==0){
            showMessage("Error","No data in db");
            return;
        }


        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append("ID: " + res.getString(0)+"\n");
            buffer.append("Topic: " + res.getString(1)+"\n");
            buffer.append("Message: " + res.getString(2)+"\n");

        }
    showMessage("History",buffer.toString());
}

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}